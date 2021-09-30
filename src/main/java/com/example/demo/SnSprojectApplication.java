package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync //非同期処理実行のために記述
@SpringBootApplication
public class SnSprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SnSprojectApplication.class, args);
	}

}
