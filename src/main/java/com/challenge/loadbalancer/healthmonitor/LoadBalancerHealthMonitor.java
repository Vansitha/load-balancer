package com.challenge.loadbalancer.healthmonitor;

import com.challenge.loadbalancer.ServerMetadataStorage;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class LoadBalancerHealthMonitor implements IHealthMonitor {

    private final ServerMetadataStorage storage;

    public LoadBalancerHealthMonitor(ServerMetadataStorage storage) {
        this.storage = storage;
    }

    @Override
    public void checkHealth() {

        for (var server : this.storage.getServerStorage()) {
            try {
                String path = "/health-check";
                URL endpoint = new URL(server.getServerURL(), path);
                boolean status = pingServer(endpoint);
                System.out.println(server.getUrlString() + ": " + status); // Temp
                this.storage.setServerActivity(server.getUrlString(), status);

            } catch (MalformedURLException e) {
                System.out.println(e.getMessage()); // TODO: Use logger
            }
        }
    }


    private boolean pingServer(URL url) {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return true;
            }

        } catch (IOException e) {
            System.out.println(e.getMessage()); // TODO: Replace with logging
        } finally {
            if (urlConnection != null) urlConnection.disconnect();
        }
        return false;
    }

}
