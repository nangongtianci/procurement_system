#--------------------后台管理表----------------------------------
#--全局分类表
DROP TABLE IF EXISTS `t_global_goods_category`;
CREATE TABLE `t_global_goods_category`(
  `id` char(32) NOT NULL COMMENT '分类主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父类id',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `desc` varchar(500) DEFAULT NULL COMMENT '描述',
  `sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局商品分类表';

#--商品分类关联表
DROP TABLE IF EXISTS `t_global_goods_category_rel`;
CREATE TABLE `t_global_goods_category_rel`(
  `id` char(32) NOT NULL COMMENT '主键',
  `gid` varchar(32) NOT NULL DEFAULT '' COMMENT '全局商品主键',
  `cid` varchar(32) NOT NULL DEFAULT '' COMMENT '全局商品分类主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局商品分类关联表';

# --全局商品表
DROP TABLE IF EXISTS `t_global_goods`;
CREATE TABLE `t_global_goods`(
  `id` char(32) NOT NULL COMMENT '商品主键',
  `name` varchar(100) NOT NULL COMMENT '商品名称',
  `imgs` varchar(500) NOT NULL DEFAULT '' COMMENT '全局商品图片（暗含默认值的意思），逗号相连接',
  `videos` varchar(500) NOT NULL DEFAULT '' COMMENT '全局商品视频（暗含默认值的意思），逗号相连接',
  `unit_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局商品表';

# --字典表
DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict`  (
  `id` char(32) NOT NULL COMMENT '商品主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父级字典id',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `code` varchar(255) NOT NULL COMMENT '编码',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统字典表';

#--部门表
DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept`  (
  `id` char(32) NOT NULL COMMENT '部门主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父部门主键',
  `simple_name` varchar(45) NOT NULL  COMMENT '简称',
  `full_name` varchar(255) NOT NULL COMMENT '全称',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

#--员工表
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee`  (
  `id` char(32) NOT NULL COMMENT '员工主键',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `account` varchar(45) NOT NULL COMMENT '账号',
  `password` varchar(45)  NOT NULL COMMENT '密码',
  `salt` varchar(45)  NOT NULL COMMENT 'md5密码盐',
  `name` varchar(45)  NOT NULL COMMENT '名字',
  `birthday` datetime(0) DEFAULT NULL COMMENT '生日',
  `sex` varchar(32)  NOT NULL COMMENT '性别(字典)',
  `email` varchar(45)  NOT NULL COMMENT '电子邮件',
  `phone` varchar(45)  NOT NULL COMMENT '电话',
  `status` varchar(32)  NOT NULL COMMENT '状态(字典)',
  `sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';


#--商品分类关联表
DROP TABLE IF EXISTS `t_dept_employee_rel`;
CREATE TABLE `t_dept_employee_rel`(
  `id` char(32) NOT NULL COMMENT '主键',
  `did` varchar(32) NOT NULL DEFAULT '' COMMENT '部门主键',
  `eid` varchar(32) NOT NULL DEFAULT '' COMMENT '员工主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门员工关联表';
















