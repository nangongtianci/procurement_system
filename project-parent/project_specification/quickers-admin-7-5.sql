DROP TABLE IF EXISTS `t_dept`;
CREATE TABLE `t_dept` (
  `id` char(32) NOT NULL COMMENT '部门主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父部门主键',
  `simple_name` varchar(45) NOT NULL COMMENT '简称',
  `full_name` varchar(255) NOT NULL COMMENT '全称',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门表';

DROP TABLE IF EXISTS `t_dept_employee`;
CREATE TABLE `t_dept_employee` (
  `id` char(32) NOT NULL COMMENT '主键',
  `did` varchar(32) NOT NULL COMMENT '部门主键',
  `eid` varchar(32) NOT NULL COMMENT '员工主键',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门员工关系表';


DROP TABLE IF EXISTS `t_global_goods`;
CREATE TABLE `t_global_goods` (
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

DROP TABLE IF EXISTS `t_global_goods_category`;
CREATE TABLE `t_global_goods_category` (
  `id` char(32) NOT NULL COMMENT '分类主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父类id',
  `name` varchar(100) NOT NULL COMMENT '分类名称',
  `desc` varchar(500) DEFAULT NULL COMMENT '描述',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局商品分类表';

DROP TABLE IF EXISTS `t_global_goods_category_rel`;
CREATE TABLE `t_global_goods_category_rel` (
  `id` char(32) NOT NULL COMMENT '主键',
  `gid` varchar(32) NOT NULL DEFAULT '' COMMENT '全局商品主键',
  `cid` varchar(32) NOT NULL DEFAULT '' COMMENT '全局商品分类主键',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局商品分类关联表';

# 7-10
alter table t_employee modify column `status` char(1) NOT NULL default '1' COMMENT '状态(字典),0禁用，1启用';
alter table t_employee add column `deleted` tinyint(1) NOT NULL default '0' COMMENT '是否删除，1删除，0不删除' after sort;
