/**
 * CursoService.java
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
import com.app.domain.model.types.Curso;
import com.app.domain.model.types.CursoAcademico;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.CursoRepository;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class CursoService {

	/**
	 * Constructor
	 */
	public CursoService() {
		super();

	}

	// Repositorios gestionados

	@Autowired
	/**
	 * repositorio de curso
	 */
	private CursoRepository cursoRepositorio;

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
	public Curso create() {
		Curso c = new Curso();
		return c;
	}

	/**
	 * Buscar un curso por id
	 * 
	 * @author David Romero Alcaide
	 * @param cursoId
	 * @return
	 */
	public Curso findOne(int cursoId) {
		Assert.isTrue(cursoId > 0);
		Curso c = cursoRepositorio.findOne(cursoId);
		return c;
	}

	/**
	 * Guarda un curso
	 * 
	 * @author David Romero Alcaide
	 * @param c
	 */
	public void save(Curso c) {
		Assert.notNull(c);
		Assert.isTrue(c.getNivel() > 0);
		Assert.isTrue(c.getNivelEducativo().length() > 3);
		cursoRepositorio.save(c);
	}

	/**
	 * Buscar todos los cursos
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Curso> findAll() {
		return cursoRepositorio.findAll();
	}

	// Otros metodos de negocio

	/**
	 * Buscar alumnos pertenecientes a un curso y profesor.
	 * 
	 * @author David Romero Alcaide
	 * @param cursoId
	 * @return
	 */
	public Collection<Alumno> getAlumnosEnCurso(Integer cursoId) {
		Assert.isTrue(cursoId > 0);
		Profesor profesor = profesorService.findPrincipal();
		Assert.isTrue(profesorService.getCursosImparteDocencia().contains(
				findOne(cursoId)));
		Assert.isTrue(profesor.isIdentidadConfirmada());
		return cursoRepositorio.getAlumnosEnCurso(cursoId);
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
		Curso c = cursoRepositorio.findCurso(nivel, nivelEducativo,
				identificador);
		if (c == null) {
			c = create();
			c.setNivel(nivel);
			c.setNivelEducativo(nivelEducativo);
			c.setIdentificador(identificador);
		}
		c.addAlumno(a);
		save(c);
	}

	/**
	 * @author David Romero Alcaide
	 * @param nivel
	 * @param nivelEducativo
	 * @param identificador
	 * @throws ParseException
	 */
	public Curso findOrCreate(int nivel, String nivelEducativo,
			char identificador) {
		Assert.isTrue(nivel > 0);
		Assert.isTrue(nivelEducativo.equals("E.S.O.")
				|| nivelEducativo.equals("Primaria")
				|| nivelEducativo.equals("Baidentificadoriller"));
		Assert.isTrue(
				(identificador >= 'a' && identificador <= 'z') 
				|| 
				(identificador >= 'A' && identificador <= 'Z'));
		Curso c = cursoRepositorio.findCurso(nivel, nivelEducativo,
				identificador);
		if (c == null) {
			c = create();
			c.setNivel(nivel);
			c.setNivelEducativo(nivelEducativo);
			c.setIdentificador(identificador);
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
	public Curso find(int nivel, String nivelEducativo, char identificador) {
		return cursoRepositorio.findCurso(nivel, nivelEducativo, identificador);
	}

}
