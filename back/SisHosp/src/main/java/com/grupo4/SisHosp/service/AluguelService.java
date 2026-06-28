package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.model.*;
import com.grupo4.SisHosp.notification.CentralNotificacoes;
import com.grupo4.SisHosp.payment.FormaPagamento;
import com.grupo4.SisHosp.payment.FormaPagamentoFactory;
import com.grupo4.SisHosp.repository.*;
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
    private final CentralNotificacoes central;

    public List<Aluguel> listarTodos() {
        return aluguelRepository.findAll();
    }

    public Aluguel buscarPorId(Long id) {
        return aluguelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aluguel nao encontrado"));
    }

    public Aluguel salvar(Long clienteId, Long quartoId, LocalDateTime entrada,
            LocalDateTime saida, int numHospedes, boolean solicitouBerco) {

        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente nao encontrado"));

        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new RuntimeException("Quarto nao encontrado"));

        List<Aluguel> conflitos = aluguelRepository.findConflitos(quartoId, entrada, saida, StatusAluguel.ATIVO);
        if (!conflitos.isEmpty()) {
            throw new RuntimeException("Quarto ja esta ocupado neste periodo!");
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

        central.notificar("RESERVA_CRIADA",
                "Reserva no " + salvo.getId() + " criada para o cliente "
                        + cliente.getNome() + ".");

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
                .orElseThrow(() -> new RuntimeException("Aluguel nao encontrado"));
        aluguel.setStatus(StatusAluguel.CANCELADO);
        Aluguel salvo = aluguelRepository.save(aluguel);
        central.notificar("RESERVA_CANCELADA",
                "Reserva no " + salvo.getId() + " foi cancelada.");

        return salvo;
    }

    public List<Aluguel> listarPorCliente(Long clienteId) {
        return aluguelRepository.findByClienteId(clienteId);
    }

    public Pagamento buscarPagamento(Long aluguelId) {
        return pagamentoRepository.findByAluguelId(aluguelId)
                .orElseThrow(() -> new RuntimeException("Pagamento nao encontrado"));
    }

    public Pagamento processarPagamento(Long aluguelId, String formaPagamento) {
        Aluguel aluguel = buscarPorId(aluguelId);

        Pagamento pagamento = pagamentoRepository.findByAluguelId(aluguelId)
                .orElse(new Pagamento(aluguel));

        FormaPagamento estrategia = FormaPagamentoFactory.criar(formaPagamento);
        String resultado = estrategia.processar(aluguel.getValorTotal());

        pagamento.setForma(estrategia.getNome());
        pagamento.setDescricao(resultado);
        pagamento.setStatus("PAGO");
        pagamentoRepository.save(pagamento);
        central.notificar("PAGAMENTO_CONFIRMADO",
                "Pagamento da reserva no " + aluguel.getId()
                        + " confirmado via " + estrategia.getNome() + ".");

        return pagamento;
    }
}
