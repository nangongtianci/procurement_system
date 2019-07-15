DROP TABLE IF EXISTS `t_dict`;
CREATE TABLE `t_dict` (
  `id` char(32) NOT NULL COMMENT '主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父级字典id',
  `name` varchar(50) NOT NULL COMMENT '名称',
  `code` varchar(50) NOT NULL COMMENT '编码',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` smallint(5) unsigned NOT NULL DEFAULT 0 COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统字典表';


DROP TABLE IF EXISTS `t_customer`;
CREATE TABLE `t_customer` (
  `id` char(32) NOT NULL COMMENT '主键',
  `name` varchar(45) NOT NULL COMMENT '用户姓名',
  `phone` varchar(11) NOT NULL COMMENT '手机号',
  `id_card` varchar(18) DEFAULT '' COMMENT '身份证',
  `company_name` varchar(80) DEFAULT '' COMMENT '公司名称(单位名称)',
  `market_name` varchar(45) DEFAULT '' COMMENT '市场名称',
  `email` varchar(100) DEFAULT '' COMMENT '邮箱',
  `icon` varchar(200) NOT NULL COMMENT '头像',
  `sex` varchar(50) NOT NULL DEFAULT '1' COMMENT '性别 1男，0女',
  `id_card_path` varchar(300) DEFAULT '' COMMENT '证件路径,2张图片以逗号相分割',
  `is_agree_protocol` varchar(50) NOT NULL COMMENT '是否同意协议',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `status` char(1) NOT NULL DEFAULT '0' COMMENT '是否在线',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息';

DROP TABLE IF EXISTS `t_bill`;
CREATE TABLE `t_bill` (
  `id` char(32) NOT NULL COMMENT '主键',
  `cid` char(32) NOT NULL COMMENT '客户主键',
  `pid` char(32) DEFAULT '' COMMENT '父主键',
  `business_status` varchar(50) NOT NULL COMMENT '交易状态(买入，卖出，盘盈，盘损，其他费用)',
  `bill_status` varchar(50) NOT NULL COMMENT '账单状态（已付，已收，应付，应收）',
  `expect_pay_days` int(1) DEFAULT '0' COMMENT '账期天数',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `bill_sn` char(20) NOT NULL DEFAULT '' COMMENT '账单号',
  `other_cost` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '其他费用',
  `total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总价格',
  `actual_total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实收总价格',
  `bill_sn_type` varchar(50) NOT NULL COMMENT '扫描，手动',
  `is_top` varchar(50) NOT NULL COMMENT '置顶，不置顶',
  `bill_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记账日期',
  `is_peer_bill` varchar(50) NOT NULL COMMENT '是否为对等账单(否，是)',
  `is_receive` varchar(50) NOT NULL COMMENT '是否已领取虚拟币（否，是）',
  `create_customer_id` char(32) NOT NULL COMMENT '创建人主键',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_time` datetime NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账单';


DROP TABLE IF EXISTS `t_bill_goods`;
CREATE TABLE `t_bill_goods` (
   `id` char(32) NOT NULL COMMENT '商品主键',
   `bill_id` varchar(32) NOT NULL COMMENT '账单主键',
   `name` varchar(100) NOT NULL DEFAULT '' COMMENT '品名',
   `number` float(11,2) NOT NULL DEFAULT '0.00' COMMENT '货品数量',
   `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '货品单价',
   `weight` int(11) NOT NULL DEFAULT 0 COMMENT '重量',
   `weight_unit` varchar(50) NOT NULL COMMENT '货品单位（元/斤，元/箱）',
   `quality` varchar(100) DEFAULT '' COMMENT '货品品质',
   `product_img_path` varchar(1000) DEFAULT '' COMMENT '货品图片路径',
   `is_goods` varchar(50) NOT NULL COMMENT '货品，商品',
   `is_show` varchar(50) NOT NULL COMMENT '下架，上架',
   `goods_fix_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品定价',
   `goods_start_count` int(11) NOT NULL DEFAULT 0 COMMENT '商品启购量',
   `goods_desc` varchar(200) DEFAULT '' COMMENT '商品描述',
   `goods_brand` varchar(50) DEFAULT '' COMMENT '商品品牌',
   `goods_spec` varchar(100) DEFAULT '' COMMENT '商品规格',
   `goods_product_place` varchar(200) DEFAULT '' COMMENT '商品产地',
   `goods_address` varchar(200) DEFAULT '' COMMENT '商品货址',
   `goods_freight` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '商品运费',
   `goods_cost_ratio` float NOT NULL DEFAULT 0 COMMENT '商品加费比例',
   `goods_img_path` varchar(1000) DEFAULT '' COMMENT '商品图片路径',
   `goods_video_path` varchar(200) DEFAULT '' COMMENT '商品视频路径',
   `create_customer_id` char(32) NOT NULL COMMENT '创建人主键',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品信息';


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


alter table  t_customer modify column icon varchar(200) COMMENT '头像';
alter table t_bill_goods drop column is_show;
alter table t_bill_goods add  column goods_is_show varchar(50) not null default '' COMMENT '0下架，1上架' after is_goods;

#--员工表
DROP TABLE IF EXISTS `t_employee`;
CREATE TABLE `t_employee`  (
   `id` char(32) NOT NULL COMMENT '员工主键',
   `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
   `account` varchar(45) NOT NULL COMMENT '账号',
   `password` char(32)  NOT NULL COMMENT '密码',
   `salt` char(32)  NOT NULL COMMENT 'md5密码盐',
   `name` varchar(45)  NOT NULL COMMENT '名字',
   `birthday` datetime DEFAULT NULL COMMENT '生日',
   `sex` char(1)  NOT NULL COMMENT '性别(字典),1男，2女',
   `email` varchar(45)  NOT NULL COMMENT '电子邮件',
   `phone` varchar(45)  NOT NULL COMMENT '电话',
   `status` varchar(32)  NOT NULL COMMENT '状态(字典),0禁用，1启用',
   `sort` SMALLINT UNSIGNED NOT NULL DEFAULT 0 COMMENT '排序',
   `create_user` char(32) NOT NULL COMMENT '创建人',
   `update_user` char(32) NOT NULL COMMENT '更新人',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';

#--初始化admin超级管理员账号
insert into t_employee (id,`account`,
                        `password`,`salt`,`name`,`birthday`,
                        `sex`,`email`,`phone`,`status`,`create_user`,`update_user`)
 values ('6ed7a9860f3d42eeb585a36fff8f9c49','admin',
         'c3284d0f94606de1fd2af172aba15bf3','36df6a1a4de1d257f4bd4f5c2239ceeb','超级管理员',now(),
         '0','675886926@qq.com','13661044107','1','6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

#--alter table t_dict add column `level` char(1) NOT NULL default '0' COMMENT '0系统，1业务';

/**创建词典数据**/
#--是否分类
insert  into t_dict
  (id,pid,name,code,`desc`,sort,create_user,update_user)
values
  ('83d977a782134415a6b9dcfb5c2b5d91','','是否分类','0','系统级别：是，否',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('02d2afa3d33e483d9d6f374538290f24','83d977a782134415a6b9dcfb5c2b5d91','是','1','是',0,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('f0015a9e9e3f48439e411fb8a6a42838','83d977a782134415a6b9dcfb5c2b5d91','否','0','否',1,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');


#--禁用启用分类
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('4942178ac5ea4e039a08f7e6ad8dfd87','','禁用启用分类','1','系统级别：启用，禁用',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('b045224de7ca4c2ba1f9b1a2ae226c8e','4942178ac5ea4e039a08f7e6ad8dfd87','禁用','0','禁用状态',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('b7a6ca540c674cba9861562507b7f3ee','4942178ac5ea4e039a08f7e6ad8dfd87','启用','1','启用状态',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

#--可抵押物品
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('c9ff81968fae43d7943b0632c99ac2c4','','可抵押物品分类','2','可抵押物品：（无，房子，车，货品）',2,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('a0d4602581694b8eab6439022df9db5f','c9ff81968fae43d7943b0632c99ac2c4','无','0','无',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('b31b91aed2f3418098fb209e087a6a89','c9ff81968fae43d7943b0632c99ac2c4','房子','1','房租',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('aed9873c630543cf9f2bafcbce6348de','c9ff81968fae43d7943b0632c99ac2c4','车','2','车',2,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('1f22d95f2ce1482badf2903bf6dd5684','c9ff81968fae43d7943b0632c99ac2c4','货品','3','货品',3,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

#--交易状态
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('d19bff99d0a94189a903c1e35704b44e','','交易状态分类','3','交易状态：(买入，卖出，盘盈，盘损，其他费用)',3,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('e82b26c7af2d450db7bc458ed42fdb96','d19bff99d0a94189a903c1e35704b44e','买入','0','买入',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('b71ba56ad31c4c70ad16deb93abccdaa','d19bff99d0a94189a903c1e35704b44e','卖出','1','卖出',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('2c943ae6b7f44eaaae7ac9690550b726','d19bff99d0a94189a903c1e35704b44e','盘盈','2','盘盈',2,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('b7781720b6ad4eb9806862e6d537dbb4','d19bff99d0a94189a903c1e35704b44e','盘损','3','盘损',3,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('e35adefc99d245d59a88fa864ef36182','d19bff99d0a94189a903c1e35704b44e','其他费用','4','其他费用',4,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

#--账单状态
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('1973727c44a142c5bd6fb3ae7e4b62b4','','账单状态分类','4','账单状态：（已付，已收，应付，应收）',4,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('b656989faa3b42d88501c0882d8768ec','1973727c44a142c5bd6fb3ae7e4b62b4','已付','0','已付',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('095a5c9aacbd4b25a0b3b7cdb4b67b37','1973727c44a142c5bd6fb3ae7e4b62b4','已收','1','已收',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('bb7c1e43fcac4c16a01d57e12c71e885','1973727c44a142c5bd6fb3ae7e4b62b4','应付','2','应付',2,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('8cfa1ade55a94b9482d550fff773ad6e','1973727c44a142c5bd6fb3ae7e4b62b4','应收','3','应收',3,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');


#--账单生成类型
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('48bba1029e8f4748aab39fee04485a71','','账单生成类型','5','账单生成类型：（扫描，手动）',5,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('7800dcfbd61243faba2b4a468e905c6e','48bba1029e8f4748aab39fee04485a71','扫描','0','扫描',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('fcc8ff13c163456595059487e4d67311','48bba1029e8f4748aab39fee04485a71','手动','1','手动',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');


#--货品单位分类
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('32bd43d16c044ef9b3a002870b82fb55','','货品单位分类','6','货品单位分类：（元/斤，元/箱）',6,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('7ea09e78f82d4545bdd24b133691333e','32bd43d16c044ef9b3a002870b82fb55','元/斤','0','元/斤',0,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('e7270cc7629f4f7dae50ff4f28c7a12e','32bd43d16c044ef9b3a002870b82fb55','元/箱','1','元/箱',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');


#--性别分类
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('2338bcab310340469ae5edab0fcd2f85','','性别分类','7','性别分类',7,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('9325f7f733454bf1a7af4a2c9040d3f8','2338bcab310340469ae5edab0fcd2f85','男','1','男',1,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
  values
  ('fdaed7a15bf640e7baa15b2ecc78bae3','2338bcab310340469ae5edab0fcd2f85','女','2','女',2,
   '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');


#--账单关系分类
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('6edf429fbf6041e294ba0aff43698ebe','','客户账单关系类别','8','客户账单关系类别',8,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');

insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('0447e45896fd4d1c8428ddc152bb9428','6edf429fbf6041e294ba0aff43698ebe','新建','0','新建',0,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('a599b2dd8bb64fd99d2112bd5973487e','6edf429fbf6041e294ba0aff43698ebe','扫描','1','扫描',1,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('6a434e4c59db4e2c941ff6ae56d49ff7','6edf429fbf6041e294ba0aff43698ebe','卖货','2','卖货',2,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('a41f692ae05d4aab8799775e2192d248','6edf429fbf6041e294ba0aff43698ebe','代卖','3','代卖',3,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');
insert  into t_dict
(id,pid,name,code,`desc`,sort,create_user,update_user)
values
('01a3ce76f85643319568ee0cf07f08c2','6edf429fbf6041e294ba0aff43698ebe','分享','4','分享',4,
 '6ed7a9860f3d42eeb585a36fff8f9c49','6ed7a9860f3d42eeb585a36fff8f9c49');


DROP TABLE IF EXISTS `t_apply_loan_record`;
CREATE TABLE `t_apply_loan_record` (
   `id` char(32) NOT NULL COMMENT '主键',
   `need_money` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '需求金额',
   `use_cycle` int(1) NOT NULL DEFAULT '0' COMMENT '使用周期',
   `accept_rate` float NOT NULL DEFAULT '0' COMMENT '可接受利率',
   `mortgage_thing` char(1) NOT NULL COMMENT '可抵押物品',
   `purpose` varchar(100) NOT NULL DEFAULT '' COMMENT '资金用途',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   `create_customer_id` varchar(32) NOT NULL COMMENT '创建人主键',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请贷款记录';

# DROP TABLE IF EXISTS `t_customer_relation`;
# CREATE TABLE `t_customer_relation` (
#    `id` char(32) NOT NULL COMMENT '主键',
#    `up_id` char(32) NOT NULL COMMENT '上游主键',
#    `down_id` char(32) NOT NULL COMMENT '下游主键',
#    `relation_count` int(11) NOT NULL DEFAULT 1 COMMENT '关系笔数',
#    `create_customer_id` char(32) NOT NULL COMMENT '创建人主键',
#    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
#    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
#    PRIMARY KEY (`id`)
# ) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户关系表';


DROP TABLE IF EXISTS `t_customer_bill_relation`;
CREATE TABLE `t_customer_bill_relation` (
    `id` char(32) NOT NULL COMMENT '主键',
    `cid` varchar(32) NOT NULL COMMENT '客户主键',
    `bid` varchar(32) NOT NULL COMMENT '账单主键',
    `relation_type` char(1) NOT NULL DEFAULT '0' COMMENT '新建，扫描，卖货，代卖',
    `pid` varchar(32) DEFAULT '' COMMENT '来自哪个客户',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户账单关系表';

alter table t_customer add column `red_packet` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '红包金额' after phone;

alter table t_bill drop column is_peer_bill;

alter table t_customer_bill_relation add column `pbid` varchar(32) DEFAULT '' COMMENT '通过哪个账单生成而来' after pid;


DROP TABLE IF EXISTS `t_customer_bill_relation`;
CREATE TABLE `t_customer_bill_relation` (
    `id` char(32) NOT NULL COMMENT '主键',
    `create_id` char(32) NOT NULL COMMENT '创建人主键（自己）',
    `source_id` char(32) DEFAULT '' COMMENT '来自哪个客户（分享,代卖账单创建人主键）',
    `customer_id` char(32) NOT NULL COMMENT '客户主键（对方）',
    `relation_type` char(1) NOT NULL DEFAULT '0' COMMENT '新建，扫描，卖货，代卖，分享',
    `bill_id` char(32) NOT NULL COMMENT '账单主键',
    `source_bill_id` char(32) DEFAULT '' COMMENT '通过哪个账单生成而来',
    `business_status` char(1) NOT NULL DEFAULT '0' COMMENT '交易状态,默认买入：0',
    `bill_status` char(1) NOT NULL DEFAULT '2' COMMENT '账单状态,默认：应付:2',
    `total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总价格',
    `actual_total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实收总价格',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户账单关系表';

# 2019-07-02
alter table t_bill drop column is_top;
alter table t_customer_bill_relation add column `is_top` char(1) NOT NULL default '0' COMMENT '置顶1，不置顶0' after actual_total_price;
alter table t_bill drop column bill_sn_type;
alter table t_customer_bill_relation add column `is_peer` char(1) NOT NULL default '0' COMMENT '对等：1，不对等：0' after is_top;

# 收付款记录
DROP TABLE IF EXISTS `t_receipt_payment_record`;
CREATE TABLE `t_receipt_payment_record` (
    `id` char(32) NOT NULL COMMENT '主键',
    `create_id` char(32) NOT NULL COMMENT '创建人主键（自己）',
    `is_payment` char(1) NOT NULL DEFAULT '0' COMMENT '0:收款，1：付款',
    `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '收款|付款金额',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='收付款记录';

# 收付款对应账单关系
DROP TABLE IF EXISTS `t_receipt_payment_record_bill`;
CREATE TABLE `t_receipt_payment_record_bill` (
    `id` char(32) NOT NULL COMMENT '主键',
    `rp_id` char(32) NOT NULL COMMENT '首付款记录主键',
    `remain_id` char(32) DEFAULT '' COMMENT '留存主键',
    `bill_id` char(32) NOT NULL COMMENT '账单主键',
    `create_id` char(32) NOT NULL COMMENT '创建人主键（自己）',
    `is_payment` char(1) NOT NULL DEFAULT '0' COMMENT '0:收款，1：付款',
    `split_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '拆分后金额（单个账单还款金额）',
    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账单收付款对应关系';

# 收付款对应账单关系
DROP TABLE IF EXISTS `t_remain_record`;
CREATE TABLE `t_remain_record` (
   `id` char(32) NOT NULL COMMENT '主键',
   `rp_id` char(32) NOT NULL COMMENT '首付款记录主键',
   `create_id` char(32) NOT NULL COMMENT '创建人主键（自己）',
   `is_payment` char(1) NOT NULL DEFAULT '0' COMMENT '0:收款，1：付款',
   `is_used` char(1) NOT NULL DEFAULT '0' COMMENT '1:使用，0：未用',
   `remain_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '留存金额',
   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='留存记录';


alter table t_receipt_payment_record_bill drop column remain_id;
alter table t_remain_record drop column rp_id;
alter table t_remain_record drop column is_used;

# 2019-07-07
alter table t_receipt_payment_record add column `customer_id` char(32) NOT NULL COMMENT '客户主键（对方）' after create_id;

# 2019-7-15
alter table t_bill_goods modify column `weight` float(11,2) NOT NULL DEFAULT '0' COMMENT '重量';