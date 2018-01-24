USE `blito`;
ALTER TABLE `blito`.`user`
ADD COLUMN `activation_retry_sent_date` DATETIME NULL AFTER `wrong_try`;
