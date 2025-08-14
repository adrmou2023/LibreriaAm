package com.alura.literatura.repository;

import com.alura.literatura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LibrosRepository extends JpaRepository<Libro, Long> {

    Optional<Libro> findByTitulo(String titulo);
    Optional<Libro> findByGutendexId(Long gutendexId);

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE l.autor IS NOT NULL ORDER BY l.autor")
    List<String> findDistinctAutores();

    @Query("SELECT DISTINCT l.autor FROM Libro l WHERE " +
            "(l.anioNacimientoAutor IS NULL OR l.anioNacimientoAutor <= :anio) AND " +
            "(l.anioMuerteAutor IS NULL OR l.anioMuerteAutor >= :anio)")
    List<String> findAutoresVivosEnAnio(@Param("anio") Integer anio);

    @Query("SELECT l FROM Libro l WHERE l.idiomas LIKE %:idioma%")
    List<Libro> findByIdiomasContaining(@Param("idioma") String idioma);

    @Query("SELECT l FROM Libro l WHERE LOWER(l.titulo) LIKE LOWER(CONCAT('%', :titulo, '%'))")
    List<Libro> findByTituloContainingIgnoreCase(@Param("titulo") String titulo);
}