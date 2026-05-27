package com.lode.manutencoes;

import com.lode.manutencoes.model.Equipamento;
import com.lode.manutencoes.repository.EquipamentoRepository;
import com.lode.manutencoes.service.EquipamentoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EquipamentoServiceTest {

    @Mock
    private EquipamentoRepository repository;

    @InjectMocks
    private EquipamentoService service;

    @Test
    @DisplayName("Não deve cadastrar um equipamento se o nome estiver vazio ou nulo")
    void naoDeveSalvarEquipamentoSemNome() {
        Equipamento equipamentoSemNome = new Equipamento();
        equipamentoSemNome.setStatus("Operacional");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.salvar(equipamentoSemNome);
        });
        assertEquals("O nome do equipamento é obrigatorio", exception.getMessage());
        verify(repository, never()).save(any(Equipamento.class));
    }

    @Test
    @DisplayName("Deve alterar o status de um equipamento com sucesso")
    void deveAlterarStatusComSucesso() {
        Equipamento equipamentoExistente = new Equipamento();
        equipamentoExistente.setId(1);
        equipamentoExistente.setNome("Bomba Submersa");
        equipamentoExistente.setStatus("Operacional");
        when(repository.findById(1)).thenReturn(Optional.of(equipamentoExistente));
        when(repository.save(any(Equipamento.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Equipamento equipamentoAtualizado = service.alterarStatus(1, "Critico");
        assertNotNull(equipamentoAtualizado);
        assertEquals("Critico", equipamentoAtualizado.getStatus());
        verify(repository, times(1)).save(equipamentoExistente);
    }
}