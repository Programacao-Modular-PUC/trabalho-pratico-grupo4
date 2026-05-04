package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Aluguel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "quarto_id")
    private Quarto quarto;

    private LocalDate dataEntrada;
    private LocalDate dataSaida;
    private Integer numHospedes;
    private Boolean solicitouBerco = false;
    private Double valorTotal;
}

