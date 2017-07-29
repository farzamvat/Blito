CREATE DATABASE  IF NOT EXISTS `blito` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `blito`;
-- MySQL dump 10.13  Distrib 5.6.19, for osx10.7 (i386)
--
-- Host: 127.0.0.1    Database: Blito
-- ------------------------------------------------------
-- Server version	5.7.18

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
-- Table structure for table `artist`
--

DROP TABLE IF EXISTS `artist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `artist` (
  `group_members` text,
  `profile_id` bigint(20) NOT NULL,
  PRIMARY KEY (`profile_id`),
  CONSTRAINT `FKct0okyn56vomcstm8dt751n81` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blit`
--

DROP TABLE IF EXISTS `blit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blit` (
  `blit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank_gateway` varchar(255) DEFAULT NULL,
  `blit_type_name` varchar(255) DEFAULT NULL,
  `count` int(11) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `customer_email` varchar(255) DEFAULT NULL,
  `customer_mobile_number` varchar(255) DEFAULT NULL,
  `customer_name` varchar(255) DEFAULT NULL,
  `event_address` varchar(255) DEFAULT NULL,
  `event_date` datetime DEFAULT NULL,
  `event_date_and_time` varchar(255) DEFAULT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `payment_error` varchar(255) DEFAULT NULL,
  `payment_status` varchar(255) DEFAULT NULL,
  `ref_num` varchar(255) DEFAULT NULL,
  `saman_trace_no` varchar(255) DEFAULT NULL,
  `seat_type` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `total_amount` bigint(20) NOT NULL,
  `track_code` varchar(255) DEFAULT NULL,
  `used` bit(1) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`blit_id`),
  KEY `FK1eyi6n57440oa9jleq40j88qt` (`user_id`),
  CONSTRAINT `FK1eyi6n57440oa9jleq40j88qt` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blit_additional_fields`
--

DROP TABLE IF EXISTS `blit_additional_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blit_additional_fields` (
  `blit_blit_id` bigint(20) NOT NULL,
  `fields` varchar(255) DEFAULT NULL,
  `additional_fields_key` varchar(255) NOT NULL,
  PRIMARY KEY (`blit_blit_id`,`additional_fields_key`),
  CONSTRAINT `FKaapo078bnrif4wul7pr2l81s1` FOREIGN KEY (`blit_blit_id`) REFERENCES `blit` (`blit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blit_type`
--

DROP TABLE IF EXISTS `blit_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blit_type` (
  `blit_type_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blit_type_state` varchar(255) DEFAULT NULL,
  `capacity` int(11) NOT NULL,
  `is_free` bit(1) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `price` bigint(20) NOT NULL,
  `sold_count` int(11) NOT NULL,
  `event_date_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`blit_type_id`),
  KEY `FKtncyui7mdo9yit76oyb4ia9gg` (`event_date_id`),
  CONSTRAINT `FKtncyui7mdo9yit76oyb4ia9gg` FOREIGN KEY (`event_date_id`) REFERENCES `event_time` (`event_date_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blit_type_discounts`
--

DROP TABLE IF EXISTS `blit_type_discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blit_type_discounts` (
  `blit_types_blit_type_id` bigint(20) NOT NULL,
  `discounts_discount_id` bigint(20) NOT NULL,
  PRIMARY KEY (`blit_types_blit_type_id`,`discounts_discount_id`),
  KEY `FK4upxlf2f0qt5o3scr0s150lke` (`discounts_discount_id`),
  CONSTRAINT `FK32gfi6200cahjp7hgd917rjca` FOREIGN KEY (`blit_types_blit_type_id`) REFERENCES `blit_type` (`blit_type_id`),
  CONSTRAINT `FK4upxlf2f0qt5o3scr0s150lke` FOREIGN KEY (`discounts_discount_id`) REFERENCES `discount` (`discount_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `blit_type_seat`
--

DROP TABLE IF EXISTS `blit_type_seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `blit_type_seat` (
  `blit_type_seat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sold_date` datetime DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `blit_type_id` bigint(20) DEFAULT NULL,
  `seat_id` bigint(20) DEFAULT NULL,
  `blit_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`blit_type_seat_id`),
  KEY `FKr3sbr8gltn239n1m04uf0vajl` (`blit_type_id`),
  KEY `FKc34jht4fv1f7k9riqv1d42xj8` (`seat_id`),
  KEY `FKdfhjhiwgc11ty61rllw225e9a` (`blit_id`),
  CONSTRAINT `FKc34jht4fv1f7k9riqv1d42xj8` FOREIGN KEY (`seat_id`) REFERENCES `seat` (`seat_id`),
  CONSTRAINT `FKdfhjhiwgc11ty61rllw225e9a` FOREIGN KEY (`blit_id`) REFERENCES `seat_blit` (`blit_id`),
  CONSTRAINT `FKr3sbr8gltn239n1m04uf0vajl` FOREIGN KEY (`blit_type_id`) REFERENCES `blit_type` (`blit_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `common_blit`
--

DROP TABLE IF EXISTS `common_blit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `common_blit` (
  `blit_id` bigint(20) NOT NULL,
  `blit_type_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`blit_id`),
  KEY `FKelgncpkvugeniahcs93fio994` (`blit_type_id`),
  CONSTRAINT `FK1ciqf32ln2mt5uxgdabfas871` FOREIGN KEY (`blit_id`) REFERENCES `blit` (`blit_id`),
  CONSTRAINT `FKelgncpkvugeniahcs93fio994` FOREIGN KEY (`blit_type_id`) REFERENCES `blit_type` (`blit_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `discount` (
  `discount_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `amount` double NOT NULL,
  `code` varchar(255) DEFAULT NULL,
  `effect_date` datetime DEFAULT NULL,
  `expiration_date` datetime DEFAULT NULL,
  `is_percent` bit(1) NOT NULL,
  `percent` double NOT NULL,
  `reusability` int(11) NOT NULL,
  `used` int(11) NOT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`discount_id`),
  KEY `FK16r3lnhy77b96inm8e1wdhvgh` (`user_id`),
  CONSTRAINT `FK16r3lnhy77b96inm8e1wdhvgh` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event` (
  `event_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` text,
  `aparat_display_code` varchar(255) DEFAULT NULL,
  `blit_sale_end_date` datetime DEFAULT NULL,
  `blit_sale_start_date` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `description` text,
  `event_link` varchar(255) DEFAULT NULL,
  `event_name` varchar(255) DEFAULT NULL,
  `sold_date` datetime DEFAULT NULL,
  `event_state` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  `is_closed_init` bit(1) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `is_evento` bit(1) NOT NULL,
  `is_open_init` bit(1) NOT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `members` text,
  `operator_state` varchar(255) DEFAULT NULL,
  `order_number` int(11) NOT NULL,
  `event_host_id` bigint(20) NOT NULL,
  `views` bigint(20) NOT NULL,
  PRIMARY KEY (`event_id`),
  UNIQUE KEY `UK_7b964t681s0qajt72j7gr9ebi` (`event_link`),
  KEY `FK3i1dqb4qd1axtjsd75h321wty` (`event_host_id`),
  CONSTRAINT `FK3i1dqb4qd1axtjsd75h321wty` FOREIGN KEY (`event_host_id`) REFERENCES `event_host` (`event_host_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_additional_fields`
--

DROP TABLE IF EXISTS `event_additional_fields`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_additional_fields` (
  `event_event_id` bigint(20) NOT NULL,
  `fields` varchar(255) DEFAULT NULL,
  `additional_fields_key` varchar(255) NOT NULL,
  PRIMARY KEY (`event_event_id`,`additional_fields_key`),
  CONSTRAINT `FKt3jlnni9m8bu3weoot8xl4oqo` FOREIGN KEY (`event_event_id`) REFERENCES `event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_host`
--

DROP TABLE IF EXISTS `event_host`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_host` (
  `event_host_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `host_name` varchar(255) DEFAULT NULL,
  `event_type` varchar(255) DEFAULT NULL,
  `instagram_link` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  `linkedin_link` varchar(255) DEFAULT NULL,
  `telegram_link` varchar(255) DEFAULT NULL,
  `telephone` varchar(255) NOT NULL,
  `twitter_link` varchar(255) DEFAULT NULL,
  `website_link` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`event_host_id`),
  KEY `FK4u610t14n3errr34sh6m2x4kb` (`user_id`),
  CONSTRAINT `FK4u610t14n3errr34sh6m2x4kb` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_host_image`
--

DROP TABLE IF EXISTS `event_host_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_host_image` (
  `event_host_id` bigint(20) NOT NULL,
  `image_id` bigint(20) NOT NULL,
  PRIMARY KEY (`event_host_id`,`image_id`),
  KEY `FKrlcwmvd0yvyjj4t76rl7jmi2j` (`image_id`),
  CONSTRAINT `FKdfcen65jyynwdqoh4xec723v2` FOREIGN KEY (`event_host_id`) REFERENCES `event_host` (`event_host_id`),
  CONSTRAINT `FKrlcwmvd0yvyjj4t76rl7jmi2j` FOREIGN KEY (`image_id`) REFERENCES `image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_image`
--

DROP TABLE IF EXISTS `event_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_image` (
  `event_id` bigint(20) NOT NULL,
  `image_id` bigint(20) NOT NULL,
  PRIMARY KEY (`event_id`,`image_id`),
  KEY `FKdpft3044wsqekotrovijyi8k9` (`image_id`),
  CONSTRAINT `FK9oirj7cwmu7k91vr0m13hqh8b` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`),
  CONSTRAINT `FKdpft3044wsqekotrovijyi8k9` FOREIGN KEY (`image_id`) REFERENCES `image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_offers`
--

DROP TABLE IF EXISTS `event_offers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_offers` (
  `event_event_id` bigint(20) NOT NULL,
  `offers` varchar(255) DEFAULT NULL,
  KEY `FKdsypjvq6ld1e9fij3996y6uxb` (`event_event_id`),
  CONSTRAINT `FKdsypjvq6ld1e9fij3996y6uxb` FOREIGN KEY (`event_event_id`) REFERENCES `event` (`event_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `event_time`
--

DROP TABLE IF EXISTS `event_time`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `event_time` (
  `event_date_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `event_date_state` varchar(255) DEFAULT NULL,
  `event_id` bigint(20) DEFAULT NULL,
  `salon_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`event_date_id`),
  KEY `FKaei54puj14hjeo5ndpbk8n4os` (`event_id`),
  KEY `FK6acqr278v7ljjbcert1fxlmil` (`salon_id`),
  CONSTRAINT `FK6acqr278v7ljjbcert1fxlmil` FOREIGN KEY (`salon_id`) REFERENCES `salon` (`salon_id`),
  CONSTRAINT `FKaei54puj14hjeo5ndpbk8n4os` FOREIGN KEY (`event_id`) REFERENCES `event` (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `exchange_blit`
--

DROP TABLE IF EXISTS `exchange_blit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `exchange_blit` (
  `exchange_blit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `blit_cost` double NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `event_address` varchar(255) DEFAULT NULL,
  `event_date` datetime DEFAULT NULL,
  `exchange_blit_type` varchar(255) DEFAULT NULL,
  `exchange_link` varchar(255) DEFAULT NULL,
  `is_blito_event` bit(1) NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `operator_state` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `image_image_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`exchange_blit_id`),
  UNIQUE KEY `UK_l3k1rrvo0ycoo2gk06i0ueh4v` (`exchange_link`),
  KEY `FKe4onf1umgu713suwcfw9dysah` (`image_image_id`),
  KEY `FKnrto3jmf4865iij13qp42vssm` (`user_id`),
  CONSTRAINT `FKe4onf1umgu713suwcfw9dysah` FOREIGN KEY (`image_image_id`) REFERENCES `image` (`image_id`),
  CONSTRAINT `FKnrto3jmf4865iij13qp42vssm` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `image_type` varchar(255) DEFAULT NULL,
  `imageuuid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `index_banner`
--

DROP TABLE IF EXISTS `index_banner`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `index_banner` (
  `index_banner_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `event_event_id` bigint(20) DEFAULT NULL,
  `image_image_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`index_banner_id`),
  KEY `FKiv3fdewo1tkgvbtu9ye8k53px` (`event_event_id`),
  KEY `FKlyid7umhtkl0l1kp8cuqy6na7` (`image_image_id`),
  CONSTRAINT `FKiv3fdewo1tkgvbtu9ye8k53px` FOREIGN KEY (`event_event_id`) REFERENCES `event` (`event_id`),
  CONSTRAINT `FKlyid7umhtkl0l1kp8cuqy6na7` FOREIGN KEY (`image_image_id`) REFERENCES `image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `location`
--

DROP TABLE IF EXISTS `location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `location` (
  `address` text,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `profile_id` bigint(20) NOT NULL,
  PRIMARY KEY (`profile_id`),
  CONSTRAINT `FK4m88q6ukke7dgd2ci0v2pue42` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permission` (
  `permission_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `api_business_name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profile`
--

DROP TABLE IF EXISTS `profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile` (
  `profile_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `active` bit(1) NOT NULL,
  `biography` varchar(255) DEFAULT NULL,
  `instagram` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `profile_type` int(11) DEFAULT NULL,
  `telegram` varchar(255) DEFAULT NULL,
  `twitter` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profile_events`
--

DROP TABLE IF EXISTS `profile_events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile_events` (
  `profile_profile_id` bigint(20) NOT NULL,
  `events_event_id` bigint(20) NOT NULL,
  PRIMARY KEY (`profile_profile_id`,`events_event_id`),
  KEY `FKc64h0kl1nxqrivb5942n6qy3c` (`events_event_id`),
  CONSTRAINT `FKc64h0kl1nxqrivb5942n6qy3c` FOREIGN KEY (`events_event_id`) REFERENCES `event` (`event_id`),
  CONSTRAINT `FKfvgwx89xbhfjldcr6jowgyd8l` FOREIGN KEY (`profile_profile_id`) REFERENCES `profile` (`profile_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `profile_image`
--

DROP TABLE IF EXISTS `profile_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile_image` (
  `profile_id` bigint(20) NOT NULL,
  `image_id` bigint(20) NOT NULL,
  PRIMARY KEY (`profile_id`,`image_id`),
  KEY `FKgfa1s4pvvmg9im0mxbjq69r9a` (`image_id`),
  CONSTRAINT `FK7499uukxrqn9c6ue4g8uox6ao` FOREIGN KEY (`profile_id`) REFERENCES `profile` (`profile_id`),
  CONSTRAINT `FKgfa1s4pvvmg9im0mxbjq69r9a` FOREIGN KEY (`image_id`) REFERENCES `image` (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `role_role_id` bigint(20) NOT NULL,
  `permissions_permission_id` bigint(20) NOT NULL,
  PRIMARY KEY (`role_role_id`,`permissions_permission_id`),
  KEY `FK73b87l8a3oskqjgqsafcpbi04` (`permissions_permission_id`),
  CONSTRAINT `FK73b87l8a3oskqjgqsafcpbi04` FOREIGN KEY (`permissions_permission_id`) REFERENCES `permission` (`permission_id`),
  CONSTRAINT `FKl3mv2hosidajoildb2bchfc20` FOREIGN KEY (`role_role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `salon`
--

DROP TABLE IF EXISTS `salon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `salon` (
  `salon_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `plan_path` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`salon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seat`
--

DROP TABLE IF EXISTS `seat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seat` (
  `seat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `row_number` varchar(255) DEFAULT NULL,
  `seat_number` varchar(255) DEFAULT NULL,
  `section` varchar(255) DEFAULT NULL,
  `salon_id` bigint(20) NOT NULL,
  PRIMARY KEY (`seat_id`),
  KEY `FKk53xo69b1ctrua9j1ics7dqrk` (`salon_id`),
  CONSTRAINT `FKk53xo69b1ctrua9j1ics7dqrk` FOREIGN KEY (`salon_id`) REFERENCES `salon` (`salon_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seat_blit`
--

DROP TABLE IF EXISTS `seat_blit`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seat_blit` (
  `seats` varchar(255) DEFAULT NULL,
  `blit_id` bigint(20) NOT NULL,
  PRIMARY KEY (`blit_id`),
  CONSTRAINT `FKawpi73fkx9kdrg23u3ekyu1iw` FOREIGN KEY (`blit_id`) REFERENCES `blit` (`blit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `activation_key` varchar(255) DEFAULT NULL,
  `banned` bit(1) NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `is_active` bit(1) NOT NULL,
  `is_first_time_login` bit(1) NOT NULL,
  `is_old_user` bit(1) NOT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `reset_key` varchar(255) DEFAULT NULL,
  `wrong_try` int(11) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKa68196081fvovjhkek5m97n3y` (`role_id`),
  CONSTRAINT `FK859n2jvi8ivhui0rl0esws6o` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKa68196081fvovjhkek5m97n3y` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-07-29 18:22:19
