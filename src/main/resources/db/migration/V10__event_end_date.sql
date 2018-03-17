USE `blito`;
ALTER TABLE `BlitoTest`.`event`
ADD COLUMN `end_date` DATETIME NULL DEFAULT NULL AFTER `is_private`;