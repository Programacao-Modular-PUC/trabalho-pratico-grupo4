package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Residencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    private String cidade;

    @OneToMany(mappedBy = "residencia", cascade = CascadeType.ALL)
    private List<Quarto> quartos;
}