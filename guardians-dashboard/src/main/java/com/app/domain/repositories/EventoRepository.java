/**
 * EventoRepository.java
 * appEducacional
 * 14/01/2014 10:44:01
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

import com.app.domain.model.types.Evento;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface EventoRepository extends JpaRepository<Evento, Integer> {
	@Query("select n from Evento n where n.profesor.id=?1 and n.fecha < CURRENT_DATE ")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Evento> findEventosProfesorPasados(int id);

	@Query("select n from Evento n where n.profesor.id=?1")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Evento> findAllEventosProfesor(int id);
	
	@Query("select n from Evento n where n.profesor.id=?1 and n.materia.id=?2")
	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return 
	 */
	Collection<Evento> findAllEventosProfesorMateria(int idProfesor,int idMateria);
}
