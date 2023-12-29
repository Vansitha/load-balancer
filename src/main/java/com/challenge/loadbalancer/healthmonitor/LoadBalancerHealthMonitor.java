package com.challenge.loadbalancer.healthmonitor;

import com.challenge.loadbalancer.ServerMetadata;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class LoadBalancerHealthMonitor implements IHealthMonitor {

    @Override
    public void checkHealth(List<ServerMetadata> serverList) {
        for (var server : serverList) {
            boolean status = pingServer(server.getServerURL());
            server.setIsHealthy(status);
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
