package com.example.snippet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SnippetApplication {

	public static void main(String[] args) {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String rawPassword = "pass"; // エンコードしたいプレーンテキストのパスワード
//        String encodedPassword = encoder.encode(rawPassword);
//        
//        System.out.println("Raw Password: " + rawPassword);
//        System.out.println("Encoded Password: " + encodedPassword);
		SpringApplication.run(SnippetApplication.class, args);
	}

}
