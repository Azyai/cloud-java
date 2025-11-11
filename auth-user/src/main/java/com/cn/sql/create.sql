create table `role`(
                       `id` int(11) NOT NULL AUTO_INCREMENT,
                       `name` varchar(32) DEFAULT NULL,
                       `nameZh` varchar(32) DEFAULT NULL,
                       PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `user`(
                       `id` int(11) NOT NULL AUTO_INCREMENT,
                       `username` varchar(32) DEFAULT NULL,
                       `password` varchar(255) DEFAULT NULL,
                       `email` varchar(255) DEFAULT NULL,
                       `enabled` TINYINT(1) DEFAULT NULL,
                       `accountNonExpired` TINYINT(1) DEFAULT NULL,
                       `accountNonLocked` TINYINT(1) DEFAULT NULL,
                       `credentialsNonExpired` TINYINT(1) DEFAULT NULL,
                       PRIMARY KEY(`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table `user_role`(
                            `id` int(11) NOT NULL AUTO_INCREMENT,
                            `uid` int(11) NOT NULL,
                            `rid` int(11) NOT NULL,
                            PRIMARY KEY(`id`),
                            KEY `uid` (`uid`),
                            KEY `rid` (`rid`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;