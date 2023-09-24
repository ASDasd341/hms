package com.hms.abhi.service.serviceimpl;

import com.hms.abhi.entities.Appointment;
import com.hms.abhi.entities.Doctor;
import com.hms.abhi.repositories.AppointmentRepo;
import com.hms.abhi.repositories.DoctorRepo;
import com.hms.abhi.request.BookAppointmentRequest;
import com.hms.abhi.response.AvailableAppointmentResponse;
import com.hms.abhi.service.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private DoctorRepo doctorRepository;

    @Autowired
    private AppointmentRepo appointmentRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Doctor addDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors(String specialization) {
        if (specialization != null) {
            return doctorRepository.findBySpecialization(specialization);
        }
        return doctorRepository.findAll();
    }

//    @Override
//    public List<Appointment> getAvailableSlots(Long doctorId, LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
//
//        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
//                doctorId, startOfDay, endOfDay);
//
//        List<Appointment> availableSlots = new ArrayList<>();
//        for (LocalDateTime time = startOfDay; time.isBefore(endOfDay); time = time.plusMinutes(30)) {
//            LocalDateTime finalTime = time;
//            boolean isSlotAvailable = existingAppointments.stream()
//                    .noneMatch(appointment -> appointment.getAppointmentTime().isEqual(finalTime));
//            if (isSlotAvailable) {
//                availableSlots.add(new Appointment(doctorId, time));
//            }
//        }
//        return availableSlots;
//    }
//    @Override
//    public List<AppointmentResponse> getAvailableSlots(Long doctorId, LocalDate date) {
//        LocalDateTime startOfDay = date.atStartOfDay();
//        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
//
//        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
//                doctorId, startOfDay, endOfDay);
//        List<AppointmentResponse> availableSlots = new ArrayList<>();
//        Doctor doctor = doctorRepository.findById(doctorId).orElse(null); // Fetch doctor information
//
//        for (LocalDateTime time = startOfDay; time.isBefore(endOfDay); time = time.plusMinutes(30)) {
//            LocalDateTime finalTime = time;
//            boolean isSlotAvailable = existingAppointments.stream()
//                    .noneMatch(appointment -> appointment.getAppointmentTime().isEqual(finalTime));
//            if (isSlotAvailable) {
//                availableSlots.add(new AppointmentResponse(doctor, finalTime));
//            }
//        }
//        return availableSlots;
//    }
    @Override
    public List<AvailableAppointmentResponse> getAvailableAppointments(Long doctorId, LocalDate date) {
        // Step 1: Query the database to find existing appointments for the specified doctor and date
        List<Appointment> existingAppointments = appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                doctorId, date.atStartOfDay(), date.atTime(LocalTime.MAX));

        // Step 2: Generate a list of all possible appointment timings for the day
        List<LocalDateTime> allPossibleTimings = generateAllPossibleTimings(date);

        // Step 3: Identify available appointment timings by filtering out existing appointments
        List<LocalDateTime> availableTimings = allPossibleTimings.stream()
                .filter(time -> existingAppointments.stream()
                        .noneMatch(appointment -> appointment.getAppointmentTime().isEqual(time)))
                .collect(Collectors.toList());

        // Step 4: Get doctor details (name and specialization) based on doctorId
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        // Step 5: Create AvailableAppointmentResponse objects
        List<AvailableAppointmentResponse> availableAppointments = availableTimings.stream()
                .map(time -> new AvailableAppointmentResponse(doctor.getName(), doctor.getSpecialization(), availableTimings))
                .collect(Collectors.toList());

        return availableAppointments;
    }

    private List<LocalDateTime> generateAllPossibleTimings(LocalDate date) {
        // Implement the logic to generate all possible appointment timings for the specified date
        // You can define your own rules for appointment intervals and hours of operation
        List<LocalDateTime> allTimings = new ArrayList<>();

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        LocalDateTime current = startOfDay;

        while (current.isBefore(endOfDay)) {
            allTimings.add(current);
            current = current.plusMinutes(30); // Adjust based on your appointment interval
        }

        return allTimings;
    }
   @Override
    public String bookAppointment(BookAppointmentRequest request) {
        // Step 1: Check if the doctor exists
        Doctor doctor = doctorRepository.findById(request.getDoctorId()).orElse(null);

        if (doctor == null) {
            return "Doctor not found.";
        }

        // Step 2: Check if the requested date and time are valid
        LocalDateTime requestedDateTime = request.getAppointmentDateTime();

        if (requestedDateTime.isBefore(LocalDateTime.now())) {
            return "Appointment date and time cannot be in the past.";
        }

        // Step 3: Check doctor availability
        if (isDoctorAvailable(doctor, requestedDateTime)) {
            // Step 4: Save the appointment
            Appointment appointment = new Appointment();
            appointment.setDoctor(doctor);
            appointment.setAppointmentTime(requestedDateTime);
            appointmentRepository.save(appointment);

            return "Appointment booked successfully!";
        } else {
            return "Appointment is not available for the specified date and time.";
        }
    }

    private boolean isDoctorAvailable(Doctor doctor, LocalDateTime requestedDateTime) {
        // Step 1: Check if the doctor has existing appointments for the requested date and time
        List<Appointment> existingAppointments = appointmentRepository
                .findByDoctorAndAppointmentTime(doctor, requestedDateTime);

        if (!existingAppointments.isEmpty()) {
            return false; // Doctor has an appointment at the requested date and time
        }

        // Step 2: Implement additional business-specific rules for availability if needed

        return true; // The slot is available
    }


    public void saveAppointment(Long doctorId, LocalDateTime appointmentDateTime) {
        // Retrieve the doctor by ID
        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);

        if (doctor == null) {
            // Handle the case where the doctor does not exist
            throw new EntityNotFoundException("Doctor not found.");
        }

        // Create a new appointment
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setAppointmentTime(appointmentDateTime);

        // Save the appointment to the database
        appointmentRepository.save(appointment);
    }



}
