DROP TABLE IF EXISTS `t_source_image_group`;
CREATE TABLE `t_source_image_group`(
	`id` CHAR(32) NOT NULL COMMENT '主键',
	`name` VARCHAR(100) NOT NULL COMMENT '组名称',
	`desc` VARCHAR(500) DEFAULT NULL COMMENT '组描述',
	`sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
	`create_user` char(32) NOT NULL COMMENT '创建人',
	`update_user` char(32) NOT NULL COMMENT '更新人',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统资源图片组表';


DROP TABLE IF EXISTS `t_source_image`;
CREATE TABLE `t_source_image`(
	`id` CHAR(32) NOT NULL COMMENT '主键',
	`gid` CHAR(32) NOT NULL COMMENT '资源图片组主键',
	`name` VARCHAR(100) NOT NULL COMMENT '图片名称',
	`desc` VARCHAR(500) DEFAULT NULL COMMENT '图片描述',
	`url` VARCHAR(300) NOT NULL COMMENT '图片路径',
	`sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
	`create_user` char(32) NOT NULL COMMENT '创建人',
	`update_user` char(32) NOT NULL COMMENT '更新人',
	`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统资源图片表';