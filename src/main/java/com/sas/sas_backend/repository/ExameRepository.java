package com.sas.sas_backend.repository;


import com.sas.sas_backend.models.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ExameRepository extends JpaRepository<Exame, String> {

    Optional<Exame> findByProntuario_Id(String id);

}