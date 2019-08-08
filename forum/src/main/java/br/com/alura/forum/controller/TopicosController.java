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
	public List<TopicoDto> lista(String nomeCurso){
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			/* Para gerar a query do bd automaticamento o
			 * Spring Data tem um padrão de nomenclatura:
			 * findBy + nome do atributo + (nome do atributo seguinte, etc) + parâmetro e crie o método
			 * no repository específico
			 * em caso de ambiguidade ex: atributo cursoNome na classe Topico
			 * é possível diferenciar e específicar utilizando '_' 
			 * ex: findByCurso_Nome()
			 */
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			// no caso findByCurso(atributo da classe Topico) + Nome(atributo da classe Curso)
			return TopicoDto.converter(topicos);
		}
	}
	
}
