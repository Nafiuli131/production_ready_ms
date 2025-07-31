package com.powerledger.patientservice.controller;

import com.powerledger.patientservice.dto.PatientRequestDto;
import com.powerledger.patientservice.dto.PatientResponseDTO;
import com.powerledger.patientservice.dto.validators.CreatePatientValidationGroup;
import com.powerledger.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patients")
@Tag(name = "Patient", description = "API for managing Patients")
//swagger ui : http://localhost:4000/swagger-ui/index.html#/
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping("")
    @Operation(summary = "Get Patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @PostMapping("")
    @Operation(summary = "Create a new Patient")
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDto patientRequestDto) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(patientRequestDto);
        return ResponseEntity.ok(patientResponseDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a new Patient")
    public ResponseEntity<PatientResponseDTO> updatePatient(
           @PathVariable String id,  @Validated({Default.class})@RequestBody PatientRequestDto patientRequestDto){
        PatientResponseDTO patientResponseDTO = patientService.updatePatient(id,patientRequestDto);
        return ResponseEntity.ok(patientResponseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a Patient")
    public void deletePatient(@PathVariable String id) {
        patientService.deletePatient(id);
    }

}
