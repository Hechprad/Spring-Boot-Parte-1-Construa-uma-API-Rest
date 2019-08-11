package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.alura.forum.model.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/*
 * 'OncePerRequestFilter' filtro do Spring chamado uma vez a cada requisição
 * Para registrar esta classe no string, precisamos ir na classe 
 * 'SecurityConfiguration', no método configure de urls e
 * adicionar o filtro no método 'addFilterBefore()' para rodar esta classe
 * antes dos outros filtros
 */

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	/*
	 * Em classes do tipo filtro, não conseguimos fazer injeção de dependências
	 * com a annotation 'Autowired', então precisamos receber estes parâmetros 
	 * via construtor
	 */
	private TokenService tokenService;
	
	private UsuarioRepository repository;
	
	/*
	 *  injetando o 'tokenService' manualmente, via construtor e injetando
	 *  o 'tokenService' com 'Autowired'na classe 'SecurityConfiguration' 
	 */
	public AutenticacaoViaTokenFilter(TokenService tokenService, UsuarioRepository repository) {
		this.tokenService = tokenService;
		this.repository = repository;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		boolean valido = tokenService.isTokenValido(token);	//Validando o token na classe 'TokenService'
		
		if(valido) {
			autenticarCliente(token);
		}
		// se não autenticar o Spring segue o fluxo da requisição e barra o usuário
		filterChain.doFilter(request, response);
	}

	private void autenticarCliente(String token) {
		/*
		 * Para avisar que o usuário está autenticado, utilizamos o método
		 * 'SecurityContextHolder' e passamos o parâmetro authentication
		 * que contém as informações do usuário
		 */
		Long idUsuario = tokenService.getIdUsuario(token);
		Usuario usuario = repository.findById(idUsuario).get();
		
		UsernamePasswordAuthenticationToken authentication = 
				new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	private String recuperarToken(HttpServletRequest request) {
		/*
		 * request tem acesso ao header com o método 'getHeader()', passamos 
		 * como parâmetro o nome do header que queremos
		 * if com verificação de validade do token
		 */
		String token = request.getHeader("Authorization");
		if(token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		return token.substring(7, token.length());
	}

}
