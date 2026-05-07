package com.grupo4.SisHosp.controller;

import com.grupo4.SisHosp.model.*;
import com.grupo4.SisHosp.service.QuartoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/quartos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
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

    @PostMapping("/individual")
    public ResponseEntity<Quarto> criarIndividual(@RequestBody QuartoIndividual quarto) {
        return ResponseEntity.ok(service.salvar(quarto));
    }

    @PostMapping("/duplo")
    public ResponseEntity<Quarto> criarDuplo(@RequestBody QuartoDuplo quarto) {
        return ResponseEntity.ok(service.salvar(quarto));
    }

    @PostMapping("/familia")
    public ResponseEntity<Quarto> criarFamilia(@RequestBody QuartoFamilia quarto) {
        return ResponseEntity.ok(service.salvar(quarto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}