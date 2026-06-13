package com.grupo4.SisHosp.repository;

import com.grupo4.SisHosp.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByAluguelId(Long aluguelId);
}