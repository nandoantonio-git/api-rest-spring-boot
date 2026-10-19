package br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Aluno;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlunoMapper {
    private final EnderecoMapper enderecoMapper;

    public Aluno toDomain(DadosCadastroAluno dados) {
        return new Aluno(null, dados.nome(), dados.email(), dados.telefone(), dados.cpf(),
                enderecoMapper.toVo(dados.endereco()), true);
    }

    public DadosDetalhamentoAluno toDetails(Aluno aluno) {
        return new DadosDetalhamentoAluno(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getTelefone(),
                aluno.getCpf(), enderecoMapper.toDto(aluno.getEndereco()), aluno.isAtivo());
    }

    public DadosListagemAluno toList(Aluno aluno) {
        return new DadosListagemAluno(aluno.getId(), aluno.getNome(), aluno.getEmail(), aluno.getCpf());
    }
}
