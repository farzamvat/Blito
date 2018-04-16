USE `blito`;
ALTER TABLE `blito`.`event_time`
ADD COLUMN `uid` VARCHAR(255) NULL AFTER `salon_id`;

ALTER TABLE `blito`.`blit_type`
ADD COLUMN `uid` VARCHAR(255) NULL AFTER `event_date_id`;

