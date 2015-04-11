/**
 * EvaluacionRepository.java
 * appEducacional
 * 14/01/2014 10:34:57
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

/**
 * imports
 */
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.Evaluacion;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface EvaluacionRepository extends
		JpaRepository<Evaluacion, Integer> {

	@Query("select e from Evaluacion e where e.inicio < ?1 and e.fin > ?1")
	Evaluacion getEvaluacionByDate(Date moment);
}
