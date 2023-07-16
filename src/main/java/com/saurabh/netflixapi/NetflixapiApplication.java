package com.saurabh.netflixapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetflixapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixapiApplication.class, args);
	}

}
