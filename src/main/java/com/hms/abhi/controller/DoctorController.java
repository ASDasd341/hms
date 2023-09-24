package com.hms.abhi.controller;

import com.hms.abhi.entities.Appointment;
import com.hms.abhi.entities.Doctor;
import com.hms.abhi.repositories.DoctorRepo;
import com.hms.abhi.request.BookAppointmentRequest;
import com.hms.abhi.response.AvailableAppointmentResponse;
import com.hms.abhi.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/doctors")
public class DoctorController {
    @Autowired
    private DoctorRepo doctorRepository;
    @Autowired
    private DoctorService doctorService;

    @PostMapping
    public ResponseEntity<Doctor> addDoctor(@RequestBody Doctor request) {
        // Create a new Doctor entity
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSpecialization(request.getSpecialization());

        // Create a list to hold the doctor's appointments
        List<Appointment> appointments = new ArrayList<>();

        // Iterate through the request's appointment list and add them to the doctor's appointments
        for (Appointment appointment : request.getAppointments()) {
            appointment.setDoctor(doctor); // Set the doctor for each appointment
            appointments.add(appointment);
        }

        // Set the appointments for the doctor
        doctor.setAppointments(appointments);

        // Save the doctor (including appointments)
        Doctor savedDoctor = doctorService.addDoctor(doctor);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }


//    @GetMapping("/{id}/available-slots")
//    public List<Appointment> getAvailableSlots(@PathVariable Long id, @RequestParam LocalDate date) {
//        return doctorService.getAvailableSlots(id, date);
//    }

//    @GetMapping("/{id}/available-slots")
//    public ResponseEntity<List<Appointment>> getAvailableSlots(
//            @PathVariable Long id,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
//    ) {
//        List<Appointment> availableSlots = doctorService.getAvailableSlots(id, date);
//        return ResponseEntity.ok(availableSlots);
//    }

    @GetMapping("/available-appointments")
    public ResponseEntity<List<AvailableAppointmentResponse>> getAvailableAppointments(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<AvailableAppointmentResponse> availableAppointments = doctorService.getAvailableAppointments(doctorId, date);

        return ResponseEntity.ok(availableAppointments);
    }



    @PostMapping("/book-appointment")
    public ResponseEntity<String> bookAppointment(@RequestBody BookAppointmentRequest request) {
        String resultMessage = doctorService.bookAppointment(request);
        return ResponseEntity.ok(resultMessage);
    }



//    @PostMapping("/{id}/book-appointment")
//    public ResponseEntity<String> bookAppointment(
//            @PathVariable Long id, @RequestBody Map<String, String> request) {
//        String appointmentTimeString = request.get("appointmentTime");
//        if (appointmentTimeString != null) {
//        try {
//            LocalDateTime parsedAppointmentTime = LocalDateTime.parse(appointmentTimeString);
//
//            Optional<Appointment> appointment = doctorService.bookAppointment(id, parsedAppointmentTime);
//            return appointment.isPresent()
//                    ? ResponseEntity.ok("Appointment booked successfully.")
//                    : ResponseEntity.badRequest().body("Appointment slot not available.");
//        } catch (DateTimeParseException e) {
//            return ResponseEntity.badRequest().body("Invalid date-time format. Use 'yyyy-MM-dd'T'HH:mm:ss'.");
//        }
//        }
//        else {
//                return ResponseEntity.badRequest().body("Missing 'appointmentTime' field in JSON.");
//            }
//        }
}
