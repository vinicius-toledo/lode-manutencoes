package com.lode.manutencoes.Dto;

import com.lode.manutencoes.model.Equipamento;


public record EquipamentoDto(
        Integer id,
        String nome,
        String tipo,
        String status
) {
    public EquipamentoDto(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getTipo(),
                equipamento.getStatus()
        );
    }
}