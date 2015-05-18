/**
 * AlumnoPresenter.java
 * guardians-dashboard
 * 1/5/2015 12:29:27
 * Copyright David
 * com.app.ui.user.alumno.presenter
 */
package com.app.ui.user.alumno.presenter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.app.applicationservices.services.AlumnoService;
import com.app.applicationservices.services.AsignaturaService;
import com.app.applicationservices.services.CursoAcademicoService;
import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.CursoAcademico;
import com.app.domain.model.types.InstanciaCurso;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.Profesor;
import com.app.domain.model.types.itemsevaluables.FaltaDeAsistencia;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Iterables;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class AlumnoPresenter {

	@Autowired
	private ProfesorService profesorService;
	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private AsignaturaService asignaturaService;
	@Autowired
	private CursoAcademicoService cursoAcademicoService;

	public Profesor getProfesor() {
		UserAccount account = AppUI.getCurrentUser();
		Profesor p = profesorService.findByUserAccount(account);
		return p;
	}

	public Collection<Alumno> getAlumnosProfesor() {
		return getAlumnosProfesor(cursoAcademicoService.findActual(), getProfesor());
	}

	public Collection<Alumno> getAlumnosProfesor(CursoAcademico c, Profesor p) {
		return Lists.newArrayList(Iterables.concat(p.getMaterias().stream().filter(materia->{
			return materia.getCursoAcademico().equals(c);
		}).map(materia->{
			return materia.getCurso().getAlumnos();
		}).collect(Collectors.toList())));
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Materia> getMateriasProfesor(CursoAcademico c, Profesor p) {
		return Lists.newArrayList(Iterables.concat(p.getMaterias().stream().filter(materia->{
			return materia.getCursoAcademico().equals(c);
		}).collect(Collectors.toList())));
	}

	public Collection<Materia> getMateriasProfesor() {
		return getMateriasProfesor(cursoAcademicoService.findActual(),getProfesor());
	}

	/**
	 * @author David
	 * @return
	 */
	public boolean existenMasAlumnos() {
		return false;
	}

	/**
	 * @author David
	 * @param id
	 * @return
	 */
	public Alumno getAlumno(Integer id) {
		Assert.notNull(id);
		Assert.isTrue(id > 0);
		return alumnoService.findOne(id);
	}

	/**
	 * @author David
	 * @param alum
	 * @return
	 */
	public Optional<InstanciaCurso> getCursoAlumno(Alumno alum) {
		CursoAcademico cursoAcademico = cursoAcademicoService.findActual();
		return alum.getCursos().stream().filter(instanciaCurso->{
			return instanciaCurso.getCursoAcademico().equals(cursoAcademico);
		}).findAny();
	}

	/**
	 * @author David
	 * @param alum
	 * @return
	 */
	public List<FaltaDeAsistencia> getFaltasAsistenciaAlumno(Alumno alum) {
		CursoAcademico cursoAcademico = cursoAcademicoService.findActual();
		return alum.getItemsEvaluables().stream().filter(item->{
			return item instanceof FaltaDeAsistencia && item.getMateria().getCursoAcademico().equals(cursoAcademico);
		}).map(item->{
			return (FaltaDeAsistencia) item;
		}).collect(Collectors.toList());
	}

}
