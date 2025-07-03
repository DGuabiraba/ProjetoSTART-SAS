package com.sas.sas_backend.models;

import com.sas.sas_backend.models.enumerated.StatusAgendamento;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Agendamento {

        @Id
        @Column(name = "id_agendamento", length = 36)
        @GeneratedValue(strategy = GenerationType.UUID)
        private String id;

        @Column(name = "data_hora_inicio", nullable = false)
        private LocalDateTime dataHoraInicio;

        @Column(name = "data_hora_fim", nullable = false)
        private LocalDateTime dataHoraFim;

        @Enumerated(EnumType.STRING)
        @Column(nullable = false)
        private StatusAgendamento status;

        @Column(name = "observacoes", columnDefinition = "TEXT")
        private String observacoes;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "paciente_id", nullable = false)
        private Paciente paciente;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "profissional_id", nullable = false)
        private ProfissionalDeSaude profissional;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "unidade_de_saude_id", nullable = false)
        private UnidadeDeSaude unidadeDeSaude;

        @OneToOne(mappedBy = "agendamento", orphanRemoval = true)
        private Exame exame;

}
