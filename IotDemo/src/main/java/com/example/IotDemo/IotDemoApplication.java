package com.example.IotDemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IotDemoApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(IotDemoApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("In Main Method");
	}
	

}
