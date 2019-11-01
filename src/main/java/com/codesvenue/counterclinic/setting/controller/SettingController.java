package com.codesvenue.counterclinic.setting.controller;

import com.codesvenue.counterclinic.clinic.model.Response;
import com.codesvenue.counterclinic.setting.model.Setting;
import com.codesvenue.counterclinic.setting.service.SettingService;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/setting")
@Log4j
public class SettingController {

    private SettingService settingService;

    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @PostMapping
    public ResponseEntity<Response<String>> upsertNewSetting(@RequestBody Setting newSetting) {
        log.debug(String.format("Creating new setting with: %s", newSetting));
        Setting setting = this.settingService.createNewSetting(newSetting);
        return ResponseEntity.ok(Response.newInstance().data("/setting/" + setting.getSettingId()));
    }

    @GetMapping
    public ResponseEntity<Response<List<Setting>>> getAllSettings() {
        return ResponseEntity.ok(Response.newInstance().data(settingService.getAllSettings()));
    }

    @DeleteMapping("/{settingId}")
    public ResponseEntity<Response<Boolean>> deleteSetting(@PathVariable Integer settingId) {
        log.debug(String.format("Deleting setting with id: %s", settingId) );
        return ResponseEntity.ok(Response.newInstance().data(settingService.deleteSetting(settingId)));
    }
}
