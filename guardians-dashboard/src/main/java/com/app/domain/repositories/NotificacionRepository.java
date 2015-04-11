/**
 * NotificacionRepository.java
 * appEducacional
 * 14/01/2014 10:44:28
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

import com.app.domain.model.types.Notificacion;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface NotificacionRepository extends
		JpaRepository<Notificacion, Integer> {

	@Query("select n from Notificacion n where n.padreMadreOTutor.id=?1 and n.emisor='TUTOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Notificacion> findTutorEmitidas(int id);

	@Query("select n from Notificacion n where n.padreMadreOTutor.id=?1 and n.emisor='PROFESOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Notificacion> findTutorRecibidas(int id);

	@Query("select n from Notificacion n where n.profesor.id=?1 and n.emisor='PROFESOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Notificacion> findProfesorEmitidas(int id);

	@Query("select n from Notificacion n where n.profesor.id=?1 and n.emisor='TUTOR'")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Notificacion> findProfesorRecibidas(int id);

}
