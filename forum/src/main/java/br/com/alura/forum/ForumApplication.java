package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/*
 * rodar classe para subir o tomcat
 * 
 * 'EnableSpringDataWebSupport' Habilitando suporte para o Spring pegar 
 * da requisição os campos de ordenação e páginação e repassar isso para o 
 * Spring Data
 * 
 * 'EnableCaching' Habilita o uso de cache na nossa aplicação
 */

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
