-- MySQL Script generated by MySQL Workbench
-- Thu Mar 16 14:22:26 2023
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema INFO2413DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema INFO2413DB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `INFO2413DB` DEFAULT CHARACTER SET utf8 ;
USE `INFO2413DB` ;

-- -----------------------------------------------------
-- Table `INFO2413DB`.`Account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`Account` (
  `email` VARCHAR(50) NOT NULL,
  `passwordHash` TEXT(512) NOT NULL,
  `firstName` VARCHAR(20) NULL,
  `lastName` VARCHAR(30) NULL,
  `phoneNo` CHAR(12) NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`StaffAccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`StaffAccount` (
  `email` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `staffEmail`
    FOREIGN KEY (`email`)
    REFERENCES `INFO2413DB`.`Account` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`PatientAccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`PatientAccount` (
  `email` VARCHAR(50) NOT NULL,
  `verifyingStaffMember` VARCHAR(50) NULL,
  `address` VARCHAR(100) NULL,
  `healthNo` CHAR(10) NULL,
  PRIMARY KEY (`email`),
  INDEX `healthNo_UNIQUE` (`healthNo` ASC) VISIBLE,
  INDEX `verifiedBy_idx` (`verifyingStaffMember` ASC) VISIBLE,
  CONSTRAINT `patientEmail`
    FOREIGN KEY (`email`)
    REFERENCES `INFO2413DB`.`Account` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `verifiedBy`
    FOREIGN KEY (`verifyingStaffMember`)
    REFERENCES `INFO2413DB`.`StaffAccount` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`DoctorAccount`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`DoctorAccount` (
  `email` VARCHAR(50) NOT NULL,
  `profile` VARCHAR(300) NULL,
  PRIMARY KEY (`email`),
  CONSTRAINT `doctorEmail`
    FOREIGN KEY (`email`)
    REFERENCES `INFO2413DB`.`Account` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`ConfidentialRecord`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`ConfidentialRecord` (
  `recordID` INT NOT NULL AUTO_INCREMENT,
  `relatedPatient` VARCHAR(50) NULL,
  `relatedDoctor` VARCHAR(50) NULL,
  `createdTimeStamp` TIMESTAMP,
  PRIMARY KEY (`recordID`),
  INDEX `relatedToPatient_idx` (`relatedPatient` ASC) VISIBLE,
  INDEX `relatedToDoctor_idx` (`relatedDoctor` ASC) VISIBLE,
  CONSTRAINT `relatedToPatient`
    FOREIGN KEY (`relatedPatient`)
    REFERENCES `INFO2413DB`.`PatientAccount` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `relatedToDoctor`
    FOREIGN KEY (`relatedDoctor`)
    REFERENCES `INFO2413DB`.`DoctorAccount` (`email`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`VisitRecord`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`VisitRecord` (
  `recordID` INT NOT NULL,
  `date` DATE NULL,
  PRIMARY KEY (`recordID`),
  CONSTRAINT `visitID`
    FOREIGN KEY (`recordID`)
    REFERENCES `INFO2413DB`.`ConfidentialRecord` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`Prescription`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`Prescription` (
  `recordID` INT NOT NULL,
  `relatedVisitRecord` INT NULL,
  `medName` VARCHAR(45) NULL,
  `medQuantity` VARCHAR(45) NULL,
  `medDose` VARCHAR(45) NULL,
  `refillable` TINYINT NULL,
  PRIMARY KEY (`recordID`),
  INDEX `prescribedDuring_idx` (`relatedVisitRecord` ASC) VISIBLE,
  CONSTRAINT `prescriptionID`
    FOREIGN KEY (`recordID`)
    REFERENCES `INFO2413DB`.`ConfidentialRecord` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `prescribedDuring`
    FOREIGN KEY (`relatedVisitRecord`)
    REFERENCES `INFO2413DB`.`VisitRecord` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`LabExam`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`LabExam` (
  `recordID` INT NOT NULL,
  `relatedVisitRecord` INT NULL,
  `date` DATE NULL,
  `examItem` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`recordID`),
  INDEX `examPrescribedDuring_idx` (`relatedVisitRecord` ASC) VISIBLE,
  CONSTRAINT `examID`
    FOREIGN KEY (`recordID`)
    REFERENCES `INFO2413DB`.`ConfidentialRecord` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `examPrescribedDuring`
    FOREIGN KEY (`relatedVisitRecord`)
    REFERENCES `INFO2413DB`.`VisitRecord` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `INFO2413DB`.`LabExamResult`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `INFO2413DB`.`LabExamResult` (
  `recordID` INT NOT NULL,
  `relatedLabExam` INT NOT NULL,
  `result` INT NULL,
  `upperBound` INT NULL,
  `lowerBound` INT NULL,
  PRIMARY KEY (`recordID`),
  INDEX `resultForExam_idx` (`relatedLabExam` ASC) VISIBLE,
  CONSTRAINT `resultID`
    FOREIGN KEY (`recordID`)
    REFERENCES `INFO2413DB`.`ConfidentialRecord` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `resultForExam`
    FOREIGN KEY (`relatedLabExam`)
    REFERENCES `INFO2413DB`.`LabExam` (`recordID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
