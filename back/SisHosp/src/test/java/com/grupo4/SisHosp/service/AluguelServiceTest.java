package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.exception.*;
import com.grupo4.SisHosp.model.*;
import com.grupo4.SisHosp.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AluguelServiceTest {

    @Mock private AluguelRepository aluguelRepository;
    @Mock private QuartoRepository quartoRepository;
    @Mock private ClienteRepository clienteRepository;
    @Mock private PagamentoRepository pagamentoRepository;
    @InjectMocks private AluguelService service;

    private final LocalDateTime entrada = LocalDateTime.of(2026, 1, 10, 14, 0);
    private final LocalDateTime saida = LocalDateTime.of(2026, 1, 12, 11, 0);

    private QuartoDuplo quartoDuploValido() {
        QuartoDuplo quarto = new QuartoDuplo();
        quarto.setId(1L);
        quarto.setValorBase(200.0);
        quarto.setTipoCama(QuartoDuplo.TipoCama.CASAL);
        quarto.setPossuiBerco(false);
        quarto.setPossuiAR(false);
        quarto.setPossuiHidro(false);
        return quarto;
    }

    @Test
    @DisplayName("Quarto ocupado no período lança QuartoIndisponivelException")
    void quartoOcupado() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(new Cliente()));
        when(quartoRepository.findById(anyLong())).thenReturn(Optional.of(quartoDuploValido()));
        when(aluguelRepository.findConflitos(anyLong(), any(), any(), any()))
                .thenReturn(List.of(new Aluguel()));

        assertThrows(QuartoIndisponivelException.class,
                () -> service.salvar(1L, 1L, entrada, saida, 2, false));
    }

    @Test
    @DisplayName("Número de hóspedes acima da capacidade lança CapacidadeExcedidaException")
    void capacidadeExcedida() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(new Cliente()));
        when(quartoRepository.findById(anyLong())).thenReturn(Optional.of(quartoDuploValido()));

        assertThrows(CapacidadeExcedidaException.class,
                () -> service.salvar(1L, 1L, entrada, saida, 3, false));
    }


    @Test
    @DisplayName("Berço em quarto que não permite lança RecursoNaoPermitidoException")
    void bercoNaoPermitido() {
        QuartoIndividual individual = new QuartoIndividual();
        individual.setId(1L);
        individual.setValorBase(100.0);
        individual.setNumeroDeCamas(2);

        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(new Cliente()));
        when(quartoRepository.findById(anyLong())).thenReturn(Optional.of(individual));

        assertThrows(RecursoNaoPermitidoException.class,
                () -> service.salvar(1L, 1L, entrada, saida, 1, true));
    }

    @Test
    @DisplayName("Data de saída anterior à entrada lança DataInvalidaException")
    void dataInvalida() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(new Cliente()));
        when(quartoRepository.findById(anyLong())).thenReturn(Optional.of(quartoDuploValido()));

        LocalDateTime saidaAntes = entrada.minusDays(1);
        assertThrows(DataInvalidaException.class,
                () -> service.salvar(1L, 1L, entrada, saidaAntes, 2, false));
    }

    @Test
    @DisplayName("Aluguel válido é salvo como ATIVO com o valor total correto")
    void aluguelValido() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(new Cliente()));
        when(quartoRepository.findById(anyLong())).thenReturn(Optional.of(quartoDuploValido()));
        when(aluguelRepository.findConflitos(anyLong(), any(), any(), any()))
                .thenReturn(List.of());
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(i -> i.getArgument(0));
        when(pagamentoRepository.save(any(Pagamento.class))).thenAnswer(i -> i.getArgument(0));

        Aluguel aluguel = service.salvar(1L, 1L, entrada, saida, 2, false);

        assertEquals(StatusAluguel.ATIVO, aluguel.getStatus());
        assertEquals(2, aluguel.getQtdDiarias());

        assertEquals(440.0, aluguel.getValorTotal(), 0.001);
    }


    @Test
    @DisplayName("Cancelar um aluguel ativo muda o status para CANCELADO")
    void cancelarAtivo() {
        Aluguel aluguel = new Aluguel();
        aluguel.setId(5L);
        aluguel.setStatus(StatusAluguel.ATIVO);

        when(aluguelRepository.findById(5L)).thenReturn(Optional.of(aluguel));
        when(pagamentoRepository.findByAluguelId(5L)).thenReturn(Optional.empty());
        when(aluguelRepository.save(any(Aluguel.class))).thenAnswer(i -> i.getArgument(0));

        Aluguel cancelado = service.cancelar(5L);

        assertEquals(StatusAluguel.CANCELADO, cancelado.getStatus());
    }

    @Test
    @DisplayName("Cancelar um aluguel já cancelado lança IllegalStateException")
    void cancelarJaCancelado() {
        Aluguel aluguel = new Aluguel();
        aluguel.setId(5L);
        aluguel.setStatus(StatusAluguel.CANCELADO);

        when(aluguelRepository.findById(5L)).thenReturn(Optional.of(aluguel));

        assertThrows(IllegalStateException.class, () -> service.cancelar(5L));
    }
}
