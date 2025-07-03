package com.sas.sas_backend.repository;

import com.sas.sas_backend.models.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, String> {

    Optional<Prontuario> findByPaciente_Cpf(String cpf);

}
