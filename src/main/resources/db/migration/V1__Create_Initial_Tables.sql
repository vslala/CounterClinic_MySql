CREATE TABLE `walkin_appointments` (
     `walkin_appointment_id` INT NOT NULL AUTO_INCREMENT,
     `patient_first_name` VARCHAR(255) NOT NULL,
     `patient_last_name` VARCHAR(255) NOT NULL,
     `appointed_doctor_id` INT NOT NULL,
     `appointment_number` INT NOT NULL,
     `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
     PRIMARY KEY (`walkin_appointment_id`)
);


CREATE TABLE `walkin_appointment_status` (
   `walkin_appointment_status_id` INT NOT NULL AUTO_INCREMENT,
   `current_appointment_id` INT NOT NULL,
   `doctor_id` INT NOT NULL,
   `avg_wait_time` INT NOT NULL,
   `appointment_start_datetime` DATETIME NOT NULL,
   `doctor_break_duration` INT NOT NULL,
   `patients_in_visited_queue` INT NOT NULL,
   `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`walkin_appointment_status_id`)
);

CREATE TABLE `users_meta` (
    `users_meta_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL ,
    `meta_key` VARCHAR(55) NOT NULL ,
    `meta_value` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`users_meta_id`)
);

CREATE TABLE `qrcode_attachments` (
    `qrcode_id` INT NOT NULL AUTO_INCREMENT ,
    `appointment_id` INT NOT NULL ,
    `height` INT NOT NULL ,
    `width` INT NOT NULL ,
    `image_name` VARCHAR(255) NOT NULL ,
    `image_file_path` VARCHAR(255) NOT NULL ,
    `image_url_path` VARCHAR(255) NOT NULL ,
    `qrcode_data` VARCHAR(500) NOT NULL ,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`qrcode_id`)
);

CREATE TABLE `users` (
    `user_id` INT NOT NULL AUTO_INCREMENT,
    `first_name` VARCHAR(255) NOT NULL ,
    `last_name` VARCHAR(255) NOT NULL ,
    `email` VARCHAR(255) NOT NULL ,
    `mobile` VARCHAR(12) NOT NULL ,
    `username` VARCHAR(55) NOT NULL ,
    `password` VARCHAR(255) NOT NULL ,
    `preferred_language` VARCHAR(25) NOT NULL ,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`)
);

CREATE TABLE `user_roles` (
    `user_id` INT NOT NULL ,
    `role_name` VARCHAR(64)
);

CREATE TABLE `settings` (
    `setting_name` VARCHAR(64) NOT NULL ,
    `setting_value` VARCHAR(64) NOT NULL ,
    PRIMARY KEY (`setting_name`, `setting_value`)
);

CREATE TABLE `users_login` (
    `user_login_id` INT NOT NULL AUTO_INCREMENT,
    `user_id` INT NOT NULL ,
    `username` VARCHAR(255) NOT NULL ,
    `password` VARCHAR(255) NOT NULL ,
    `logged_in_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_login_id`)
);

CREATE TABLE `clinic_rooms` (
  `clinic_room_id` INT NOT NULL AUTO_INCREMENT,
  `clinic_id` INT NOT NULL,
  `room_name` VARCHAR(155) NOT NULL ,
  PRIMARY KEY (`clinic_room_id`)
);

CREATE TABLE `clinics` (
    `clinic_id` INT NOT NULL AUTO_INCREMENT,
    `clinic_name` VARCHAR(155) NOT NULL ,
    PRIMARY KEY (`clinic_id`)
);
