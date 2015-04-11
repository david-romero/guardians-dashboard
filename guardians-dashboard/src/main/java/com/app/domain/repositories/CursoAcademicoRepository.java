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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.CursoAcademico;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface CursoAcademicoRepository extends
		JpaRepository<CursoAcademico, Integer> {

	@Query("select c from CursoAcademico c where c.inicio <= CURRENT_TIMESTAMP and c.fin >= CURRENT_TIMESTAMP")
	/**
	 * Devuelve el curso academico actual
	 * @author David Romero Alcaide
	 * @return
	 */
	CursoAcademico findActualCursoAcademico();
}
