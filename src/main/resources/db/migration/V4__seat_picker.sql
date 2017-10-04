USE `blito`;
ALTER TABLE `blito`.`salon`
  ADD COLUMN `salon_uid` VARCHAR(255) NULL
  AFTER `plan_path`;
ALTER TABLE `blito`.`seat`
  CHANGE COLUMN `row_number` `row_name` VARCHAR(255) NULL DEFAULT NULL,
  CHANGE COLUMN `seat_number` `seat_name` VARCHAR(255) NULL DEFAULT NULL,
  CHANGE COLUMN `section` `section_name` VARCHAR(255) NULL DEFAULT NULL,
  ADD COLUMN `section_uid` VARCHAR(255) NULL
  AFTER `salon_id`,
  ADD COLUMN `seat_uid` VARCHAR(255) NULL
  AFTER `section_uid`,
  ADD COLUMN `row_uid` VARCHAR(255) NULL
  AFTER `seat_uid`;
