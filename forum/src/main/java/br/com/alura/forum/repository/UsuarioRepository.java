package br.com.alura.forum.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.model.Usuario;

//JpaRepository<> generics da classe que queremos acessar e do tipo do Id
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	// necessário apenas declarar a assinatura do método
	Optional<Usuario> findByEmail(String email);
}
