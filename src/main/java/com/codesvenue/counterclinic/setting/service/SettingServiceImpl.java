package com.codesvenue.counterclinic.setting.service;

import com.codesvenue.counterclinic.handler.ResourceNotFoundException;
import com.codesvenue.counterclinic.setting.dao.SettingDao;
import com.codesvenue.counterclinic.setting.model.Setting;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j
@Service
public class SettingServiceImpl implements SettingService {

    private final SettingDao settingDao;

    public SettingServiceImpl(SettingDao settingDaoImpl) {
        this.settingDao = settingDaoImpl;
    }

    @Override
    public Setting getSetting(String settingName) {
        return settingDao.fetchSettingByName(settingName)
                .orElseThrow(() ->
                        new ResourceNotFoundException(String.format("Setting not found with name: %s", settingName)));
    }

    @Override
    public List<Setting> getAllSettings() {
        return settingDao.fetchAllSettings();
    }

    @Transactional
    @Override
    public Setting createOrUpdateSetting(Setting settingInfo) {
        return settingDao.fetchSettingByName(settingInfo.getSettingName())
                .map(setting -> settingDao.updateSettingInfo(settingInfo))
                .orElseGet(() -> settingDao.createNewSetting(settingInfo));
    }

    @Override
    public Boolean deleteSetting(Integer settingId) {
        return settingDao.deleteSettingById(settingId);
    }

    @Override
    public Setting getSetting(int settingId) {
        return settingDao.fetchSettingById(settingId).orElseThrow(() ->
                new ResourceNotFoundException("Setting with ID: " + settingId + " not found!"));
    }
}
