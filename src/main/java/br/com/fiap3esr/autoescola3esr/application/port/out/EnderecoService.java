package br.com.fiap3esr.autoescola3esr.application.port.out;

import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.dto.DadosEndereco;

public interface EnderecoService {
    DadosEndereco consultarPorCep(String cep);
}
