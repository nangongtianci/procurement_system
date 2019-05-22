DROP TABLE IF EXISTS `t_sys_permission`;
create table `t_sys_permission`(
 `id` char(32) NOT NULL COMMENT '主键',
 `available` char(20) not null,
 `name` char(20) not null,
 `parent_id` char(32) NOT NULL COMMENT '父主键',
 `parent_ids` char(20) not null,
 `permission` char(20) not null,
 `resource_type` char(20) not null,
 `url` char(20) not null,
  PRIMARY KEY (`id`)
 )ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';

create table sys_role(
 id int(4) not null primary key ,
 available char(20) not null,
 description char(20) not null,
 role char(20) not null

 );

create table sys_role_permission(
 permission_id int(4) not null  ,
 role_id int(4) not null
 );

create table sys_user_role(
 role_id int(4) not null  primary key,
 uid int(4) not null
 );

create table user_info(
 uid char(20) not null primary key ,
 userName char(20) not null,
 name char(20) not null,
 password char(50) not null,
 salt char(50) not null,
 state char(20) not null
);


INSERT INTO sys_permission (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`)
VALUES (1,0,'用户管理',0,'0/','view','menu','userInfo/userList');
INSERT INTO sys_permission (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`)
VALUES (2,0,'用户添加',1,'0/1','add','button','userInfo/userAdd');
INSERT INTO sys_permission (`id`,`available`,`name`,`parent_id`,`parent_ids`,`permission`,`resource_type`,`url`)
VALUES (3,0,'用户删除',1,'0/1','del','button','userInfo/userDel');
INSERT INTO sys_role (`id`,`available`,`description`,`role`) VALUES (1,'0','管理员','admin');
INSERT INTO sys_role (`id`,`available`,`description`,`role`) VALUES (2,'0','VIP会员','vip');

INSERT INTO sys_role_permission (`permission_id`,`role_id`) VALUES (1,1);
INSERT INTO sys_role_permission (`permission_id`,`role_id`) VALUES (1,2);
INSERT INTO sys_role_permission (`permission_id`,`role_id`) VALUES (1,3);
INSERT INTO sys_role_permission (`permission_id`,`role_id`) VALUES (2,1);
INSERT INTO sys_role_permission (`permission_id`,`role_id`) VALUES (3,1);

INSERT INTO sys_user_role (`role_id`,`uid`) VALUES (1,1);
INSERT INTO sys_user_role (`role_id`,`uid`) VALUES (2,2);
INSERT INTO sys_user_role (`role_id`,`uid`) VALUES (2,1);

INSERT INTO user_info (`uid`,`userName`,`name`,`password`,`salt`,`state`) VALUES ('1', 'admin', '管理员', '4f251ab52c7b431254a7adc8ea31724b', 'saltadmin', 0);
INSERT INTO user_info  (`uid`,`userName`,`name`,`password`,`salt`,`state`) VALUES ('2', 'vip', '会员', 'e1e34dfbc204ba288b6645caad95babe', 'saltvip', 0);


##一对多关联查询
SELECT  a.userName,a.name,b.role_id   FROM  user_info a
INNER JOIN sys_user_role b
 ON  a.uid=b.uid
WHERE  a.uid=1  ;