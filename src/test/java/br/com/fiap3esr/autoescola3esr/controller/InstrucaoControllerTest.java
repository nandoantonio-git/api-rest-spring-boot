package br.com.fiap3esr.autoescola3esr.controller;

import br.com.fiap3esr.autoescola3esr.application.service.AgendaDeInstrucoes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class InstrucaoControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AgendaDeInstrucoes agenda;

    @Test
    @DisplayName("Expectativa: Retornar código 400 para dados inválidos.")
    @WithMockUser
    void agendarInstrucaoCenario1() throws Exception {
        //act
        MockHttpServletResponse response = mockMvc
                .perform(post("/instrucoes"))
                .andReturn()
                .getResponse();

        //assert
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Expectativa: Retornar código 400 ao cancelar sem informar os dados.")
    @WithMockUser
    void cancelarInstrucaoCenario1() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(delete("/instrucoes").contentType(MediaType.APPLICATION_JSON).content("{}"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("Expectativa: Retornar código 204 ao cancelar com dados válidos.")
    @WithMockUser
    void cancelarInstrucaoCenario2() throws Exception {
        MockHttpServletResponse response = mockMvc
                .perform(delete("/instrucoes").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"id_instrucao": 1, "motivo": "ALUNO_DESISTIU"}
                                """))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
