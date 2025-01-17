/**
 * AsignaturaService.java
 * appEducacional
 * 15/02/2014 21:28:02
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.Curso;
import com.app.domain.model.types.CursoAcademico;
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.AsignaturaRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class AsignaturaService {

	/**
	 * Constructor
	 */
	public AsignaturaService() {
		super();

	}

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private AsignaturaRepository asignaturaRepositorio;

	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private CursoAcademicoService cursoAcademicoService;

	// Métodos CRUD

	/**
	 * Crear evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Asignatura create() {
		Asignatura nueva = new Asignatura();
		return nueva;
	}

	/**
	 * Buscar todas las evaluaciones
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Asignatura> findAll() {
		return asignaturaRepositorio.findAll();
	}

	/**
	 * encontrar una evaluacion por id
	 * 
	 * @author David Romero Alcaide
	 * @param profesorId
	 * @return
	 */
	public Asignatura findOne(int asignaturaId) {
		Asignatura asignatura;
		asignatura = asignaturaRepositorio.findOne(asignaturaId);
		return asignatura;
	}

	/**
	 * guardar una evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void save(Asignatura asign) {
		Assert.notNull(asign);
		Assert.isTrue(asign.getNombre().length() > 3);
		asignaturaRepositorio.save(asign);
	}

	/**
	 * Eliminar una evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void delete(Asignatura asign) {
		Assert.notNull(asign);
		Assert.isTrue(asign.getId() > 0);
		asignaturaRepositorio.delete(asign);
	}

	// Otros métodos de negocio


	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Materia> findAllByProfesor(Profesor p) {
		Assert.notNull(p);
		CursoAcademico cursoAcademico = cursoAcademicoService.findActual();
		return p.getMaterias().stream().filter(materia->{
			return materia.getCursoAcademico().equals(cursoAcademico);
		}).collect(Collectors.toList());
	}

	/**
	 * @author David Romero Alcaide
	 * @param nombreAsignatura
	 * @return
	 */
	public Asignatura findByName(String nombreAsignatura) {
		List<Asignatura> todasConMismoNombre = Lists
				.newArrayList(asignaturaRepositorio
						.findAsignaturasPorNombre(nombreAsignatura));
		if (todasConMismoNombre.size() > 0) {
			return todasConMismoNombre.get(0);
		} else {
			return null;
		}

	}

	/**
	 * @author David Romero Alcaide
	 * @param p
	 * @param curso
	 * @param nombre
	 * @return
	 */
	public Materia findByProfesorCurso(Profesor p, final Curso curso,
			final String nombre) {

		List<Materia> asignaturasProfesor = Lists
				.newArrayList(findAllByProfesor(p));
		Materia buscada = Iterables.find(asignaturasProfesor,
				new Predicate<Materia>() {

					
					public boolean apply(Materia input) {
						return input.getAsignatura().getNombre().compareTo(nombre) == 0
								&& input.getAsignatura().getNombre().equals(nombre)
								&& input.getAsignatura().getCurso().equals(curso);
					}
				});
		Assert.notNull(buscada);
		return buscada;
	}

}
