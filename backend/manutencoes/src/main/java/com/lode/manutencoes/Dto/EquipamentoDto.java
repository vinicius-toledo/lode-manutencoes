package com.lode.manutencoes.Dto;

import com.lode.manutencoes.model.Equipamento;
import java.time.LocalDate;


public record EquipamentoDto(
        Integer id,
        String nome,
        String tipo,
        LocalDate dataInstalacao,
        String status
) {
    public EquipamentoDto(Equipamento equipamento) {
        this(
                equipamento.getId(),
                equipamento.getNome(),
                equipamento.getTipo(),
                equipamento.getDataInstalacao(),
                equipamento.getStatus()

        );
    }
}