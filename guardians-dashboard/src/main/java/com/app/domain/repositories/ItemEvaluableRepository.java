/**
 * ItemEvaluableRepository.java
 * appEducacional
 * 14/01/2014 10:35:31
 * Copyright David Romero Alcaide
 * com.app.domainLayer.repositories
 */
package com.app.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.domain.model.types.ItemEvaluable;

@Repository
/**
 * @author David Romero Alcaide
 *
 */
public interface ItemEvaluableRepository extends
		JpaRepository<ItemEvaluable, Integer> {

}
