package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

@Service
@Log4j
public class UserRepositoryImpl implements UserRepository {
    @Override
    public Clinic createNewClinic(Clinic newClinic) {
        return null;
    }

    @Override
    public WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment) {
        return null;
    }

    @Override
    public User findDoctorById(Integer doctorId) {
        return null;
    }

    @Override
    public QRCode createNewQRCode(QRCode qrCode) {
        return null;
    }

    @Override
    public WalkInAppointment findAppointmentById(Integer nextAppointmentId) {
        return null;
    }
}
