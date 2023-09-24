package com.hms.abhi.service;

import com.hms.abhi.entities.Appointment;
import com.hms.abhi.entities.Doctor;
import com.hms.abhi.request.BookAppointmentRequest;
import com.hms.abhi.response.AvailableAppointmentResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor addDoctor(Doctor doctor);
    List<Doctor> getAllDoctors(String specialization);
    //List<Appointment> getAvailableSlots(Long doctorId, LocalDate date);

    List<AvailableAppointmentResponse> getAvailableAppointments(Long doctorId, LocalDate date);


    String bookAppointment(BookAppointmentRequest request);
}
