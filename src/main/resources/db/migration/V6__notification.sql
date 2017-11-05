USE `blito`;
DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `notification_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `date` datetime DEFAULT NULL,
  `message` text,
  `notification_type` varchar(255) DEFAULT NULL,
  `seen` bit(1) DEFAULT NULL,
  `receiver_id` bigint(20) NOT NULL,
  `sender_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`notification_id`),
  KEY `FKmlidwdldgmdw67l7pbrval0un` (`receiver_id`),
  KEY `FKnbt1hengkgjqru2q44q8rlc2c` (`sender_id`),
  CONSTRAINT `FKmlidwdldgmdw67l7pbrval0un` FOREIGN KEY (`receiver_id`) REFERENCES `user` (`user_id`),
  CONSTRAINT `FKnbt1hengkgjqru2q44q8rlc2c` FOREIGN KEY (`sender_id`) REFERENCES `user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



