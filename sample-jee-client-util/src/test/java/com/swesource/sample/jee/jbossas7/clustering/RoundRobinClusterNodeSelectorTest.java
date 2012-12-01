package com.swesource.sample.jee.jbossas7.clustering;

import static junit.framework.Assert.*;
import org.junit.Test;

/**
 *
 */
public class RoundRobinClusterNodeSelectorTest {

    private static String[] connectedNodes = {"node1", "node2"};

    @Test
    public void testSelectNode_1AvailableNode() {
        String[] availableNodes = {"node2"};
        RoundRobinClusterNodeSelector selector = new RoundRobinClusterNodeSelector();
        String selectedNodeName = selector.selectNode("Cluster", connectedNodes, availableNodes);
        assertEquals(selectedNodeName, availableNodes[0]);
    }

    @Test
    public void testSelectNode_2AvailableNodes() {
        String[] availableNodes = {"node1", "node2"};
        RoundRobinClusterNodeSelector selector = new RoundRobinClusterNodeSelector();
        String selectedNodeName = selector.selectNode("Cluster", connectedNodes, availableNodes);
        assertEquals(selectedNodeName, availableNodes[0]);
    }
}
