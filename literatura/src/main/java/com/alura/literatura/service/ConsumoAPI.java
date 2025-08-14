package com.alura.literatura.service;

import com.alura.literatura.model.Libro;
import com.alura.literatura.repository.LibrosRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsumoAPI {

    @Autowired
    private LibrosRepository librosRepository;

    @Autowired
    private RestTemplate restTemplate;

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String URL_BASE = "https://gutendex.com/books/";

    @Transactional
    public void buscarYGuardarLibrosPorTitulo(String titulo) {
        try {
            String url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
            System.out.println("Consultando: " + url);

            String jsonResponse = restTemplate.getForObject(url, String.class);

            if (jsonResponse != null) {
                JsonNode rootNode = objectMapper.readTree(jsonResponse);
                JsonNode resultsNode = rootNode.get("results");

                if (resultsNode != null && resultsNode.isArray() && resultsNode.size() > 0) {
                    int contador = 0;
                    for (JsonNode bookNode : resultsNode) {
                        if (procesarYGuardarLibro(bookNode)) {
                            contador++;
                        }
                    }
                    System.out.println("Búsqueda completada. Se procesaron " + resultsNode.size() + " libros, se guardaron " + contador + " nuevos.");
                } else {
                    System.out.println("No se encontraron libros con el título: " + titulo);
                }
            }
        } catch (Exception e) {
            System.err.println("Error al conectar con la API Gutendex: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Transactional
    private boolean procesarYGuardarLibro(JsonNode bookNode) {
        try {
            Long gutendexId = bookNode.get("id").asLong();

            // Verificar si ya existe
            Optional<Libro> libroExistente = librosRepository.findByGutendexId(gutendexId);
            if (libroExistente.isPresent()) {
                System.out.println("El libro con ID " + gutendexId + " ya existe en la base de datos.");
                return false;
            }

            String titulo = bookNode.get("title").asText();

            // Procesar autores
            String autor = "Desconocido";
            Integer anioNacimiento = null;
            Integer anioMuerte = null;

            JsonNode authorsNode = bookNode.get("authors");
            if (authorsNode != null && authorsNode.isArray() && authorsNode.size() > 0) {
                JsonNode firstAuthor = authorsNode.get(0);
                autor = firstAuthor.get("name").asText();

                if (firstAuthor.has("birth_year") && !firstAuthor.get("birth_year").isNull()) {
                    anioNacimiento = firstAuthor.get("birth_year").asInt();
                }
                if (firstAuthor.has("death_year") && !firstAuthor.get("death_year").isNull()) {
                    anioMuerte = firstAuthor.get("death_year").asInt();
                }
            }

            // Procesar idiomas
            List<String> idiomas = new ArrayList<>();
            JsonNode languagesNode = bookNode.get("languages");
            if (languagesNode != null && languagesNode.isArray()) {
                for (JsonNode langNode : languagesNode) {
                    idiomas.add(langNode.asText());
                }
            }

            Integer numeroDescargas = 0;
            if (bookNode.has("download_count") && !bookNode.get("download_count").isNull()) {
                numeroDescargas = bookNode.get("download_count").asInt();
            }

            // Crear y guardar el libro
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setAnioNacimientoAutor(anioNacimiento);
            libro.setAnioMuerteAutor(anioMuerte);
            libro.setIdiomas(String.join(",", idiomas));
            libro.setNumeroDescargas(numeroDescargas);
            libro.setGutendexId(gutendexId);

            librosRepository.save(libro);
            System.out.println("Libro guardado: " + titulo + " por " + autor);
            return true;

        } catch (Exception e) {
            System.err.println("Error al procesar libro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}