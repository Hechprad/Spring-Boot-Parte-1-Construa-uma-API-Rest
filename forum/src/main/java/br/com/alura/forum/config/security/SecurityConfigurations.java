package br.com.alura.forum.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

	@Autowired
	private AutenticacaoService autenticacaoService;
	
	/*
	 * Método necessário para injetar o AuthenticationManager na classe 
	 * 'AutenticacaoController'
	 * 'Bean' com esta annotation o Spring consegue identificar o retorno,
	 * tornando possível injetar na classe 'AutenticacaoController'
	 */
	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	/*
	 * Configurações da parte de autenticação
	 * controle de acesso, login, etc
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(autenticacaoService)	//passamos a classe que possui a lógica de autenticação
			.passwordEncoder(new BCryptPasswordEncoder());	//Qual encoder, algoritimo de hash é utilizado para a senha
	}
	
	/*
	 * Configurações da parte de Autorização
	 * quem pode acessar cada URL, perfil de acesso, etc
	 */
	@Override
		protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(HttpMethod.GET, "/topicos").permitAll()	//liberando método GET do endereço /topicos
			.antMatchers(HttpMethod.GET, "/topicos/*").permitAll()	//liberando todos métodos GET da URL após /topicos ex:/topicos/{id} 
			.antMatchers(HttpMethod.POST, "/auth").permitAll()	//liberando método POST, URL de login
			.anyRequest().authenticated()	//Qualquer outra requisição precisa estar autenticado (403) Forbidden
			// .and().formLogin() // Spring gera um formulário de autenticação e cria uma sessão
			.and().csrf().disable()	//csrf - Crosssite Request Forgery desabilitado, autenticação via TOKEN deixa a API livre de ataques deste tipo
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);	//politica de criação de sessão
	}

	/*
	 * Configurações de Recursos Estáticos
	 * requisições para arquivos, javascript, css, imagens, etc
	 */
	@Override
		public void configure(WebSecurity web) throws Exception {

	}
}
