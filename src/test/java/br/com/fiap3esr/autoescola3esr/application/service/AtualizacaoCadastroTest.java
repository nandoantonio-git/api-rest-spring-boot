package br.com.fiap3esr.autoescola3esr.application.service;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper.AlunoMapper;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.mapper.InstrutorMapper;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.aluno.DadosAtualicaoAluno;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrutor.DadosAtualizacaoInstrutor;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Aluno;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrutor;
import br.com.fiap3esr.autoescola3esr.application.port.out.AlunoRepository;
import br.com.fiap3esr.autoescola3esr.application.port.out.InstrutorRepository;
import br.com.fiap3esr.autoescola3esr.exception.type.ValidacaoException;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.Endereco;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.EnderecoMapper;
import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.Especialidade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AtualizacaoCadastroTest {
    private final EnderecoMapper enderecoMapper = new EnderecoMapper();
    private InstrutorRepository instrutorRepository;
    private AlunoRepository alunoRepository;
    private InstrutorService instrutorService;
    private AlunoService alunoService;
    private Instrutor instrutor;
    private Aluno aluno;

    @BeforeEach
    void setUp() {
        instrutorRepository = mock(InstrutorRepository.class);
        alunoRepository = mock(AlunoRepository.class);
        instrutorService = new InstrutorService(instrutorRepository, new InstrutorMapper(enderecoMapper), enderecoMapper);
        alunoService = new AlunoService(alunoRepository, new AlunoMapper(enderecoMapper), enderecoMapper);

        Endereco endereco = new Endereco("Rua A", "10", null, "Centro", "São Paulo", "SP", "01000-000");
        instrutor = new Instrutor(1L, "Carlos", "carlos@email.com", "11999999999", "12345678901",
                Especialidade.CARROS, endereco, true);
        aluno = new Aluno(1L, "Ana", "ana@email.com", "11999999999", "12345678901", endereco, true);
        when(instrutorRepository.findById(1L)).thenReturn(Optional.of(instrutor));
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(instrutorRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(alunoRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    @DisplayName("Expectativa: Atualizar nome e telefone do instrutor sem informar endereço.")
    void atualizarInstrutorCenario1() {
        instrutorService.atualizarInstrutor(
                new DadosAtualizacaoInstrutor(1L, "Carlos Silva", null, "11888888888", null, null, null));

        assertThat(instrutor.getNome()).isEqualTo("Carlos Silva");
        assertThat(instrutor.getTelefone()).isEqualTo("11888888888");
    }

    @Test
    @DisplayName("Expectativa: Recusar alteração de e-mail, CNH ou especialidade do instrutor.")
    void atualizarInstrutorCenario2() {
        assertThatThrownBy(() -> instrutorService.atualizarInstrutor(
                new DadosAtualizacaoInstrutor(1L, null, "novo@email.com", null, null, null, null)))
                .isInstanceOf(ValidacaoException.class);
        assertThatThrownBy(() -> instrutorService.atualizarInstrutor(
                new DadosAtualizacaoInstrutor(1L, null, null, null, "99999999999", null, null)))
                .isInstanceOf(ValidacaoException.class);
        assertThatThrownBy(() -> instrutorService.atualizarInstrutor(
                new DadosAtualizacaoInstrutor(1L, null, null, null, null, Especialidade.MOTOS, null)))
                .isInstanceOf(ValidacaoException.class);

        assertThat(instrutor.getEmail()).isEqualTo("carlos@email.com");
        assertThat(instrutor.getCnh()).isEqualTo("12345678901");
        assertThat(instrutor.getEspecialidade()).isEqualTo(Especialidade.CARROS);
        verify(instrutorRepository, never()).save(any());
    }

    @Test
    @DisplayName("Expectativa: Recusar alteração de e-mail ou CPF do aluno.")
    void atualizarAlunoCenario1() {
        assertThatThrownBy(() -> alunoService.atualizar(
                new DadosAtualicaoAluno(1L, null, "nova@email.com", null, null, null)))
                .isInstanceOf(ValidacaoException.class);
        assertThatThrownBy(() -> alunoService.atualizar(
                new DadosAtualicaoAluno(1L, null, null, "98765432100", null, null)))
                .isInstanceOf(ValidacaoException.class);

        assertThat(aluno.getEmail()).isEqualTo("ana@email.com");
        assertThat(aluno.getCpf()).isEqualTo("12345678901");
        verify(alunoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Expectativa: Listagem de instrutores exibe a CNH.")
    void listarInstrutorExibeCnh() {
        assertThat(new InstrutorMapper(enderecoMapper).toList(instrutor).cnh()).isEqualTo("12345678901");
    }
}
