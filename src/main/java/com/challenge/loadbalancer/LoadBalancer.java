package com.challenge.loadbalancer;

import com.challenge.loadbalancer.util.FileMetadataProvider;
import com.challenge.loadbalancer.util.IMetadataProvider;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoadBalancer {

    private final static String LB_HOST_ADDRESS = "127.0.0.1";
    private final static int OPEN_PORT = 80;

    public static void main(String[] args) {
        String filepath = "server-list.txt"; // TODO: get from a cmd arg ??
        IMetadataProvider metadataProvider = new FileMetadataProvider(filepath);
        List<String> serverList = metadataProvider.getServerList();

        ServerMetadataStorage serverMetadataStorage = new ServerMetadataStorage(serverList);
        List<ServerMetadata> storage = serverMetadataStorage.getServerStorage();

        // If there are no backend servers online don't start up LB
        if (storage.size() == 0) return;

        // TODO: Set the balancing strategy


        try {
            InetAddress localAddress = InetAddress.getByName(LB_HOST_ADDRESS);
            HttpServer server = HttpServer.create(new InetSocketAddress(localAddress, OPEN_PORT), 0);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            String targetServer = "http://localhost:8080"; // TODO: Replace with URL object
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(targetServer);

            server.createContext("/", exchange -> {
                executorService.submit(() -> clientRequestHandler.handle(exchange));
            });

            server.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}