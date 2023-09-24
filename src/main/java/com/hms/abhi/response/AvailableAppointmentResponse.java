package com.hms.abhi.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AvailableAppointmentResponse {
    private String doctorName;
    private String specialization;
    private List<LocalDateTime> availableTimings;

    public AvailableAppointmentResponse(String name, String specialization, List<LocalDateTime> time) {
        this.doctorName = name;
        this.specialization = specialization;
        this.availableTimings = time;
    }

//    public AvailableAppointmentResponse(String name, String specialization, LocalDateTime time) {
//    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public List<LocalDateTime> getAvailableTimings() {
        return availableTimings;
    }

    public void setAvailableTimings(List<LocalDateTime> availableTimings) {
        this.availableTimings = availableTimings;
    }
// Getters and setters
}
