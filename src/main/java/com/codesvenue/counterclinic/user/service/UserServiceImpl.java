package com.codesvenue.counterclinic.user.service;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.clinic.model.Setting;
import com.codesvenue.counterclinic.login.model.LoginCredentials;
import com.codesvenue.counterclinic.login.model.LoginResponse;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.qrcode.QRCodeBuilder;
import com.codesvenue.counterclinic.user.FileUploadFailedException;
import com.codesvenue.counterclinic.user.UserConstants;
import com.codesvenue.counterclinic.user.dao.UserRepository;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserMeta;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.log4j.Log4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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

    @Deprecated
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

    @Value("${images.folder.path}")
    String imagesFolder;

    @Value("${images.url.path}")
    String imagesUrlPath;

    @Override
    public Setting uploadFile(MultipartFile file, String attachmentType) {
        String originalFilename = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.]+","")
                .replaceAll("\\s", "");
        String filePath = imagesFolder + "/" + originalFilename;
        String fileUrl = imagesUrlPath + "/" + originalFilename;
        generateDirectoryStructure(filePath);
        try {
            FileUtils.writeByteArrayToFile(new File(filePath), file.getBytes());
            return userRepository.upsertSetting(attachmentType, fileUrl);
        } catch (IOException e) {
            log.warn("Error uploading file. Error: " + e.getMessage());
            throw new FileUploadFailedException("Error uploading file. Error: " + e.getMessage());
        }
    }

    @Override
    public Setting getSetting(String settingName) {
        return userRepository.fetchSettingByName(settingName);
    }

    @Override
    public List<Setting> getSettings() {
        return userRepository.fetchSettings();
    }

    @Override
    public Setting updateSetting(Setting setting) {
        return userRepository.upsertSetting(setting.getSettingName(), setting.getSettingValue());
    }

    @Override
    public Boolean deleteSetting(Integer settingId) {
        return userRepository.deleteSetting(settingId) > 0;
    }

    @Override
    public LoginResponse loginUser(LoginCredentials loginCredentials) {
        return null;
    }

}
