/*package com.example.loadBalancer.loadBalancer_Tutorial.service;

// WeightedRoundRobinLoadBalancer.java
import java.util.ArrayList;
import java.util.List;

public class WeightedRoundRobinLoadBalancer {
	private List<Server> servers;
    private int currentIndex;
    private int currentWeight;
    
    public WeightedRoundRobinLoadBalancer() {
        servers = new ArrayList<>();
        currentIndex = 0;
    }
    
    public void setServers(List<Server> server) {
        servers.addAll(server);
    }
    
    public Server getNextServer() {
        int totalWeight = servers.stream().mapToInt(Server::getWeight).sum();
        int currentWeight = 0;
        while (true) {
            Server server = servers.get(currentIndex);
            currentWeight += server.getWeight();
            if (currentWeight > totalWeight) {
            	 currentWeight = 0;
                currentIndex = (currentIndex + 1) % servers.size();
                return server;
            }
            currentIndex = (currentIndex + 1) % servers.size();
    }
}
}

*/

package com.example.loadBalancer.loadBalancer_Tutorial.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class WeightedRoundRobinLoadBalancer {
    private List<Server> servers;
    private AtomicInteger currentIndex;

    public WeightedRoundRobinLoadBalancer() {
        servers = new ArrayList<>();
        currentIndex = new AtomicInteger(0);
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

    public Server getNextServer() {
        int totalWeight = servers.stream().mapToInt(Server::getWeight).sum();
        int serverCount = servers.size();

        while (true) {
            int index = currentIndex.getAndIncrement() % serverCount;
            Server server = servers.get(index);

            if (server.getWeight() >= totalWeight) {
                return server;
            }

            if (currentIndex.get() >= serverCount) {
                currentIndex.set(0);
            }

            totalWeight -= server.getWeight();
        }
    }
}

