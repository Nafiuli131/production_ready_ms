package com.powerledger.patientservice.repository;

import com.powerledger.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PatientRepository extends JpaRepository<Patient, String> {

    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, String id);
}
