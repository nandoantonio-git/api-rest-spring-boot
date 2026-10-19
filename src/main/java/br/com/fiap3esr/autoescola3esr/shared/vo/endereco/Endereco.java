package br.com.fiap3esr.autoescola3esr.shared.vo.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Endereco {
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    public void atualizarInformacoes(
            String logradouro,
            String numero,
            String complemento,
            String bairro,
            String cidade,
            String uf,
            String cep) {
        if(logradouro != null && !logradouro.isBlank()) {
            this.logradouro = logradouro;
        }
        if(numero != null) {
            this.numero = numero;
        }
        if(complemento != null) {
            this.complemento = complemento;
        }
        if(bairro != null && !bairro.isBlank()) {
            this.bairro = bairro;
        }
        if(cidade != null && !cidade.isBlank()) {
            this.cidade = cidade;
        }
        if(uf != null && !uf.isBlank()) {
            this.uf = uf;
        }
        if(cep != null && !cep.isBlank()) {
            this.cep = cep;
        }
    }
}