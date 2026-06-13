package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.model.*;
import com.grupo4.SisHosp.repository.*;
import com.grupo4.SisHosp.model.StatusAluguel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AluguelService {

    private final AluguelRepository aluguelRepository;
    private final QuartoRepository quartoRepository;
    private final ClienteRepository clienteRepository;
    private final PagamentoRepository pagamentoRepository;

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public Aluguel buscarPorId(Long id) {
        return aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
    }

    public Aluguel salvar(Long clienteId, Long quartoId, LocalDateTime entrada,
            LocalDateTime saida, int numHospedes, boolean solicitouBerco) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new RuntimeException("Quarto não encontrado"));

        List<Aluguel> conflitos = aluguelRepository.findConflitos(quartoId, entrada, saida, StatusAluguel.ATIVO);
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Quarto já está ocupado neste período!");
        }

        Aluguel aluguel = new Aluguel();
        aluguel.setCliente(cliente);
        aluguel.setQuarto(quarto);
        aluguel.setResidencia(quarto.getResidencia());
        aluguel.setDataEntrada(entrada);
        aluguel.setDataSaida(saida);
        aluguel.setNumHospedes(numHospedes);
        aluguel.setSolicitouBerco(solicitouBerco);

        int qtdDiarias = Aluguel.calcularQtdDiarias(entrada, saida);
        aluguel.setQtdDiarias(qtdDiarias);

        double valorDiaria;
        if (quarto instanceof QuartoDuplo duplo) {
            valorDiaria = duplo.calcularDiaria(solicitouBerco);
        } else if (quarto instanceof QuartoFamilia familia) {
            valorDiaria = familia.calcularDiaria(numHospedes);
        } else {
            valorDiaria = quarto.calcularDiaria();
        }

        aluguel.setValorTotal(valorDiaria * qtdDiarias);

        Aluguel salvo = aluguelRepository.save(aluguel);

        Pagamento pagamento = new Pagamento(salvo);
        pagamentoRepository.save(pagamento);

        return salvo;
    }

    public List<Aluguel> listarPorResidencia(Long residenciaId) {
        return aluguelRepository.findByResidenciaId(residenciaId);
    }

    public void deletar(Long id) {
        aluguelRepository.deleteById(id);
    }

    public Aluguel cancelar(Long id) {
        Aluguel aluguel = aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel não encontrado"));
        aluguel.setStatus(StatusAluguel.CANCELADO);
        return aluguelRepository.save(aluguel);
    }

    public List<Aluguel> listarPorCliente(Long clienteId) {
        return aluguelRepository.findByClienteId(clienteId);
    }
}