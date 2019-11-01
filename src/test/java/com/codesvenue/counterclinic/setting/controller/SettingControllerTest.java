package com.codesvenue.counterclinic.setting.controller;

import com.codesvenue.counterclinic.setting.model.Setting;
import com.codesvenue.counterclinic.setting.service.SettingService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.codesvenue.counterclinic.fixtures.AppointmentMother.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SettingControllerTest {

    private MockMvc mockMvc;

    @Mock
    SettingService settingService;

    @InjectMocks
    SettingController settingController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = standaloneSetup(settingController).build();
    }

    @Test
    public void itShouldCreateNewSettingsInTheDatabase() throws Exception {
        // Given
        Setting newSetting = new Setting("test", "testValue");
        given(settingService.createNewSetting(any())).willReturn(Setting.newInstance("test", "testValue").settingId(1));

        // When
        this.mockMvc.perform(post("/setting")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(newSetting)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("/setting/1"));
    }
}
