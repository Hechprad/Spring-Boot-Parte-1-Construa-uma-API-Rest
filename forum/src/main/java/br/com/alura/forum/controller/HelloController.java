package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;

@Controller	//anotação do spring mvc para encontrar esta classe
public class HelloController {

	
	public String hello() {
		return "Hello World!";
	}
}
