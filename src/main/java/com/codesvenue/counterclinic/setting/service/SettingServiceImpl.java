package com.codesvenue.counterclinic.setting.service;

import com.codesvenue.counterclinic.handler.ResourceNotFoundException;
import com.codesvenue.counterclinic.setting.dao.SettingDao;
import com.codesvenue.counterclinic.setting.model.Setting;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j
@Service
public class SettingServiceImpl implements SettingService {

    private final SettingDao settingDao;

    public SettingServiceImpl(SettingDao settingDaoImpl) {
        this.settingDao = settingDaoImpl;
    }

    @Override
    public List<Setting> getAllSettings() {
        return null;
    }

    @Override
    public Setting createNewSetting(Setting settingInfo) {
        return null;
    }

    @Override
    public Boolean deleteSetting(Integer settingId) {
        return null;
    }

    @Override
    public Setting getSetting(int settingId) {
        return settingDao.fetchSettingById(settingId).orElseThrow(() ->
                new ResourceNotFoundException("Setting with ID: " + settingId + " not found!"));
    }
}
