package br.com.alura.forum.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.model.Curso;

//JpaRepository<> generics da classe que queremos acessar e do tipo do Id
public interface CursoRepository extends JpaRepository<Curso, Long>{

	Curso findByNome(String nome);

}
