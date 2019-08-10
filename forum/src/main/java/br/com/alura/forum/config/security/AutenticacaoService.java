package br.com.alura.forum.config.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.alura.forum.model.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

/*
 * 'Service' service do Spring
 * 'UserDetailsService' interface para avisar ao Spring que esta classe é a service que tem a lógica 
 * de autenticação
 */

@Service
public class AutenticacaoService implements UserDetailsService {

	// Pedindo para o Spring injetar
	@Autowired
	private UsuarioRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> usuario = repository.findByEmail(username);
		if(usuario.isPresent()) {
			return usuario.get();	//usuario é um 'Optional', get() pega, de fato, o objeto usuario
		} 
		throw new UsernameNotFoundException("Dados inválidos!");
	}
}
