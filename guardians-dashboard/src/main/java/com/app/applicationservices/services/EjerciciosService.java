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

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.itemsevaluables.Ejercicios;
import com.app.domain.repositories.EjerciciosEntregadosRepository;
import com.google.common.collect.Lists;

@org.springframework.stereotype.Service
@Transactional
/**
 * @author David Romero Alcaide
 * 
 */
public class EjerciciosService implements Service {

	@Autowired
	private EjerciciosEntregadosRepository repositorio;

	/**
	 * Constructor
	 */
	public EjerciciosService() {
		super();

	}

	public void delete(Ejercicios acti) {
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
		Ejercicios ejer = (Ejercicios) item;
		repositorio.save(ejer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#findOne(int)
	 */
	public ItemEvaluable findOne(int id) {
		Assert.isTrue(id > 0);
		return repositorio.findOne(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#findAll()
	 */
	public Collection<ItemEvaluable> findAll() {
		List<ItemEvaluable> items = Lists.newArrayList();
		items.addAll(repositorio.findAll());
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#create()
	 */
	public ItemEvaluable create() {
		return new Ejercicios();
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
		Collection<Ejercicios> items = repositorio
				.findByDateAsignaturaTitle(date, asign.getId(), titulo);
		Ejercicios e = null;
		for (Ejercicios ejerciciosEntregados : items) {
				if (ejerciciosEntregados.getAlumno().equals(a)) {
					e = ejerciciosEntregados;
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
		Ejercicios ejer = (Ejercicios) item;
		this.delete(ejer);
	}

}
