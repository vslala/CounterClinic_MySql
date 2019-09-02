ALTER TABLE `walkin_appointment_status`
    CHANGE COLUMN `avg_wait_time` `avg_wait_time` INT(11) NOT NULL DEFAULT 0 ,
    CHANGE COLUMN `appointment_start_datetime` `appointment_start_datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    CHANGE COLUMN `doctor_break_duration` `doctor_break_duration` INT(11) NOT NULL DEFAULT 0 ,
    CHANGE COLUMN `patients_in_visited_queue` `patients_in_visited_queue` INT(11) NOT NULL DEFAULT 0 ;