package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.model.*;
import com.grupo4.SisHosp.model.QuartoDuplo;
import com.grupo4.SisHosp.model.QuartoFamilia;
import com.grupo4.SisHosp.repository.AluguelRepository;
import com.grupo4.SisHosp.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final QuartoRepository quartoRepository;

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public Aluguel buscarPorId(Long id) {
        return aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
    }

    public Aluguel salvar(Aluguel aluguel) {
        Quarto quarto = quartoRepository.findById(aluguel.getQuarto().getId())
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        int numHospedes = aluguel.getNumHospedes() != null ? aluguel.getNumHospedes() : 1;

        double valorDiaria;

        if (quarto instanceof QuartoDuplo duplo) {
            boolean solicitouBerco = Boolean.TRUE.equals(aluguel.getSolicitouBerco());
            valorDiaria = duplo.calcularDiaria(solicitouBerco);
        } else if (quarto instanceof QuartoFamilia familia) {
            valorDiaria = familia.calcularDiaria(numHospedes);
        } else {
            valorDiaria = quarto.calcularDiaria();
        }

        aluguel.setValorTotal(valorDiaria);
        return aluguelRepository.save(aluguel);
    }

    public void deletar(Long id) {
        aluguelRepository.deleteById(id);
    }
}