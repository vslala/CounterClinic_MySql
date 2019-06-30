-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 30, 2019 at 09:26 AM
-- Server version: 10.1.40-MariaDB
-- PHP Version: 7.3.5

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `counterclinicdb`
--
CREATE DATABASE IF NOT EXISTS `counterclinicdb` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `counterclinicdb`;

-- --------------------------------------------------------

--
-- Table structure for table `appointment_payment`
--

DROP TABLE IF EXISTS `appointment_payment`;
CREATE TABLE IF NOT EXISTS `appointment_payment` (
  `payment_id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_status` tinyint(1) NOT NULL DEFAULT '0',
  `payment_amount` float NOT NULL DEFAULT '0',
  PRIMARY KEY (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `clinics`
--

DROP TABLE IF EXISTS `clinics`;
CREATE TABLE IF NOT EXISTS `clinics` (
  `clinic_id` int(11) NOT NULL AUTO_INCREMENT,
  `clinic_name` varchar(255) NOT NULL,
  `clinic_address` varchar(200) DEFAULT NULL,
  `clinic_fees_tax` float DEFAULT NULL,
  PRIMARY KEY (`clinic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `clinic_rooms`
--

DROP TABLE IF EXISTS `clinic_rooms`;
CREATE TABLE IF NOT EXISTS `clinic_rooms` (
  `clinic_room_id` int(11) NOT NULL AUTO_INCREMENT,
  `clinic_id` int(11) NOT NULL,
  `room_name` varchar(255) NOT NULL,
  PRIMARY KEY (`clinic_room_id`),
  KEY `FK_CR_CID` (`clinic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `doctor_booked_slots`
--

DROP TABLE IF EXISTS `doctor_booked_slots`;
CREATE TABLE IF NOT EXISTS `doctor_booked_slots` (
  `doctor_id` int(11) NOT NULL,
  `appointment_date` date NOT NULL,
  `booked_slot_id` int(11) NOT NULL,
  KEY `FK_DBS_UID` (`doctor_id`),
  KEY `FK_DBS_SLOTID` (`booked_slot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `doctor_slots`
--

DROP TABLE IF EXISTS `doctor_slots`;
CREATE TABLE IF NOT EXISTS `doctor_slots` (
  `doctor_id` int(11) NOT NULL,
  `day_of_week` enum('MONDAY','TUESDAY','WEDNESDAY','THURSDAY','FRIDAY','SATURDAY','SUNDAY') NOT NULL,
  `slot_id` int(11) NOT NULL,
  KEY `FK_DS_UID` (`doctor_id`),
  KEY `FK_DS_SLOTID` (`slot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `online_appointments`
--

DROP TABLE IF EXISTS `online_appointments`;
CREATE TABLE IF NOT EXISTS `online_appointments` (
  `online_appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_id` int(11) NOT NULL,
  `patient_id` int(11) NOT NULL,
  `appointment_date` date NOT NULL,
  `slot_id` int(11) NOT NULL,
  `additional_comments` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `payment_id` int(11) NOT NULL,
  PRIMARY KEY (`online_appointment_id`),
  KEY `FK_OA_DUID` (`doctor_id`),
  KEY `FK_OA_PUID` (`patient_id`),
  KEY `FK_OA_SLOTID` (`slot_id`),
  KEY `payment_id` (`payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `qrcode_attachments`
--

DROP TABLE IF EXISTS `qrcode_attachments`;
CREATE TABLE IF NOT EXISTS `qrcode_attachments` (
  `qrcode_id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_id` int(11) NOT NULL,
  `height` int(11) NOT NULL,
  `width` int(11) NOT NULL,
  `image_name` varchar(200) NOT NULL,
  `image_file_path` varchar(500) NOT NULL,
  `image_url_path` varchar(500) NOT NULL,
  `qrcode_data` varchar(1000) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`qrcode_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `slots`
--

DROP TABLE IF EXISTS `slots`;
CREATE TABLE IF NOT EXISTS `slots` (
  `slot_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` time NOT NULL,
  `end_time` time NOT NULL,
  PRIMARY KEY (`slot_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `mobile` varchar(20) NOT NULL,
  `username` varchar(55) NOT NULL,
  `preferred_language` varchar(55) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users_login`
--

DROP TABLE IF EXISTS `users_login`;
CREATE TABLE IF NOT EXISTS `users_login` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `username` varchar(55) NOT NULL,
  `password` text NOT NULL,
  `logged_in_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `user_id` (`user_id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `users_meta`
--

DROP TABLE IF EXISTS `users_meta`;
CREATE TABLE IF NOT EXISTS `users_meta` (
  `meta_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `meta_key` varchar(255) NOT NULL,
  `meta_value` text NOT NULL,
  PRIMARY KEY (`meta_id`),
  UNIQUE KEY `user_id` (`user_id`,`meta_key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
CREATE TABLE IF NOT EXISTS `user_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `role_name` enum('DOCTOR','PATIENT','RECEPTIONIST','ADMIN','SUPER_ADMIN') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_UR_UID` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `walkin_appointments`
--

DROP TABLE IF EXISTS `walkin_appointments`;
CREATE TABLE IF NOT EXISTS `walkin_appointments` (
  `walkin_appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_first_name` varchar(255) NOT NULL,
  `patient_last_name` varchar(255) NOT NULL,
  `appointed_doctor_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`walkin_appointment_id`),
  KEY `FK_WA_DUID` (`appointed_doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `walkin_appointment_status`
--

DROP TABLE IF EXISTS `walkin_appointment_status`;
CREATE TABLE IF NOT EXISTS `walkin_appointment_status` (
  `walkin_appointment_status_id` int(11) NOT NULL AUTO_INCREMENT,
  `current_appointment_id` int(11) NOT NULL,
  `doctor_id` int(11) NOT NULL,
  `avg_wait_time` int(11) NOT NULL,
  `appointment_start_datetime` datetime NOT NULL,
  `doctor_break_duration` int(11) NOT NULL,
  `patients_in_visited_queue` int(11) NOT NULL,
  PRIMARY KEY (`walkin_appointment_status_id`),
  KEY `FK_WAS_DUID` (`doctor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `clinic_rooms`
--
ALTER TABLE `clinic_rooms`
  ADD CONSTRAINT `FK_CR_CID` FOREIGN KEY (`clinic_id`) REFERENCES `clinics` (`clinic_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `doctor_booked_slots`
--
ALTER TABLE `doctor_booked_slots`
  ADD CONSTRAINT `FK_DBS_SLOTID` FOREIGN KEY (`booked_slot_id`) REFERENCES `slots` (`slot_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_DBS_UID` FOREIGN KEY (`doctor_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `doctor_slots`
--
ALTER TABLE `doctor_slots`
  ADD CONSTRAINT `FK_DS_SLOTID` FOREIGN KEY (`slot_id`) REFERENCES `slots` (`slot_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_DS_UID` FOREIGN KEY (`doctor_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `online_appointments`
--
ALTER TABLE `online_appointments`
  ADD CONSTRAINT `FK_OA_DUID` FOREIGN KEY (`doctor_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_OA_PUID` FOREIGN KEY (`patient_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_OA_SLOTID` FOREIGN KEY (`slot_id`) REFERENCES `slots` (`slot_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users_login`
--
ALTER TABLE `users_login`
  ADD CONSTRAINT `FK_UL_UID` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_UL_UNAME` FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `users_meta`
--
ALTER TABLE `users_meta`
  ADD CONSTRAINT `FK_UM_UID` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FK_UR_UID` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `walkin_appointments`
--
ALTER TABLE `walkin_appointments`
  ADD CONSTRAINT `FK_WA_DUID` FOREIGN KEY (`appointed_doctor_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `walkin_appointment_status`
--
ALTER TABLE `walkin_appointment_status`
  ADD CONSTRAINT `FK_WAS_DUID` FOREIGN KEY (`doctor_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_WAS_WAID` FOREIGN KEY (`current_appointment_id`) REFERENCES `walkin_appointments` (`walkin_appointment_id`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
