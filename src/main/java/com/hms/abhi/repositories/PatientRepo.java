package com.hms.abhi.repositories;

import com.hms.abhi.entities.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepo extends JpaRepository<Patient, Long> {

    Optional<Patient> findByName(String name);

}
