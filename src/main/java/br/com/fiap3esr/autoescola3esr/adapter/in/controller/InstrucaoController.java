package br.com.fiap3esr.autoescola3esr.adapter.in.controller;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrucao.DadosAgendamento;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.request.instrucao.DadosCancelamento;
import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.instrucao.DadosDetalhamentoAgendamento;
import br.com.fiap3esr.autoescola3esr.application.service.AgendaDeInstrucoes;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/instrucoes")
@SecurityRequirement(name = "bearer-key")
@RequiredArgsConstructor
public class InstrucaoController {
    private final AgendaDeInstrucoes agenda;

    @PostMapping
    public ResponseEntity<DadosDetalhamentoAgendamento> agendarInstrucao(
            @RequestBody @Valid DadosAgendamento dados) {
        return ResponseEntity.ok(agenda.agendar(dados));
    }

    @GetMapping
    public ResponseEntity<Page<DadosDetalhamentoAgendamento>> listar(
            @ParameterObject @PageableDefault(size = 10, sort = "dataHora") Pageable pageable) {
        return ResponseEntity.ok(agenda.listar(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoAgendamento> detalhar(@PathVariable Long id) {
        return ResponseEntity.ok(agenda.detalhar(id));
    }

    @DeleteMapping
    public ResponseEntity<Void> cancelar(@RequestBody @Valid DadosCancelamento dados) {
        agenda.cancelar(dados);
        return ResponseEntity.noContent().build();
    }
}
