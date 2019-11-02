package com.codesvenue.counterclinic.setting.service;

import com.codesvenue.counterclinic.handler.ResourceNotFoundException;
import com.codesvenue.counterclinic.setting.dao.SettingDao;
import com.codesvenue.counterclinic.setting.model.Setting;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@Log4j
public class SettingServiceImplTest {

    @Mock
    private SettingDao settingDaoImpl;
    private SettingService settingService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        settingService = new SettingServiceImpl(settingDaoImpl);
    }

    @Test
    public void itShouldGetSettingByItsIdFromTheDatabase() {
        // Given
        int settingId = 1;
        Setting mockedSetting = Setting.newInstance("test", "test");
        when(settingDaoImpl.fetchSettingById(anyInt())).thenReturn(Optional.of(mockedSetting));

        // When
        Setting setting = settingService.getSetting(settingId);

        // Then
        assertNotNull(setting);
        assertEquals("test", setting.getSettingName());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void itShouldThrowResourceNotFoundExceptionIfNoDataIsFoundInTheDatabase() {
        // Given
        int settingId = 1;
        when(settingDaoImpl.fetchSettingById(anyInt())).thenReturn(Optional.empty());

        // When
        Setting setting = settingService.getSetting(settingId);
    }
}
