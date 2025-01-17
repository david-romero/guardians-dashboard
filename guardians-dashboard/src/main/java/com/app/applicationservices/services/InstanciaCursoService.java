/**
 * InstanciaCursoService.java
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

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.CursoAcademico;
import com.app.domain.model.types.InstanciaCurso;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.InstanciaCursoRepository;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class InstanciaCursoService {

	/**
	 * Constructor
	 */
	public InstanciaCursoService() {
		super();

	}

	// Repositorios gestionados

	@Autowired
	/**
	 * repositorio de InstanciaCurso
	 */
	private InstanciaCursoRepository instanciaCursoRepositorio;

	// Servicios gestionados

	@Autowired
	/**
	 * 
	 */
	private ProfesorService profesorService;

	@Autowired
	/**
	 * 
	 */
	private CursoAcademicoService cursoAcademicoService;

	// CRUD

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public InstanciaCurso create() {
		InstanciaCurso c = new InstanciaCurso();
		return c;
	}

	/**
	 * Buscar un InstanciaCurso por id
	 * 
	 * @author David Romero Alcaide
	 * @param InstanciaCursoId
	 * @return
	 */
	public InstanciaCurso findOne(int instanciaCursoId) {
		Assert.isTrue(instanciaCursoId > 0);
		InstanciaCurso c = instanciaCursoRepositorio.findOne(instanciaCursoId);
		return c;
	}

	/**
	 * Guarda un InstanciaCurso
	 * 
	 * @author David Romero Alcaide
	 * @param c
	 */
	public void save(InstanciaCurso c) {
		Assert.notNull(c);
		Assert.isTrue(c.getCurso().getNivel() > 0);
		Assert.isTrue(c.getCurso().getNivelEducativo().length() > 3);
		instanciaCursoRepositorio.save(c);
	}

	/**
	 * Buscar todos los InstanciaCursos
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<InstanciaCurso> findAll() {
		return instanciaCursoRepositorio.findAll();
	}

	// Otros metodos de negocio

	/**
	 * Buscar alumnos pertenecientes a un InstanciaCurso y profesor.
	 * 
	 * @author David Romero Alcaide
	 * @param InstanciaCursoId
	 * @return
	 */
	public Collection<Alumno> getAlumnosEnInstanciaCurso(Integer instanciaCursoId,Profesor profesor) {
		Assert.isTrue(instanciaCursoId > 0);
		Assert.isTrue(profesorService.getCursosImparteDocencia(profesor).contains(
				findOne(instanciaCursoId)));
		Assert.isTrue(profesor.isIdentidadConfirmada());
		return instanciaCursoRepositorio.getAlumnosEnCurso(instanciaCursoId);
	}

	/**
	 * @author David Romero Alcaide
	 * @param nivel
	 * @param nivelEducativo
	 * @param identificador
	 * @param a
	 */
	public void find(int nivel, String nivelEducativo, char identificador,
			Alumno a) {
		Assert.isTrue(nivel > 0);
		Assert.isTrue(nivelEducativo.equals("E.S.O.")
				|| nivelEducativo.equals("Primaria")
				|| nivelEducativo.equals("Baidentificadoriller"));
		Assert.isTrue(
				(identificador >= 'a' && identificador <= 'z') 
				|| 
				(identificador >= 'A' && identificador <= 'Z'));
		InstanciaCurso c = instanciaCursoRepositorio.findCurso(nivel, nivelEducativo,
				identificador);
		if (c == null) {
			c = create();
			c.getCurso().setNivel(nivel);
			c.getCurso().setNivelEducativo(nivelEducativo);
			c.getCurso().setIdentificador(identificador);
		}
		c.getAlumnos().add(a);
		a.getCursos().add(c);
		save(c);
	}

	/**
	 * @author David Romero Alcaide
	 * @param nivel
	 * @param nivelEducativo
	 * @param identificador
	 * @throws ParseException
	 */
	public InstanciaCurso findOrCreate(int nivel, String nivelEducativo,
			char identificador) {
		Assert.isTrue(nivel > 0);
		Assert.isTrue(nivelEducativo.equals("E.S.O.")
				|| nivelEducativo.equals("Primaria")
				|| nivelEducativo.equals("Baidentificadoriller"));
		Assert.isTrue(
				(identificador >= 'a' && identificador <= 'z') 
				|| 
				(identificador >= 'A' && identificador <= 'Z'));
		InstanciaCurso c = instanciaCursoRepositorio.findCurso(nivel, nivelEducativo,
				identificador);
		if (c == null) {
			c = create();
			c.getCurso().setNivel(nivel);
			c.getCurso().setNivelEducativo(nivelEducativo);
			c.getCurso().setIdentificador(identificador);
			CursoAcademico cursoAcademico = cursoAcademicoService.findActual();
			c.setCursoAcademico(cursoAcademico);
		}
		save(c);
		return c;
	}

	/**
	 * @author David Romero Alcaide
	 * @param nivel
	 * @param nivelEducativo
	 * @param identificador
	 * @return
	 */
	public InstanciaCurso find(int nivel, String nivelEducativo, char identificador) {
		return instanciaCursoRepositorio.findCurso(nivel, nivelEducativo, identificador);
	}

}
