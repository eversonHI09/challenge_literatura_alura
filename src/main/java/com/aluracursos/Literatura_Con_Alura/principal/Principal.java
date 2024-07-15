package com.aluracursos.Literatura_Con_Alura.principal;

import com.aluracursos.Literatura_Con_Alura.model.*;
import com.aluracursos.Literatura_Con_Alura.repository.AutorRepository;
import com.aluracursos.Literatura_Con_Alura.repository.LibroRepository;
import com.aluracursos.Literatura_Con_Alura.service.ConsumoAPI;
import com.aluracursos.Literatura_Con_Alura.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class Principal {
    private final Scanner teclado = new Scanner(System.in);
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/?search=";
    private LibroRepository repositoryLibro;
    private AutorRepository repositoryAutor;

    public Principal(LibroRepository libro,AutorRepository autor){
        this.repositoryLibro = libro;
        this.repositoryAutor = autor;
    }
    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            var menu =
                    """
                            ********* Menu **************
                    
                            1 📚 - Buscar libro por título
                            2 📚 - Lista de libros registrados
                            3 👨‍💼 - Autores vivos en determinado año
                            4 🌐 - Buscar libros por idioma
                            5 🔝 - Top 10 libros más descargados
                            0 ❌ - Salir
                 
                            *****************************
                    """;
            System.out.println(menu);
            while (!teclado.hasNextInt()) {
                System.out.println("Eliga una opcion para continuar");
                teclado.nextLine();
            }
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1 :
                    buscarLibro();
                    break;
                case 2 :
                    librosRegistrados();
                    break;
                case 3:
                    autoresVivosPorAnio();
                    break;
                case 4:
                    buscarLibroPorIdioma();
                    break;
                case 5:
                    top10LibrosMasDescargados();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private Datos buscarDatosLibros() {
        System.out.println("Ingrese el nombre del libro que desea buscar: ");
        String libro = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + libro.replace(" ", "+"));
        return conversor.obtenerDatos(json, Datos.class);
    }

    private void buscarLibro() {
        try {
            Datos datos = buscarDatosLibros();

            if (datos.resultados().isEmpty()) {
                System.out.println("No tenemos ese libro, regresando al menú principal...");
                return; // Salir del método y regresar al menú principal
            }

            DatosLibros datosLibros = datos.resultados().get(0);
            DatosAutor datosAutor = datosLibros.autor().get(0);

            System.out.println("Título: " + datosLibros.titulo());
            System.out.println("Autor: " + datosAutor.nombre());

            Autor autor = new Autor(datosAutor); // Se cambia la variable a 'autor'
            repositoryAutor.save(autor); // Se utiliza 'autor'
            repositoryLibro.save(new Libro(datosLibros, autor)); // Se utiliza 'autor'
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error al obtener datos del libro. Intente nuevamente.");
        } catch (Exception e) { // Captura excepciones generales
            System.out.println("Ocurrió un error inesperado. Contacte al administrador.");
            e.printStackTrace(); // Imprime la traza de la excepción para debugging
        }
    }



    @Transactional
    private void librosRegistrados() {
        try {
            List<Libro> libros = repositoryLibro.findAll();
            if (libros.isEmpty()) {
                System.out.println("No hay ningún Libro registrado.");
            } else {
                libros.forEach(libro -> {
                    System.out.println("Título: " + libro.getTitulo());
                    System.out.println("Autor: " + libro.getAutor().getNombre());
                    System.out.println("Número de descargas: " + libro.getNumeroDeDescargas());
                    System.out.println("Idiomas: " + String.join(", ", libro.getIdiomas()));
                });
            }
        } catch (Exception e) {
            System.out.println("error al listar los libros.");
            e.printStackTrace();
        }
    }


    private void autoresVivosPorAnio() {
        while (true) {
            System.out.println("\nMenú Autores Vivos por Año:");
            System.out.println("1. 👨‍💼 Buscar autores vivos por año");
            System.out.println("0. ❌ Regresar al menú principal");

            System.out.print("Ingrese su opción: ");
            int opcion = teclado.nextInt();
            teclado.nextLine(); // Consumir salto de línea

            switch (opcion) {
                case 1:
                    System.out.println("Ingrese el año para buscar autores vivos: ");
                    while (!teclado.hasNextInt()) {
                        System.out.println("Formato inválido, ingrese un año válido.");
                        teclado.nextLine();
                    }
                    int anio = teclado.nextInt();
                    teclado.nextLine();
                    String anioString = String.valueOf(anio);

                    List<Autor> autoresVivos = repositoryAutor.autorVivoEnDeterminadoAnio(anioString);
                    if (autoresVivos.isEmpty()) {
                        System.out.println("No se encontraron autores vivos en el año especificado.");
                    } else {
                        autoresVivos.forEach(System.out::println);
                    }
                    break;
                case 0:
                    System.out.println("Regresando al menú principal...");
                    return; // Salir del bucle y regresar al menú principal
                default:
                    System.out.println("Opción inválida, intente nuevamente.");
            }
        }
    }




    private void buscarLibroPorIdioma() {
        System.out.println("""
                1) Español
                2) Inglés
                3) Frances
                4) Portugues
                5) Japones
                6) Regresar al menú principal
                
                Eliga un idioma:
                """);
        int opcion = teclado.nextInt();
        teclado.nextLine();
        List<Libro> libros;
        switch (opcion) {
            case 1:
                libros = repositoryLibro.findByIdiomasContains("es");
                if (!libros.isEmpty()) {
                    libros.forEach(System.out::println);
                } else {
                    System.out.println("los sentimos, no tenemos libros en el idioma Español.");
                }
                break;
            case 2:
                libros = repositoryLibro.findByIdiomasContains("en");
                if (!libros.isEmpty()) {
                    libros.forEach(System.out::println);
                } else {
                    System.out.println("los sentimos, no tenemos libros en el idioma Inglés.");
                }
                break;
            case 3:
                libros = repositoryLibro.findByIdiomasContains("fr");
                if (!libros.isEmpty()) {
                    libros.forEach(System.out::println);
                } else {
                    System.out.println("los sentimos, no tenemos libros en el idioma Frances.");
                }
                break;
            case 4:
                libros = repositoryLibro.findByIdiomasContains("pr");
                if (!libros.isEmpty()) {
                    libros.forEach(System.out::println);
                } else {
                    System.out.println("los sentimos, no tenemos libros en el idioma Portugues.");
                }
                break;
            case 5:
                libros = repositoryLibro.findByIdiomasContains("jp");
                if (!libros.isEmpty()) {
                    libros.forEach(System.out::println);
                } else {
                    System.out.println("los sentimos, no tenemos libros en el idioma Japones.");
                }
                break;
            case 6:
                muestraElMenu();
                break;
            default:
                System.out.println("La opción seleccionada no es válida.");
        }
    }

    private void top10LibrosMasDescargados() {
        String json = consumoApi.obtenerDatos(URL_BASE + "&sort=download_count&order=desc&limit=10");
        Datos datos = conversor.obtenerDatos(json, Datos.class);
        if (!datos.resultados().isEmpty()) {
            System.out.println("*************** Top 10 Libros Más Descargados **************");
            System.out.println("-------------------------------------------------------------");
            for (int i = 0; i < Math.min(10, datos.resultados().size()); i++) {
                DatosLibros datosLibros = datos.resultados().get(i);
                System.out.println("Título: " + datosLibros.titulo());
                System.out.println("Autor: " + datosLibros.autor().get(0).nombre());
                System.out.println("Idioma: " + datosLibros.idiomas().get(0));
                System.out.println("Número de descargas: " + datosLibros.numeroDeDescargas());
                System.out.println("-------------------------------------------------------------");
            }
        } else {
            System.out.println("No se encontraron libros en el top 10 de descargas.");
        }
    }
}
