package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicForm;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
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

    @Value("qrcode.url.path")
    private String qrCodeUrlPath;

    @Transactional
    @Override
    public WalkInAppointment createNewWalkInAppointment(User receptionist, WalkInAppointmentInfoForm walkInAppointmentInfoForm) {
        WalkInAppointment walkInAppointment = receptionist.createWalkInAppointment(
                walkInAppointmentInfoForm.getFirstName(),
                walkInAppointmentInfoForm.getLastName(),
                userRepository.findDoctorById(walkInAppointmentInfoForm.getDoctorId()));
        WalkInAppointment newWalkInAppointment = userRepository.createNewWalkInAppointment(walkInAppointment);
        QRCode qrCode = generateQRCode(newWalkInAppointment);
        userRepository.createNewQRCode(qrCode);
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

    @Transactional
    @Override
    public User assignDoctorClinic(User receptionist, Integer clinicRoomId, Integer assignDoctorId) {
        User doctor = userRepository.findDoctorById(assignDoctorId);
        ClinicRoom clinicRoom = userRepository.findClinicRoomById(clinicRoomId);
        User assignedDoctor = receptionist.assignClinicRoom(clinicRoom, doctor);
        UserMeta userMeta = userRepository.updateUserMeta(assignedDoctor.getUserId(), UserConstants.ASSIGNED_CLINIC_ROOM, clinicRoom.getClinicRoomId().toString());
        return assignedDoctor;
    }

    private QRCode generateQRCode(final WalkInAppointment newWalkInAppointment) {
        Map<String, Object> qrCodeData = new HashMap<>();
        qrCodeData.put("appointmentId", newWalkInAppointment.getWalkInAppointmentId());
        qrCodeData.put("appointedDoctorId", newWalkInAppointment.getAppointedDoctorId());
        long now = System.currentTimeMillis();
        String qrCodeFilePath = String.format("%s/%s.png", qrCodeFolder, now);
        String url = String.format("%s/%s.png", qrCodeUrlPath, now);

        QRCode qrCode = QRCodeBuilder.newInstance()
                .filePath(qrCodeFilePath).url(url).build(
                newWalkInAppointment.getWalkInAppointmentId(),
                qrCodeData
                );

        log.info("QRCode Generated Successfully!");
        log.info("QRCode Data: " + qrCodeData);
        log.info("QRCode File Path: " + qrCodeFilePath);
        return qrCode;
    }
}
