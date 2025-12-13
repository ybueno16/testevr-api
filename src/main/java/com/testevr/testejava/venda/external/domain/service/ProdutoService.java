package com.testevr.testejava.venda.external.domain.service;

import com.testevr.testejava.venda.external.application.dto.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ProdutoService {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoService.class);

    @Value("${produto.externa.api.url}")
    private String apiExternaUrl;

    private final Gson gson;

    public ProdutoService() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new com.google.gson.JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(com.google.gson.JsonElement json, Type type,
                                                     com.google.gson.JsonDeserializationContext context)
                            throws com.google.gson.JsonParseException {
                        return LocalDateTime.parse(json.getAsString(),
                                DateTimeFormatter.ISO_DATE_TIME);
                    }
                })
                .create();
    }

    public boolean verificarEstoqueDisponivel(Long produtoId, Integer quantidade) throws IOException {
        ProdutoDto produto = buscarProdutoPorId(produtoId);
        
        if (produto == null) {
            return false;
        }
        
        if (produto.getEstoque() == null) {
            return false;
        }
        
        return produto.getEstoque() >= quantidade;
    }

    public ProdutoDto buscarProdutoPorId(Long id) throws IOException {
        String urlString = apiExternaUrl + "/produtos/" + id;

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return parseSuccessResponse(connection);
            }
            
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return handleNotFoundResponse(connection);
            }
            
            throw new IOException("Erro na requisição: " + responseCode);

        } catch (Exception e) {
            logger.error("Erro ao conectar com a API externa de produtos: {}", e.getMessage());
            throw new IOException("API de produtos indisponível: " + e.getMessage(), e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private ProdutoDto parseSuccessResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return gson.fromJson(response.toString(), ProdutoDto.class);
        }
    }

    private ProdutoDto handleNotFoundResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {

            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorResponse.append(line);
            }

            logger.warn("Produto não encontrado: {}", errorResponse.toString());
            return null;
        }
    }

    public BaixaEstoqueResponse realizarBaixaEstoque(Long produtoId, Integer quantidade) throws IOException {
        String urlString = apiExternaUrl + "/produtos/" + produtoId + "/baixa";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            BaixaEstoqueRequest request = new BaixaEstoqueRequest();
            request.setQuantidade(quantidade);
            String requestBody = gson.toJson(request);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                return parseSuccessBaixaResponse(connection);
            }
            
            if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST || responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return parseErrorBaixaResponse(connection);
            }
            
            throw new IOException("Erro na requisição: " + responseCode);

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private BaixaEstoqueResponse parseSuccessBaixaResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return gson.fromJson(response.toString(), BaixaEstoqueResponse.class);
        }
    }

    private BaixaEstoqueResponse parseErrorBaixaResponse(HttpURLConnection connection) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getErrorStream(), StandardCharsets.UTF_8))) {

            StringBuilder errorResponse = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorResponse.append(line);
            }

            try {
                return gson.fromJson(errorResponse.toString(), BaixaEstoqueResponse.class);
            } catch (Exception e) {
                ErrorResponse error = gson.fromJson(errorResponse.toString(), ErrorResponse.class);
                BaixaEstoqueResponse baixaError = new BaixaEstoqueResponse();
                baixaError.setMessage(error.getMessage());
                return baixaError;
            }
        }
    }

    public Object realizarBaixaEstoqueEmLote(List<BaixaEstoqueRequest> requests) throws IOException {
        String urlString = apiExternaUrl + "/produtos/baixa";

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            String requestBody = gson.toJson(requests);

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            responseCode < HttpURLConnection.HTTP_BAD_REQUEST ?
                                    connection.getInputStream() : connection.getErrorStream(),
                            StandardCharsets.UTF_8))) {

                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return gson.fromJson(response.toString(), BaixaEstoqueResponse.class);
                }
                
                if (responseCode == 207 || responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    Type mapType = new TypeToken<Map<String, Object>>(){}.getType();
                    return gson.fromJson(response.toString(), mapType);
                }
                
                throw new IOException("Erro na requisição: " + responseCode + " - " + response.toString());
            }

        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}