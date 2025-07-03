package com.sas.sas_backend.repository;

import com.sas.sas_backend.models.ProfissionalDeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfissionalDeSaudeRepository extends JpaRepository<ProfissionalDeSaude, String> {
    Optional<ProfissionalDeSaude> findByNome(String nome);
    Optional<ProfissionalDeSaude> findByEmail(String email);

    Optional<ProfissionalDeSaude> findByNumeroRegistro(String numeroRegistro);
}
