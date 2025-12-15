package com.testevr.testejava.venda.internal.infra.external;

import com.testevr.testejava.venda.internal.application.dto.VendaExternaRequest;
import com.testevr.testejava.venda.internal.application.dto.VendaExternaResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class VendaExternaServiceImpl {

    private final RestTemplate restTemplate;

    @Value("${produto.externa.api.url}")
    private String apiExternaUrl;

    public VendaExternaServiceImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public VendaExternaResponse processarVenda(VendaExternaRequest request) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<VendaExternaRequest> entity = new HttpEntity<>(request, headers);

            ResponseEntity<VendaExternaResponse> response = restTemplate.exchange(
                apiExternaUrl,
                HttpMethod.POST,
                entity,
                VendaExternaResponse.class
            );

            VendaExternaResponse responseBody = response.getBody();
            if (responseBody == null) {
                return new VendaExternaResponse(false, "Resposta vazia da API externa", null);
            }

            return responseBody;

        } catch (RestClientException e) {
            return new VendaExternaResponse(false, "Erro ao processar venda: " + e.getMessage(), null);
        }
    }
}
