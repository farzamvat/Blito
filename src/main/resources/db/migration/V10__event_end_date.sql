USE `blito`;
ALTER TABLE `blito`.`event`
ADD COLUMN `end_date` DATETIME NULL DEFAULT NULL AFTER `is_private`;