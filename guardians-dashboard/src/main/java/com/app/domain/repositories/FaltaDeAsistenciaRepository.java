/**
 * FaltaDeAsistenciaRepository.java
 * appEducacional
 * 14/01/2014 10:46:06
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

import com.app.domain.model.types.itemsevaluables.FaltaDeAsistencia;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface FaltaDeAsistenciaRepository extends
		JpaRepository<FaltaDeAsistencia, Integer> {
	@Query("select e from FaltaDeAsistencia e where e.fecha = ?1 and e.asignatura.id = ?2 "
			+ "and e.titulo = ?3")
	/**
	 * 
	 * @author David Romero Alcaide
	 * @param date
	 * @param asignId
	 * @param title
	 * @return
	 */
	Collection<FaltaDeAsistencia> findByDateAsignaturaTitle(Date date,
			int asignId, String title);

	@Query("select e from FaltaDeAsistencia e where  e.asignatura.id = ?1 "
			+ "")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @param id2
	 * @return
	 */
	Collection<FaltaDeAsistencia> findByAsignaturaAndAlumno(int id);
}
