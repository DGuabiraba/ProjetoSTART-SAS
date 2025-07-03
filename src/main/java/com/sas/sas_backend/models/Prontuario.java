package com.sas.sas_backend.models;

import com.sas.sas_backend.models.enumerated.TipoSanguineo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prontuario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prontuario {

    @Id
    @Column(name = "id_prontuario", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private String alergias;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String observacoes;

    @Column(name = "tipo_sanguineo", nullable = false)
    private TipoSanguineo tipoSanguineo;

    @Column(nullable = false)
    private String doencasCronicas;

    @Column(columnDefinition = "TEXT")
    private String historicoFamiliar;

    @OneToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profissional_id")
    private ProfissionalDeSaude profissionalDeSaude;

}