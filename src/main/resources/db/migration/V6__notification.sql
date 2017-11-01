USE `blito`;
CREATE TABLE `blito`.`notification` (
  `notification_id` BIGINT(20) NOT NULL,
  `message` TEXT(500) NULL,
  `seen` BIT(1) NULL,
  `date` DATETIME NULL,
  `notification_type` VARCHAR(45) NULL,
  `receiver_id` BIGINT(20) NULL,
  `sender_id` BIGINT(20) NULL,
  PRIMARY KEY (`notification_id`),
  INDEX `user_id_idx` (`receiver_id` ASC),
  INDEX `user_id_idx1` (`sender_id` ASC),
  CONSTRAINT `receiver_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `blito`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `sender_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `blito`.`user` (`user_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

