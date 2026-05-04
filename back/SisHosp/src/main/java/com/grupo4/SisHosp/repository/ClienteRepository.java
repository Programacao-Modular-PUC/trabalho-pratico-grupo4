package com.grupo4.SisHosp.repository;

import com.grupo4.SisHosp.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {}