/**
 * CursoAcademicoService.java
 * appEducacional
 * 26/01/2014 13:16:36
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

/**
 * imports
 */
import java.text.ParseException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.CursoAcademico;
import com.app.domain.repositories.CursoAcademicoRepository;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class CursoAcademicoService {

	/**
	 * Constructor
	 */
	public CursoAcademicoService() {
		super();

	}

	// Repositorios gestionados

	@Autowired
	/**
	 * repositorio de CursoAcademico
	 */
	private CursoAcademicoRepository cursoAcademicoRepositorio;

	// Servicios gestionados

	// CRUD

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public CursoAcademico create() {
		CursoAcademico c = new CursoAcademico();
		return c;
	}

	/**
	 * Buscar un CursoAcademico por id
	 * 
	 * @author David Romero Alcaide
	 * @param CursoAcademicoId
	 * @return
	 */
	public CursoAcademico findOne(int cursoAcademicoId) {
		Assert.isTrue(cursoAcademicoId > 0);
		CursoAcademico c = cursoAcademicoRepositorio.findOne(cursoAcademicoId);
		return c;
	}

	/**
	 * Guarda un CursoAcademico
	 * 
	 * @author David Romero Alcaide
	 * @param c
	 */
	public void save(CursoAcademico c) {
		Assert.notNull(c);

		cursoAcademicoRepositorio.save(c);
	}

	/**
	 * Buscar todos los CursoAcademicos
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<CursoAcademico> findAll() {
		return cursoAcademicoRepositorio.findAll();
	}

	// Otros metodos de negocio

	/**
	 * @author David Romero Alcaide
	 * @return
	 * @throws ParseException
	 */
	public CursoAcademico findActual() {
		CursoAcademico cursoAcademico = cursoAcademicoRepositorio
				.findActualCursoAcademico();
		return cursoAcademico;
	}

}
