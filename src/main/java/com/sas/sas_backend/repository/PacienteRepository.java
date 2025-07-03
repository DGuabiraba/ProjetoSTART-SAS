package com.sas.sas_backend.repository;

import com.sas.sas_backend.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, String> {

    Optional<Paciente> findByCpf(String cpf);

    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Paciente p WHERE p.email = :email OR p.cpf = :cpf")
    boolean findByEmailOrCpf(@Param("email") String email, @Param("cpf") String cpf);

    Optional<Paciente> findByEmail(String email);

}
