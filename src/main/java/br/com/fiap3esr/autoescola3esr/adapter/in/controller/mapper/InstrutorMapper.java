package br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.instrutor.DadosListagemInstrutor;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrutor;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InstrutorMapper {
    private final EnderecoMapper enderecoMapper;

    public Instrutor toDomain(DadosCadastroInstrutor dados) {
        return new Instrutor(
                null,
                dados.nome(),
                dados.email(),
                dados.telefone(),
                dados.cnh(),
                dados.especialidade(),
                enderecoMapper.toVo(dados.endereco()),
                true
        );
    }

    public DadosDetalhamentoInstrutor toDetails(Instrutor domain) {
        return new DadosDetalhamentoInstrutor(
                domain.getId(),
                domain.getNome(),
                domain.getEmail(),
                domain.getTelefone(),
                domain.getCnh(),
                domain.getEspecialidade(),
                enderecoMapper.toDto(domain.getEndereco()),
                domain.isAtivo()
        );
    }

    public DadosListagemInstrutor toList(Instrutor domain) {
        return new DadosListagemInstrutor(
                domain.getId(),
                domain.getNome(),
                domain.getEmail(),
                domain.getCnh(),
                domain.getEspecialidade()
        );
    }
}