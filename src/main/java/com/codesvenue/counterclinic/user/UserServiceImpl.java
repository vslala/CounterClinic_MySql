package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicForm;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointmentInfoForm;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public UserServiceImpl(UserRepository userRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public Clinic createNewClinic(User admin, ClinicForm clinicForm) {
        Clinic newClinic = admin.createNewClinic(clinicForm.getClinicName());
        return userRepository.createNewClinic(newClinic);
    }

    @Value("qrcode.folder.path")
    private String qrCodeFolder;

    @Transactional
    @Override
    public WalkInAppointment createNewWalkInAppointment(User receptionist, WalkInAppointmentInfoForm walkInAppointmentInfoForm) {
        WalkInAppointment walkInAppointment = receptionist.createWalkInAppointment(
                walkInAppointmentInfoForm.getFirstName(),
                walkInAppointmentInfoForm.getLastName(),
                userRepository.findDoctorById(walkInAppointmentInfoForm.getDoctorId()));
        WalkInAppointment newWalkInAppointment = userRepository.createNewWalkInAppointment(walkInAppointment);
        generateQRCode(newWalkInAppointment);
//        CompletableFuture.runAsync(()-> generateQRCode(newWalkInAppointment));
        return newWalkInAppointment;
    }

    @Override
    public WalkInAppointment notifyReceptionToSendNextPatient(User doctor, Integer nextAppointmentId) {
        doctor.askReceptionistToSendNextPatient(nextAppointmentId, simpMessagingTemplate);
        return userRepository.findAppointmentById(nextAppointmentId);
    }

    @Transactional
    @Override
    public User addNewDoctor(User admin, User doctor) {
        User newDoctor = admin.createNewDoctor(doctor);
        User newUser = userRepository.createNewUser(newDoctor);
        userRepository.createNewUserLogin(UserLogin.newInstance(newUser));
        return newUser;
    }

    private QRCode generateQRCode(final WalkInAppointment newWalkInAppointment) {
        Map<String, Object> qrCodeData = new HashMap<>();
        qrCodeData.put("appointmentId", newWalkInAppointment.getWalkInAppointmentId());
        qrCodeData.put("appointedDoctorId", newWalkInAppointment.getAppointedDoctorId());
        String qrCodeFilePath = String.format("%s/%s.png", qrCodeFolder, System.currentTimeMillis());

        QRCode qrCode = QRCodeBuilder.newInstance()
                .filePath(qrCodeFilePath).build(
                newWalkInAppointment.getWalkInAppointmentId(),
                qrCodeData
                );

        log.info("QRCode Generated Successfully!");
        log.info("QRCode Data: " + qrCodeData);
        log.info("QRCode File Path: " + qrCodeFilePath);
        return userRepository.createNewQRCode(qrCode);
    }
}
