package com.grupo4.SisHosp.repository;

import com.grupo4.SisHosp.model.Aluguel;
import com.grupo4.SisHosp.model.StatusAluguel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface AluguelRepository extends JpaRepository<Aluguel, Long> {

    @Query("SELECT a FROM Aluguel a WHERE a.quarto.id = :quartoId " +
            "AND a.status = :status " +
            "AND a.dataEntrada < :dataSaida " +
            "AND a.dataSaida > :dataEntrada")
    List<Aluguel> findConflitos(
            @Param("quartoId") Long quartoId,
            @Param("dataEntrada") LocalDateTime dataEntrada,
            @Param("dataSaida") LocalDateTime dataSaida,
            @Param("status") StatusAluguel status);

    List<Aluguel> findByResidenciaId(Long residenciaId);

    List<Aluguel> findByClienteId(Long clienteId);
}