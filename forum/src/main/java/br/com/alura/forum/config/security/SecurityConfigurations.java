package br.com.alura.forum.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
 * 'EnableWebSecurity' habilita o Spring Security
 * 'Configuration' como esta classe possue diversas configurações
 * esta annotation faz o spring ler na hora de iniciar o projeto 
 * 
 * Herda de 'WebSecurityConfigurerAdapter' que possue alguns métodos
 * que precisamos sobrescrever.
 * 
 * Após os passos acima, por padrão o Spring Security bloqueia todo 
 * o acesso à nossa API até que façamos as configurações (401 Unauthorized)
 */

@EnableWebSecurity
@Configuration
public class SecurityConfigurations extends WebSecurityConfigurerAdapter {

	/*
	 * Configurações da parte de autenticação
	 * controle de acesso, login, etc
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	}
	
	/*
	 * Configurações da parte de Autorização
	 * quem pode acessar cada URL, perfil de acesso, etc
	 */
	@Override
		protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll()	//liberando método GET do endereço /topicos
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()	//liberanto todos métodos GET da URL após /topicos ex:/topicos/{id} 
			.anyRequest().authenticated()	//Qualquer outra requisição precisa estar autenticado (403) Forbidden
			.and().formLogin();	// Spring gera um formulário de autenticação
	}

	/*
	 * Configurações de Recursos Estáticos
	 * requisições para arquivos, javascript, css, imagens, etc
	 */
	@Override
		public void configure(WebSecurity web) throws Exception {

	}
}
