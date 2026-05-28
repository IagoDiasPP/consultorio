package com.consultorio.service;

import com.consultorio.dto.appointment.AppointmentCreateDto;
import com.consultorio.dto.appointment.AppointmentUpdateDto;
import com.consultorio.enums.AppointmentStatus;
import com.consultorio.enums.CallStatus;
import com.consultorio.mapper.AppointmentMapper;
import com.consultorio.model.*;
import com.consultorio.repository.AppointmentRepository;
import com.consultorio.repository.CallListRepository;
import com.consultorio.repository.SpecialtyRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.consultorio.dto.appointment.DoctorSlotResponseDto;
import java.time.LocalTime;
import java.time.LocalDate;
import java.util.ArrayList;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorService doctorService;
    private final ScheduleService scheduleService;
    private final AppointmentMapper appointmentMapper;
    private final CallListRepository callListRepository;
    private final PatientService patientService;
    private final SpecialtyRepository specialtyRepository;

    private static final int DURACAO_CONSULTA_MINUTES = 60;


    public Appointment save(AppointmentCreateDto appointmentDto) {

        Appointment appointment = new Appointment();

        appointment.setPatient(
                patientService.findByIdOrThrowRequestException(
                        appointmentDto.getPatientId()
                )
        );

        Specialty specialty = specialtyRepository
                .findById(appointmentDto.getSpecialtyId())
                .orElseThrow();

        appointment.setSpecialty(specialty);
        appointment.setStatus(AppointmentStatus.PENDING);

        // salva primeiro
        appointment = appointmentRepository.save(appointment);

        Pageable pageable = PageRequest.of(0, 10);
        tentarMarcarConsulta(appointment, pageable);

        return appointmentRepository.save(appointment);
    }


    private void tentarMarcarConsulta(Appointment appointment, Pageable pageable) {

        Page<Doctor> doctors =
                doctorService.findBySpecialty(appointment.getSpecialty().getId(), pageable);

        for (Doctor doctor : doctors) {

            Optional<LocalDateTime> vaga =
                    encontrarPrimeiraVagaLivre(doctor);

            if (vaga.isPresent()) {

                appointment.setDoctor(doctor);

                appointment.setDate(vaga.get().toLocalDate());

                appointment.setStartTime(vaga.get().toLocalTime());

                appointment.setStatus(AppointmentStatus.PENDING);

                return;
            }
        }

        appointment.setStatus(AppointmentStatus.WAITING);
    }


    private Optional<LocalDateTime> encontrarPrimeiraVagaLivre(Doctor doctor) {

        List<Schedule> schedules =
                scheduleService.findByDoctor(doctor.getId());

        for (Schedule schedule : schedules) {

            LocalDate data =
                    proximaDataDoDia(schedule.getDayOfWeek());

            LocalTime hora = schedule.getStartTime();

            while (hora.isBefore(schedule.getEndTime())) {

                boolean ocupado =

                        appointmentRepository
                                .existsByDoctorAndDateAndStartTimeAndStatusIn(

                                        doctor,

                                        data,

                                        hora,

                                        List.of(

                                                AppointmentStatus.PENDING,

                                                AppointmentStatus.CONFIRMED
                                        )
                                );

                if (!ocupado) {
                    return Optional.of(LocalDateTime.of(data, hora));
                }

                hora = hora.plusMinutes(DURACAO_CONSULTA_MINUTES);
            }
        }

        return Optional.empty();
    }


    private LocalDate proximaDataDoDia(DayOfWeek dia) {

        LocalDate data = LocalDate.now();

        while (data.getDayOfWeek() != dia) {
            data = data.plusDays(1);
        }

        return data;
    }


    private void criarCallList(Appointment appointment) {

        boolean existe =
                callListRepository
                        .existsByAppointment(appointment);

        if (existe) {
            return;
        }

        CallList call = new CallList();

        call.setAppointment(appointment);

        call.setStatus(CallStatus.PENDING);

        callListRepository.save(call);
    }


    // chamado quando abrir vaga
    public void tentarAgendarFila(Pageable pageable) {

        List<Appointment> fila =
                appointmentRepository
                        .findByStatusOrderByIdAsc(AppointmentStatus.WAITING);

        for (Appointment appointment : fila) {

            tentarMarcarConsulta(appointment, pageable);

            if (appointment.getStatus() != AppointmentStatus.WAITING) {

                appointmentRepository.save(appointment);

                criarCallList(appointment);

                return;
            }
        }
    }

    public Appointment findByIdOrThrowRequestException(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Appointment not found"));
    }

    public List<Appointment> findWaiting() {
        return appointmentRepository
                .findByStatusOrderByIdAsc(AppointmentStatus.WAITING);
    }

    public Appointment update(Long id, AppointmentUpdateDto dto) {

        Appointment appointment =
                findByIdOrThrowRequestException(id);

        appointmentMapper.updateFromDto(dto, appointment);

        return appointmentRepository.save(appointment);
    }

    public void delete(Long id) {

        Appointment appointment =
                findByIdOrThrowRequestException(id);

        callListRepository
                .deleteByAppointment(appointment);

        appointmentRepository.delete(appointment);

        tentarAgendarFila(
                PageRequest.of(0, 10)
        );
    }

    public List<DoctorSlotResponseDto> getDoctorSlots(
            Long doctorId,
            DayOfWeek dayOfWeek
    ) {

        Doctor doctor =
                doctorService.findByIdOrThrowRequestException(doctorId);

        List<Schedule> schedules =
                scheduleService.findByDoctor(doctorId);

        List<DoctorSlotResponseDto> slots = new ArrayList<>();

        LocalDate data = proximaDataDoDia(dayOfWeek);

        for (Schedule schedule : schedules) {

            if (schedule.getDayOfWeek() != dayOfWeek) {
                continue;
            }

            LocalTime hora = schedule.getStartTime();

            while (hora.isBefore(schedule.getEndTime())) {

                // pula horário de pausa
                if (
                        hora.equals(schedule.getBreakStart())
                                || (
                                hora.isAfter(schedule.getBreakStart())
                                        && hora.isBefore(schedule.getBreakEnd())
                        )
                ) {

                    hora = hora.plusMinutes(DURACAO_CONSULTA_MINUTES);

                    continue;
                }

                Appointment appointment =
                        appointmentRepository
                                .findByDoctorAndDateAndStartTimeAndStatusIn(

                                        doctor,

                                        data,

                                        hora,

                                        List.of(

                                                AppointmentStatus.PENDING,

                                                AppointmentStatus.CONFIRMED
                                        )
                                )
                                .orElse(null);

                if (appointment == null) {

                    slots.add(
                            DoctorSlotResponseDto.builder()
                                    .time(hora.toString())
                                    .available(true)
                                    .build()
                    );

                } else {

                    slots.add(
                            DoctorSlotResponseDto.builder()
                                    .date(appointment.getDate())
                                    .time(hora.toString())
                                    .available(false)
                                    .patientPhone(
                                            appointment.getPatient().getPhone()
                                    )

                                    .specialtyName(
                                            appointment.getSpecialty().getName()
                                    )

                                    .doctorName(
                                            appointment.getDoctor().getName()
                                    )
                                    .appointmentId(appointment.getId())
                                    .patientName(
                                            appointment.getPatient().getName()
                                    )
                                    .status(
                                            appointment.getStatus().name()
                                    )
                                    .build()
                    );
                }

                hora =
                        hora.plusMinutes(DURACAO_CONSULTA_MINUTES);
            }
        }

        return slots;
    }

    public List<Appointment> findAll() {

        return appointmentRepository.findAll();

    }

    public Appointment remarcarProximaSemana(Long id) {

        Appointment appointment =
                findByIdOrThrowRequestException(id);

        appointment.setDate(
                appointment.getDate().plusWeeks(1)
        );

        appointment.setStatus(
                AppointmentStatus.PENDING
        );

        criarCallList(appointment);

        return appointmentRepository.save(appointment);
    }

    public Appointment darAlta(Long id) {

        Appointment appointment =
                findByIdOrThrowRequestException(id);

        appointment.setStatus(
                AppointmentStatus.FINISHED
        );

        appointmentRepository.save(appointment);

        tentarAgendarFila(
                PageRequest.of(0, 10)
        );

        return appointment;
    }
}

