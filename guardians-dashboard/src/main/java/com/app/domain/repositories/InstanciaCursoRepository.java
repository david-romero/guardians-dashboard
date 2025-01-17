/**
 * CursoRepository.java
 * appEducacional
 * 14/01/2014 10:41:49
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

/**
 * imports
 */
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.InstanciaCurso;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface InstanciaCursoRepository extends JpaRepository<InstanciaCurso, Integer> {

	@Query("select c.alumnos from InstanciaCurso c where c.id = ?1")
	Collection<Alumno> getAlumnosEnCurso(Integer cursoId);

	@Query("select c from InstanciaCurso c where c.curso.nivel=?1 and c.curso.nivelEducativo=?2 and c.curso.identificador=?3")
	InstanciaCurso findCurso(int nivel, String nivelEducativo, char identificador);
	
	@Query("select c from InstanciaCurso c where c.cursoAcademico.id = ?1")
	Collection<InstanciaCurso> findCursosAnioAcademico(int cursoAcademicoId);

}
