package br.com.alura.forum.controller;

import java.net.URI;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.model.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

/*
 *'RestController' indica para o Spring que todos os métodos da classe são
 *'ResponseBody', portanto, não é necessário a anotação do 'ResponseBody'
 * nos métodos. 
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
	 * 
	 * 'RequestParam' avisa o Spring que este é um parâmetro de request, de URL
	 * e torna este parâmetro obrigatório, para modificar isto basta colocar
	 * '(required = false)'
	 * 
	 * 'Cacheable' anotação que avisa o Spring para guardar o retorno do método
	 * em cache o 'value' é o identificador único deste cache
	 * -- para verificar se o cache está funcionando, devemos habilitar 
	 * nas propriedades do hibernate para mostrar a busca no bd no console:
	 * "spring.jpa.properties.hibernate.show_sql=true" mostra no console
	 * "spring.jpa.properties.hibernate.format_sql=true" formata a saída
	 * 
	 * 'PageableDefault' esta anotação permite deixar uma paginação padrão caso o
	 * usuário não passe dados na url (esta anotação é ignorada caso o usuário passe parâmetros)
	 */
	@GetMapping
	@Cacheable(value = "ListaDeTopicos")
	public Page<TopicoDto> lista(@RequestParam(required = false) String nomeCurso, 
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {	//'DTO' usado quando dados saem da API
		/* 
		 * Para o Spring conseguir pegar os parâmetros de paginação é necessário
		 * habilitar um módulo que faz este suporte, isso é feito na classe main
		 * 'ForumApplication'
		 * exemplo de url com os parâmetros de paginação:
		 * um campo: "localhost:8080/topicos?page=0&size=3&sort=id,asc"
		 * mais de um campo: "localhost:8080/topicos?page=0&size=3&sort=id,asc&sort=dataCriacao,desc"
		 * pagina, tamanho, ordenação e 'asc' crescente, 'desc' decrescente 
		 */
		
		if(nomeCurso == null) {
			Page<Topico> topicos = topicoRepository.findAll(paginacao);
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
			Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
			// no caso findByCurso(atributo da classe Topico) + Nome(atributo da classe Curso)
			
			return TopicoDto.converter(topicos);
		}
	}
	
	/* 
	 * 'RequestBody', avisa o Spring que o parâmetro deve ser obtido no corpo da requisição
	 * 'Valid' avisa que o form precisa ser validade
	 * 'Trasational' avisa o Spring que precisa commitar este método quando for executado
	 * 'CacheEvict' avisa o Spring que o cache em memória precisa ser expulso, limpo.
	 * Cada vez que cadastrarmos um novo item, o cache da 'ListaDeTopicos' precisa ser 
	 * atualizado com os novos dados, allEntries limpa todos os registros
	 */
	@PostMapping
	@Transactional
	@CacheEvict(value = "ListaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {	//'form' usado para dados que chegam na API
		Topico topico = form.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);	// getOne() do Spring já busca o id, getOne substituído por findById() para tratar 404
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	/* 
	 * Para atualizações as annotations podem ser 'PutMapping' ou 'PatchMapping'
	 * Put: para sobrescrever o recurso inteiro, mudar todas as informações.
	 * Patch: pequena att, apenas alguns campos.
	 */
	@PutMapping("/{id}")
	@Transactional	//'Trasational' avisa o Spring que precisa commitar este método quando for executado
	@CacheEvict(value = "ListaDeTopicos", allEntries = true)
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, 
			@RequestBody @Valid AtualizacaoTopicoForm form) {
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = form.atualizar(id, topicoRepository);

			return ResponseEntity.ok(new TopicoDto(topico));
		}
		
		return ResponseEntity.notFound().build();
	}
	
	
	@DeleteMapping("/{id}")
	@Transactional	//'Trasational' avisa o Spring que precisa commitar este método quando for executado
	@CacheEvict(value = "ListaDeTopicos", allEntries = true)
	public ResponseEntity<?> remover(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			topicoRepository.deleteById(id);
			
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.notFound().build();
	}
}
