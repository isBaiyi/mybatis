create database mybatis;
use mybatis;

create table t_user
(
    id      int auto_increment primary key comment '主键',
    name    varchar(20) comment '姓名',
    version int default 0 comment '版本号'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='用户信息';


create table t_account
(
    id      int primary key comment '主键',
    accountNo    varchar(20) comment '账号',
    balance double comment '余额'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='账户信息';

insert into t_user (name) values ('baiyi1'),('baiyi2'),('baiyi3'),('baiyi4'),('baiyi5'),('baiyi6');
