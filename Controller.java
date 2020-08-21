package com.example.aravind_demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class Controller {
	
	@RequestMapping("/")
	public String helloWorld() {
		return "Hello World!";
	}

}
