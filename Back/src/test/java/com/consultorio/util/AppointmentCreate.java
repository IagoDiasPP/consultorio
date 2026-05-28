package com.consultorio.util;

import com.consultorio.enums.AppointmentStatus;
import com.consultorio.model.Appointment;
import com.consultorio.model.Doctor;
import com.consultorio.model.Patient;
import com.consultorio.model.Specialty;

import java.time.LocalDate;
import java.time.LocalTime;

public final class AppointmentCreate {

    public static Appointment creatAppointmentValid() {

        Appointment appointment = new Appointment();

        appointment.setId(1L);

        // Patient
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("João");

        appointment.setPatient(patient);

        // Specialty
        Specialty specialty = new Specialty();
        specialty.setId(1L);
        specialty.setName("Ortodontia");

        appointment.setSpecialty(specialty);

        // Doctor (pode ser null dependendo do fluxo)
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Gabriel");

        appointment.setDoctor(doctor);

        // Datas
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.of(10, 0));

        // Status
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return appointment;
    }

    public static Appointment creatAppointment() {

        Appointment appointment = new Appointment();


        // Patient
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setName("João");

        appointment.setPatient(patient);

        // Specialty
        Specialty specialty = new Specialty();
        specialty.setId(1L);
        specialty.setName("Ortodontia");

        appointment.setSpecialty(specialty);

        // Doctor (pode ser null dependendo do fluxo)
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("Dr. Gabriel");

        appointment.setDoctor(doctor);

        // Datas
        appointment.setDate(LocalDate.now().plusDays(1));
        appointment.setStartTime(LocalTime.of(10, 0));

        // Status
        appointment.setStatus(AppointmentStatus.CONFIRMED);

        return appointment;
    }
}
