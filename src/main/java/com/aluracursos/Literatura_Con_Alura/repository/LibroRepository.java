package com.aluracursos.Literatura_Con_Alura.repository;

import com.aluracursos.Literatura_Con_Alura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTituloIgnoreCase(String titulo);

    List<Libro> findByIdiomasContains(String idioma);
}
