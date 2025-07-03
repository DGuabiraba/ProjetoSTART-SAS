package com.sas.sas_backend.repository;

import com.sas.sas_backend.models.UnidadeDeSaude;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnidadeDeSaudeRepository extends JpaRepository<UnidadeDeSaude, String> {
    Optional<UnidadeDeSaude> findByEmail(String email);
    Optional<UnidadeDeSaude> findByCnpj(String cnpj);



}
