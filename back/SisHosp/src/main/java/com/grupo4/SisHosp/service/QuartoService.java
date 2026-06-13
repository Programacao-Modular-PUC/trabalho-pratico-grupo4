package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.model.Quarto;
import com.grupo4.SisHosp.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuartoService {

    private final QuartoRepository repository;

    public List<Quarto> listarTodos() {
        return repository.findAll();
    }

    public List<Quarto> listarPorTipo(String tipo) {
        return repository.findAll().stream()
                .filter(q -> q.getTipo().equalsIgnoreCase(tipo))
                .collect(Collectors.toList());
    }

    public Quarto buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));
    }

    public Quarto salvar(Quarto quarto) {
        return repository.save(quarto);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}