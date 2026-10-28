package com.consultorio.model;

import com.consultorio.enums.AppointmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Column;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "appointments")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Specialty specialty;

    private LocalDate date;

    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(nullable = false)
    @Builder.Default
    private Integer tentativasNaoConfirmadas = 0;

    private LocalDateTime dataEntradaFila;

    private LocalDate disponivelParaReagendamentoEm;

    @Builder.Default
    @Column(nullable = false)
    private Integer prioridadeFila = 1;

    @PrePersist
    public void inicializarDataEntradaFila() {
        if (dataEntradaFila == null) {
            dataEntradaFila = LocalDateTime.now();
        }
    }
}
