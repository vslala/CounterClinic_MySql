package com.codesvenue.counterclinic.user;

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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    UserService mockUserService;

    // Given
    User newUser = TestData.getNewUser(UserRole.ADMIN)
            .userId(1)
            .firstName("Varun")
            .lastName("Shrivastava")
            .username("vslala")
            .password("simplepass");

    @Before
    public void setup() {
        mockUserService = mock(UserService.class);
    }

    @Test
    public void itShouldRegisterNewUser() throws Exception {
        //Given
        var userLoginInfo = UserLogin.newInstance(newUser);
        given(mockUserService.createNewUser(newUser)).willReturn(userLoginInfo);

        // When
        this.mockMvc.perform(post("/user/register-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AppointmentMother.asJsonString(newUser)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("vslala"));
    }

    @Test
    public void itShouldUpdateUserInTheDatabase() throws Exception {
        // Given
        User oldUser = newUser.username("vs_shrivastava");
        given(mockUserService.updateUser(oldUser)).willReturn(oldUser);

        // When
        this.mockMvc.perform(patch("/user/update-user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(AppointmentMother.asJsonString(oldUser)))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("vs_shrivastava"));
    }

    @Test
    public void itShouldDeleteUserById() throws Exception {
        UserService mockUserService = mock(UserService.class);
        given(mockUserService.deleteUser(Mockito.anyInt())).willReturn(true);

        this.mockMvc.perform(delete("/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void itShouldGetUserById() throws Exception {
        // Given
        User user = TestData.getNewUser(UserRole.SUPER_ADMIN).username("vslala");
        given(mockUserService.getUser(1)).willReturn(user);

        // When
        this.mockMvc.perform(get("/user/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("vslala"));
    }

    @Test
    public void fetchAllUsersByGivenRole() throws Exception {
        // Given
        User user = TestData.getNewUser(UserRole.SUPER_ADMIN).username("vslala");
        given(mockUserService.getAllUsers(any())).willReturn(Arrays.asList(user));

        // When
        this.mockMvc.perform(get("/user/all/SUPER_ADMIN"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void itShouldGetAllUsers() throws Exception {
        // Given
        User user = TestData.getNewUser(UserRole.SUPER_ADMIN).username("vslala");
        given(mockUserService.getAllUsers()).willReturn(Arrays.asList(user));

        // When
        this.mockMvc.perform(get("/user/all"))
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void itShouldFetchAllUserRoles() throws Exception {
        // When
        this.mockMvc.perform(get("/user/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void itShouldSuccessfullyUploadFileAndReturnSetting() throws Exception {
        // Given
        MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "TestFile.png", "png", "This is a test file".getBytes());
        Setting uploadedFileSetting = new Setting("png", "someUrl");
        given(mockUserService.uploadFile(any(), anyString())).willReturn(uploadedFileSetting);

        // When
        this.mockMvc.perform(multipart("/user/file-upload")
                .file(mockMultipartFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("attachmentType", "png")
        )
                // Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.settingName").value("png"));
    }

}
