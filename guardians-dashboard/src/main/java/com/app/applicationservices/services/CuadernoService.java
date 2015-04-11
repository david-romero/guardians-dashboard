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
import com.app.domain.model.types.itemsevaluables.Cuaderno;
import com.app.domain.repositories.CuadernoRepository;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 * 
 */
public class CuadernoService implements
		com.app.applicationservices.services.Service {

	@Autowired
	private CuadernoRepository repositorio;

	/**
	 * Constructor
	 */
	public CuadernoService() {
		super();

	}

	public void delete(Cuaderno acti) {
		Assert.notNull(acti);
		Assert.isTrue(acti.getId()>0);
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
		Assert.notNull(item.getAsignatura());
		Assert.notNull(item.getEvaluacion());
		Cuaderno ejer = (Cuaderno) item;
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
		List<Cuaderno> list = Lists.newArrayList(repositorio.findAll());
		List<ItemEvaluable> list2 = Lists.newArrayList();
		list2.addAll(list);
		return list2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.app.applicationservices.services.Service#create()
	 */
	public ItemEvaluable create() {
		return new Cuaderno();
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
		Collection<Cuaderno> items = repositorio.findByDateAsignaturaTitle(
				date, asign.getId(), titulo);
		Cuaderno e = null;
		for (Cuaderno cuaderno : items) {
				if (cuaderno.getAlumno().equals(a)) {
					e = cuaderno;
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
		Assert.isInstanceOf(Cuaderno.class, item);
		Assert.notNull(item);
		Cuaderno cua = (Cuaderno) item;
		this.delete(cua);
	}

}
