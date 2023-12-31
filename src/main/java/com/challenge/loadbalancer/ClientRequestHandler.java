package com.challenge.loadbalancer;

import com.challenge.loadbalancer.strategies.BalancingStrategyContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ClientRequestHandler implements HttpHandler {

    BalancingStrategyContext balancingStrategyContext;
    public ClientRequestHandler(BalancingStrategyContext balancingStrategyContext) {
        this.balancingStrategyContext = balancingStrategyContext;
    }

    @Override
    public void handle(HttpExchange exchange) {

        URL serverUrl = balancingStrategyContext.getServerToHandleRequest().getServerURL();
        String logDetails = prepareLogDetails(exchange); // TODO: use a logger

        // TODO: Replace with try-with
        HttpURLConnection targetConnection = null;
        try {
            targetConnection = (HttpURLConnection) serverUrl.openConnection();
            forwardRequestToServer(exchange, targetConnection);
            forwardResponseToClient(exchange, targetConnection);

        } catch (IOException e) {
            System.out.println("handle: Could not establish connection"); // TODO: use a logger

        } finally {
            targetConnection.disconnect();
        }
        exchange.close();
    }

    private void forwardRequestToServer(HttpExchange exchange, HttpURLConnection targetConnection) {
        try {
            targetConnection.setRequestMethod(exchange.getRequestMethod());
            targetConnection.setDoOutput(true);

        } catch (ProtocolException e){
            System.out.println("forwardRequestToServer: Could not get request method"); // TODO: use a logger
            return;
        }

        try (OutputStream outputStream = targetConnection.getOutputStream();
             InputStream inputStream = exchange.getRequestBody();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream)) {

            byte[] dataBuffer = new byte[1024];
            while ((bufferedInputStream.read(dataBuffer)) != -1) {
                outputStream.write(dataBuffer);
            }

        } catch (IOException e) {
            System.out.println("forwardRequest: could not send body"); // TODO: use a logger
        }
    }

    private void forwardResponseToClient(HttpExchange exchange, HttpURLConnection targetConnection) {
        final int EOF = -1;

        try {
            int responseCode = targetConnection.getResponseCode();
            exchange.sendResponseHeaders(responseCode, 0);

        } catch (IOException e) {
            System.out.println("sendtoclient: could not send the request method"); // TODO: use a logger
            return;
        }

        try (InputStream inputStream = targetConnection.getInputStream();
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
             OutputStream outputStream = exchange.getResponseBody()) {

            int dataByte;
            while((dataByte = bufferedReader.read()) != EOF) {
                outputStream.write(dataByte);
            }

        } catch (IOException e) {
            System.out.println("sendtoclient: could not send body"); // TODO: use a logger
        }
    }

    private String prepareLogDetails(HttpExchange exchange) {
        Map<String, List<String>> headers = exchange.getRequestHeaders();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Received request from ").append(exchange.getRemoteAddress().getAddress()).append("\n");

        String requestMethod = exchange.getRequestMethod();
        String protocol = exchange.getProtocol();
        stringBuilder.append(requestMethod).append(" ").append(protocol).append("\n");

        String host = headers.getOrDefault("Host", List.of("Not provided")).get(0);
        stringBuilder.append("Host: ").append(host).append("\n");

        String agent = headers.getOrDefault("User-Agent", List.of("Not provided")).get(0);
        stringBuilder.append("User-Agent: ").append(agent).append("\n");

        String accept = headers.getOrDefault("Accept", List.of("Not provided")).get(0);
        stringBuilder.append("Accept: ").append(accept).append("\n");

        return stringBuilder.toString();
    }
}