package com.challenge.loadbalancer.healthmonitor;

import com.challenge.loadbalancer.ServerMetadata;

import java.util.List;

public interface IHealthMonitor {
    public void checkHealth(List<ServerMetadata> serverList);

}
