package com.codesvenue.counterclinic.user;

import com.codesvenue.counterclinic.clinic.Clinic;
import com.codesvenue.counterclinic.clinic.ClinicRoom;
import com.codesvenue.counterclinic.walkinappointment.WalkInAppointment;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Data
@NoArgsConstructor
public class User {

    private Integer userId;

    @NotNull(message = "First Name cannot be empty")
    @Min(value = 3)
    private String firstName;

    @NotNull(message = "Last Name cannot be empty")
    @Min(value = 3)
    private String lastName;

    @Email
    private String email;

    @NotNull(message = "Mobile number cannot be empty")
    @Min(value = 3)
    @Max(value = 20)
    private String mobile;

    @NotNull(message = "Username should be unique and cannot be null.")
    private String username;

    @NotNull(message = "Password cannot be empty")
    @Min(value = 5, message = "Password should be atleast 5 chars long")
    private String password;

    @NotNull(message = "Preferred Language cannot be empty")
    private PreferredLanguage preferredLanguage;

    @Setter
    private List<UserRole> roles;

    @Getter
    private ClinicRoom assignedClinicRoom;

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
        this.assignedClinicRoom = other.assignedClinicRoom;
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
        return !Objects.isNull(roles) && this.roles.contains(UserRole.SUPERADMIN);
    }

    public Clinic addClinicRoomToClinic(Clinic clinic, ClinicRoom clinicRoom) {
        if (isSuperAdmin())
            return clinic.addNewRoom(clinicRoom);
        throw new ActionNotAllowedException("User do not have privileges to add new room to clinic.");
    }

    public boolean assignClinicRoom(ClinicRoom clinicRoom) {
        if (isSuperAdminOrReceptionist()) {
            this.assignedClinicRoom = clinicRoom;
            return !Objects.isNull(this.assignedClinicRoom);
        }
        throw new ActionNotAllowedException("User do not have privileges to assign clinic rooms to doctor.");
    }

    private boolean isSuperAdminOrReceptionist() {
        return !Objects.isNull(roles) &&
                (this.roles.contains(UserRole.RECEPTIONIST) || this.roles.contains(UserRole.SUPERADMIN));
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
        this.assignedClinicRoom = clinicRoom;
    }

    public boolean askReceptionistToSendNextPatient(Integer nextAppointmentId, SimpMessagingTemplate simpMessagingTemplate) {
        if (!Objects.isNull(roles) && roles.contains(UserRole.DOCTOR)) {
            simpMessagingTemplate.convertAndSend("/doctoraction/nextpatient", nextAppointmentId);
            return true;
        }
        throw new ActionNotAllowedException("Only doctors are allowed to perform this action");
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
}
