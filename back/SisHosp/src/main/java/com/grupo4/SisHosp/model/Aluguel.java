package com.grupo4.SisHosp.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

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

    @ManyToOne
    @JoinColumn(name = "residencia_id")
    private Residencia residencia;

    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaida;
    private Integer numHospedes;
    private Boolean solicitouBerco = false;
    private Integer qtdDiarias;
    private Double valorTotal;

    public static int calcularQtdDiarias(LocalDateTime entrada, LocalDateTime saida) {
        long dias = ChronoUnit.DAYS.between(entrada.toLocalDate(), saida.toLocalDate());
        if (saida.getHour() > 12 || (saida.getHour() == 12 && saida.getMinute() > 0)) {
            dias++;
        }
        return (int) Math.max(1, dias);
    }

    public String gerarFormulario() {
        return String.format(
            "Data e horário de entrada: %s\n" +
            "Data e horário de saída: %s\n" +
            "Número de diárias: %d\n" +
            "Total a pagar: R$ %.2f",
            dataEntrada, dataSaida, qtdDiarias, valorTotal
        );
    }
}