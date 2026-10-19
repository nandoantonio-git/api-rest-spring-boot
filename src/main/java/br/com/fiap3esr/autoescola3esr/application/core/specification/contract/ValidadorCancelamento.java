package br.com.fiap3esr.autoescola3esr.application.core.specification.contract;

import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrucao;

public interface ValidadorCancelamento {
    void validar(Instrucao instrucao);
}
