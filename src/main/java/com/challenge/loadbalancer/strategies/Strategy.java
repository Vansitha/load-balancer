package com.challenge.loadbalancer.strategies;

import com.challenge.loadbalancer.ServerMetadata;

import java.util.List;

public abstract class Strategy {

    List<ServerMetadata> serverList;

    public Strategy(List<ServerMetadata> serverList) {
        this.serverList = serverList;
    }

    public abstract ServerMetadata getServerToHandleRequest();
}
