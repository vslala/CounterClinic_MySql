package com.codesvenue.counterclinic.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

@Data
@NoArgsConstructor
public class UserRegistrationForm {

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

    @NotNull(message = "User roles cannot be empty")
    private List<UserRole> userRoles;

    @NotNull(message = "Preferred Language cannot be empty")
    private String preferredLanguage;

    public static UserRegistrationForm newInstance() {
        return new UserRegistrationForm();
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    private String hashPassword(String password) {
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

    private byte[] getRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public UserRegistrationForm firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserRegistrationForm lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserRegistrationForm email(String email) {
        this.email = email;
        return this;
    }

    public UserRegistrationForm mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public UserRegistrationForm username(String username) {
        this.username = username;
        return this;
    }

    public UserRegistrationForm password(String password) {
        this.password = hashPassword(password);
        return this;
    }

    public UserRegistrationForm preferredLanugage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        return this;
    }
}
