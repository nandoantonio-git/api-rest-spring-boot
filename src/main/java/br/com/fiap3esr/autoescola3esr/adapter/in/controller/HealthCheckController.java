package br.com.fiap3esr.autoescola3esr.adapter.in.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheckController {
    @GetMapping
    public String healthCheck() {
        return "Verificação de integridado da API da Auto Escola 3ESR!";
    }
}