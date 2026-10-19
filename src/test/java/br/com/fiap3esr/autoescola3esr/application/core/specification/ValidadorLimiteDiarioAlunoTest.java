package br.com.fiap3esr.autoescola3esr.application.core.specification;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.application.port.out.InstrucaoRepository;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidadorLimiteDiarioAlunoTest {
    @Mock
    InstrucaoRepository repository;

    @InjectMocks
    ValidadorLimiteDiarioAluno validador;

    private final DadosAgendamento dados = new DadosAgendamento(1L, null, null,
            LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0));

    @Test
    @DisplayName("Expectativa: Permitir a segunda instrução do aluno no mesmo dia.")
    void validarCenario1() {
        when(repository.countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(eq(1L), any(), any()))
                .thenReturn(1L);

        assertThatCode(() -> validador.validar(dados)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Expectativa: Recusar a terceira instrução do aluno no mesmo dia.")
    void validarCenario2() {
        when(repository.countByAlunoIdAndDataHoraBetweenAndMotivoCancelamentoIsNull(eq(1L), any(), any()))
                .thenReturn(2L);

        assertThatThrownBy(() -> validador.validar(dados)).isInstanceOf(ValidacaoException.class);
    }
}
