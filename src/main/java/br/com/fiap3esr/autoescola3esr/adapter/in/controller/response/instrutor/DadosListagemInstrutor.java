package br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.instrutor;

import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.Especialidade;

public record DadosListagemInstrutor(
        Long id,
        String nome,
        String email,
        String cnh,
        Especialidade especialidade) {
}