package com.consultorio.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receptionists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Receptionist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    @Column(nullable = false)
    @JsonIgnore
    private String password;
}

