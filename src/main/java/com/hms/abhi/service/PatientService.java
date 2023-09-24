package com.hms.abhi.service;

import com.hms.abhi.entities.Patient;

import java.util.List;
import java.util.Optional;

public interface PatientService {
    Patient addPatient(Patient patient);
    List<Patient> getAllPatients();
    Optional<Patient> getPatientById(Long id);
    Optional<Patient> getPatientByName(String name);
    Optional<Patient> updatePatient(Long id, Patient updatedPatient);
    void deletePatient(Long id);
}
