package com.codesvenue.counterclinic.user.model;

import com.codesvenue.counterclinic.clinic.model.Clinic;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.user.AlgorithmNotFoundException;
import com.codesvenue.counterclinic.walkinappointment.model.AppointmentStatus;
import com.codesvenue.counterclinic.walkinappointment.model.WalkInAppointment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.validation.constraints.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@Log4j
public class User {

    public static final String ASSIGNED_CLINIC_ROOM_KEY = "assigned_clinic_room";

    private Integer userId;

    @NotNull(message = "First Name cannot be empty")
    @Size(min = 3)
    private String firstName;

    @NotNull(message = "Last Name cannot be empty")
    @Size(min = 3)
    private String lastName;

    @Email
    private String email;

    @NotNull(message = "Mobile number cannot be empty")
    @Size(min = 3, max = 20)
    private String mobile;

    @NotNull(message = "Username should be unique and cannot be null.")
    private String username;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 5, message = "Password should be atleast 5 chars long")
    private String password;

    @NotNull(message = "Preferred Language cannot be empty")
    private PreferredLanguage preferredLanguage = PreferredLanguage.ENGLISH;

    @Setter
    private List<UserRole> roles;

    @Getter
    private Integer clinicRoomId;

    public static User newInstance() {
        return new User();
    }

    private User(User other) {
        this.userId = other.userId;
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.email = other.email;
        this.mobile = other.mobile;
        this.username = other.username;
        this.password = other.password;
        this.preferredLanguage = other.preferredLanguage;
        this.roles = other.roles;
        this.clinicRoomId = other.clinicRoomId;
    }

    public static User copyInstance(User user) {
        return new User(user);
    }

    public List<UserRole> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    public Clinic createNewClinic(String clinicName) {
        if (isSuperAdmin() || isAdmin())
            return Clinic.newInstance(clinicName);
        throw new ActionNotAllowedException("User do not have privileges to create new clinic.");
    }

    public User firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public User lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User mobile(String  mobile) {
        this.mobile = mobile;
        return this;
    }

    public User username(String username) {
        this.username  = username;
        return this;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public User preferredLanguage(PreferredLanguage preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public static String hashPassword(String password) {
        MessageDigest md = null;
        try {
            byte[] salt = getRandomSalt();

            md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
        } catch (NoSuchAlgorithmException e) {
            throw new AlgorithmNotFoundException();
        }
        return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
    }

    private static byte[] getRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }


    private boolean isAdmin() {
        return !Objects.isNull(roles) && this.roles.contains(UserRole.ADMIN);
    }

    private boolean isSuperAdmin() {
        return !Objects.isNull(roles) && this.roles.contains(UserRole.SUPER_ADMIN);
    }

    public Clinic addClinicRoomToClinic(Clinic clinic, ClinicRoom clinicRoom) {
        if (isSuperAdmin())
            return clinic.addNewRoom(clinicRoom);
        throw new ActionNotAllowedException("User do not have privileges to add new room to clinic.");
    }

    public User assignClinicRoomId(Integer clinicRoomId) {
        this.clinicRoomId = clinicRoomId;
        return this;
    }

    public User assignClinicRoom(ClinicRoom clinicRoom, User doctor) {
        if (isSuperAdminOrReceptionist()) {
            doctor.clinicRoomId = clinicRoom.getClinicRoomId();
            return User.copyInstance(doctor);
        }
        throw new ActionNotAllowedException("User do not have privileges to assign clinic rooms to doctor.");
    }

    private boolean isSuperAdminOrReceptionist() {
        return !Objects.isNull(roles) &&
                (this.roles.contains(UserRole.RECEPTIONIST) || this.roles.contains(UserRole.SUPER_ADMIN));
    }

    public WalkInAppointment createWalkInAppointment(String patientFirstName, String patientLastName, User appointedDoctor) {
        if (isReceptionist())
            return new WalkInAppointment().create(patientFirstName, patientLastName, appointedDoctor);
        throw new ActionNotAllowedException("Only receptionists are allowed to create Walk In Appointments for patients");
    }

    private boolean isReceptionist() {
        return !Objects.isNull(roles) && roles.contains(UserRole.RECEPTIONIST);
    }

    public void setClinicRoom(ClinicRoom clinicRoom) {
        this.clinicRoomId = clinicRoom.getClinicRoomId();
    }

    public boolean askReceptionistToSendNextPatient(AppointmentStatus appointmentStatus, SimpMessagingTemplate simpMessagingTemplate) {
        if (isDoctor()) {
            log.info("Sending data to topic: /topic/appointment-status");
            simpMessagingTemplate.convertAndSend("/topic/appointment-status", appointmentStatus);
            return true;
        }
        throw new ActionNotAllowedException("Only doctors are allowed to perform this action");
    }

    private boolean isDoctor() {
        return !Objects.isNull(roles) && roles.contains(UserRole.DOCTOR);
    }

    public User roles(UserRole... userRole) {
        if (Objects.isNull(roles))
            roles = new ArrayList<>();
        roles.addAll(Arrays.asList(userRole));
        return this;
    }

    public User userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public User createNewDoctor(User doctor) {
        if (isAdmin() || isSuperAdmin())
            return doctor;
        throw new ActionNotAllowedException("Only admins or super admins can create new user.");
    }

    public static UserRole[] convertRoleToUserRoleEnum(String[] roles) {
        UserRole[] userRoles = new UserRole[roles.length];
        for(int index = 0; index < roles.length; index++)
            userRoles[index] = UserRole.valueOf(roles[index]);
        return userRoles;
    }

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public AppointmentStatus takeBreak(AppointmentStatus latestAppointmentStatus, int breakDuration) {
        int newBreakDuration = breakDuration < 0 ? 0 : breakDuration; // if negative duration is returned.
        return AppointmentStatus.copyInstance(latestAppointmentStatus)
                .calculateNewAvgWaitTime(breakDuration)
                .doctorBreakDuration(newBreakDuration);
    }

    public boolean broadcastNewState(String topic, Object state, SimpMessagingTemplate simpMessagingTemplate) {
        if (isDoctor() || isAdmin() || isSuperAdmin()) {
            simpMessagingTemplate.convertAndSend(topic, state);
            return true;
        }
        throw new ActionNotAllowedException("Only Admin, Doctor and SuperAdmin can broadcast messages");
    }

    public User assignClinicRoom(ClinicRoom clinicRoom) {
        this.clinicRoomId = clinicRoom.getClinicRoomId();
        return User.copyInstance(this);
    }

    public static class UserRowMapper implements RowMapper<User> {

        public static UserRowMapper newInstance() {
            return new UserRowMapper();
        }

        @Override
        public User mapRow(ResultSet resultSet, int i) throws SQLException {
            return User.newInstance()
                    .userId(resultSet.getInt("user_id"))
                    .firstName(resultSet.getString("first_name"))
                    .lastName(resultSet.getString("last_name"))
                    .email(resultSet.getString("email"))
                    .mobile(resultSet.getString("mobile"))
                    .username(resultSet.getString("username"))
                    .roles(User.convertRoleToUserRoleEnum(resultSet.getString("user_roles").split(",")))
                    .preferredLanguage(PreferredLanguage.valueOf(resultSet.getString("preferred_language")))
                    .assignClinicRoomId(resultSet.getInt("assigned_clinic_room"));
        }


    }
}
