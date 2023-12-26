package com.challenge.loadbalancer.strategies;

import com.challenge.loadbalancer.ServerMetadata;
import java.util.List;

public class RoundRobinStrategy extends Strategy {

    private int currServerIdx = 0;
    public RoundRobinStrategy(List<ServerMetadata> serverList) {
        super(serverList);
    }

    @Override
    public ServerMetadata getServerToHandleRequest() {
        if (this.currServerIdx == this.serverList.size()) {
            this.currServerIdx = 0;
        }

        ServerMetadata server = this.serverList.get(this.currServerIdx);
        this.currServerIdx++;
        return server;
    }
}
