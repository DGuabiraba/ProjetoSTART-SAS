package com.sas.sas_backend.models;

import com.sas.sas_backend.models.enumerated.TipoProfissional;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "profissional_de_saude")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfissionalDeSaude {

    @Id
    @Column(name = "id_profissional_de_saude", length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 50)
    private String nome;

    @Column(length = 15)
    private String telefone;

    @Column(name = "especialidade", length = 50)
    private TipoProfissional tipoProfissional;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false)
    private String senha;

    @Column()
    private String salt;

    @Column(name = "numero_registro", nullable = false, unique = true, length = 20)
    private String numeroRegistro;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "especialidade_id")
    private Especialidade especialidade;

    @ManyToOne
    @JoinColumn(name = "unidade_de_saude_id")
    private UnidadeDeSaude unidadeDeSaude;

    @OneToMany(mappedBy = "profissional")
    private List<Exame> exames = new ArrayList<>();
}
