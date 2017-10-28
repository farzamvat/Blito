USE `blito`;
DROP TABLE IF EXISTS `section`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `section` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `section_svg` text,
  `section_uid` varchar(255) DEFAULT NULL,
  `salon_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2ytmwrqut92tx8q38qt97w5gx` (`salon_id`),
  CONSTRAINT `FK2ytmwrqut92tx8q38qt97w5gx` FOREIGN KEY (`salon_id`) REFERENCES `salon` (`salon_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;