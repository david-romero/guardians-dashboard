/**
 * CitaRepository.java
 * appEducacional
 * 14/01/2014 10:44:53
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.Cita;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface CitaRepository extends JpaRepository<Cita, Integer> {
	@Query("select n from Cita n where n.padreMadreOTutor.id=?1 and n.emisor='TUTOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Cita> findTutorEmitidas(int id);

	@Query("select n from Cita n where n.padreMadreOTutor.id=?1 and n.emisor='PROFESOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Cita> findTutorRecibidas(int id);

	@Query("select n from Cita n where n.profesor.id=?1 and n.emisor='PROFESOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Cita> findProfesorEmitidas(int id);

	@Query("select n from Cita n where n.profesor.id=?1 and n.emisor='TUTOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Cita> findProfesorRecibidas(int id);
}
