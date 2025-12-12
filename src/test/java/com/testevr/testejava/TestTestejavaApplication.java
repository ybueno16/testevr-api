package com.testevr.testejava;

import org.springframework.boot.SpringApplication;

public class TestTestejavaApplication {

	public static void main(String[] args) {
		SpringApplication.from(TestejavaApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
