package com.sas.sas_backend.repository;

import com.sas.sas_backend.models.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AgendamentoRepository extends JpaRepository<Agendamento, String> {

    List<Agendamento> findByProfissionalNumeroRegistroOrPacienteCpfAndDataHoraInicioBetween(
            String profissionalNumeroRegistro,
            String pacienteCpf,
            LocalDateTime inicioData,
            LocalDateTime fimData
    );

    boolean existsByProfissionalIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            String profissionalId,
            LocalDateTime fim,
            LocalDateTime inicio
    );

    boolean existsByPacienteIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
            String pacienteId,
            LocalDateTime fim,
            LocalDateTime inicio
    );

    default boolean existeConflitoParaProfissional(String profissionalId, LocalDateTime inicio, LocalDateTime fim) {
        return existsByProfissionalIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                profissionalId, fim, inicio);
    }

    default boolean existeConflitoParaPaciente(String pacienteId, LocalDateTime inicio, LocalDateTime fim) {
        return existsByPacienteIdAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(
                pacienteId, fim, inicio);
    }

    List<Agendamento> id(String id);

    boolean existsByProfissionalNumeroRegistroAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(String profissionalNumero, LocalDateTime fim, LocalDateTime inicio);

    boolean existsByPacienteCpfAndDataHoraInicioLessThanAndDataHoraFimGreaterThan(String pacienteCpf, LocalDateTime fim, LocalDateTime inicio);
}
