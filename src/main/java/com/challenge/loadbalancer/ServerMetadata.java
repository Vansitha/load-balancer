package com.challenge.loadbalancer;

import java.net.MalformedURLException;
import java.net.URL;

public class ServerMetadata {

    private String urlString;
    private URL serverURL;
    private boolean isHealthy;

    public ServerMetadata(String urlString) throws MalformedURLException {
        this.isHealthy = true; // NOTE: All servers assumed to be online initially.
        this.urlString = urlString;
        this.serverURL = new URL(this.urlString);
    }

    public boolean getIsHealthy() {
        return isHealthy;
    }

    public void setIsHealthy(boolean online) {
        isHealthy = online;
    }

    public String getUrlString() {
        return urlString;
    }

    public URL getServerURL() {
        return serverURL;
    }
}
