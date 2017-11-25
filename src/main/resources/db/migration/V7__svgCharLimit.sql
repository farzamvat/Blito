USE `blito`;
ALTER TABLE `blito`.`salon`
CHANGE COLUMN `salon_svg` `salon_svg` LONGTEXT NULL DEFAULT NULL ;
ALTER TABLE `blito`.`section`
CHANGE COLUMN `section_svg` `section_svg` LONGTEXT NULL DEFAULT NULL ;