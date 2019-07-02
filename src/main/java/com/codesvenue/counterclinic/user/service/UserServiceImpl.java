package com.codesvenue.counterclinic.user.service;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import com.codesvenue.counterclinic.user.UserConstants;
import com.codesvenue.counterclinic.user.dao.UserRepository;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserMeta;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
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

    @Value("${qrcode.folder.path}")
    private String qrCodeFolder;

    @Value("${qrcode.url.path}")
    private String qrCodeUrlPath;

    @Transactional
    @Override
    public WalkInAppointment createNewWalkInAppointment(User receptionist, WalkInAppointmentInfoForm walkInAppointmentInfoForm) {
        WalkInAppointment walkInAppointment = receptionist.createWalkInAppointment(
                walkInAppointmentInfoForm.getPatientFirstName(),
                walkInAppointmentInfoForm.getPatientLastName(),
                userRepository.findUserById(walkInAppointmentInfoForm.getDoctorId()));
        WalkInAppointment newWalkInAppointment = userRepository.createNewWalkInAppointment(walkInAppointment);
        QRCode qrCode = generateQRCode(newWalkInAppointment);
        userRepository.createNewQRCode(qrCode);
//        CompletableFuture.runAsync(()-> generateQRCode(newWalkInAppointment));
        return newWalkInAppointment;
    }

    @Override
    public WalkInAppointment notifyReceptionToSendNextPatient(User doctor, AppointmentStatus appointmentStatus) {
        doctor.askReceptionistToSendNextPatient(appointmentStatus, simpMessagingTemplate);
        return userRepository.findAppointmentById(appointmentStatus.getCurrentAppointmentId());
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

    @Override
    public List<User> getAllUsers(UserRole userRole) {
        return userRepository.findAllUsersByRole(userRole);
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findUserById(userId);
    }

    @Transactional
    @Override
    public UserLogin createNewUser(User user) {
        User newUser = userRepository.createNewUser(user);
        UserLogin newUserLogin = userRepository.createNewUserLogin(UserLogin.newInstance(newUser));
        userRepository.createUserRoles(newUserLogin.getUserId(), newUser.getRoles().toArray(UserRole[]::new));
        return newUserLogin;
    }

    @Override
    public boolean deleteUser(Integer userId) {
        return userRepository.deleteCascadeUser(userId) > 0;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllUsers();
    }

    @Override
    public User updateUser(User user) {
        return userRepository.updateUser(user);
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
