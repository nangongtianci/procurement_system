-- MySQL dump 10.13  Distrib 5.7.21, for Linux (x86_64)
--
-- Host: localhost    Database: procurement_system
-- ------------------------------------------------------
-- Server version	5.7.21-1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_apply_loan_record`
--

DROP TABLE IF EXISTS `t_apply_loan_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_apply_loan_record` (
  `id` char(32) NOT NULL COMMENT '主键',
  `need_money` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '需求金额',
  `use_cycle` int(1) NOT NULL DEFAULT '0' COMMENT '使用周期',
  `accept_rate` float NOT NULL DEFAULT '0' COMMENT '可接受利率',
  `mortgage_thing` enum('none','house','car','goods') NOT NULL COMMENT '可抵押物品（无，房子，车，货品）',
  `purpose` varchar(100) NOT NULL DEFAULT '' COMMENT '资金用途',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_customer_id` varchar(32) NOT NULL COMMENT '创建人主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='申请贷款记录';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_bill`
--

DROP TABLE IF EXISTS `t_bill`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_bill` (
  `id` char(32) NOT NULL COMMENT '主键',
  `customer_name` varchar(45) DEFAULT '' COMMENT '客户名称',
  `customer_phone` varchar(15) DEFAULT '' COMMENT '客户电话',
  `customer_id_card` varchar(18) DEFAULT '' COMMENT '客户身份证号',
  `city_name` varchar(80) DEFAULT '' COMMENT '客户所在县城',
  `market_name` varchar(80) DEFAULT '' COMMENT '客户所在市场',
  `customer_unit` varchar(100) DEFAULT '' COMMENT '客户单位名称',
  `bill_date` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记账日期',
  `business_status` enum('in','out','profit','loss','other') DEFAULT NULL COMMENT '交易状态(买入，卖出，盘赢，盘损，其他费用)',
  `total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '总价格',
  `bill_status` enum('paid','received','payable','receivable') DEFAULT NULL COMMENT '账单状态（已付，已收，应付，应收）',
  `expect_pay_days` int(1) DEFAULT '0' COMMENT '账期天数',
  `remark` varchar(200) DEFAULT '' COMMENT '备注',
  `is_source` enum('no','yes') NOT NULL COMMENT '是否公开溯源信息（否，是）',
  `transaction_address` varchar(100) NOT NULL DEFAULT '' COMMENT '交易地点',
  `create_customer_id` varchar(45) NOT NULL COMMENT '创建人主键',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_time` datetime NOT NULL COMMENT '更新日期',
  `bill_sn` char(20) NOT NULL DEFAULT '' COMMENT '账单号',
  `is_peer_bill` enum('no','yes') NOT NULL COMMENT '是否为对等账单(否，是)',
  `is_receive` enum('no','yes') NOT NULL DEFAULT 'no' COMMENT '是否已领取虚拟币（否，是）',
  `freight` decimal(10,0) NOT NULL DEFAULT '0' COMMENT '运费',
  `actual_total_price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '实收总价格',
  `bill_sn_type` enum('scan','manual') DEFAULT NULL COMMENT '扫描，手动',
  `pid` char(32) DEFAULT '' COMMENT '父主键',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1:置顶，0：不置顶',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='账单';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_code_manage`
--

DROP TABLE IF EXISTS `t_code_manage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_code_manage` (
  `id` char(32) NOT NULL COMMENT '主键',
  `code` tinyint(1) NOT NULL DEFAULT '0' COMMENT '代码编号',
  `name` varchar(16) NOT NULL DEFAULT '' COMMENT '产品名称',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '价格',
  `product_place` varchar(12) DEFAULT '' COMMENT '产地',
  `product_spec` varchar(18) DEFAULT '' COMMENT '产品规格',
  `product_img_path` varchar(200) DEFAULT '' COMMENT '产品图片',
  `create_customer_id` char(32) NOT NULL DEFAULT '' COMMENT '创建人主键',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `status` enum('yes','no') NOT NULL DEFAULT 'no' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代码管理表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_customer`
--

DROP TABLE IF EXISTS `t_customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_customer` (
  `id` char(32) NOT NULL COMMENT '主键',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '用户姓名',
  `password` varchar(32) NOT NULL DEFAULT '' COMMENT '密码',
  `secret_key` varchar(32) NOT NULL DEFAULT '' COMMENT '秘钥key',
  `phone` varchar(11) NOT NULL DEFAULT '' COMMENT '常用手机号',
  `id_card` varchar(18) NOT NULL DEFAULT '' COMMENT '身份证',
  `company_name` varchar(80) DEFAULT '' COMMENT '公司名称(单位名称)',
  `market_name` varchar(45) DEFAULT '' COMMENT '市场名称',
  `email` varchar(100) DEFAULT '' COMMENT '邮箱',
  `id_card_path` varchar(300) DEFAULT '' COMMENT '证件路径,2张图片以逗号相分割',
  `is_agree_protocol` enum('no','yes') NOT NULL COMMENT '是否同意协议（yes:同意，no:不同意）',
  `login_time` datetime DEFAULT NULL COMMENT '登录时间',
  `status` enum('no','yes') NOT NULL COMMENT '是否在线(yes:在线,no:不在线)',
  `red_packet` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '红包金额',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_customer_bill_link`
--

DROP TABLE IF EXISTS `t_customer_bill_link`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_customer_bill_link` (
  `id` char(32) NOT NULL COMMENT '客户账单关联主键',
  `cid` varchar(32) NOT NULL COMMENT '客户主键',
  `bid` varchar(32) NOT NULL COMMENT '账单主键',
  `master_share` enum('master','share') NOT NULL DEFAULT 'master' COMMENT '是否来自共享（主，共享）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='客户账单关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_customer_virtual_coin`
--

DROP TABLE IF EXISTS `t_customer_virtual_coin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_customer_virtual_coin` (
  `id` char(32) NOT NULL COMMENT '主键',
  `customer_id` varchar(45) NOT NULL COMMENT '客户主键',
  `virtual_coin_count` int(11) NOT NULL DEFAULT '0' COMMENT '虚拟币个数',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_time` datetime NOT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与虚拟币关系';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dept`
--

DROP TABLE IF EXISTS `t_dept`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dept_employee_rel`
--

DROP TABLE IF EXISTS `t_dept_employee_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_dept_employee_rel` (
  `id` char(32) NOT NULL COMMENT '主键',
  `did` varchar(32) NOT NULL DEFAULT '' COMMENT '部门主键',
  `eid` varchar(32) NOT NULL DEFAULT '' COMMENT '员工主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门员工关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_dict`
--

DROP TABLE IF EXISTS `t_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_dict` (
  `id` char(32) NOT NULL COMMENT '商品主键',
  `pid` varchar(32) NOT NULL DEFAULT '' COMMENT '父级字典id',
  `name` varchar(255) NOT NULL COMMENT '名称',
  `code` varchar(255) NOT NULL COMMENT '编码',
  `desc` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_employee`
--

DROP TABLE IF EXISTS `t_employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_employee` (
  `id` char(32) NOT NULL COMMENT '员工主键',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `account` varchar(45) NOT NULL COMMENT '账号',
  `password` varchar(45) NOT NULL COMMENT '密码',
  `salt` varchar(45) NOT NULL COMMENT 'md5密码盐',
  `name` varchar(45) NOT NULL COMMENT '名字',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` varchar(32) NOT NULL COMMENT '性别(字典)',
  `email` varchar(45) NOT NULL COMMENT '电子邮件',
  `phone` varchar(45) NOT NULL COMMENT '电话',
  `status` varchar(32) NOT NULL COMMENT '状态(字典)',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='员工表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_global_goods`
--

DROP TABLE IF EXISTS `t_global_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_global_goods_category`
--

DROP TABLE IF EXISTS `t_global_goods_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_global_goods_category_rel`
--

DROP TABLE IF EXISTS `t_global_goods_category_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_global_goods_category_rel` (
  `id` char(32) NOT NULL COMMENT '主键',
  `gid` varchar(32) NOT NULL DEFAULT '' COMMENT '全局商品主键',
  `cid` varchar(32) NOT NULL DEFAULT '' COMMENT '全局商品分类主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='全局商品分类关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_goods`
--

DROP TABLE IF EXISTS `t_goods`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_goods` (
  `id` char(32) NOT NULL COMMENT '商品主键',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '品名',
  `price` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',
  `weight_unit` enum('ton','kilo','box','sheet','catty') NOT NULL COMMENT '商品单位（吨，公斤，箱,单,斤）',
  `security_detection_info` varchar(100) DEFAULT '' COMMENT '安全信息检测信息',
  `bill_id` varchar(32) NOT NULL COMMENT '账单主键（一对多关系）',
  `create_customer_id` varchar(32) NOT NULL COMMENT '创建人主键',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `number` float(11,2) NOT NULL DEFAULT '0.00' COMMENT '商品数量',
  `amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '合计',
  `code_img_path` varchar(200) DEFAULT '' COMMENT '产品管理图片',
  `cmid` char(32) DEFAULT '' COMMENT '代码管理主键',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='订单商品信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_source_image`
--

DROP TABLE IF EXISTS `t_source_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_source_image` (
  `id` char(32) NOT NULL COMMENT '主键',
  `gid` char(32) NOT NULL COMMENT '资源图片组主键',
  `name` varchar(100) NOT NULL COMMENT '图片名称',
  `desc` varchar(500) DEFAULT NULL COMMENT '图片描述',
  `url` varchar(300) NOT NULL COMMENT '图片路径',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统资源图片表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `t_source_image_group`
--

DROP TABLE IF EXISTS `t_source_image_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_source_image_group` (
  `id` char(32) NOT NULL COMMENT '主键',
  `name` varchar(100) NOT NULL COMMENT '组名称',
  `desc` varchar(500) DEFAULT NULL COMMENT '组描述',
  `sort` smallint(5) unsigned NOT NULL DEFAULT '0' COMMENT '排序',
  `create_user` char(32) NOT NULL COMMENT '创建人',
  `update_user` char(32) NOT NULL COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统资源图片组表';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-04-28 13:08:03