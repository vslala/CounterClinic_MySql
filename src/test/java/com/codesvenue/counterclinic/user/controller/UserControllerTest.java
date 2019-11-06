package com.codesvenue.counterclinic.user.controller;

import com.codesvenue.counterclinic.setting.model.Setting;
import com.codesvenue.counterclinic.fixtures.AppointmentMother;
import com.codesvenue.counterclinic.user.model.User;
import com.codesvenue.counterclinic.user.model.UserLogin;
import com.codesvenue.counterclinic.user.model.UserRole;
import com.codesvenue.counterclinic.user.service.UserService;
import com.codesvenue.counterclinic.walkinappointment.TestData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    UserService userService;

    @InjectMocks
    private UserController userController;

    // Given
    User newUser = TestData.getNewUser(UserRole.ADMIN)
            .userId(1)
            .firstName("Varun")
            .lastName("Shrivastava")
            .username("vslala")
            .password("simplepass");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(userController).build();
    }

    @Test
    public void itShouldRegisterNewUser() throws Exception {
        //Given
        var userLoginInfo = UserLogin.newInstance(newUser);
        given(userService.createNewUser(any(User.class))).willReturn(userLoginInfo);

        // When
        this.mockMvc.perform(post("/user/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AppointmentMother.asJsonString(newUser)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("vslala"));
    }

    @Test
    public void itShouldUpdateUserInTheDatabase() throws Exception {
        // Given
        User oldUser = newUser.username("vs_shrivastava");
        given(userService.updateUser(any(User.class))).willReturn(oldUser);

        // When
        this.mockMvc.perform(patch("/user/update-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AppointmentMother.asJsonString(oldUser)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("vs_shrivastava"));
    }

    @Test
    public void itShouldDeleteUserById() throws Exception {
        given(userService.deleteUser(Mockito.anyInt())).willReturn(true);

        this.mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(true));
    }

    @Test
    public void itShouldGetUserById() throws Exception {
        // Given
        User user = TestData.getNewUser(UserRole.SUPER_ADMIN).username("vslala");
        given(userService.getUser(anyInt())).willReturn(user);

        // When
        this.mockMvc.perform(get("/user/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.username").value("vslala"));
    }

    @Test
    public void fetchAllUsersByGivenRole() throws Exception {
        // Given
        User user = TestData.getNewUser(UserRole.SUPER_ADMIN).username("vslala");
        given(userService.getAllUsers(any())).willReturn(Arrays.asList(user));

        // When
        this.mockMvc.perform(get("/user/all/SUPER_ADMIN"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void itShouldGetAllUsers() throws Exception {
        // Given
        User user = TestData.getNewUser(UserRole.SUPER_ADMIN).username("vslala");
        given(userService.getAllUsers()).willReturn(Arrays.asList(user));

        // When
        this.mockMvc.perform(get("/user/all"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void itShouldFetchAllUserRoles() throws Exception {
        // When
        this.mockMvc.perform(get("/user/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    public void itShouldSuccessfullyUploadFileAndReturnSetting() throws Exception {
        // Given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "TestFile.png", "png", "This is a test file".getBytes());
        Setting uploadedFileSetting = new Setting("png", "someUrl");
        given(userService.uploadFile(any(), anyString())).willReturn(uploadedFileSetting);

        // When
        this.mockMvc.perform(multipart("/user/file-upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("attachmentType", "png")
        )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.settingName").value("png"));
    }

}
