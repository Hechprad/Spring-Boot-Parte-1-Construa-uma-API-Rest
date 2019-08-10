package br.com.alura.forum.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.config.security.TokenService;
import br.com.alura.forum.controller.dto.TokenDto;
import br.com.alura.forum.controller.form.LoginForm;

/*
 *'RestController' indica para o Spring que todos os métodos da classe são
 */

@RestController
@RequestMapping(value="/auth")	// endereço para autenticação
public class AutenticacaoController {

	/*
	 * Para fazer uma autenticação de maneira manual precisaremos da classe
	 * 'AuthenticationManager'
	 * Porém o Spring não injeta automaticamente esta classe, precisamos habilitar
	 * isso na nossa classe 'SecurityConfiguration' sobrescrevendo o método
	 * 'authenticationManager()' e colocando a annotation 'Bean' para o Spring
	 * poder identificar o método
	 */
	@Autowired
	private AuthenticationManager authManager;
	
	//Classe para a criação do token (Stateless)
	@Autowired
	private TokenService tokenService;
	
	/* 
	 * 'RequestBody', avisa o Spring que o parâmetro deve ser obtido no corpo da requisição
	 * 'Valid' avisa que o form precisa ser validado
	 * precisamos criar um objeto 'UsernamePasswordAuthenticationToken' para
	 * passarmos os dados de login do form.
	 * try-catch para capturarmos a exception 'AuthenticationException'
	 * caso os dados sejam inexistentes, devolvendo um badRequest
	 */
	@PostMapping
	public ResponseEntity<TokenDto> autenticar(@RequestBody @Valid LoginForm form ) {	//'form' usado para dados que chegam na API
		UsernamePasswordAuthenticationToken dadosLogin = form.converter();
		/*
		 * Utilizaremos JJWT para gerar o token caso as informações de login 
		 * estejam corretas.
		 * Criamos uma classe para gerar os códigos do token e nela, um
		 * método que recebe os dados do Authentication
		 */
		try {
			Authentication authentication = authManager.authenticate(dadosLogin);
			String token = tokenService.gerarToken(authentication);
			return ResponseEntity.ok(new TokenDto(token, "Bearer"));	//'Bearer' é o tipo de autenticação
		} catch (AuthenticationException e) {
			return ResponseEntity.badRequest().build();
		}
	}
}
