package com.codesvenue.counterclinic.user.dao;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.clinic.model.Setting;
import com.codesvenue.counterclinic.qrcode.QRCode;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserMeta;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
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

    int[] createUserRoles(Integer userId, UserRole... userRoles);

    int deleteCascadeUser(Integer userId);

    List<User> findAllUsers();

    User updateUser(User user);

    Setting upsertSetting(String settingName, String settingValue);

    Setting fetchSettingByName(String settingName);

    List<Setting> fetchSettings();

    int deleteSetting(Integer settingId);
}
