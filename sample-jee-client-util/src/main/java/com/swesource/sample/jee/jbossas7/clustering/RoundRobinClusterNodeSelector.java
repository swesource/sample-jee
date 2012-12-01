package com.swesource.sample.jee.jbossas7.clustering;

import org.jboss.ejb.client.ClusterNodeSelector;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 *
 */
public class RoundRobinClusterNodeSelector implements ClusterNodeSelector {

    private static final Logger LOGGER = Logger.getLogger(RoundRobinClusterNodeSelector.class.getName());

    private AtomicInteger nodeIndex;

    public RoundRobinClusterNodeSelector() {
        LOGGER.info("RoundRobinClusterNodeSelector created");
        nodeIndex = new AtomicInteger(0);
    }

    @Override
    public String selectNode(String clusterName, String[] connectedNodes, String[] availableNodes) {
        LOGGER.info("RoundRobinClusterNodeSelector::selectNode " + clusterName + " ConnectedNodes: " + Arrays.asList(connectedNodes) + " AvailableNodes: " + Arrays.asList(availableNodes));
        if (availableNodes.length < 2) {
            return availableNodes[0];
        }
        return availableNodes[nodeIndex.getAndIncrement() % availableNodes.length];
    }
}
