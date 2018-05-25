USE `blito`;
ALTER TABLE `blito`.`event_time`
ADD COLUMN `date_time` VARCHAR(255) NULL AFTER `salon_id`;
