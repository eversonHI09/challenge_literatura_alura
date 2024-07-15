package com.aluracursos.Literatura_Con_Alura.model;
import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String nombre;
    private String fechaDeNacimiento;
    private String fechaDeFallecimiento;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libro;

    // Constructor por defecto necesario para JPA
    public Autor() {
    }

    // Constructor para crear un Autor a partir de un objeto DatosAutor
    public Autor(DatosAutor datosAutor) {
        this.nombre = datosAutor.nombre();
        this.fechaDeNacimiento = datosAutor.fechaDeNacimiento();
        this.fechaDeFallecimiento = datosAutor.fechaDeFallecimiento();
    }

    // Getters y setters para los campos de la clase
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaDeNacimiento() {
        return fechaDeNacimiento;
    }

    public void setFechaDeNacimiento(String fechaDeNacimiento) {
        this.fechaDeNacimiento = fechaDeNacimiento;
    }

    public String getFechaDeFallecimiento() {
        return fechaDeFallecimiento;
    }

    public void setFechaDeFallecimiento(String fechaDeFallecimiento) {
        this.fechaDeFallecimiento = fechaDeFallecimiento;
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

    // Método toString para proporcionar una representación legible de un objeto Autor
    @Override
    public String toString() {
        return "********** AUTOR ***********\n" +
                "📚 Nombre: " + nombre + "\n" +
                "🎂 Fecha De Nacimiento: " + fechaDeNacimiento + "\n" +
                "🕯️ Fecha De Fallecimiento: " + fechaDeFallecimiento + "\n" +
                "📖 Libros: " + libro.stream().map(Libro::getTitulo).collect(Collectors.toUnmodifiableList()) + "\n" +
                "****************************\n";
    }
}
