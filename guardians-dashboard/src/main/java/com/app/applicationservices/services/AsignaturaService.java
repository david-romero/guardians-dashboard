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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.Curso;
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
		Assert.isTrue(asign.getProfesor().isIdentidadConfirmada());
		Assert.isTrue(profesorService.findPrincipal().isIdentidadConfirmada());
		asignaturaRepositorio.delete(asign);
	}

	// Otros métodos de negocio

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Asignatura> findAllByProfesor() {
		Profesor p = profesorService.findPrincipal();
		Assert.notNull(p);
		Assert.isTrue(p.isIdentidadConfirmada());
		return asignaturaRepositorio.findAsignaturasDeProfesor(p.getId());
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Asignatura> findAllByProfesor(Profesor p) {
		Assert.notNull(p);
		return asignaturaRepositorio.findAsignaturasDeProfesor(p.getId());
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
	public Asignatura findByProfesorCurso(Profesor p, final Curso curso,
			final String nombre) {

		List<Asignatura> asignaturasProfesor = Lists
				.newArrayList(findAllByProfesor(p));
		Asignatura buscada = Iterables.find(asignaturasProfesor,
				new Predicate<Asignatura>() {

					
					public boolean apply(Asignatura input) {
						return input.getNombre().compareTo(nombre) == 0
								&& input.getNombre().equals(nombre)
								&& input.getCurso().equals(curso);
					}
				});
		Assert.notNull(buscada);
		return buscada;
	}

}
