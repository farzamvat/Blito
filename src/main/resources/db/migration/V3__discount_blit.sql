USE `blito`;
ALTER TABLE `blito`.`blit`
ADD COLUMN `discount_code` VARCHAR(255) NULL DEFAULT NULL AFTER `user_id`,
ADD COLUMN `primary_amount` BIGINT(20) NULL DEFAULT NULL AFTER `discount_code`;
ALTER TABLE `blito`.`discount`
CHANGE COLUMN `amount` `amount` BIGINT(20) NOT NULL ,
CHANGE COLUMN `percent` `percentage` DOUBLE NOT NULL ,
ADD COLUMN `is_enabled` BIT(1) NULL AFTER `user_id`;
