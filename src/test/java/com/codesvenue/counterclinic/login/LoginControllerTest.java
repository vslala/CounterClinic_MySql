package com.codesvenue.counterclinic.login;

import com.codesvenue.counterclinic.fixtures.AppointmentMother;
import com.codesvenue.counterclinic.login.model.LoginCredentials;
import com.codesvenue.counterclinic.login.model.LoginResponse;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.codesvenue.counterclinic.fixtures.AppointmentMother.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Test
    public void itShouldGenerateValidJwtTokenOnUsernameAndPassword() throws Exception {
        // Given
        var loginCredentials = new LoginCredentials().newInstance().username("vslala").password("simplepass");
        var loginResponse = LoginResponse.newInstance()
                .user(TestData.getNewUser(UserRole.DOCTOR).firstName("Varun").lastName("Shrivastava"))
                .accessToken("accessToken");
        BDDMockito.given(userService.loginUser(loginCredentials)).willReturn(loginResponse);

        // When
        this.mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginCredentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.fullName").value("Varun Shrivastava"))
                .andExpect(jsonPath("$.accessToken").value("accessToken"));
    }
}
