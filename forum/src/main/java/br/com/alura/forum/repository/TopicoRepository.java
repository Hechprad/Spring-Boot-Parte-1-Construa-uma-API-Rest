package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.alura.forum.model.Topico;

// JpaRepository<> generics da classe que queremos acessar e do tipo do Id
public interface TopicoRepository extends JpaRepository<Topico, Long>{

	// necessário apenas declarar a assinatura do método
	List<Topico> findByCursoNome(String nomeCurso);
	
	/*
	 * Se quiséssemos mudar a nomenclatura do método acima, criado pelo 
	 * Spring Data, poderiamos utilizar a annotation '@Query',
	 * criar a query necessária, e anotar o parâmetro da busca com
	 * '@Param' passando o nome utilizado na query
	 */
	@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")	//org.springframework.data.jpa.repository.Query
	List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
}
