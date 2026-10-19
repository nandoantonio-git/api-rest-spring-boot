package br.com.fiap3esr.autoescola3esr.application.core.domain;

import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.MotivoCancelamento;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Instrucao")
@Table(name = "instrucoes")
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = "id")
public class Instrucao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;

    @Column(name = "data_hora")
    LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    @Column(name = "motivo_cancelamento")
    private MotivoCancelamento motivoCancelamento;

    public Instrucao(Long id, Aluno aluno, Instrutor instrutor, LocalDateTime dataHora) {
        this.id = id;
        this.aluno = aluno;
        this.instrutor = instrutor;
        this.dataHora = dataHora;
    }

    public boolean isCancelada() {
        return motivoCancelamento != null;
    }

    public void cancelar(MotivoCancelamento motivo) {
        this.motivoCancelamento = motivo;
    }
}
