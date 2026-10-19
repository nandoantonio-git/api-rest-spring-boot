package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrucao.DadosCancelamento;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrucao;
import br.com.fiap3esr.autoescola3esr.application.core.specification.ValidadorAntecedenciaCancelamento;
import br.com.fiap3esr.autoescola3esr.application.port.out.AlunoRepository;
import br.com.fiap3esr.autoescola3esr.application.port.out.InstrucaoRepository;
import br.com.fiap3esr.autoescola3esr.application.port.out.InstrutorRepository;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.MotivoCancelamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AgendaDeInstrucoesCancelamentoTest {
    private InstrucaoRepository repository;
    private AgendaDeInstrucoes agenda;

    @BeforeEach
    void setUp() {
        repository = mock(InstrucaoRepository.class);
        agenda = new AgendaDeInstrucoes(mock(AlunoRepository.class), mock(InstrutorRepository.class),
                repository, List.of(), List.of(new ValidadorAntecedenciaCancelamento()));
    }

    @Test
    @DisplayName("Expectativa: Cancelar instrução com antecedência mínima de 24 horas.")
    void cancelarCenario1() {
        Instrucao instrucao = instrucaoEm(LocalDateTime.now().plusDays(2));

        agenda.cancelar(new DadosCancelamento(1L, MotivoCancelamento.ALUNO_DESISTIU));

        assertThat(instrucao.getMotivoCancelamento()).isEqualTo(MotivoCancelamento.ALUNO_DESISTIU);
        verify(repository).save(instrucao);
    }

    @Test
    @DisplayName("Expectativa: Recusar cancelamento com menos de 24 horas de antecedência.")
    void cancelarCenario2() {
        Instrucao instrucao = instrucaoEm(LocalDateTime.now().plusHours(5));

        assertThatThrownBy(() -> agenda.cancelar(new DadosCancelamento(1L, MotivoCancelamento.OUTROS)))
                .isInstanceOf(ValidacaoException.class);
        assertThat(instrucao.isCancelada()).isFalse();
        verify(repository, never()).save(any());
    }

    @Test
    @DisplayName("Expectativa: Recusar cancelamento de instrução já cancelada.")
    void cancelarCenario3() {
        Instrucao instrucao = instrucaoEm(LocalDateTime.now().plusDays(2));
        instrucao.cancelar(MotivoCancelamento.INSTRUTOR_CANCELOU);

        assertThatThrownBy(() -> agenda.cancelar(new DadosCancelamento(1L, MotivoCancelamento.OUTROS)))
                .isInstanceOf(ValidacaoException.class);
    }

    private Instrucao instrucaoEm(LocalDateTime dataHora) {
        Instrucao instrucao = new Instrucao(1L, null, null, dataHora);
        when(repository.findById(1L)).thenReturn(Optional.of(instrucao));
        return instrucao;
    }
}
