package br.com.fiap3esr.autoescola3esr.adapter.in.controller;

import br.com.fiap3esr.autoescola3esr.application.port.out.EnderecoService;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.dto.DadosEndereco;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class EnderecoController {
    private final EnderecoService enderecoService;

    @GetMapping("/{cep}")
    @Operation(summary = "Consulta endereço por CEP no ViaCEP")
    public ResponseEntity<DadosEndereco> consultar(@PathVariable String cep) {
        return ResponseEntity.ok(enderecoService.consultarPorCep(cep));
    }
}
