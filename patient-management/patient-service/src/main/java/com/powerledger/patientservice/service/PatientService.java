package com.powerledger.patientservice.service;

import com.powerledger.patientservice.dto.PatientRequestDto;
import com.powerledger.patientservice.dto.PatientResponseDTO;
import com.powerledger.patientservice.exception.EmailAlreadyExistsException;
import com.powerledger.patientservice.exception.PatientNotFoundException;
import com.powerledger.patientservice.grpc.BillingServiceGrpcClient;
import com.powerledger.patientservice.kafka.KafkaProducer;
import com.powerledger.patientservice.mapper.PatientMapper;
import com.powerledger.patientservice.model.Patient;
import com.powerledger.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public PatientService(PatientRepository patientRepository, BillingServiceGrpcClient billingServiceGrpcClient, KafkaProducer kafkaProducer) {
        this.patientRepository = patientRepository;
        this.billingServiceGrpcClient = billingServiceGrpcClient;
        this.kafkaProducer = kafkaProducer;
    }

    public List<PatientResponseDTO> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDTO createPatient(PatientRequestDto patientRequestDto) {
        if (patientRepository.existsByEmail(patientRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("");
        }
        Patient patient = patientRepository.save(PatientMapper.toEntity(patientRequestDto));
        billingServiceGrpcClient.createBillingAccount(patient.getId(), patient.getName(),patient.getEmail());
        kafkaProducer.sendEvent(patient);
        return PatientMapper.toDto(patient);
    }

    public PatientResponseDTO updatePatient(String id,
                                            PatientRequestDto patientRequestDTO) {

        Patient patient = patientRepository.findById(id).orElseThrow(
                () -> new PatientNotFoundException("Patient not found with ID: " + id));

        if (patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),
                id)) {
            throw new EmailAlreadyExistsException(
                    "A patient with this email " + "already exists"
                            + patientRequestDTO.getEmail());
        }

        patient.setName(patientRequestDTO.getName());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        Patient updatedPatient = patientRepository.save(patient);
        return PatientMapper.toDto(updatedPatient);
    }

    public void deletePatient(String id) {
        patientRepository.deleteById(id);
    }

}
