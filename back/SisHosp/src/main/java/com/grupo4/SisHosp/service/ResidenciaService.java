package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.model.Residencia;
import com.grupo4.SisHosp.repository.ResidenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResidenciaService {

    private final ResidenciaRepository repository;

    public List<Residencia> listarTodas() {
        return repository.findAll();
    }

    public Residencia buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Residência não encontrada"));
    }

    public Residencia salvar(Residencia residencia) {
        return repository.save(residencia);
    }

    public Residencia atualizar(Long id, Residencia dados) {
        Residencia residencia = buscarPorId(id);
        residencia.setNome(dados.getNome());
        residencia.setEndereco(dados.getEndereco());
        residencia.setCidade(dados.getCidade());
        return repository.save(residencia);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}