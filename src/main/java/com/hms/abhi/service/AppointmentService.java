package com.hms.abhi.service;

import com.hms.abhi.entities.Appointment;

import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<Appointment> getAppointmentsByDoctorAndDate(Long doctorId, LocalDate date);

}
