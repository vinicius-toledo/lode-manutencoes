package com.lode.manutencoes.controller;

import com.lode.manutencoes.Dto.EquipamentoDto;
import com.lode.manutencoes.model.Equipamento;
import com.lode.manutencoes.service.EquipamentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService service;


    @GetMapping
    public Page<EquipamentoDto> listarTodos(@PageableDefault(size = 10, page = 0,sort = "nome")Pageable pageable){
        Page<Equipamento> equipamentos = service.listarTodos(pageable);
        return equipamentos.map(EquipamentoDto::new);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoDto> buscarPorId(@PathVariable Integer id){
        return service.buscarPorId(id)
                .map(equipamento -> ResponseEntity.ok(new EquipamentoDto(equipamento)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EquipamentoDto> salvar(@RequestBody Equipamento equipamento) {
            Equipamento novoEquipamento = service.salvar(equipamento);
            return ResponseEntity.ok(new EquipamentoDto(novoEquipamento));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<EquipamentoDto> alterarStatus(@PathVariable Integer id, @RequestParam String novoStatus){
            Equipamento equipamentoAtualizado = service.alterarStatus(id, novoStatus);
            return ResponseEntity.ok(new EquipamentoDto(equipamentoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id){
            service.deletar(id);
            return ResponseEntity.noContent().build();
    }
}