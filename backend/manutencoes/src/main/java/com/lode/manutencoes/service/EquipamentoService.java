package com.lode.manutencoes.service;


import com.lode.manutencoes.model.Equipamento;
import com.lode.manutencoes.repository.EquipamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EquipamentoService {

    @Autowired
    private EquipamentoRepository repository;

    public Page<Equipamento> listarTodos(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Optional<Equipamento> buscarPorId(Integer id){
        return repository.findById(id);
    }

    public Equipamento salvar(Equipamento equipamento){
        if (equipamento.getNome() == null || equipamento.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("O nome do equipamento é obrigatorio");
        }
        return repository.save(equipamento);
    }

    public Equipamento alterarStatus(Integer id, String novoStatus){
        Equipamento equipamento = repository.findById(id).orElseThrow(()-> new IllegalArgumentException("Equipamento não encontrado com o id :" + id));
        equipamento.setStatus(novoStatus);
        return repository.save(equipamento);
    }

    public void deletar(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Equipamento não encontrado para eliminação.");
        }
        repository.deleteById(id);
    }
}
