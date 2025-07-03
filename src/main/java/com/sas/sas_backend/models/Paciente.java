package com.sas.sas_backend.models;

import com.sas.sas_backend.models.enumerated.Genero;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paciente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Paciente {

    @Id
    @Column(name = "id_paciente", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;


    @Column(nullable = false, length = 50)
    private String nome;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column()
    private String salt;

    @Column(nullable = false, length = 250)
    private String senha;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Enumerated(EnumType.STRING)
    @Column(name = "genero", nullable = false)
    private Genero genero;

    @Column(length = 15)
    private String telefone;

    @Column(length = 50)
    private String grauInstrucao;

    @Column(nullable = false)
    private Boolean notificacoesAtivadas;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToMany(mappedBy = "paciente")
    private List<Exame> exames = new ArrayList<>();

    private boolean ativo = false;

    private String tokenAtivacao;

    private LocalDateTime dataExpiracaoToken;
}
