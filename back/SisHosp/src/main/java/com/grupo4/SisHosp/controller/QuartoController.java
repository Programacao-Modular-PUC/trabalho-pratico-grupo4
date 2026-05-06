package com.grupo4.SisHosp.controller;

import com.grupo4.SisHosp.model.Quarto;
import com.grupo4.SisHosp.service.QuartoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/quartos")
@RequiredArgsConstructor
public class QuartoController {

    private final QuartoService service;

    @GetMapping
    public List<Quarto> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Quarto> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Quarto> criar(@RequestBody Quarto quarto) {
        return ResponseEntity.ok(service.salvar(quarto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}