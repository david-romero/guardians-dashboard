/**
 * MateriaRepository.java
 * appEducacional
 * 14/01/2014 10:43:26
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.Materia;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface MateriaRepository extends
		JpaRepository<Materia, Integer> {

	@Query("select a from Materia a where a.profesor.id=?1")
	Collection<Materia> findMateriasDeProfesor(int profesorId);

	@Query("select a from Materia a where a.asignatura.nombre=?1")
	Collection<Materia> findMateriasPorNombre(String nombre);

}
