package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	//anotação do spring mvc para encontrar esta classe
public class HelloController {

	@RequestMapping("/")
	public String hello() {
		return "Hello World!";
	}
}
