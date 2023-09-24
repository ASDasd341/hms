package com.hms.abhi.repositories;

import com.hms.abhi.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor, Long> {
    List<Doctor> findBySpecialization(String specialization);
    List<Doctor> findByAppointmentsAppointmentTimeBetweenAndId(
            LocalDateTime start, LocalDateTime end, Long id);

}
