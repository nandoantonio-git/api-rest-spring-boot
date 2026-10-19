package br.com.fiap3esr.autoescola3esr.application.core.specification;

import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrucao;
import br.com.fiap3esr.autoescola3esr.application.core.specification.contract.ValidadorCancelamento;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ValidadorAntecedenciaCancelamento implements ValidadorCancelamento {
    @Override
    public void validar(Instrucao instrucao) {
        long antecedencia = Duration.between(LocalDateTime.now(), instrucao.getDataHora()).toHours();

        if (antecedencia < 24) {
            throw new ValidacaoException("O cancelamento precisa ocorrer com antecedência mínima de 24 horas!");
        }
    }
}
