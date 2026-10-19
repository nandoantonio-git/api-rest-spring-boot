package br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno;

import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.dto.DadosEndereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record DadosAtualicaoAluno(
        @NotNull Long id,
        String nome,
        String email,
        String telefone,
        DadosEndereco endereco) {
}
