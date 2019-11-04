package com.codesvenue.counterclinic.setting.dao;

import com.codesvenue.counterclinic.setting.model.Setting;
import lombok.extern.log4j.Log4j;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Log4j
@Repository
public class SettingDaoImpl implements SettingDao {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final KeyHolder keyHolder;

    public SettingDaoImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, KeyHolder mockKeyHolder) {
        this.jdbcTemplate = namedParameterJdbcTemplate;
        this.keyHolder = mockKeyHolder;
    }

    @Override
    public Optional<Setting> fetchSettingById(int settingId) {
        final String sql = "SELECT (setting_name, setting_value) FROM settings WHERE setting_id = :settingId";
        return jdbcTemplate.query(sql,
                new MapSqlParameterSource().addValue("settingId", settingId),
                Setting.SettingRowMapper.newInstance()).stream().findFirst();

    }

    @Override
    public Optional<Setting> fetchSettingByName(String settingName) {
        return Optional.empty();
    }

    @Override
    public List<Setting> fetchAllSettings() {
        return null;
    }

    @Override
    public Setting createNewSetting(Setting setting) {
        final String sql = "INSERT INTO settings (setting_name, setting_value) VALUES (:settingName, :settingValue)";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("settingName", setting.getSettingName())
                .addValue("settingValue", setting.getSettingValue());
        jdbcTemplate.update(sql, params, keyHolder);
        return setting.settingId(keyHolder.getKey().intValue());
    }

    @Override
    public Setting updateSettingInfo(Setting settingInfo) {
        final String sql = "UPDATE settings SET setting_name = :settingName, setting_value = :settingValue WHERE setting_name = :settingName";
        SqlParameterSource params = new MapSqlParameterSource()
                .addValue("settingName", settingInfo.getSettingName())
                .addValue("settingValue", settingInfo.getSettingValue());
        jdbcTemplate.update(sql, params);
        return settingInfo;
    }

    @Override
    public Boolean deleteSettingById(Integer settingId) {
        final String sql = "DELETE FROM settings WHERE setting_id = :settingId";
        SqlParameterSource params = new MapSqlParameterSource().addValue("settingId", settingId);
        int rowsChanged = jdbcTemplate.update(sql, params);
        return rowsChanged == 1;
    }
}
