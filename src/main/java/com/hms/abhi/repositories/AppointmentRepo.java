package com.hms.abhi.repositories;

import com.hms.abhi.entities.Appointment;
import com.hms.abhi.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository

public interface AppointmentRepo extends JpaRepository<Appointment, Long> {


    @Query("SELECT a FROM Appointment a WHERE a.doctor = :doctor AND a.appointmentTime = :appointmentTime")
    List<Appointment> findByDoctorAndAppointmentTime(@Param("doctor") Doctor doctor, @Param("appointmentTime") LocalDateTime appointmentTime);

    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            Long doctorId, LocalDateTime start, LocalDateTime end);

}
