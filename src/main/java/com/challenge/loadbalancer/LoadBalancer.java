package com.challenge.loadbalancer;

import com.challenge.loadbalancer.healthmonitor.IHealthMonitor;
import com.challenge.loadbalancer.healthmonitor.LoadBalancerHealthMonitor;
import com.challenge.loadbalancer.strategies.BalancingStrategyContext;
import com.challenge.loadbalancer.strategies.RoundRobinStrategy;
import com.challenge.loadbalancer.strategies.Strategy;
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
        String filepath = "server-list.txt"; // TODO: Get from cmd args
        IMetadataProvider metadataProvider = new FileMetadataProvider(filepath);
        List<String> serverList = metadataProvider.getServerList();

        ServerMetadataStorage serverMetadataStorage = new ServerMetadataStorage(serverList);
        List<ServerMetadata> activeServerList = serverMetadataStorage.getActiveServerList();

        // Can still start is the servers are inactive, this just checks whether the list is empty
        // TODO: Change to all server list
        if (activeServerList.size() == 0) {
            System.out.println("No servers available to start.");
            return;
        }

        BalancingStrategyContext strategyContext = new BalancingStrategyContext();
        RoundRobinStrategy roundRobinStrategy = new RoundRobinStrategy(activeServerList);
        strategyContext.setBalancingStrategy(roundRobinStrategy);

        startLoadBalancer(strategyContext);
        // TODO: Do this in a separate thread
        IHealthMonitor healthMonitor = new LoadBalancerHealthMonitor();
        healthMonitor.checkHealth(serverMetadataStorage.getServerStorage());
    }

    private static void startLoadBalancer(BalancingStrategyContext strategyContext) {
        try {
            InetAddress localAddress = InetAddress.getByName(LB_HOST_ADDRESS);
            HttpServer server = HttpServer.create(new InetSocketAddress(localAddress, OPEN_PORT), 0);

            ExecutorService executorService = Executors.newFixedThreadPool(10);
            ClientRequestHandler clientRequestHandler = new ClientRequestHandler(strategyContext);

            server.createContext("/", exchange -> {
                executorService.submit(() -> clientRequestHandler.handle(exchange));
            });

            server.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}