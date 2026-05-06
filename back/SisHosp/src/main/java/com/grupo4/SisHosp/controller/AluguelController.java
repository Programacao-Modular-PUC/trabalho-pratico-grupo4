package com.grupo4.SisHosp.controller;

import com.grupo4.SisHosp.model.Aluguel;
import com.grupo4.SisHosp.service.AluguelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alugueis")
@RequiredArgsConstructor
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

    @PostMapping
    public ResponseEntity<Aluguel> criar(@RequestBody Aluguel aluguel) {
        return ResponseEntity.ok(service.salvar(aluguel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}