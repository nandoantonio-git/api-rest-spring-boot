package br.com.fiap3esr.autoescola3esr.adapter.out.external.viacep;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ViaCepResponse(
        String logradouro,
        String bairro,
        @JsonProperty("localidade") String cidade,
        String uf,
        String cep,
        @JsonProperty("erro") Boolean erro) {
}
