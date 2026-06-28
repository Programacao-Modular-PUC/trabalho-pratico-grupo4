package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "aluguel_id")
    private Aluguel aluguel;

    private Double valor;
    private String status;
    private String forma;

    @Column(length = 500)
    private String descricao;

    public Pagamento(Aluguel aluguel) {
        this.aluguel = aluguel;
        this.valor = aluguel.getValorTotal();
        this.status = "PENDENTE";
    }
}