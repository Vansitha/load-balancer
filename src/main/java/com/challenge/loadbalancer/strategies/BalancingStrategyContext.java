package com.challenge.loadbalancer.strategies;

import com.challenge.loadbalancer.ServerMetadata;

public class BalancingStrategyContext {

    Strategy balancingStrategy;

    public ServerMetadata getServerToHandleRequest() {
        return balancingStrategy.getServerToHandleRequest();
    }

    public void setBalancingStrategy(Strategy balancingStrategy) {
        this.balancingStrategy = balancingStrategy;
    }

}
