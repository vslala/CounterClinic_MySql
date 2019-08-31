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
    `setting_id` INT NOT NULL AUTO_INCREMENT,
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

// WalkInAppointments
INSERT INTO `walkin_appointments` (`patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `appointment_number`)
VALUES ( 'First Name 1', 'Last Name 1', 1, 1);
INSERT INTO `walkin_appointments` (`patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `appointment_number`)
VALUES ( 'First Name 2', 'Last Name 2', 1, 1);
INSERT INTO `walkin_appointments` (`patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `appointment_number`)
VALUES ( 'First Name 3', 'Last Name 3', 1, 1);

// QRCodeAttachments
INSERT INTO `qrcode_attachments` (`appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`)
values ( 1, 300, 300, 'testimage01.png', 'src/main/resources/static/qrcode/1564860895258.png', 'qrcode/1564860895258.png', '{"appointmentId": 1, "appointedDoctorId": 1}' );
INSERT INTO `qrcode_attachments` (`appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`)
values ( 2, 300, 300, 'testimage02.png', 'src/main/resources/static/qrcode/1564860895741.png', 'qrcode/1564860895741.png', '{"appointmentId": 2, "appointedDoctorId": 1}' );
INSERT INTO `qrcode_attachments` (`appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`)
values ( 3, 300, 300, 'testimage03.png', 'src/main/resources/static/qrcode/1564860895620.png', 'qrcode/1564860895620.png', '{"appointmentId": 3, "appointedDoctorId": 1}' );

// WalkInAppointmentStatus
INSERT INTO `walkin_appointment_status` (`current_appointment_id`, `doctor_id`, `avg_wait_time`, `appointment_start_datetime`)
values ( 1, 1, 15, CONCAT(CURRENT_DATE, ' ', CURRENT_TIME) );
INSERT INTO `walkin_appointment_status` (`current_appointment_id`, `doctor_id`, `avg_wait_time`, `appointment_start_datetime`)
values ( 2, 1, 15, CONCAT(CURRENT_DATE, ' ', CURRENT_TIME) );

// Users
INSERT INTO `users` (`first_name`, `last_name`, `email`, `mobile`, `username`, `password`, `preferred_language`)
VALUES ( 'Varun', 'Shrivastava', 'varunshrivastava007@gmail.com', '9960543885', 'vslala', 'simplepass', 'ENGLISH' );

// UserRole
INSERT INTO `user_roles` (`user_id`, `role_name`) VALUES ( 1, 'SUPER_ADMIN' );

// UsersMeta
INSERT INTO `users_meta` (`user_id`, `meta_key`, `meta_value`)
VALUES ( 1, 'assigned_clinic_room', 1 );

// Settings
INSERT INTO `settings` (`setting_name`, `setting_value`) VALUES ( 'Setting_One', 'Setting_Two' );



// ClinicRooms
INSERT INTO `clinic_rooms` (`clinic_id`, `room_name`) VALUES ( 1, 'X-RAY' );