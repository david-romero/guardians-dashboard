/**
 * ExamenRepository.java
 * appEducacional
 * 14/01/2014 10:46:39
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

/**
 * imports
 */
import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.itemsevaluables.Examen;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface ExamenRepository extends JpaRepository<Examen, Integer> {
	@Query("select e from Examen e where e.fecha = ?1 and e.materia.id = ?2 "
			+ "and e.titulo = ?3")
	/**
	 * 
	 * @author David Romero Alcaide
	 * @param date
	 * @param asignId
	 * @param title
	 * @return
	 */
	Collection<Examen> findByDateAsignaturaTitle(Date date, int asignId,
			String title);
}
