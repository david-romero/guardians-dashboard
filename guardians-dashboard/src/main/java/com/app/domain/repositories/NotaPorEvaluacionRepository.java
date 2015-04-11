/**
 * NotaPorEvaluacionRepository.java
 * appEducacional
 * 14/01/2014 10:42:13
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.NotaPorEvaluacion;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface NotaPorEvaluacionRepository extends
		JpaRepository<NotaPorEvaluacion, Integer> {

	@Query("select nota from NotaPorEvaluacion nota where nota.alumno.id = ?1 and"
			+ " nota.evaluacion.indicador = ?2 and nota.asignatura.id = ?3")
	public NotaPorEvaluacion findByAlumnoAndEvaluacion(int alumnoId,
			int evaluacionId, int asignaturaId);

}
