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
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.MateriaRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class MateriaService {

	/**
	 * Constructor
	 */
	public MateriaService() {
		super();

	}

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private MateriaRepository materiaRepositorio;

	@Autowired
	private ProfesorService profesorService;

	// Métodos CRUD

	/**
	 * Crear evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Materia create() {
		Materia nueva = new Materia();
		return nueva;
	}

	/**
	 * Buscar todas las evaluaciones
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Materia> findAll() {
		return materiaRepositorio.findAll();
	}

	/**
	 * encontrar una evaluacion por id
	 * 
	 * @author David Romero Alcaide
	 * @param profesorId
	 * @return
	 */
	public Materia findOne(int MateriaId) {
		Materia Materia;
		Materia = materiaRepositorio.findOne(MateriaId);
		return Materia;
	}

	/**
	 * guardar una evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void save(Materia asign) {
		Assert.notNull(asign);
		Assert.isTrue(asign.getAsignatura().getNombre().length() > 3);
		materiaRepositorio.save(asign);
	}

	/**
	 * Eliminar una evaluacion
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void delete(Materia asign) {
		Assert.notNull(asign);
		Assert.isTrue(asign.getId() > 0);
		Assert.isTrue(asign.getProfesor().isIdentidadConfirmada());
		materiaRepositorio.delete(asign);
	}

	// Otros métodos de negocio

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Materia> findAllByProfesor() {
		Profesor p = profesorService.findPrincipal();
		Assert.notNull(p);
		Assert.isTrue(p.isIdentidadConfirmada());
		return materiaRepositorio.findMateriasDeProfesor(p.getId());
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Materia> findAllByProfesor(Profesor p) {
		Assert.notNull(p);
		return materiaRepositorio.findMateriasDeProfesor(p.getId());
	}

	/**
	 * @author David Romero Alcaide
	 * @param nombreAsignatura
	 * @return
	 */
	public Materia findByName(String nombreAsignatura) {
		List<Materia> todasConMismoNombre = Lists
				.newArrayList(materiaRepositorio
						.findMateriasPorNombre(nombreAsignatura));
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
								&& input.getCurso().equals(curso);
					}
				});
		Assert.notNull(buscada);
		return buscada;
	}

}
