package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class FakeUserRepository implements UserRepository {
    @Override
    public Clinic createNewClinic(Clinic newClinic) {
        Clinic clinic = Clinic.newInstance(newClinic.getClinicName());
        clinic.setClinicId(1);
        return clinic;
    }

    @Override
    public WalkInAppointment createNewWalkInAppointment(WalkInAppointment walkInAppointment) {
        WalkInAppointment newWalkInAppointment = new WalkInAppointment();
        newWalkInAppointment.setAppointedDoctor(walkInAppointment.getAppointedDoctor());
        newWalkInAppointment.setPatientFirstName(walkInAppointment.getPatientFirstName());
        newWalkInAppointment.setPatientLastName(walkInAppointment.getPatientLastName());
        newWalkInAppointment.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC)
                .format(DateTimeFormatter.ISO_DATE_TIME)
                .replace("T", " "));
        newWalkInAppointment.setWalkInAppointmentId(1);
        return newWalkInAppointment;
    }

    @Override
    public User findDoctorById(Integer doctorId) {
        ClinicRoom clinicRoom = new ClinicRoom();
        clinicRoom.setName("Clinic Room");

        User doctor = new User();
        doctor.setRoles(Arrays.asList(UserRole.DOCTOR));
        doctor.setClinicRoom(clinicRoom);
        return doctor;
    }

    @Override
    public QRCode createNewQRCode(final QRCode qrCode) {
        QRCode newQRCode = qrCode;
        newQRCode.setQrCodeId(1);
        return newQRCode;
    }

    @Override
    public WalkInAppointment findAppointmentById(Integer nextAppointmentId) {
        User doctor = new User();
        doctor.setRoles(Arrays.asList(UserRole.DOCTOR));

        WalkInAppointment walkInAppointment = new WalkInAppointment();
        walkInAppointment.setAppointedDoctor(doctor);
        walkInAppointment.setPatientFirstName("Patient First Name");
        walkInAppointment.setPatientLastName("Patient Last Name");
        walkInAppointment.setWalkInAppointmentId(2);
        walkInAppointment.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_LOCAL_DATE).replace("T", " "));
        return walkInAppointment;
    }

    @Override
    public Clinic createNewClinicRoom(Clinic newClinic) {
        return null;
    }

    @Override
    public User createNewUser(User userRegistrationForm) {
        User user = User.newInstance()
                .firstName("Varun").lastName("Shrivastava")
                .email("varunshrivastava007@gmail.com")
                .mobile("9960543885")
                .username("vslala")
                .preferredLanguage(PreferredLanguage.ENGLISH);
        user.setPassword("simplepass"); // set password to hash it
        user.setUserId(1);
        return user;
    }

    @Override
    public UserLogin createNewUserLogin(UserLogin userLogin) {
        return UserLogin.copyInstance(userLogin).id(1);
    }
}
