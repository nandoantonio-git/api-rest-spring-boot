package br.com.fiap3esr.autoescola3esr.controller;

import br.com.fiap3esr.autoescola3esr.adapter.in.controller.response.usuario.DadosDetalhamentoUsuario;
import br.com.fiap3esr.autoescola3esr.application.service.UsuarioService;
import br.com.fiap3esr.autoescola3esr.shared.vo.enumeration.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UsuarioControllerTest {
    private static final String JSON_VALIDO = """
            {"login": "secretaria", "senha": "senha123", "perfil": "USER"}
            """;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UsuarioService service;

    @Test
    @DisplayName("Expectativa: Retornar código 201 ao cadastrar usuário válido.")
    @WithMockUser(roles = "ADMIN")
    void cadastrarCenario1() throws Exception {
        when(service.cadastrar(any())).thenReturn(new DadosDetalhamentoUsuario(1L, "secretaria", Role.USER, true));

        MockHttpServletResponse response = mockMvc
                .perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.getHeader("Location")).endsWith("/usuarios/1");
        assertThat(response.getContentAsString()).doesNotContain("senha");
    }

    @Test
    @DisplayName("Expectativa: Retornar código 400 para dados inválidos.")
    @WithMockUser(roles = "ADMIN")
    void cadastrarCenario2() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Expectativa: Retornar código 403 para usuário sem perfil ADMIN.")
    @WithMockUser(roles = "USER")
    void cadastrarCenario3() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(post("/usuarios").contentType(MediaType.APPLICATION_JSON).content(JSON_VALIDO))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
    }
}
