package br.com.alura.forum.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.filter.OncePerRequestFilter;

/*
 * 'OncePerRequestFilter' filtro do Spring chamado uma vez a cada requisição
 * Para registrar esta classe no string, precisamos ir na classe 
 * 'SecurityConfiguration', no método configure de urls e
 * adicionar o filtro no método 'addFilterBefore()' para rodar esta classe
 * antes dos outros filtros
 */

public class AutenticacaoViaTokenFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = recuperarToken(request);
		System.out.println(token);
		
		filterChain.doFilter(request, response);
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
