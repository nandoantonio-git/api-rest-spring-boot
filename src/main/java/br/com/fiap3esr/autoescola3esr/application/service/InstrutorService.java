package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper.InstrutorMapper;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrutor.DadosCadastroInstrutor;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.instrutor.DadosDetalhamentoInstrutor;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.instrutor.DadosListagemInstrutor;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrutor;
import br.com.fiap3esr.autoescola3esr.application.port.out.InstrutorRepository;
import br.com.fiap3esr.autoescola3esr.exception.type.InstrutorNotFoundException;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstrutorService {
    private final InstrutorRepository repository;
    private final InstrutorMapper mapper;
    private final EnderecoMapper enderecoMapper;

    @Transactional
    public DadosDetalhamentoInstrutor cadastrarInstrutor(DadosCadastroInstrutor dados) {
        Instrutor instrutor = mapper.toDomain(dados);
        Instrutor saved = repository.save(instrutor);
        return mapper.toDetails(saved);
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemInstrutor> listarInstrutores(Pageable paginacao) {
        return repository
                .findAllByAtivoTrue(paginacao)
                .map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoInstrutor detalharInstrutor(Long id) {
        Instrutor instrutor = repository
                .findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do Instrutor informado não existe!"));
        return mapper.toDetails(instrutor);
    }

    @Transactional
    public DadosDetalhamentoInstrutor atualizarInstrutor(DadosAtualizacaoInstrutor dados) {
        Instrutor instrutor = repository
                .findById(dados.id())
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do Instrutor informado não existe!"));
        if (dados.email() != null || dados.cnh() != null || dados.especialidade() != null) {
            throw new ValidacaoException("Não é permitido alterar o e-mail, a CNH ou a especialidade do instrutor!");
        }
        instrutor.atualizarInformacoes(
                dados.nome(),
                dados.telefone(),
                dados.endereco() == null ? null : enderecoMapper.toVo(dados.endereco())
        );
        Instrutor saved = repository.save(instrutor);
        return mapper.toDetails(saved);
    }

    @Transactional
    public void excluirInstrutor(Long id) {
        Instrutor instrutor = repository
                .findById(id)
                .orElseThrow(() ->
                        new InstrutorNotFoundException("ID do Instrutor informado não existe!"));
        instrutor.excluir();
        repository.save(instrutor);
    }
}