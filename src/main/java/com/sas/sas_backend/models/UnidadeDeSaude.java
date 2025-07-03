package com.sas.sas_backend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "unidade_de_saude")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeDeSaude {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_unidade_de_saude", length = 36)
    private String id;

    @Column(name = "nome")
    private String nome;

    @Column()
    private String cnpj;

    @Column()
    private String salt;

    @Column(name = "email_unidade", unique = true, length = 250)
    private String email;

    @Column(name = "senha_unidade")
    private String senha;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

}
