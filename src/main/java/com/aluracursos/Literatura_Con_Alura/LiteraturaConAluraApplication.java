package com.aluracursos.Literatura_Con_Alura;

import com.aluracursos.Literatura_Con_Alura.principal.Principal;
import com.aluracursos.Literatura_Con_Alura.repository.AutorRepository;
import com.aluracursos.Literatura_Con_Alura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraturaConAluraApplication implements CommandLineRunner {
	@Autowired
	private LibroRepository repositoryLibro;

	@Autowired
	private AutorRepository repositoryAutor;

	public static void main(String[] args) {

		SpringApplication.run(LiteraturaConAluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repositoryLibro, repositoryAutor);
		principal.muestraElMenu();
	}
}
