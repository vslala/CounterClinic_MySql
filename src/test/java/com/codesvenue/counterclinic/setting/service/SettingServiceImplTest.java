package com.codesvenue.counterclinic.setting.service;

import com.codesvenue.counterclinic.handler.ResourceNotFoundException;
import com.codesvenue.counterclinic.setting.dao.SettingDao;
import com.codesvenue.counterclinic.setting.model.Setting;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
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

    @Test
    public void itShouldGetAllSettingsPresentInTheDatabase() {
        // Given
        Setting mockedSetting = Setting.newInstance("test", "test");
        when(settingDaoImpl.fetchAllSettings()).thenReturn(Arrays.asList(mockedSetting));

        // When
        List<Setting> settings = settingService.getAllSettings();

        // Then
        assertNotNull(settings);
        assertEquals(1, settings.size());
    }

    @Test
    public void itShouldFetchSettingByItsNameFromTheDatabase() {
        // Given
        String settingName = "test";
        Setting mockedSetting = Setting.newInstance("test", "test");
        when(settingDaoImpl.fetchSettingByName(settingName)).thenReturn(Optional.of(mockedSetting));

        // When
        Setting settingInfo = settingService.getSetting(settingName);

        // Then
        assertEquals("test", settingInfo.getSettingValue());
    }

    @Test
    public void itShouldCreateNewSettingInTheDatabase() {
        // Given
        Setting mockedSetting = Setting.newInstance("test", "test");
        when(settingDaoImpl.createNewSetting(mockedSetting)).thenReturn(mockedSetting.settingId(1));

        // When
        Setting newSetting = settingService.createOrUpdateSetting(mockedSetting);

        // Then
        assertEquals(1, (int) newSetting.getSettingId());
    }

    @Test
    public void itShouldUpdateCurrentSettingIfTheSettingWithSameNameIsPresentInTheDatabase() {
        // Given
        Setting mockedSetting = Setting.newInstance("test", "test");
        when(settingDaoImpl.fetchSettingByName("test"))
                .thenReturn(Optional.of(mockedSetting.settingId(1)));
        when(settingDaoImpl.updateSettingInfo(mockedSetting.settingValue("testForChange")))
                .thenReturn(mockedSetting.settingValue("testForChange"));

        // When
        Setting newSetting = settingService.createOrUpdateSetting(mockedSetting);

        // Then
        assertEquals(1, (int) newSetting.getSettingId());
        assertEquals("testForChange", newSetting.getSettingValue());
    }

    @Test
    public void itShouldDeleteSettingWithItsIdFromTheDatabase() {
        // Given
        Integer settingId = 1;
        when(settingDaoImpl.deleteSettingById(settingId)).thenReturn(true);

        // When
        Boolean isDeleteSuccess = settingService.deleteSetting(settingId);

        // Then
        assertNotNull(isDeleteSuccess);
        assertEquals(true, isDeleteSuccess);
    }
}
