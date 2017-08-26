USE `blito`;
ALTER TABLE `blito`.`event` 
ADD COLUMN `is_private` BIT(1) NOT NULL DEFAULT 0 AFTER `event_host_id`;
