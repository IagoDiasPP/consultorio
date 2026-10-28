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
import java.util.Comparator;
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
        appointment.setStatus(AppointmentStatus.WAITING);

        appointment = appointmentRepository.save(appointment);

        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE);

        tentarMarcarConsulta(appointment, pageable);

        appointment = appointmentRepository.save(appointment);

        if (appointment.getStatus() == AppointmentStatus.PENDING) {
            criarCallList(appointment);
        }

        return appointment;
    }




    private void tentarMarcarConsulta(Appointment appointment, Pageable pageable) {

        Page<Doctor> doctors = doctorService.findBySpecialty(
                appointment.getSpecialty().getId(),
                pageable
        );

        LocalDate dataInicio = appointment.getDisponivelParaReagendamentoEm() != null
                ? appointment.getDisponivelParaReagendamentoEm()
                : LocalDate.now();

        Doctor melhorMedico = null;
        LocalDateTime melhorVaga = null;

        for (Doctor doctor : doctors) {
            Optional<LocalDateTime> vaga =
                    encontrarPrimeiraVagaLivre(doctor, dataInicio);

            if (vaga.isPresent()
                    && (melhorVaga == null || vaga.get().isBefore(melhorVaga))) {
                melhorVaga = vaga.get();
                melhorMedico = doctor;
            }
        }

        if (melhorVaga != null) {
            appointment.setDoctor(melhorMedico);
            appointment.setDate(melhorVaga.toLocalDate());
            appointment.setStartTime(melhorVaga.toLocalTime());
            appointment.setStatus(AppointmentStatus.PENDING);
            appointment.setDisponivelParaReagendamentoEm(null);
        } else {
            appointment.setStatus(AppointmentStatus.WAITING);
        }
    }





    private Optional<LocalDateTime> encontrarPrimeiraVagaLivre(
            Doctor doctor,
            LocalDate dataInicio
    ) {
        List<Schedule> schedules =
                scheduleService.findByDoctor(doctor.getId());

        LocalDate hoje = LocalDate.now();
        LocalDate inicioBusca = dataInicio.isAfter(hoje) ? dataInicio : hoje;
        LocalDate fimBusca = inicioBusca.plusWeeks(8);

        return schedules.stream()
                .flatMap(schedule -> {
                    LocalDate data = proximaDataDoDia(
                            schedule.getDayOfWeek(),
                            inicioBusca
                    );

                    List<LocalDateTime> horarios = new ArrayList<>();

                    while (!data.isAfter(fimBusca)) {
                        LocalTime hora = schedule.getStartTime();

                        while (true) {
                            LocalTime fimConsulta =
                                    hora.plusMinutes(DURACAO_CONSULTA_MINUTES);

                            if (fimConsulta.isAfter(schedule.getEndTime())) {
                                break;
                            }

                            boolean cruzaAlmoco =
                                    schedule.getBreakStart() != null
                                            && schedule.getBreakEnd() != null
                                            && hora.isBefore(schedule.getBreakEnd())
                                            && fimConsulta.isAfter(schedule.getBreakStart());

                            boolean horarioJaPassou =
                                    data.equals(hoje)
                                            && !hora.isAfter(LocalTime.now());

                            if (!cruzaAlmoco && !horarioJaPassou) {
                                horarios.add(LocalDateTime.of(data, hora));
                            }

                            hora = hora.plusMinutes(DURACAO_CONSULTA_MINUTES);
                        }

                        data = data.plusWeeks(1);
                    }

                    return horarios.stream();
                })
                .sorted()
                .filter(vaga -> !appointmentRepository
                        .existsByDoctorAndDateAndStartTimeAndStatusIn(
                                doctor,
                                vaga.toLocalDate(),
                                vaga.toLocalTime(),
                                List.of(
                                        AppointmentStatus.PENDING,
                                        AppointmentStatus.CONFIRMED
                                )
                        ))
                .findFirst();
    }

    private LocalDate proximaDataDoDia(DayOfWeek dia, LocalDate aPartirDe) {
        LocalDate data = aPartirDe;

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

        List<Appointment> fila = new ArrayList<>(
                appointmentRepository.findByStatusOrderByDataEntradaFilaAsc(
                        AppointmentStatus.WAITING
                )
        );

        // Prioridade 0: primeira recusa.
        // Prioridade 1: fila normal e recusas posteriores.
        fila.sort(
                Comparator.comparing(
                        (Appointment a) -> a.getPrioridadeFila() == null
                                ? 1
                                : a.getPrioridadeFila()
                ).thenComparing(Appointment::getDataEntradaFila)
        );

        Pageable pageableTodos = PageRequest.of(0, Integer.MAX_VALUE);

        for (Appointment appointment : fila) {

            LocalDate disponivelEm =
                    appointment.getDisponivelParaReagendamentoEm();

            // Não agenda pacientes antes da data permitida.
            if (disponivelEm != null
                    && disponivelEm.isAfter(LocalDate.now())) {
                continue;
            }

            tentarMarcarConsulta(appointment, pageableTodos);

            if (appointment.getStatus() == AppointmentStatus.PENDING) {
                appointmentRepository.save(appointment);
                criarCallList(appointment);

                // Uma vaga por paciente em cada processamento.
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
                .findByStatusOrderByDataEntradaFilaAsc(
                        AppointmentStatus.WAITING
                );
    }





    public Appointment update(Long id, AppointmentUpdateDto dto) {
        Appointment appointment = findByIdOrThrowRequestException(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Não é possível atualizar uma consulta cancelada."
            );
        }

        AppointmentStatus statusAnterior = appointment.getStatus();
        AppointmentStatus novoStatus = dto.getStatus();

        if (novoStatus == null) {
            throw new IllegalArgumentException(
                    "O status da consulta deve ser informado."
            );
        }

        // Só permite confirmar uma consulta que já tenha
        // médico, data e horário definidos.
        if (novoStatus == AppointmentStatus.CONFIRMED) {
            if (appointment.getDoctor() == null
                    || appointment.getDate() == null
                    || appointment.getStartTime() == null) {
                throw new IllegalStateException(
                        "Não é possível confirmar uma consulta sem médico, data e horário."
                );
            }

            if (statusAnterior != AppointmentStatus.PENDING
                    && statusAnterior != AppointmentStatus.CONFIRMED) {
                throw new IllegalStateException(
                        "Só é possível confirmar uma consulta pendente."
                );
            }
        }


        // Paciente não confirmou a consulta.
        if (statusAnterior == AppointmentStatus.PENDING
                && novoStatus == AppointmentStatus.WAITING) {

            int tentativas = appointment.getTentativasNaoConfirmadas() == null
                    ? 0
                    : appointment.getTentativasNaoConfirmadas();

            tentativas++;
            appointment.setTentativasNaoConfirmadas(tentativas);

            LocalDate hoje = LocalDate.now();

            // Só poderá receber outra consulta na próxima segunda-feira.
            LocalDate proximaSegunda = hoje.with(
                    java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY)
            );

            appointment.setStatus(AppointmentStatus.WAITING);
            appointment.setDisponivelParaReagendamentoEm(proximaSegunda);

            if (tentativas == 1) {
                // Primeira recusa: prioridade máxima na fila.
                appointment.setPrioridadeFila(0);
            } else {
                // Segunda recusa ou posterior: vai para o fim da fila.
                appointment.setPrioridadeFila(1);
                appointment.setDataEntradaFila(LocalDateTime.now());
            }

            Appointment atualizada = appointmentRepository.save(appointment);

            // Libera a vaga para o próximo paciente elegível.
            tentarAgendarFila(PageRequest.of(0, Integer.MAX_VALUE));

            return atualizada;
        }


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
            LocalDate date
    ) {

        Doctor doctor =
                doctorService.findByIdOrThrowRequestException(doctorId);

        List<Schedule> schedules =
                scheduleService.findByDoctor(doctorId);

        List<DoctorSlotResponseDto> slots = new ArrayList<>();


        for (Schedule schedule : schedules) {

            if (schedule.getDayOfWeek() != date.getDayOfWeek()) {
                continue;
            }

            LocalTime hora = schedule.getStartTime();


            while (!hora.plusMinutes(DURACAO_CONSULTA_MINUTES)
                    .isAfter(schedule.getEndTime())) {


                // pula horário de pausa

                boolean cruzaAlmoco =
                        schedule.getBreakStart() != null
                                && schedule.getBreakEnd() != null
                                && hora.isBefore(schedule.getBreakEnd())
                                && hora.plusMinutes(DURACAO_CONSULTA_MINUTES)
                                .isAfter(schedule.getBreakStart());


                if (cruzaAlmoco) {
                    hora = hora.plusMinutes(DURACAO_CONSULTA_MINUTES);
                    continue;
                }

                boolean horarioJaPassou =
                        date.isBefore(LocalDate.now())
                                || (date.equals(LocalDate.now())
                                && !hora.isAfter(LocalTime.now()));

                if (horarioJaPassou) {
                    hora = hora.plusMinutes(DURACAO_CONSULTA_MINUTES);
                    continue;
                }



                Appointment appointment =
                        appointmentRepository
                                .findByDoctorAndDateAndStartTimeAndStatusIn(

                                        doctor,

                                        date,

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
        Appointment appointment = findByIdOrThrowRequestException(id);

        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new IllegalStateException(
                    "Não é possível remarcar uma consulta cancelada."
            );
        }

        Doctor doctor = appointment.getDoctor();

        if (doctor == null || appointment.getDate() == null
                || appointment.getStartTime() == null) {
            throw new IllegalStateException(
                    "A consulta precisa ter médico, data e horário para ser remarcada."
            );
        }

        LocalDate novaData = appointment.getDate().plusWeeks(1);

        // Impede remarcação para uma data que já passou.
        if (novaData.isBefore(LocalDate.now())) {
            throw new IllegalStateException(
                    "Não é possível remarcar a consulta para uma data passada."
            );
        }

        List<Schedule> schedules =
                scheduleService.findByDoctor(doctor.getId());

        schedules.stream()
                .filter(schedule ->
                        schedule.getDayOfWeek() == novaData.getDayOfWeek())
                .filter(schedule ->
                        !appointment.getStartTime().isBefore(schedule.getStartTime()))
                .filter(schedule ->
                        !appointment.getStartTime()
                                .plusMinutes(DURACAO_CONSULTA_MINUTES)
                                .isAfter(schedule.getEndTime()))
                .filter(schedule -> {
                    LocalTime fimConsulta = appointment.getStartTime()
                            .plusMinutes(DURACAO_CONSULTA_MINUTES);

                    return schedule.getBreakStart() == null
                            || schedule.getBreakEnd() == null
                            || !(appointment.getStartTime()
                            .isBefore(schedule.getBreakEnd())
                            && fimConsulta.isAfter(schedule.getBreakStart()));
                })
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "O horário original não está disponível na escala do médico na próxima semana."
                ));

        boolean horarioOcupado =
                appointmentRepository.existsByDoctorAndDateAndStartTimeAndStatusIn(
                        doctor,
                        novaData,
                        appointment.getStartTime(),
                        List.of(
                                AppointmentStatus.PENDING,
                                AppointmentStatus.CONFIRMED
                        )
                );

        if (horarioOcupado) {
            throw new IllegalStateException(
                    "O médico já possui uma consulta nesse horário na próxima semana."
            );
        }

        appointment.setDate(novaData);
        appointment.setStatus(AppointmentStatus.PENDING);

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

