package com.grupo4.SisHosp.controller;

import com.grupo4.SisHosp.model.Aluguel;
import com.grupo4.SisHosp.service.AluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alugueis")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AluguelController {

    private final AluguelService service;

    @GetMapping
    public List<Aluguel> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Aluguel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/residencia/{residenciaId}")
    public List<Aluguel> historicoPorResidencia(@PathVariable Long residenciaId) {
        return service.listarPorResidencia(residenciaId);
    }

    
    @GetMapping("/cliente/{clienteId}")
    public List<Aluguel> historicoPorCliente(@PathVariable Long clienteId) {
        return service.listarPorCliente(clienteId);
    }

    @GetMapping("/{id}/formulario")
    public ResponseEntity<String> formulario(@PathVariable Long id) {
        Aluguel aluguel = service.buscarPorId(id);
        return ResponseEntity.ok(aluguel.gerarFormulario());
    }

    @PostMapping
    public ResponseEntity<Aluguel> criar(@RequestBody Map<String, Object> body) {
        Long clienteId = Long.valueOf(obrigatorio(body, "clienteId").toString());
        Long quartoId = Long.valueOf(obrigatorio(body, "quartoId").toString());
        LocalDateTime entrada = LocalDateTime.parse(obrigatorio(body, "dataEntrada").toString());
        LocalDateTime saida = LocalDateTime.parse(obrigatorio(body, "dataSaida").toString());
        int numHospedes = Integer.parseInt(obrigatorio(body, "numHospedes").toString());
        boolean solicitouBerco = Boolean.parseBoolean(body.getOrDefault("solicitouBerco", false).toString());

        return ResponseEntity.ok(service.salvar(clienteId, quartoId, entrada, saida, numHospedes, solicitouBerco));
    }

    
    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Aluguel> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(service.cancelar(id));
    }
    
    @PostMapping("/{id}/pagar")
    public ResponseEntity<?> pagar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        String forma = obrigatorio(body, "forma").toString();
        return ResponseEntity.ok(service.processarPagamento(id, forma));
    }

    @GetMapping("/{id}/pagamento")
    public ResponseEntity<?> pagamento(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPagamento(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    private static Object obrigatorio(Map<String, Object> body, String campo) {
        Object valor = body.get(campo);
        if (valor == null) {
            throw new IllegalArgumentException("Campo obrigatório ausente: " + campo);
        }
        return valor;
    }
}