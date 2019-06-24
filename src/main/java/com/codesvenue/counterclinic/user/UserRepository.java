package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserRepository {
    Clinic createNewClinic(Clinic newClinic);

    WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment);

    User findDoctorById(Integer doctorId);

    QRCode createNewQRCode(QRCode qrCode);

    WalkInAppointment findAppointmentById(Integer nextAppointmentId);

    Clinic createNewClinicRoom(Clinic newClinic);

    User createNewUser(User userRegistrationForm);

    UserLogin createNewUserLogin(UserLogin userLogin);

    ClinicRoom findClinicRoomById(Integer clinicRoomId);

    UserMeta updateUserMeta(Integer userId, String metaKey, String metaValue);

    List<User> findAllUsersByRole(UserRole userRole);

    User findUserById(int userId);
}
