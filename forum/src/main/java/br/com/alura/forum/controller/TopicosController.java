package br.com.alura.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.TopicoRepository;

/*
 *'RestController' indica para o Spring que todos os métodos da classe são
 *'ResponseBody', portanto, não é necessário a anotação do 'ResponseBody'
 *nos métodos. 
 */

@Controller
@RestController
public class TopicosController {
	
	// injetando o repository
	@Autowired
	private TopicoRepository topicoRepository;

	@RequestMapping("/topicos")
	public List<TopicoDto> lista(){
		List<Topico> topicos = topicoRepository.findAll();
		return TopicoDto.converter(topicos);
	}
	
}
