package com.codesvenue.counterclinic.setting.dao;

import com.codesvenue.counterclinic.fixtures.AppointmentMother;
import com.codesvenue.counterclinic.setting.model.Setting;
import lombok.extern.log4j.Log4j;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Log4j
public class SettingDaoImplTest {

    @Mock
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static AppointmentMother.MockKeyHolder mockKeyHolder;
    private SettingDao settingDao;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockKeyHolder = AppointmentMother.MockKeyHolder.newInstance();
        settingDao = new SettingDaoImpl(namedParameterJdbcTemplate, mockKeyHolder);
    }

    @Test
    public void itShouldCreateNewSettingInTheDatabase() {
        // Given
        Setting newSetting = Setting.newInstance("test", "test");
        mockKeyHolder.setKey(1);

        // When
        Setting setting = settingDao.createNewSetting(newSetting);

        // Then
        assertNotNull(setting);
        assertEquals(1, (int)setting.getSettingId());
    }

    @Test
    public void itShouldDeleteSettingFromTheDatabaseBasedOnItsId() {
        // Given
        Integer settingId = 1;
        when(namedParameterJdbcTemplate.update(anyString(), any(SqlParameterSource.class))).thenReturn(1);

        // When
        Boolean isDeleteSuccess = settingDao.deleteSettingById(settingId);

        // Then
        assertTrue(isDeleteSuccess);
    }

    @Test
    public void itShouldUpdateSettingInTheDatabase() {
        // Given
        Setting newSetting = Setting.newInstance("test", "testForChange");

        // When
        Setting updatedSetting = settingDao.updateSettingInfo(newSetting);

        // Then
        assertEquals("testForChange", updatedSetting.getSettingValue());
    }

    @Test
    public void itShouldFetchSettingInfoByItsIdFromTheDatabase() {
        // Given
        Integer settingId = 1;
        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(Setting.newInstance("test", "test")));

        // When
        Optional<Setting> setting = settingDao.fetchSettingById(settingId);

        // Then
        assertTrue(setting.isPresent());
    }

    @Test
    public void itShouldReturnOptionalOfEmptyIfNoSettingInfoForTheGivenIdIsPresent() {
        // Given
        Integer settingId = 1;
        when(namedParameterJdbcTemplate.query(anyString(), any(SqlParameterSource.class), any(RowMapper.class)))
                .thenReturn(Collections.emptyList());

        // When
        Optional<Setting> setting = settingDao.fetchSettingById(settingId);

        // Then
        assertTrue(setting.isEmpty());
    }


}
