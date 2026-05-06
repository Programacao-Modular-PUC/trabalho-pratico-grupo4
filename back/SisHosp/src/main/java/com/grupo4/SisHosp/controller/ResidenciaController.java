package com.grupo4.SisHosp.controller;

import com.grupo4.SisHosp.model.Residencia;
import com.grupo4.SisHosp.service.ResidenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/residencias")
@RequiredArgsConstructor
public class ResidenciaController {

    private final ResidenciaService service;

    @GetMapping
    public List<Residencia> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Residencia> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Residencia> criar(@RequestBody Residencia residencia) {
        return ResponseEntity.ok(service.salvar(residencia));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Residencia> atualizar(@PathVariable Long id, @RequestBody Residencia dados) {
        return ResponseEntity.ok(service.atualizar(id, dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}