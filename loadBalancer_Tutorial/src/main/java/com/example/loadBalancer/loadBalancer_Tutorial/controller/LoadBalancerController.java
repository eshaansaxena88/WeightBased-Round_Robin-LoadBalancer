package com.example.loadBalancer.loadBalancer_Tutorial.controller;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//LoadBalancerController.java
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.loadBalancer.loadBalancer_Tutorial.service.Server;
import com.example.loadBalancer.loadBalancer_Tutorial.service.WeightedRoundRobinLoadBalancer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Controller
@RequestMapping("/api")
public class LoadBalancerController {
 private WeightedRoundRobinLoadBalancer loadBalancer;
 
 public LoadBalancerController(@Value("classpath:loadbalancer.properties") Resource resource) throws IOException {
     loadBalancer = new WeightedRoundRobinLoadBalancer();
     loadBalancer.setServers(loadServersFromProperties(resource));
 }
 
 @GetMapping("/load-balancer")
 @ResponseBody
 public String handleRequest() {
     Server server = loadBalancer.getNextServer();
     
     // Simulate making a request to the target server
     System.out.println("Request dispacthing to "+server.getHost());
     String response = makeRequestToTarget(server.getHost());
     return "Request handled by: " + server.getHost() + "\nResponse: " + response;
  
 }
 
 @Async
 private String makeRequestToTarget(String targetUrl) {
	 try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
	        HttpGet httpGet = new HttpGet(targetUrl);
	        HttpResponse response = httpClient.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        return EntityUtils.toString(entity);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "Error occurred while sending request to the server.";
	    }
 }
 private List<Server> loadServersFromProperties(Resource resource) throws IOException {
     Properties properties = new Properties();
     properties.load(resource.getInputStream());
     
     List<Server> servers = new ArrayList<>();
     
     int serverIndex = 0;
     while (properties.containsKey("servers[" + serverIndex + "].host")) {
         String host = properties.getProperty("servers[" + serverIndex + "].host");
         int weight = Integer.parseInt(properties.getProperty("servers[" + serverIndex + "].weight"));
         servers.add(new Server(host, weight));
         
         serverIndex++;
     }
     
     return servers;
 }
}
