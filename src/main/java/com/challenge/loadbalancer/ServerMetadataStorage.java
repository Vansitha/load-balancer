package com.challenge.loadbalancer;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ServerMetadataStorage {

    private List<ServerMetadata> storage;

    public ServerMetadataStorage(List<String> serverUrls) {
        this.storage = new ArrayList<>();
        for (String url : serverUrls) {
            addServerToCluster(url);
        }
    }

    public void addServerToCluster(String url) {
        if (url == null) return;
        try {
            ServerMetadata serverMetadata = new ServerMetadata(url);
            this.storage.add(serverMetadata);
        } catch (MalformedURLException e) {
            System.out.println(e.getMessage()); // TODO: logger this
        }
    }

    public void setServerActivity(String url, boolean status) {
        for (var server : this.storage) {
            if (server.getUrlString().equals(url)) {
                server.setIsHealthy(status);
                break;
            }
        }
    }

    public List<ServerMetadata> getServerStorage() {
        return this.storage;
    }

    public List<ServerMetadata> getActiveServerList() {
        List<ServerMetadata> activeServerList = new ArrayList<>();

        for (var server : this.storage) {
            if (server.getIsHealthy()) {
                activeServerList.add(server);
            }
        }
        return activeServerList;
    }
}
