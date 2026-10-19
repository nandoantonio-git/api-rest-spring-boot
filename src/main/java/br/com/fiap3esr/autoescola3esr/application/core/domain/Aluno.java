package br.com.fiap3esr.autoescola3esr.application.core.domain;

import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.Endereco;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "Aluno")
@Table(name = "alunos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;

    @Embedded
    private Endereco endereco;
    private boolean ativo = true;

    public void atualizarInformacoes(String nome, String email, String telefone, Endereco endereco) {
        if (nome != null && !nome.isBlank()) this.nome = nome;
        if (email != null && !email.isBlank()) this.email = email;
        if (telefone != null && !telefone.isBlank()) this.telefone = telefone;
        if (endereco != null) {
            this.endereco.atualizarInformacoes(
                    endereco.getLogradouro(), endereco.getNumero(), endereco.getComplemento(),
                    endereco.getBairro(), endereco.getCidade(), endereco.getUf(), endereco.getCep());
        }
    }

    public void excluir() {
        this.ativo = false;
    }
}
