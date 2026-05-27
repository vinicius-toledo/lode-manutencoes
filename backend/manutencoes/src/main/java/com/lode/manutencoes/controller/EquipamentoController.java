package com.lode.manutencoes.controller;

import com.lode.manutencoes.Dto.EquipamentoDto;
import com.lode.manutencoes.model.Equipamento;
import com.lode.manutencoes.service.EquipamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipamentos")
@CrossOrigin(origins = "*")
public class EquipamentoController {

    @Autowired
    private EquipamentoService service;


    @GetMapping
    public List<EquipamentoDto> listarTodos(){
        List<Equipamento> equipamentos = service.listarTodos();
        return equipamentos.stream()
                .map(EquipamentoDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoDto> buscarPorId(@PathVariable Integer id){
        return service.buscarPorId(id)
                .map(equipamento -> ResponseEntity.ok(new EquipamentoDto(equipamento)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EquipamentoDto> salvar(@RequestBody Equipamento equipamento) {
        try {
            Equipamento novoEquipamento = service.salvar(equipamento);
            return ResponseEntity.ok(new EquipamentoDto(novoEquipamento));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EquipamentoDto> alterarStatus(@PathVariable Integer id, @RequestParam String novoStatus){
        try {
            Equipamento equipamentoAtualizado = service.alterarStatus(id, novoStatus);
            return ResponseEntity.ok(new EquipamentoDto(equipamentoAtualizado));
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
        try {
            service.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e){
            return ResponseEntity.notFound().build();
        }
    }
}