package com.challenge.loadbalancer.util;

import com.challenge.loadbalancer.ServerMetadata;
import java.util.List;

public interface IMetadataProvider {
    List<String> getServerList();
}
