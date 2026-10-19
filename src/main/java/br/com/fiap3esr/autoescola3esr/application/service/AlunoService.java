package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper.AlunoMapper;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosAtualicaoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosCadastroAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosDetalhamentoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.aluno.DadosListagemAluno;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Aluno;
import br.com.fiap3esr.autoescola3esr.application.port.out.AlunoRepository;
import br.com.fiap3esr.autoescola3esr.exception.type.AlunoNotFoundException;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.EnderecoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlunoService {
    private final AlunoRepository repository;
    private final AlunoMapper mapper;
    private final EnderecoMapper enderecoMapper;

    @Transactional
    public DadosDetalhamentoAluno cadastrar(DadosCadastroAluno dados) {
        return mapper.toDetails(repository.save(mapper.toDomain(dados)));
    }

    @Transactional(readOnly = true)
    public Page<DadosListagemAluno> listar(Pageable pageable) {
        return repository.findAllByAtivoTrue(pageable).map(mapper::toList);
    }

    @Transactional(readOnly = true)
    public DadosDetalhamentoAluno detalhar(Long id) {
        return mapper.toDetails(buscar(id));
    }

    @Transactional
    public DadosDetalhamentoAluno atualizar(DadosAtualicaoAluno dados) {
        Aluno aluno = buscar(dados.id());
        aluno.atualizarInformacoes(dados.nome(), dados.email(), dados.telefone(),
                dados.endereco() == null ? null : enderecoMapper.toVo(dados.endereco()));
        return mapper.toDetails(repository.save(aluno));
    }

    @Transactional
    public void excluir(Long id) {
        Aluno aluno = buscar(id);
        aluno.excluir();
        repository.save(aluno);
    }

    private Aluno buscar(Long id) {
        return repository.findById(id).orElseThrow(() ->
                new AlunoNotFoundException("ID do aluno informado não existe!"));
    }
}
