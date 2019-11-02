package com.codesvenue.counterclinic.setting.dao;

import com.codesvenue.counterclinic.setting.model.Setting;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingDao {

    Optional<Setting> fetchSettingById(int settingId);
}
