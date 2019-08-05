package com.codesvenue.counterclinic.walkinappointment.model;

import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.model.PreferredLanguage;
import com.codesvenue.counterclinic.user.model.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@Log4j
public class WalkInAppointmentWrapper {

    private WalkInAppointment walkInAppointment;
    private User appointedDoctor;
    private QRCode qrCode;
    private AppointmentStatus appointmentStatus;

    public static WalkInAppointmentWrapper newInstance() {
        return new WalkInAppointmentWrapper();
    }

    public WalkInAppointmentWrapper walkInAppointment(WalkInAppointment walkInAppointment) {
        this.walkInAppointment = walkInAppointment;
        return this;
    }

    public WalkInAppointmentWrapper appointedDoctor(User appointedDoctor) {
        this.appointedDoctor = appointedDoctor;
        return this;
    }

    public WalkInAppointmentWrapper qrCode(QRCode qrCode) {
        this.qrCode = qrCode;
        return this;
    }

    public WalkInAppointmentWrapper appointmentStatus(AppointmentStatus appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
        return this;
    }

    public static class WalkInAppointmentWrapperRowMapper implements RowMapper<WalkInAppointmentWrapper> {

        public static WalkInAppointmentWrapperRowMapper newInstance() {
            return new WalkInAppointmentWrapperRowMapper();
        }

        @Override
        public WalkInAppointmentWrapper mapRow(ResultSet resultSet, int i) throws SQLException {
            User appointedDoctor = User.UserRowMapper.newInstance().mapRow(resultSet, i);

            WalkInAppointment walkInAppointment = WalkInAppointment.WalkInAppointmentRowMapper.newInstance().mapRow(resultSet, i);

            QRCode qrCode = QRCode.QRCodeRowMapper.newInstance().mapRow(resultSet, i);

            return WalkInAppointmentWrapper.newInstance()
                    .walkInAppointment(walkInAppointment)
                    .appointedDoctor(appointedDoctor)
                    .qrCode(qrCode);
        }
    }
}
