package com.hms.abhi.service.serviceimpl;

import com.hms.abhi.entities.Patient;
import com.hms.abhi.repositories.PatientRepo;
import com.hms.abhi.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepo patientRepository;
    @Override
    public Patient addPatient(Patient patient) {

        return patientRepository.save(patient);
    }

    @Override
    public List<Patient> getAllPatients() {

        return patientRepository.findAll();
    }

    @Override
    public Optional<Patient> getPatientById(Long id) {

        return patientRepository.findById(id);
    }

    @Override
    public Optional<Patient> getPatientByName(String name) {
        return patientRepository.findByName(name);
    }

    @Override
    public Optional<Patient> updatePatient(Long id, Patient updatedPatient) {
        if (!patientRepository.existsById(id)) {
            return Optional.empty();
        }
        updatedPatient.setId(id);
        return Optional.of(patientRepository.save(updatedPatient));

    }

    @Override
    public void deletePatient(Long id) {
        if (patientRepository.existsById(id)) {
            patientRepository.deleteById(id);
        }
    }
}
