package com.swesource.sample.jee.jbossas7.clustering;

import org.jboss.ejb.client.ClusterNodeSelector;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public class RoundRobinClusterNodeSelector implements ClusterNodeSelector {

    private AtomicInteger nodeIndex;

    public RoundRobinClusterNodeSelector() {
        System.out.println("***** RoundRobinClusterNodeSelector created");
        nodeIndex = new AtomicInteger(0);
    }

    @Override
    public String selectNode(String clusterName, String[] connectedNodes, String[] availableNodes) {
        System.out.println("***** RoundRobinClusterNodeSelector::selectNode " + clusterName + " ConnectedNodes: " + Arrays.asList(connectedNodes) + " AvailableNodes: " + Arrays.asList(availableNodes));
        if (availableNodes.length < 2) {
            return availableNodes[0];
        }
        return availableNodes[nodeIndex.getAndIncrement() % availableNodes.length];
    }
}
