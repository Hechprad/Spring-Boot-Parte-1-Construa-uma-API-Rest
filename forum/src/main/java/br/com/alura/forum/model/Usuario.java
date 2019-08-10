package br.com.alura.forum.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * para avisar o Spring Security que esta é a classe de usuários precisamos 
 * implementar a interface 'UserDetails'
 */

@Entity	//entidade do bd
public class Usuario implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)	//chave primária e gerada automaticamente
	private Long id;
	private String nome;
	private String email;
	private String senha;
	
	@ManyToMany(fetch = FetchType.EAGER) //Por padrão toMany é lazy, então deixamos EAGER para carregar a lista de perfis quando carregar os usuários
	private List<Perfil> perfis = new ArrayList<>();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	/*
	 * Métodos da interface UserDetails 
	 */
	
	// o Spring precisa de uma classe que represente o perfil do usuário, 
	// relacionada com as permissões de acesso do usuário
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.perfis;
	}

	// colocar o atributo que representa a senha
	@Override
	public String getPassword() {
		return this.senha;
	}

	// colocar o atributo que representa que representa o usuário
	@Override
	public String getUsername() {
		return this.email;
	}
	
	// conta não está expirada, true
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// conta não está bloqueada, true
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// credencial não está expirada, true
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// conta está habilitada, true
	@Override
	public boolean isEnabled() {
		return true;
	}

}
