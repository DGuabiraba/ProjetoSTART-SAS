package com.sas.sas_backend.models.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoPacienteProfissionalEmbed implements Serializable {

    @Column(name = "medicamento_id", length = 36)
    private String medicamentoId;

    @Column(name = "paciente_id", length = 36)
    private String pacienteId;

    @Column(name = "profissional_id", length = 36)
    private String profissionalId;
}
