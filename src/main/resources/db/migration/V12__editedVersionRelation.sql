USE `blito`;
ALTER TABLE `blito`.`event` 
ADD COLUMN `edited_version_event_id` BIGINT(20) NULL DEFAULT NULL AFTER `end_date`,
ADD INDEX `FK788ip51uh9bh5qesppbkj1nai_idx` (`edited_version_event_id` ASC);
ALTER TABLE `blito`.`event` 
ADD CONSTRAINT `FK788ip51uh9bh5qesppbkj1nai`
  FOREIGN KEY (`edited_version_event_id`)
  REFERENCES `blito`.`event` (`event_id`)
  ON DELETE RESTRICT
  ON UPDATE RESTRICT;
