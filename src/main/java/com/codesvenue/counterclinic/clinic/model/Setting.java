package com.codesvenue.counterclinic.clinic.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@NoArgsConstructor
@Log4j
public class Setting {

    private Integer settingId;
    private String settingName;
    private String settingValue;

    public Setting(String settingName, String settingValue) {
        this.settingName = settingName;
        this.settingValue = settingValue;
    }

    public static Setting newInstance() {
        return new Setting();
    }

    public static Setting newInstance(String settingName, String settingValue) {
        return new Setting(settingName, settingValue);
    }

    public Setting settingId(Integer settingId) {
        this.settingId = settingId;
        return this;
    }

    public Setting settingName(String settingName) {
        this.settingName = settingName;
        return this;
    }

    public Setting settingValue(String settingValue) {
        this.settingValue = settingValue;
        return this;
    }

    public static class SettingRowMapper implements RowMapper<Setting> {

        public static SettingRowMapper newInstance() {
            return new SettingRowMapper();
        }

        @Override
        public Setting mapRow(ResultSet resultSet, int i) throws SQLException {
            return Setting.newInstance(
                    resultSet.getString("setting_name"),
                    resultSet.getString("setting_value"))
                    .settingId(resultSet.getInt("setting_id"));
        }
    }
}
