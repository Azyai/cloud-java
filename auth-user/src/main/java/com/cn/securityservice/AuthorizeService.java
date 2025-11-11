package com.cn.securityservice;

import com.google.code.kaptcha.Producer;
import com.cn.entity.*;
import com.cn.mapper.*;
import com.cn.service.UserProfileService;
import com.cn.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


@Service
public class AuthorizeService implements UserDetailsService {

    @Resource
    private RbacService rbacService;

    @Autowired
    private UserService userService;

    @Autowired
    Producer producer;

    @Value("${spring.mail.username}")
    String from;

    @Resource
    MailSender mailSender;

    @Resource
    StringRedisTemplate template;


    @Resource
    UserMapper userMapper;

    @Autowired
    private UserProfileService userProfileService;
    @Autowired
    private UserRoleMapper userRoleMapper;


    public String sendValidateEmail(String email, String sessionId,boolean hasAccount) {
        // 我们这里设置的是60s后才可以重新发送一次验证码，而一次验证码时间过期为3分钟
        // 也就是我们刚刚发送的验证码过期时间低于2分钟，就可以重新发送一次验证码，重复此流程
        String key = "email:" + sessionId + ":" + email + ":" + hasAccount;
        // 必须要加上这个hasAccount来保证注册时的验证码不会用到重置密码上
        if(Boolean.TRUE.equals(template.hasKey(key))){
            //  如果这样包装类可能会存在空指针！因此修改一下,默认情况拦截即0s
            //  Long expire = template.getExpire(key, TimeUnit.SECONDS);
            Long expire = Optional.ofNullable(template.getExpire(key, TimeUnit.SECONDS)).orElse(0L);
//            即当前过期时间如果大于2分钟，就不会让你去请求发送验证码，直接拦截掉
            if(expire > 120){
                return "请求频繁，请稍后再试！";
            }
        }
        User user = userMapper.findByUsernameOrEmail(email);

        //这里的逻辑很简单
        // 当我们要修改密码时走这个验证：修改密码传进来的是真，但是没有查出来用户就返回退出
        if(hasAccount && user == null){
            return "没有此邮件地址的账户";
        }

        // 当我们要注册用户时走这个验证：传进来的是假，但是 user却能查出来直接返回
        // 如果两个都没通过，则正常发送验证码
        if(!hasAccount && user != null){
            return "此邮箱已被其他用户注册";
        }

        // 1.先生成对应的验证码:可以使用随机数 或者 开源框架kaptcha
        String code = producer.createText();
        // 2.发送验证码到指定邮箱
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("您的验证邮件");  //发送的标题
        message.setText("验证码是：" + code); //正文
        try{
            mailSender.send(message);
            // 3.如果发送成功，将邮件地址和code插入到redis里
            // 这里光存邮箱地址还不够，还需要存session,如果不加session那他就可以绕过当前邮箱验证60s后才能重发验证码的时间了
            // 如果他要是换了一个邮箱地址呢?那这样用其他的邮箱地址验证码也能是不合理的

            template.opsForValue().set(key,String.valueOf(code),3, TimeUnit.MINUTES); //过期时间3分钟
            // 我们这里设置的是60s后才可以重新发送一次验证码，而一次验证码时间过期为3分钟
            // 也就是我们刚刚发送的验证码过期时间低于2分钟，就可以重新发送一次验证码，重复此流程（开头实现）

            return null;
        }catch (MailException e){
            e.printStackTrace();
            return "邮件发送失败，，查看邮件地址是否有效或联系管理员";
        }

        // 4.用户在注册时，再从redis里面取出对应键值对，然后看验证码是否一致
    }

    PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Transactional(rollbackFor = Exception.class)
    public String validateAndRegister(String username, String password, String email, String code, String sessionId) {
        // session做限流使用的
        String key = "email:" + sessionId + ":" + email + ":false"; // 必须没有这个账户
        // 如果只填邮箱，又没有验证码因此需要验证,又分很多种情况不能使用Boolean类型
        if(Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if(s == null) { //验证码可能为空，也就是刚刚好失效了
                return "验证码失效，请重新请求";
            }

            if(s.equals(code)){
                // 先查找一下有没有这个用户名的用户
                User user = userMapper.findByUsernameOrEmail(username);
                if(user != null){
                    return "此用户名已被注册，请更换用户名";
                }

                // 这俩放下面， 如果用户名重复的话，我们再次提交就不能使用了
                template.delete(key); //验证码使用完毕，需要清除
                password = encoder.encode(password); //密码加密

                // 注册账号
                int account = userMapper.createAccount(username, password, email);

                Integer id = userMapper.findByUsernameOrEmail(username).getId();

                // 设置用户的默认角色，
                UserRole userRole = new UserRole();
                userRole.setUserId(id);
                userRole.setRoleId(2);
                int insert = userRoleMapper.insert(userRole);

                // 存储默认的用户信息
                UserProfile userProfile  = new UserProfile();
                userProfile.setUserId(id);
                userProfile.setGender(1);
                boolean save = userProfileService.save(userProfile);


                if(account > 0 && save && insert > 0){
                    return null;
                }else{
                    return "内部错误，请联系管理员";
                }


            }else {
                return "验证码错误，请检查后再提交";
            }

        }else{
            return "请先请求一封验证码邮件";
        }

    }




    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1判断是否为空
        if(username == null){
            throw new UsernameNotFoundException("用户名不能为空");
        }

        // 2.获取用户的基本信息
        User user = userService.findByUsernameOrEmail(username);

        if(user == null){
            throw new UsernameNotFoundException("用户名或密码错误");
        }

        // 3.获取用户的角色信息
        List<String> roleNames = rbacService.findRolesByUsername(user.getUsername());

        System.out.println(roleNames);

        // 4.获取用户的权限信息
        List<String> authorities = rbacService.findAuthoritiesByRoleName(roleNames);

        // 5.告诉security角色有那些，角色的格式为：Role_xxx 所以这里需要修改一下
        roleNames = roleNames.stream().map(role -> "ROLE_" + role).toList();

//        System.out.println("用户权限列表：" + authorities); // 应该包含'2099'

        // 6.告诉security角色和权限:这里也是做了一个转换，因为setAuthorities需要的是GrantedAuthority
        //  而GrantedAuthority实现了Serializable，因此我们使用commaSeparatedStringToAuthorityList方法进行转换
        user.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",",authorities)));

//        System.out.println(user + "-----------------");
        return user;

    }


    public boolean resetPasswordByEmail(String password, String email) {
        password = encoder.encode(password);
        return userMapper.resetPasswordByEmail(password, email) > 0;
    }

    public String validateOnly(String email, String code, String sessionId) {
        String key = "email:" + sessionId + ":" + email + ":true"; // 必须有这个账户
        // 如果只填邮箱，又没有验证码因此需要验证,又分很多种情况不能使用Boolean类型
        if(Boolean.TRUE.equals(template.hasKey(key))){
            String s = template.opsForValue().get(key);
            if(s == null) { //验证码可能为空，也就是刚刚好失效了
                template.delete(key); //验证码使用完毕，需要清除
                return "验证码失效，请重新请求";
            }

            if(s.equals(code)){
                return null;
            }else {
                return "验证码错误，请检查后再提交";
            }

        }else{
            return "请先请求一封验证码邮件";
        }
    }


}
