-- WalkInAppointments
INSERT INTO `walkin_appointments` (`patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `appointment_number`)
VALUES ( 'First Name 1', 'Last Name 1', 1, 1);
INSERT INTO `walkin_appointments` (`patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `appointment_number`)
VALUES ( 'First Name 2', 'Last Name 2', 1, 1);
INSERT INTO `walkin_appointments` (`patient_first_name`, `patient_last_name`, `appointed_doctor_id`, `appointment_number`)
VALUES ( 'First Name 3', 'Last Name 3', 1, 1);

-- QRCodeAttachments
INSERT INTO `qrcode_attachments` (`appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`)
values ( 1, 300, 300, 'testimage01.png', 'src/main/resources/static/qrcode/1564860895258.png', 'qrcode/1564860895258.png', '{"appointmentId": 1, "appointedDoctorId": 1}' );
INSERT INTO `qrcode_attachments` (`appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`)
values ( 2, 300, 300, 'testimage02.png', 'src/main/resources/static/qrcode/1564860895741.png', 'qrcode/1564860895741.png', '{"appointmentId": 2, "appointedDoctorId": 1}' );
INSERT INTO `qrcode_attachments` (`appointment_id`, `height`, `width`, `image_name`, `image_file_path`, `image_url_path`, `qrcode_data`)
values ( 3, 300, 300, 'testimage03.png', 'src/main/resources/static/qrcode/1564860895620.png', 'qrcode/1564860895620.png', '{"appointmentId": 3, "appointedDoctorId": 1}' );

-- WalkInAppointmentStatus
INSERT INTO `walkin_appointment_status` (`current_appointment_id`, `doctor_id`, `avg_wait_time`, `appointment_start_datetime`)
values ( 1, 1, 15, CONCAT(CURRENT_DATE, ' ', CURRENT_TIME) );
INSERT INTO `walkin_appointment_status` (`current_appointment_id`, `doctor_id`, `avg_wait_time`, `appointment_start_datetime`)
values ( 2, 1, 15, CONCAT(CURRENT_DATE, ' ', CURRENT_TIME) );

-- Users
INSERT INTO `users` (`first_name`, `last_name`, `email`, `mobile`, `username`, `password`, `preferred_language`)
VALUES ( 'Varun', 'Shrivastava', 'varunshrivastava007@gmail.com', '9960543885', 'vslala', 'simplepass', 'ENGLISH' );

-- UserRole
INSERT INTO `user_roles` (`user_id`, `role_name`) VALUES ( 1, 'SUPER_ADMIN' );

-- UsersMeta
INSERT INTO `users_meta` (`user_id`, `meta_key`, `meta_value`)
VALUES ( 1, 'assigned_clinic_room', 1 );

-- Settings
INSERT INTO `settings` (`setting_name`, `setting_value`) VALUES ( 'Setting_One', 'Setting_Two' );



-- ClinicRooms
INSERT INTO `clinic_rooms` (`clinic_id`, `room_name`) VALUES ( 1, 'X-RAY' );