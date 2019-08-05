package com.codesvenue.counterclinic.user.service;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicForm;
import com.codesvenue.counterclinic.clinic.model.Setting;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointmentInfoForm;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

@Service
public interface UserService {
    Clinic createNewClinic(User admin, ClinicForm clinicForm);

    WalkInAppointment notifyReceptionToSendNextPatient(User doctor, AppointmentStatus nextAppointmentId);

    User addNewDoctor(User admin, User doctor);

    User assignDoctorClinic(User receptionist, Integer clinicForm, Integer assignDoctorId);

    List<User> getAllUsers(UserRole userRole);

    User getUser(int userId);

    UserLogin createNewUser(User user);

    boolean deleteUser(Integer userId);

    List<User> getAllUsers();

    User updateUser(User user);

    Setting uploadFile(MultipartFile file, String attachmentType);

    default void generateDirectoryStructure(String filePath) {
        File file = Paths.get(filePath).toFile();
        if (! file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
    }

    Setting getSetting(String settingName);

    List<Setting> getSettings();

    Setting updateSetting(Setting setting);

    Boolean deleteSetting(Integer settingId);
}
