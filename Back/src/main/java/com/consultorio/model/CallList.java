package com.consultorio.model;

import com.consultorio.enums.CallStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "call_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CallList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;

    @Enumerated(EnumType.STRING)
    private CallStatus status;
}
