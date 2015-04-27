/**
 * ActividadService.java
 * appEducacional
 * 15/02/2014 17:20:03
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.itemsevaluables.Actitud;
import com.app.domain.repositories.ActitudRepository;
import com.google.common.collect.Lists;

/**
 * @author David Romero Alcaide
 * 
 */
public class ActitudService implements Service {

	@Autowired
	private ActitudRepository repositorio;

	/**
	 * Constructor
	 */
	public ActitudService() {
		super();

	}

	public void delete(Actitud acti) {
		Assert.notNull(acti);
		Assert.isTrue(acti.getId() > 0);
		Assert.notNull(acti.getMateria());
		repositorio.delete(acti);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#save(com.app.domainLayer
	 * .domainModel.types.ItemEvaluable)
	 */
	public void save(ItemEvaluable item) {
		Assert.notNull(item);
		Assert.notNull(item.getMateria());
		Assert.notNull(item.getEvaluacion());
		Actitud acti = (Actitud) item;
		repositorio.save(acti);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#findOne(int)
	 */
	public ItemEvaluable findOne(int id) {
		return repositorio.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#findAll()
	 */
	public Collection<ItemEvaluable> findAll() {
		List<ItemEvaluable> list2 = Lists.newArrayList();
		List<Actitud> list = Lists.newArrayList(repositorio.findAll());
		list2.addAll(list);
		return list2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#create()
	 */
	public ItemEvaluable create() {
		return new Actitud();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#findByDateAsignaturaAndTitulo
	 * (java.util.Date, com.app.domainLayer.domainModel.types.Asignatura,
	 * java.lang.String)
	 */
	public ItemEvaluable findByDateAsignaturaAndTitulo(Date date,
			Asignatura asign, String titulo, final Alumno a) {
		Collection<Actitud> items = repositorio.findByDateAsignaturaTitle(date,
				asign.getId(), titulo);
		Actitud e = null;
		for (Actitud actitud : items) {
			if (actitud.getAlumno().equals(a)) {
				e = actitud;
			}
		}
		Assert.notNull(e);
		return e;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#delete(com.app.domainLayer
	 * .domainModel.types.ItemEvaluable)
	 */
	public void delete(ItemEvaluable item) {
		Assert.isInstanceOf(Actitud.class, item);
		Assert.notNull(item);
		Assert.isTrue(item.getId() > 0);
		Actitud acti = (Actitud) item;
		delete(acti);
	}

}
