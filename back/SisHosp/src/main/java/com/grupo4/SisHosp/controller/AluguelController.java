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

    @GetMapping("/{id}/formulario")
    public ResponseEntity<String> formulario(@PathVariable Long id) {
        Aluguel aluguel = service.buscarPorId(id);
        return ResponseEntity.ok(aluguel.gerarFormulario());
    }

    @PostMapping
    public ResponseEntity<Aluguel> criar(@RequestBody Map<String, Object> body) {
        Long clienteId = Long.valueOf(body.get("clienteId").toString());
        Long quartoId = Long.valueOf(body.get("quartoId").toString());
        LocalDateTime entrada = LocalDateTime.parse(body.get("dataEntrada").toString());
        LocalDateTime saida = LocalDateTime.parse(body.get("dataSaida").toString());
        int numHospedes = Integer.parseInt(body.get("numHospedes").toString());
        boolean solicitouBerco = Boolean.parseBoolean(body.getOrDefault("solicitouBerco", false).toString());

        return ResponseEntity.ok(service.salvar(clienteId, quartoId, entrada, saida, numHospedes, solicitouBerco));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}