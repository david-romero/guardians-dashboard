/**
 * ActitudRepository.java
 * appEducacional
 * 14/01/2014 10:49:20
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.itemsevaluables.Actitud;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface ActitudRepository extends JpaRepository<Actitud, Integer> {
	@Query("select e from Actitud e where e.fecha = ?1 and e.asignatura.id = ?2 "
			+ "and e.titulo = ?3")
	/**
	 * 
	 * @author David Romero Alcaide
	 * @param date
	 * @param asignId
	 * @param title
	 * @return
	 */
	Collection<Actitud> findByDateAsignaturaTitle(Date date, int asignId,
			String title);
}
