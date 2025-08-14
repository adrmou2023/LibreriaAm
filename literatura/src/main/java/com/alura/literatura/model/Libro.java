package com.alura.literatura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String titulo;

    @Column(nullable = false, length = 300)
    private String autor;

    @Column(name = "anio_nacimiento_autor")
    private Integer anioNacimientoAutor;

    @Column(name = "anio_muerte_autor")
    private Integer anioMuerteAutor;

    @Column(length = 100)
    private String idiomas;

    @Column(name = "numero_descargas")
    private Integer numeroDescargas;

    @Column(name = "gutendex_id", unique = true, nullable = false)
    private Long gutendexId;

    // Constructores
    public Libro() {}

    public Libro(String titulo, String autor, Integer anioNacimientoAutor, Integer anioMuerteAutor,
                 String idiomas, Integer numeroDescargas, Long gutendexId) {
        this.titulo = titulo;
        this.autor = autor;
        this.anioNacimientoAutor = anioNacimientoAutor;
        this.anioMuerteAutor = anioMuerteAutor;
        this.idiomas = idiomas;
        this.numeroDescargas = numeroDescargas;
        this.gutendexId = gutendexId;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Integer getAnioNacimientoAutor() { return anioNacimientoAutor; }
    public void setAnioNacimientoAutor(Integer anioNacimientoAutor) { this.anioNacimientoAutor = anioNacimientoAutor; }

    public Integer getAnioMuerteAutor() { return anioMuerteAutor; }
    public void setAnioMuerteAutor(Integer anioMuerteAutor) { this.anioMuerteAutor = anioMuerteAutor; }

    public String getIdiomas() { return idiomas; }
    public void setIdiomas(String idiomas) { this.idiomas = idiomas; }

    public Integer getNumeroDescargas() { return numeroDescargas; }
    public void setNumeroDescargas(Integer numeroDescargas) { this.numeroDescargas = numeroDescargas; }

    public Long getGutendexId() { return gutendexId; }
    public void setGutendexId(Long gutendexId) { this.gutendexId = gutendexId; }

    @Override
    public String toString() {
        return "Título: " + titulo +
                "\nAutor: " + autor +
                (anioNacimientoAutor != null ? " (" + anioNacimientoAutor : " (N/A") +
                (anioMuerteAutor != null ? " - " + anioMuerteAutor + ")" : " - N/A)") +
                "\nIdiomas: " + idiomas +
                "\nDescargas: " + numeroDescargas + "\n";
    }
}