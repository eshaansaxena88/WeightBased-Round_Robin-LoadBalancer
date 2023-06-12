package com.example.loadBalancer.loadBalancer_Tutorial;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoadBalancerTutorialApplication {

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		SpringApplication.run(LoadBalancerTutorialApplication.class, args);
	}

}
