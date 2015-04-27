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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.itemsevaluables.Actividad;
import com.app.domain.repositories.ActividadRepository;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 * 
 */
public class ActividadService implements
		com.app.applicationservices.services.Service {

	@Autowired
	private ActividadRepository repositorio;

	/**
	 * Constructor
	 */
	public ActividadService() {
		super();

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
		Actividad acti = (Actividad) item;
		repositorio.save(acti);
	}

	public void delete(Actividad acti) {
		Assert.notNull(acti);
		Assert.isTrue(acti.getId()>0);
		Assert.notNull(acti.getMateria());
		repositorio.delete(acti);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#findOne(int)
	 */
	public Actividad findOne(int id) {
		Assert.isTrue(id > 0);
		Actividad acti = repositorio.findOne(id);
		return acti;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#findAll()
	 */
	public Collection<ItemEvaluable> findAll() {
		List<ItemEvaluable> actividades = Lists.newArrayList();
		actividades.addAll(repositorio.findAll());
		return actividades;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#create()
	 */
	public ItemEvaluable create() {
		return new Actividad();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.app.applicationservices.services.Service#findByDateAsignaturaAndTitulo
	 * (java.util.Date, com.app.domainLayer.domainModel.types.Asignatura,
	 * java.lang.String)
	 */
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
		Collection<Actividad> items = repositorio.findByDateAsignaturaTitle(
				date, asign.getId(), titulo);
		Actividad e = null;
		for (Actividad actividad : items) {
				if (actividad.getAlumno().equals(a)) {
					e = actividad;
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
		Assert.isInstanceOf(Actividad.class, item);
		Assert.notNull(item);
		Actividad acti = (Actividad) item;
		delete(acti);
	}

}
