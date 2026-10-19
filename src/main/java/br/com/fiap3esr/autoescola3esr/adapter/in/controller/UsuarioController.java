package br.com.fiap3esr.autoescola3esr.adapter.in.controller;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.usuario.DadosAtualizacaoUsuario;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.usuario.DadosCadastroUsuario;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.usuario.DadosDetalhamentoUsuario;
import br.com.fiap3esr.autoescola3esr.application.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/usuarios")
@SecurityRequirement(name = "bearer-key")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoUsuario> cadastrar(
            @RequestBody @Valid DadosCadastroUsuario dados, UriComponentsBuilder uriBuilder) {
        DadosDetalhamentoUsuario dto = service.cadastrar(dados);
        URI uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(dto.id()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoUsuario>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "login") Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoUsuario> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(service.detalhar(id));
    }

    @PutMapping
    public ResponseEntity<DadosDetalhamentoUsuario> atualizar(@RequestBody @Valid DadosAtualizacaoUsuario dados) {
        return ResponseEntity.ok(service.atualizar(dados));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }
}
