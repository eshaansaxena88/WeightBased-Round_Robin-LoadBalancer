package com.example.loadBalancer.loadBalancer_Tutorial.service;

// Server.java
public class Server {
    private String host;
    private int weight;
    
    public Server(String host, int weight) {
        this.host = host;
        this.weight = weight;
    }
    
    public String getHost() {
        return host;
    }
    
    public int getWeight() {
        return weight;
    }

	public void setWeight(int i) {
		
		  this.weight = i;
	}
}
