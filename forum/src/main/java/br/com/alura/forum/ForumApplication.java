package br.com.alura.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

/*
 * rodar classe para subir o tomcat
 * 
 * 'EnableSpringDataWebSupport' Habilitando suporte para o Spring pegar 
 * da requisição os campos de ordenação e páginação e repassar isso para o 
 * Spring Data
 * 
 * 'EnableCaching' Habilita o uso de cache na nossa aplicação
 * 
 * 'EnaleSwagger2' Habilita o uso do Swagger
 */

@SpringBootApplication
@EnableSpringDataWebSupport
@EnableCaching
@EnableSwagger2
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
