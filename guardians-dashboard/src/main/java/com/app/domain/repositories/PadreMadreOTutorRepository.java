/**
 * PadreMadreOTutorRepository.java
 * appEducacional
 * 14/01/2014 10:27:13
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

import com.app.domain.model.types.PadreMadreOTutor;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface PadreMadreOTutorRepository extends
		JpaRepository<PadreMadreOTutor, Integer> {

	/**
	 * @author David Romero Alcaide
	 * @param id
	 * @return
	 */
	@Query("select p from PadreMadreOTutor p where p.userAccount.id = ?1")
	PadreMadreOTutor findByUserAccountId(int id);

}
