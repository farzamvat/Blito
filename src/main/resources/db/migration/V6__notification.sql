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
  INDEX `sender_id_idx` (`sender_id` ASC),
  INDEX `receiver_id_idx` (`receiver_id` ASC),
  CONSTRAINT `sender_id`
    FOREIGN KEY (`sender_id`)
    REFERENCES `blito`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `receiver_id`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `blito`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);



