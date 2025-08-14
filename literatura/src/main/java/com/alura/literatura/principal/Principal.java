package com.alura.literatura.principal;

import com.alura.literatura.service.ConsumoAPI;
import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Scanner;

@Component
public class Principal implements CommandLineRunner {

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private LibrosRepository librosRepository;

    private Scanner scanner = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";

    @Override
    public void run(String... args) throws Exception {
        System.out.println("¡Bienvenido a la Literatura Digital!");
        System.out.println("====================================");

        mostrarMenu();
    }

    private void mostrarMenu() {
        int opcion = 0;

        do {
            System.out.println("\n--- MENÚ PRINCIPAL ---");
            System.out.println("1- Buscar libro por título");
            System.out.println("2- Listar libros registrados");
            System.out.println("3- Listar autores registrados");
            System.out.println("4- Listar autores vivos en un determinado año");
            System.out.println("5- Listar libros por idioma");
            System.out.println("0- Salir");
            System.out.print("Elija una opción: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAnio();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("¡Gracias por usar Literatura Digital!");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, elija una opción del 0 al 5.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor, ingrese un número válido.");
                opcion = -1; // Para que no salga del bucle
            }
        } while (opcion != 0);
    }

    private void buscarLibroPorTitulo() {
        System.out.print("Ingrese el título del libro a buscar: ");
        String titulo = scanner.nextLine();

        if (titulo.trim().isEmpty()) {
            System.out.println("El título no puede estar vacío.");
            return;
        }

        System.out.println("Buscando libros...");
        consumoAPI.buscarYGuardarLibrosPorTitulo(titulo);
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = librosRepository.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
            return;
        }

        System.out.println("\n--- LIBROS REGISTRADOS ---");
        System.out.println("Total de libros: " + libros.size());
        System.out.println("==========================");

        for (int i = 0; i < libros.size(); i++) {
            System.out.println((i + 1) + ". " + libros.get(i));
        }
    }

    private void listarAutoresRegistrados() {
        List<String> autores = librosRepository.findDistinctAutores();

        if (autores.isEmpty()) {
            System.out.println("No hay autores registrados en la base de datos.");
            return;
        }

        System.out.println("\n--- AUTORES REGISTRADOS ---");
        System.out.println("Total de autores: " + autores.size());
        System.out.println("============================");

        for (int i = 0; i < autores.size(); i++) {
            System.out.println((i + 1) + ". " + autores.get(i));
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("Ingrese el año: ");

        try {
            Integer anio = Integer.parseInt(scanner.nextLine());
            List<String> autores = librosRepository.findAutoresVivosEnAnio(anio);

            if (autores.isEmpty()) {
                System.out.println("No se encontraron autores vivos en el año " + anio + ".");
                return;
            }

            System.out.println("\n--- AUTORES VIVOS EN " + anio + " ---");
            System.out.println("Total encontrados: " + autores.size());
            System.out.println("=====================================");

            for (int i = 0; i < autores.size(); i++) {
                System.out.println((i + 1) + ". " + autores.get(i));
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, ingrese un año válido.");
        }
    }

    private void listarLibrosPorIdioma() {
        System.out.println("\nCódigos de idioma comunes:");
        System.out.println("en - Inglés");
        System.out.println("es - Español");
        System.out.println("fr - Francés");
        System.out.println("de - Alemán");
        System.out.println("it - Italiano");
        System.out.println("pt - Portugués");
        System.out.print("Ingrese el código del idioma: ");

        String idioma = scanner.nextLine().toLowerCase().trim();

        if (idioma.isEmpty()) {
            System.out.println("El código del idioma no puede estar vacío.");
            return;
        }

        List<Libro> libros = librosRepository.findByIdiomasContaining(idioma);

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma: " + idioma);
            return;
        }

        System.out.println("\n--- LIBROS EN IDIOMA: " + idioma.toUpperCase() + " ---");
        System.out.println("Total encontrados: " + libros.size());
        System.out.println("=============================================");

        for (int i = 0; i < libros.size(); i++) {
            System.out.println((i + 1) + ". " + libros.get(i));
        }
    }
}