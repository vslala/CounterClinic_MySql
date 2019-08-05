package com.codesvenue.counterclinic.clinic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Log4j
public class FetchAppointmentInfoQuery {

    @NotNull
    @Min(value = 1, message = "Invalid appointment id!")
    private Integer appointmentId;

    @NotNull
    @Min(value = 1, message = "Invalid doctor id!")
    private Integer doctorId;

    private String inquiryDateTime;

}
