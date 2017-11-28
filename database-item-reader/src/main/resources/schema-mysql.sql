DROP TABLE IF EXISTS `springbootdb`.`customer`;

CREATE TABLE IF NOT EXISTS `springbootdb`.`customer` (
  `id` INT(8) PRIMARY KEY,
  `first_name` VARCHAR(100),
  `last_name` VARCHAR(100),
  `random`  VARCHAR(250)
);