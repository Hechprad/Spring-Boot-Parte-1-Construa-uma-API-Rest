package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/*
 * 'Service' service do Spring
 * Aqui utilizamos o JJWT
 */

@Service
public class TokenService {
	
	/*
	 * vamos injetar o tempo de duração do token, as informações de duração e
	 * o segredo do token estão na 'application.properties'
	 * Para injetar parâmetros do 'application.properties' utilizamos
	 * a annotation 'Value' que recebe a propriedade dentro de "${}"
	 */
	@Value("${forum.jwt.expiration}")
	private String expiration;

	// mesmo processo do 'expiration'
	@Value("${forum.jwt.secret}")
	private String secret;
	
	public String gerarToken(Authentication authentication) {
		/*
		 * Recuperando as informações do usuário que efetuou login,
		 * 'authentication' devolve um object, então precisamos fazer um cast
		 */
		Usuario logado = (Usuario) authentication.getPrincipal();
		Date hoje = new Date();
		
		Date dataExpiracao = new Date(hoje.getTime() + Long.parseLong(expiration));
		return Jwts.builder()
				.setIssuer("API do Fórum da Alura")	//Quem gerou este token
				.setSubject(logado.getId().toString())	//Quem é o dono, usuário deste token
				.setIssuedAt(hoje)	//data de geração do token
				.setExpiration(dataExpiracao)	//Data de validade do token
				.signWith(SignatureAlgorithm.HS256, secret)	//o token precisa ser criptografado, recebe o algoritmo de criptografia e a senha da aplicação 
				.compact();	// campacta e transforma em String
	}

	public boolean isTokenValido(String token) {
		/*
		 * 'parser()' faz o parse do token, descriptografa e diz se está ok
		 * 'setSigningKey(secret)' passamos a chave para descriptografar
		 */
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		Claims claims = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
		// 'getSubject()' no método 'gerarToken()' desta classe, setamos o subject contendo o ID do usuário
		return Long.parseLong(claims.getSubject());
	}

}
