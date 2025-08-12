package com.fileversioncontrol.fileversioncontrolmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;

/*
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "com.fileversioncontrol.fileversioncontrolmanager")

 */
@SpringBootApplication
public class FileVersionControlManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileVersionControlManagerApplication.class, args);
	}

}
