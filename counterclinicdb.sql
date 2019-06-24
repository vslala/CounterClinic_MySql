-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 24, 2019 at 07:46 PM
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clinics`
--

INSERT INTO `clinics` (`clinic_id`, `clinic_name`, `clinic_address`, `clinic_fees_tax`) VALUES
(1, 'TestClinic', 'Test Address 1', 12),
(2, 'TestClinic', 'Test Address 2', 10);

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `clinic_rooms`
--

INSERT INTO `clinic_rooms` (`clinic_room_id`, `clinic_id`, `room_name`) VALUES
(2, 1, 'TestClinicRoom-A'),
(3, 1, 'TestClinicRoom-B'),
(4, 1, 'TestClinicRoom-A'),
(5, 1, 'TestClinicRoom-B');

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

--
-- Dumping data for table `doctor_slots`
--

INSERT INTO `doctor_slots` (`doctor_id`, `day_of_week`, `slot_id`) VALUES
(1, 'SATURDAY', 1),
(1, 'SATURDAY', 2),
(1, 'SATURDAY', 3);

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `online_appointments`
--

INSERT INTO `online_appointments` (`online_appointment_id`, `doctor_id`, `patient_id`, `appointment_date`, `slot_id`, `additional_comments`, `created_at`, `payment_id`) VALUES
(1, 1, 2, '2019-06-21', 1, NULL, '0000-00-00 00:00:00', 0);

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `qrcode_attachments`
--

INSERT INTO `qrcode_attachments` (`qrcode_id`, `appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`, `created_at`) VALUES
(1, 1, 300, 300, '1561284774853.png', 'src/main/resources/static/qrcode/1561284774853.png', 'qrcode/1561284774853.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 1\r\n}', '2019-06-23 15:42:55'),
(2, 2, 300, 300, '1561286911334.png', 'src/main/resources/static/qrcode/1561286911334.png', 'qrcode/1561286911334.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 2\r\n}', '2019-06-23 16:18:31'),
(3, 3, 300, 300, '1561289804787.png', 'src/main/resources/static/qrcode/1561289804787.png', 'qrcode/1561289804787.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 3\r\n}', '2019-06-23 17:06:44'),
(4, 4, 300, 300, '1561295357671.png', 'src/main/resources/static/qrcode/1561295357671.png', 'qrcode/1561295357671.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 4\r\n}', '2019-06-23 18:39:17'),
(5, 5, 300, 300, '1561295438335.png', 'src/main/resources/static/qrcode/1561295438335.png', 'qrcode/1561295438335.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 5\r\n}', '2019-06-23 18:40:38'),
(6, 6, 300, 300, '1561295539543.png', 'src/main/resources/static/qrcode/1561295539543.png', 'qrcode/1561295539543.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 6\r\n}', '2019-06-23 18:42:19'),
(7, 7, 300, 300, '1561316982215.png', 'src/main/resources/static/qrcode/1561316982215.png', 'qrcode/1561316982215.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 7\r\n}', '2019-06-24 00:39:42'),
(8, 8, 300, 300, '1561382307364.png', 'src/main/resources/static/qrcode/1561382307364.png', 'qrcode/1561382307364.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 8\r\n}', '2019-06-24 18:48:27'),
(9, 9, 300, 300, '1561382488597.png', 'src/main/resources/static/qrcode/1561382488597.png', 'qrcode/1561382488597.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 9\r\n}', '2019-06-24 18:51:28'),
(10, 10, 300, 300, '1561383817991.png', 'src/main/resources/static/qrcode/1561383817991.png', 'qrcode/1561383817991.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 10\r\n}', '2019-06-24 19:13:38'),
(11, 11, 300, 300, '1561384120547.png', 'src/main/resources/static/qrcode/1561384120547.png', 'qrcode/1561384120547.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 11\r\n}', '2019-06-24 19:18:40'),
(12, 12, 300, 300, '1561384234863.png', 'src/main/resources/static/qrcode/1561384234863.png', 'qrcode/1561384234863.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 12\r\n}', '2019-06-24 19:20:34'),
(13, 13, 300, 300, '1561384526596.png', 'src/main/resources/static/qrcode/1561384526596.png', 'qrcode/1561384526596.png', '{\r\n  \"appointedDoctorId\" : 1,\r\n  \"appointmentId\" : 13\r\n}', '2019-06-24 19:25:26');

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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `slots`
--

INSERT INTO `slots` (`slot_id`, `start_time`, `end_time`) VALUES
(1, '09:00:00', '09:30:00'),
(2, '09:30:00', '10:00:00'),
(3, '10:30:00', '11:00:00');

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `first_name`, `last_name`, `email`, `mobile`, `username`, `preferred_language`, `created_at`) VALUES
(1, 'Priyanka', 'Yadav', 'pvrano@gmail.com', '7773456789', 'pvrano', 'ENGLISH', '2019-06-12 22:48:56'),
(2, '2740f', 'cbdf573226', 'e6b090@gmail.com', '0c8e624002', '806cd', 'ENGLISH', '2019-06-15 22:06:53'),
(3, 'Varun', 'Shrivastava', 'varunshrivastava007@gmail.com', '9960543885', 'vslala', 'ENGLISH', '2019-06-16 00:10:02'),
(5, 'ram', 'kumar', 'test@test.com', '65438973214', 'ramk', 'Arabic', NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users_login`
--

INSERT INTO `users_login` (`ID`, `user_id`, `username`, `password`, `logged_in_at`) VALUES
(1, 1, 'vslala', '???Z?A\\C?}?????V0?k??[???', '2019-06-15 18:40:02');

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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users_meta`
--

INSERT INTO `users_meta` (`meta_id`, `user_id`, `meta_key`, `meta_value`) VALUES
(1, 1, 'user_role', 'DOCTOR'),
(2, 2, 'user_role', 'RECEPTIONIST'),
(5, 1, 'assigned_clinic_room', '3'),
(6, 1, 'doctor_fees', '200'),
(7, 1, 'clinic_id', '1');

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
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user_roles`
--

INSERT INTO `user_roles` (`id`, `user_id`, `role_name`) VALUES
(1, 1, 'DOCTOR'),
(2, 2, 'RECEPTIONIST'),
(3, 3, 'SUPER_ADMIN'),
(4, 3, 'ADMIN'),
(5, 5, 'ADMIN');

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
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=latin1;

--
-- Dumping data for table `walkin_appointments`
--

INSERT INTO `walkin_appointments` (`walkin_appointment_id`, `patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `created_at`) VALUES
(1, 'John', 'Doe', 1, '2019-06-23 15:42:54'),
(2, 'Paul', 'Andrew', 1, '2019-06-23 16:18:31'),
(3, 'Susan', 'Nickson', 1, '2019-06-23 17:06:44'),
(4, 'Clark', 'Michael', 1, '2019-06-23 18:39:17'),
(5, 'Jeniffer', 'Aniston', 1, '2019-06-23 18:40:38'),
(6, 'Ritika', 'Bali', 1, '2019-06-23 18:42:19'),
(7, 'Nita', 'Ambani', 1, '2019-06-24 00:39:42'),
(8, 'Aakruti', 'Shah', 1, '2019-06-24 18:48:27'),
(9, 'Kishore', 'Kumar', 1, '2019-06-24 18:51:28'),
(10, 'Kedar', 'Nath', 1, '2019-06-24 19:13:37'),
(11, 'Pranab', 'Mukherjee', 1, '2019-06-24 19:18:40'),
(12, 'Kim', 'Kardashian', 1, '2019-06-24 19:20:34'),
(13, 'King', 'Super', 1, '2019-06-24 19:25:26');

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
  KEY `FK_WAS_WAID` (`current_appointment_id`),
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
  ADD CONSTRAINT `FK_UL_UID` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`),
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
  ADD CONSTRAINT `FK_UR_UID` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);

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
