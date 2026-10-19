package br.com.fiap3esr.autoescola3esr.external;

import br.com.fiap3esr.autoescola3esr.adapter.out.external.viacep.ViaCepClient;
import br.com.fiap3esr.autoescola3esr.exception.type.ExternalServiceException;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.dto.DadosEndereco;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class ViaCepClientTest {
    private MockRestServiceServer server;
    private ViaCepClient client;

    @BeforeEach
    void setUp() {
        RestClient.Builder builder = RestClient.builder().baseUrl("http://viacep.test");
        server = MockRestServiceServer.bindTo(builder).build();
        client = new ViaCepClient(builder.build());
    }

    @Test
    void deveConsultarCepENormalizarResposta() {
        server.expect(requestTo("http://viacep.test/ws/01311000/json/"))
                .andRespond(withSuccess("""
                        {"logradouro":"Avenida Paulista","bairro":"Bela Vista","localidade":"São Paulo","uf":"SP","cep":"01311-000"}
                        """, APPLICATION_JSON));

        DadosEndereco endereco = client.consultarPorCep("01311-000");

        assertThat(endereco.logradouro()).isEqualTo("Avenida Paulista");
        assertThat(endereco.cidade()).isEqualTo("São Paulo");
        assertThat(endereco.cep()).isEqualTo("01311-000");
        server.verify();
    }

    @Test
    void deveRejeitarCepComFormatoInvalido() {
        assertThatThrownBy(() -> client.consultarPorCep("123"))
                .isInstanceOf(ExternalServiceException.class)
                .hasMessage("CEP deve conter 8 dígitos");
    }
}
