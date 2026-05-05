package com.grupo4.SisHosp.service;

import com.grupo4.SisHosp.model.Cliente;
import com.grupo4.SisHosp.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public List<Cliente> listarTodos() {
        return repository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente atualizar(Long id, Cliente dados) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setCpf(dados.getCpf());
        cliente.setTelefone(dados.getTelefone());
        return repository.save(cliente);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}