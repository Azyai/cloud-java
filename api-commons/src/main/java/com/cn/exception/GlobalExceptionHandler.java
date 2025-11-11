package com.cn.exception;


import com.cn.resp.ResultData;
import com.cn.resp.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(RuntimeException.class)
     @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
     public ResultData<String> exception(Exception e){
         System.out.println("#####come in GlobalExceptionHandler#####");
         System.out.println(e.toString());
         return ResultData.fail(ReturnCodeEnum.RC500.getCode(),e.getMessage());
     }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultData<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst()
                .orElse("参数校验失败");
        return ResultData.fail(errorMessage);
    }

}
