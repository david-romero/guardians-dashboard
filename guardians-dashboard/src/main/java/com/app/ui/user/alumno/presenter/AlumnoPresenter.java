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
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.applicationservices.services.AlumnoService;
import com.app.applicationservices.services.AsignaturaService;
import com.app.applicationservices.services.CursoAcademicoService;
import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.CursoAcademico;
import com.app.domain.model.types.Profesor;
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

	/**
	 * @author David
	 * @return
	 */
	public boolean existenMasAlumnos() {
		return false;
	}

}
