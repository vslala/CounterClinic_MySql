package com.codesvenue.counterclinic.setting.dao;

import com.codesvenue.counterclinic.setting.model.Setting;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SettingDao {

    Optional<Setting> fetchSettingById(int settingId);

    Optional<Setting> fetchSettingByName(String settingName);

    List<Setting> fetchAllSettings();

    Setting createNewSetting(Setting setting);

    Setting updateSettingInfo(Setting settingInfo);

    Boolean deleteSettingById(Integer settingId);
}
