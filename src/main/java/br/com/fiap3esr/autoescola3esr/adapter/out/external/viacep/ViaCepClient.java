package br.com.fiap3esr.autoescola3esr.adapter.out.external.viacep;

import br.com.fiap3esr.autoescola3esr.application.port.out.EnderecoService;
import br.com.fiap3esr.autoescola3esr.exception.type.ExternalServiceException;
import br.com.fiap3esr.autoescola3esr.shared.vo.endereco.dto.DadosEndereco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;

@Component
public class ViaCepClient implements EnderecoService {
    private final RestClient restClient;

    @Autowired
    public ViaCepClient(RestClient.Builder builder,
                        @Value("${app.external.viacep.base-url:https://viacep.com.br}") String baseUrl) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(Duration.ofSeconds(3));
        requestFactory.setReadTimeout(Duration.ofSeconds(3));
        this.restClient = builder
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .build();
    }

    public ViaCepClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public DadosEndereco consultarPorCep(String cep) {
        String cepNormalizado = cep == null ? "" : cep.replaceAll("\\D", "");
        if (!cepNormalizado.matches("\\d{8}")) {
            throw new ExternalServiceException("CEP deve conter 8 dígitos");
        }
        try {
            ViaCepResponse response = restClient.get()
                    .uri("/ws/{cep}/json/", cepNormalizado)
                    .retrieve()
                    .body(ViaCepResponse.class);
            if (response == null || Boolean.TRUE.equals(response.erro())) {
                throw new ExternalServiceException("CEP não encontrado");
            }
            return new DadosEndereco(response.logradouro(), null, null, response.bairro(),
                    response.cidade(), response.uf(), formatarCep(response.cep(), cepNormalizado));
        } catch (RestClientException ex) {
            throw new ExternalServiceException("Falha ao consultar o serviço ViaCEP", ex);
        }
    }

    private String formatarCep(String cep, String fallback) {
        String valor = cep == null || cep.isBlank() ? fallback : cep.replaceAll("\\D", "");
        return valor.substring(0, 5) + "-" + valor.substring(5);
    }
}
