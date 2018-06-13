package com.virlabs.catalogservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication 
//@SpringBootApplication(scanBasePackages = {"com.virlabs.catalogservice"} , exclude = JpaRepositoriesAutoConfiguration.class)
public class CatalogServiceApplication { 
 
 
     @Bean 
     public RestTemplate restTemplate() { 
         return new RestTemplate(); 
     } 
 
     public static void main(String[] args) { 
         SpringApplication.run(CatalogServiceApplication.class, args); 
     } 
}   
