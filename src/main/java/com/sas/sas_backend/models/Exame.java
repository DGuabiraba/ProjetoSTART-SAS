package com.sas.sas_backend.models;

import com.sas.sas_backend.models.enumerated.StatusExame;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "exame")
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor
public class Exame {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_exame")
    private String id;

    @Column(nullable = false)
    private String tipoExame;

    @Column(columnDefinition = "TEXT")
    private String descricao;

    @Column(name = "data_solicitacao", nullable = false)
    private LocalDate dataSolicitacao;

    @Column(name = "data_realizacao")
    private LocalDate dataRealizacao;

    @Column(columnDefinition = "TEXT")
    private String resultado;

    @Enumerated(EnumType.STRING)
    private StatusExame status = StatusExame.PENDENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prontuario_id")
    private Prontuario prontuario;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profissional_id")
    private ProfissionalDeSaude profissional;

    @OneToOne
    @JoinColumn(name = "agendamento_id", unique = true)
    private Agendamento agendamento;
}