/**
 * Service.java
 * appEducacional
 * 15/03/2014 19:28:02
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.Date;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.ItemEvaluable;

/**
 * @author David Romero Alcaide
 * 
 */
public interface Service {
	public void save(ItemEvaluable item);

	public ItemEvaluable findOne(int id);

	public Collection<ItemEvaluable> findAll();

	public ItemEvaluable create();

	public void delete(ItemEvaluable item);

	/**
	 * @author David Romero Alcaide
	 */
	public ItemEvaluable findByDateAsignaturaAndTitulo(Date date,
			Asignatura asign, String titulo, Alumno a);
}
