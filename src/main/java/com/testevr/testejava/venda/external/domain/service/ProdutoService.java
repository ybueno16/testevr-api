package com.testevr.testejava.venda.external.domain.service;

import com.testevr.testejava.venda.external.application.dto.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonParseException;
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
                .registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type type,
                                                     JsonDeserializationContext context)
                            throws JsonParseException {
                        return LocalDateTime.parse(json.getAsString(),
                                DateTimeFormatter.ISO_DATE_TIME);
                    }
                })
                .create();
    }

    /**
     * Busca um produto pelo seu identificador único na API externa.
     *
     * @param id Identificador único do produto
     * @return ProdutoDto DTO contendo os dados do produto ou null se não encontrado
     * @throws IOException Se ocorrer um erro de comunicação com a API externa
     */
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

    /**
     * Processa uma resposta HTTP bem-sucedida da API externa.
     *
     * @param connection Conexão HTTP com a resposta de sucesso
     * @return ProdutoDto DTO desserializado a partir da resposta
     * @throws IOException Se ocorrer erro na leitura da resposta
     */
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

    /**
     * Processa uma resposta HTTP 404 (Not Found) da API externa.
     *
     * @param connection Conexão HTTP com a resposta de não encontrado
     * @return null indicando que o produto não foi encontrado
     * @throws IOException Se ocorrer erro na leitura da resposta
     */
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

    /**
     * Realiza a baixa de estoque de um produto específico na API externa.
     *
     * @param produtoId Identificador único do produto
     * @param quantidade Quantidade a ser baixada do estoque
     * @return BaixaEstoqueResponse Resposta da operação de baixa de estoque
     * @throws IOException Se ocorrer um erro de comunicação com a API externa
     */
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

    /**
     * Processa uma resposta HTTP bem-sucedida da operação de baixa de estoque.
     *
     * @param connection Conexão HTTP com a resposta de sucesso
     * @return BaixaEstoqueResponse Resposta desserializada da operação
     * @throws IOException Se ocorrer erro na leitura da resposta
     */
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

    /**
     * Processa uma resposta HTTP de erro da operação de baixa de estoque.
     *
     * @param connection Conexão HTTP com a resposta de erro
     * @return BaixaEstoqueResponse Resposta de erro desserializada
     * @throws IOException Se ocorrer erro na leitura da resposta
     */
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

    /**
     * Realiza baixa de estoque em lote para múltiplos produtos na API externa.
     *
     * @param requests Lista de requisições de baixa de estoque
     * @return Object Pode ser BaixaEstoqueResponse (sucesso total) ou
     *         Map<String, Object> (resposta parcial ou erro)
     * @throws IOException Se ocorrer um erro de comunicação com a API externa
     */
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