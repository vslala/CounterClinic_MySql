package com.codesvenue.counterclinic.user.model;

import com.codesvenue.counterclinic.configuration.DateTimeConstants;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
@NoArgsConstructor
public class UserLogin {

    private Integer id;
    private Integer userId;
    private String username;
    private String password;
    private List<UserRole> userRoles;
    private String loggedInAt;

    public UserLogin(int userId, String username, String password) {
        this.userId = userId;
        this.username = username;
        this.password = User.hashPassword(password);
        this.loggedInAt = LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN));
    }

    private UserLogin(UserLogin other) {
        this.id = other.id;
        this.userId = other.userId;
        this.username = other.username;
        this.password = other.password;
        this.loggedInAt = other.loggedInAt;
    }

    public static UserLogin copyInstance(UserLogin userLogin) {
        return new UserLogin(userLogin);
    }

    private static UserLogin newInstance() {
        return new UserLogin();
    }

    public static UserLogin newInstance(User newUser) {
        return UserLogin.newInstance()
                .userId(newUser.getUserId())
                .username(newUser.getUsername())
                .password(newUser.getPassword())
                .loggedInAt(LocalDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ofPattern(DateTimeConstants.MYSQL_DATETIME_PATTERN)));
    }

    public UserLogin id(Integer id) {
        this.id = id;
        return  this;
    }

    public UserLogin userId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public UserLogin username(String username) {
        this.username = username;
        return this;
    }

    public UserLogin password(String password) {
        this.password  = password;
        return this;
    }

    public UserLogin loggedInAt(String loggedInAt) {
        this.loggedInAt = loggedInAt;
        return this;
    }

    public void setPassword(String password) {
        this.password = User.hashPassword(password);
    }
}
