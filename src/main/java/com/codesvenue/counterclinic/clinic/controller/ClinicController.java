package com.codesvenue.counterclinic.clinic.controller;

import com.codesvenue.counterclinic.clinic.model.AssignClinicForm;
import com.codesvenue.counterclinic.clinic.model.ClinicRoom;
import com.codesvenue.counterclinic.clinic.service.ClinicService;
import com.codesvenue.counterclinic.user.model.User;
import lombok.extern.log4j.Log4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/clinic")
@Log4j
public class ClinicController {

    private final ClinicService clinicService;

    public ClinicController(ClinicService clinicService) {
        this.clinicService = clinicService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<ClinicRoom>> getAllClinics() {

        return ResponseEntity.ok(clinicService.getAllClinics());
    }

    @PostMapping("/assign")
    public ResponseEntity<User> assignClinic(@RequestBody AssignClinicForm assignClinicForm) {
        log.debug("Assign Clinic: " + assignClinicForm);
        User doctorAssignedClinic = clinicService.assignClinicRoom(assignClinicForm);
        return ResponseEntity.ok(doctorAssignedClinic);
    }
}
