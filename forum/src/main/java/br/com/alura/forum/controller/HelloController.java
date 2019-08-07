package br.com.alura.forum.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller	//anotação do spring mvc para encontrar esta classe
public class HelloController {

	@RequestMapping("/")	//Mapeando a url para o spring saber quando chamar o método
	@ResponseBody			//Sem esta anotação o spring vai procurar a página no nosso projeto
	public String hello() {
		return "Hello World!";
	}
}
