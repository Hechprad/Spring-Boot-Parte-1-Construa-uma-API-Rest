package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

/*
 *'RestController' indica para o Spring que todos os métodos da classe são
 *'ResponseBody', portanto, não é necessário a anotação do 'ResponseBody'
 *nos métodos. 
 */

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	// injetando o repository
	@Autowired
	private TopicoRepository topicoRepository;

	@Autowired
	private CursoRepository cursoRepository;

	/* 
	 * é possível utilizar @RequestMapping(value="/topicos", method=RequestMethod.GET)
	 * mas como todos os métodos são da "/topicos", jogamos a annotation para para a classe
	 * e substituímos pela annotation com o método na frente: 
	 * GetMapping / PostMapping 
	 */
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso){	//'DTO' usado quando dados saem da API
		if(nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			/* 
			 * Para gerar a query do bd automaticamento o
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
	
	// RequestBody, avisa o Spring que o parâmetro deve ser obtido no corpo da requisição
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {	//'form' usado para dados que chegam na API
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public DetalhesDoTopicoDto detalhar(@PathVariable Long id) {
		Topico topico = topicoRepository.getOne(id);	// getOne() do Spring já busca o id
		return new DetalhesDoTopicoDto(topico);
	}
	
}
