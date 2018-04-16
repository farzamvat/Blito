USE `blito`;
DROP TABLE IF EXISTS `address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` NVARCHAR(255) NOT NULL,
  `address` text,
  `latitude` double DEFAULT NULL,
  `longitude` double DEFAULT NULL,
  `event_host_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKtp245rk0ucbe7tfu06gg2riaa` (`event_host_id`),
  CONSTRAINT `FKtp245rk0ucbe7tfu06gg2riaa` FOREIGN KEY (`event_host_id`) REFERENCES `event_host` (`event_host_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;