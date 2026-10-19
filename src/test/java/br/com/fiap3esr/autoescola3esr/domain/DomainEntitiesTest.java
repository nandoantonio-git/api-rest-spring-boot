package br.com.fiap3esr.autoescola3esr.domain;

import br.com.fiap3esr.autoescola3esr.application.core.domain.Aluno;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Instrucao;
import br.com.fiap3esr.autoescola3esr.application.core.domain.Usuario;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.Endereco;
import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.MotivoCancelamento;
import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.Role;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class DomainEntitiesTest {
    private Endereco endereco() {
        return new Endereco("Rua A", "10", null, "Centro", "São Paulo", "SP", "01000-000");
    }

    @Test
    void alunoAtualizaInformacoesEExcluiPorInativacao() {
        Aluno aluno = new Aluno(null, "Ana", "ana@email.com", "11999999999", "12345678901", endereco(), true);

        aluno.atualizarInformacoes("Ana Silva", "11888888888", endereco());
        aluno.excluir();

        assertThat(aluno.getNome()).isEqualTo("Ana Silva");
        assertThat(aluno.getTelefone()).isEqualTo("11888888888");
        assertThat(aluno.getEmail()).isEqualTo("ana@email.com");
        assertThat(aluno.isAtivo()).isFalse();
    }

    @Test
    void usuarioExpõeRoleSpringSecurity() {
        Usuario usuario = new Usuario(1L, "admin", "hash", Role.ADMIN);

        assertThat(usuario.getUsername()).isEqualTo("admin");
        assertThat(usuario.getPassword()).isEqualTo("hash");
        assertThat(usuario.getAuthorities()).extracting("authority").containsExactly("ROLE_ADMIN");
        assertThat(usuario.isEnabled()).isTrue();
    }

    @Test
    void usuarioInativoNaoPodeAutenticar() {
        Usuario usuario = new Usuario(1L, "admin", "hash", Role.ADMIN);

        usuario.excluir();

        assertThat(usuario.isEnabled()).isFalse();
    }

    @Test
    void instrucaoMantemAlunoInstrutorEData() {
        Aluno aluno = new Aluno(null, "Ana", "ana@email.com", "11999999999", "12345678901", endereco(), true);
        Instrucao instrucao = new Instrucao(1L, aluno, null, LocalDateTime.of(2026, 10, 20, 10, 0));

        assertThat(instrucao.getAluno()).isSameAs(aluno);
        assertThat(instrucao.getDataHora()).isEqualTo(LocalDateTime.of(2026, 10, 20, 10, 0));
        assertThat(instrucao.isCancelada()).isFalse();
    }

    @Test
    void instrucaoCanceladaRegistraMotivo() {
        Instrucao instrucao = new Instrucao(1L, null, null, LocalDateTime.of(2026, 10, 20, 10, 0));

        instrucao.cancelar(MotivoCancelamento.ALUNO_DESISTIU);

        assertThat(instrucao.isCancelada()).isTrue();
        assertThat(instrucao.getMotivoCancelamento()).isEqualTo(MotivoCancelamento.ALUNO_DESISTIU);
    }
}
