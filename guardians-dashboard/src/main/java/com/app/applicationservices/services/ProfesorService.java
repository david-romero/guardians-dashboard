/**
 * ProfesorService.java
 * appEducacional
 * 16/01/2014 11:36:24
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.Assert;

import com.app.domain.domainservices.Valida;
import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.InstanciaCurso;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Profesor;
import com.app.domain.model.types.itemsevaluables.Actividad;
import com.app.domain.model.types.itemsevaluables.Ejercicios;
import com.app.domain.model.types.itemsevaluables.Examen;
import com.app.domain.model.types.itemsevaluables.FaltaDeAsistencia;
import com.app.domain.model.types.itemsevaluables.Trabajo;
import com.app.domain.repositories.ProfesorRepository;
import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.LoginService;
import com.app.infrastructure.security.UserAccount;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class ProfesorService implements Serializable {

	// Repositorios gestionados

	/**
	 * 
	 */
	private static final long serialVersionUID = -928342560252536918L;

	@Autowired
	/**
	 * 
	 */
	private ProfesorRepository profesorRepositorio;

	// Servicios gestionados

	@Autowired
	private ExamenService examenService;

	@Autowired
	private CursoAcademicoService cursoAcademicoService;

	@Autowired
	private TrabajoService trabajoService;

	@Autowired
	private ActividadService actividadService;

	@Autowired
	private EjerciciosService ejerciciosService;
	
	@Autowired
	/**
	 * Manager
	 */
	private PlatformTransactionManager transactionManager;
	/**
	 * State of transaction
	 */
	protected TransactionStatus txStatus;

	/**
	 * Constructor
	 */
	public ProfesorService() {
		super();

	}

	// Métodos CRUD
	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Profesor create() {
		Profesor p = new Profesor();
		p.setIdentidadConfirmada(false);
		UserAccount account = new UserAccount();
		List<Authority> authorities = Lists.newArrayList();
		Authority auth = new Authority();
		auth.setAuthority(Authority.PROFESOR);
		authorities.add(auth);
		account.setAuthorities(authorities);
		p.setUserAccount(account);

		return p;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Profesor> findAll() {
		List<Profesor> ite;
		ite = profesorRepositorio.findAll();

		return ite;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param profesorId
	 * @return
	 */
	public Profesor findOne(int profesorId) {
		Assert.isTrue(profesorId > 0);
		return profesorRepositorio.findOne(profesorId);
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void save(Profesor profesor) {
		Assert.notNull(profesor);
		Assert.isTrue(Valida.validaDNI(profesor.getDNI()), "pasarLista.error");
		Assert.isTrue(profesor.getApellidos().length() >= 4, "pasarLista.error");
		Assert.isTrue(profesor.getNombre().length() > 2, "pasarLista.error");
		profesorRepositorio.save(profesor);
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 */
	public void delete(Profesor profesor) {
		Assert.notNull(profesor);
		Assert.isTrue(profesor.getId() > 0);
		profesorRepositorio.delete(profesor);
	}

	// Otros métodos de negocio

	/**
	 * Obtener los cursos en los que imparte docencia un profesor
	 * 
	 * @author David Romero Alcaide
	 * @param profesor
	 * @return
	 */
	public Collection<InstanciaCurso> getCursosImparteDocencia(Profesor profesor) {
		Assert.notNull(profesor);
		Assert.isTrue(profesor.getId() > 0);
		Assert.isTrue(profesor.isIdentidadConfirmada());
		Collection<InstanciaCurso> cursos;
		cursos = profesorRepositorio.getCursosDondeImparteClase(profesor
				.getId());
		return cursos;
	}

	/**
	 * Obtiene la asignatura vinculada a un curso y a un profesor
	 * 
	 * @author David Romero Alcaide
	 * @param c
	 * @param p
	 * @return
	 */
	public Materia getAsignaturaCursoProfesor(InstanciaCurso c,
			Profesor profesor) {
		Assert.notNull(c);
		Assert.notNull(profesor, "pasarLista.profesor");
		Assert.isTrue(profesor.isIdentidadConfirmada());
		List<Materia> materias = profesor
				.getMaterias()
				.stream()
				.filter(materia -> {
					return materia.getCursoAcademico().equals(
							c.getCursoAcademico());
				}).collect(Collectors.toList());
		if (materias.size() > 0) {
			return materias.get(0);
		} else {
			return new Materia();
		}
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Profesor findPrincipal() {
		Profesor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public Profesor findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);

		Profesor result;

		result = profesorRepositorio.findByUserAccountId(userAccount.getId());

		Assert.notNull(result.getUserAccount());

		Assert.notEmpty(result.getUserAccount().getAuthorities());
		
		Assert.notEmpty(result.getCursos());
		
		result.getCursos().forEach(curso->{
			Assert.notEmpty(curso.getAlumnos());
		}); 

		return result;
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Alumno> getTodosLosAlumnosProfesor(Profesor profe) {
		List<Collection<Alumno>> alumnos = profe
				.getMaterias()
				.stream()
				.filter(materia -> {
					return materia.getCursoAcademico().equals(
							cursoAcademicoService.findActual());
				}).map(materia -> {
					return materia.getCurso().getAlumnos();
				}).collect(Collectors.toList());
		return Lists.newArrayList(Iterables.concat(alumnos));
	}

	/**
	 * @author David Romero Alcaide
	 * @param item
	 */
	public void guardarCalificacion(int idItem, double calificacion,
			String className) {

		if (className.equals(Examen.class.getSimpleName())) {
			Examen exam = examenService.findOne(idItem);
			exam.setCalificacion(calificacion);
			examenService.save(exam);
		} else if (className.equals(Trabajo.class.getSimpleName())) {
			Trabajo trabajo = trabajoService.findOne(idItem);
			trabajo.setCalificacion(calificacion);
			trabajoService.save(trabajo);
		} else if (className.equals(Actividad.class.getSimpleName())) {
			Actividad acti = actividadService.findOne(idItem);
			acti.setCalificacion(calificacion);
			actividadService.save(acti);
		} else if (className.equals(Ejercicios.class.getSimpleName())) {
			Ejercicios ejer = (Ejercicios) ejerciciosService.findOne(idItem);
			ejer.setCalificacion(calificacion);
			ejerciciosService.save(ejer);
		}
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public List<Alumno> getTodosLosAlumnosProfesorConTutor(Profesor profe) {
		return getTodosLosAlumnosProfesor(profe).stream().filter(alumno -> {
			return alumno.getPadresMadresOTutores().size() > 0;
		}).collect(Collectors.toList());
	}

	/**
	 * @author David Romero Alcaide
	 * @param c
	 * @return
	 */
	public List<Alumno> getTodosLosAlumnosProfesorConTutorEnCurso(
			InstanciaCurso c) {
		return c.getAlumnos().stream().filter(alumno -> {
			return alumno.getPadresMadresOTutores().size() > 0;
		}).collect(Collectors.toList());
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public List<Alumno> getTodosLosAlumnosProfesorConTutor(Profesor profe,
			PadreMadreOTutor tutor) {
		return getTodosLosAlumnosProfesorConTutor(profe).stream()
				.filter(alumno -> {
					return alumno.getPadresMadresOTutores().contains(tutor);
				}).collect(Collectors.toList());
	}

	/**
	 * @author David Romero Alcaide
	 * @param hash1
	 */
	public void updatePassword(String hash1) {
		Assert.notNull(hash1);
		Assert.isTrue(hash1.length() > 5);
		Profesor p = findPrincipal();
		Assert.notNull(p);
		p.getUserAccount().setPassword(hash1);
		save(p);
	}

	@Transactional
	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<ItemEvaluable> findAllItems(Profesor p) {
		try{
			beginTransaction(true);
			Assert.notNull(p);
			Assert.isTrue(p.isIdentidadConfirmada());
			Assert.notEmpty(p.getCursos());
			Iterable<ItemEvaluable> items = Iterables
					.concat(getTodosLosAlumnosProfesor(p).stream().map(alumno -> {
						Assert.notNull(alumno);
						Assert.notNull(alumno.getItemsEvaluables());
						return alumno.getItemsEvaluables();
					}).collect(Collectors.toList()));
			Assert.notNull(items);
			List<ItemEvaluable> itemsList = Lists.newArrayList(items);
			commitTransaction();
			return itemsList;
		}catch(Exception e){
			e.printStackTrace();
			rollbackTransaction();
			return Lists.newArrayList();
		}
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<ItemEvaluable> findAllFaltaSinJustificar(Materia materia) {

		return findAllItems(materia.getProfesor())
				.stream()
				.filter(item -> {
					return item instanceof FaltaDeAsistencia
							&& !((FaltaDeAsistencia) item).isJustificada();
				}).collect(Collectors.toList());
	}

	/**
	 * @author David Romero Alcaide
	 * @param dni
	 * @return
	 */
	public Profesor findByDNI(String dni) {
		Assert.isTrue(dni.length() == 9);
		Profesor p = profesorRepositorio.findByDNI(dni);
		Assert.notNull(p);
		return p;
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @param profe
	 * @return
	 */
	public Collection<ItemEvaluable> findAllItemsAlumnoProfesor(
			final Alumno alum, Profesor profe) {
		Assert.notNull(profe);
		Assert.isTrue(profe.isIdentidadConfirmada());
		Collection<ItemEvaluable> itemsProfesor = findAllItems(profe);
		Iterable<ItemEvaluable> itemsProfesorAlumno = Iterables.filter(
				itemsProfesor, new Predicate<ItemEvaluable>() {

					public boolean apply(ItemEvaluable input) {
						return checkItemEvaluablePerteneceAAlumno(input, alum);
					}

				});
		List<ItemEvaluable> itemsProfesorAlumnoList = Lists
				.newArrayList(itemsProfesorAlumno);
		return itemsProfesorAlumnoList;
	}

	private boolean checkItemEvaluablePerteneceAAlumno(ItemEvaluable item,
			Alumno alum) {
		return item.getAlumno().equals(alum);
	}

	public List<PadreMadreOTutor> getTutoresAlumnosPertenecientesProfesor(
			Profesor profesor) {
		List<PadreMadreOTutor> tutores = Lists.newArrayList();
		List<Alumno> alumnosProfesor = (List<Alumno>) getTodosLosAlumnosProfesor(profesor);
		for (Alumno alumno : alumnosProfesor) {
			if (alumno.getPadresMadresOTutores() != null
					&& alumno.getPadresMadresOTutores().size() > 0) {
				tutores.addAll(alumno.getPadresMadresOTutores());
			}
		}
		return tutores;
	}
	
	/**
	 * Begin a transaction with the database
	 * 
	 * @author David Romero Alcaide
	 * @param readOnly
	 */
	protected void beginTransaction(boolean readOnly) {
		assert txStatus == null;

		DefaultTransactionDefinition definition;

		definition = new DefaultTransactionDefinition();
		definition
				.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
		definition
				.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		definition.setReadOnly(readOnly);
		txStatus = transactionManager.getTransaction(definition);
	}
	
	/**
	 * Commit a transaction
	 * 
	 * @author David Romero Alcaide
	 * @throws Throwable
	 */
	protected void commitTransaction() throws TransactionException {
		assert txStatus != null;

		try {
			transactionManager.commit(txStatus);
			txStatus = null;
		} catch (TransactionException oops) {
			throw oops;
		}
	}

	/**
	 * Rollback a transaction
	 * 
	 * @author David Romero Alcaide
	 * @throws Throwable
	 */
	protected void rollbackTransaction() throws TransactionException {
		assert txStatus != null;
		try {
			if (!txStatus.isCompleted()) {
				transactionManager.rollback(txStatus);
			}
			txStatus = null;
		} catch (TransactionException oops) {
			throw oops;
		}
	}

}
