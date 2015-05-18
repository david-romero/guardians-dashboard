/**
* ProfesorPresenter.java
* guardians-dashboard
* 18/4/2015 17:49:21
* Copyright David
* com.app.ui.user.profesor.view
*/
package com.app.ui.user.profesor.presenter;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.app.applicationservices.services.AlumnoService;
import com.app.applicationservices.services.AsignaturaService;
import com.app.applicationservices.services.CursoAcademicoService;
import com.app.applicationservices.services.PersonaService;
import com.app.applicationservices.services.ProfesorService;
import com.app.applicationservices.services.EventoService;
import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.CursoAcademico;
import com.app.domain.model.types.Evento;
import com.app.domain.model.types.InstanciaCurso;
import com.app.domain.model.types.Materia;
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
public class ProfesorPresenter {

	
	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private PersonaService personaService;
	
	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private AsignaturaService asignaturaService;
	@Autowired
	private CursoAcademicoService cursoAcademicoService;
	@Autowired
	private EventoService eventoService;

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


	public Profesor getProfesor() {
		UserAccount account = AppUI.getCurrentUser();
		com.app.domain.model.types.Profesor p = profesorService
				.findByUserAccount(account);
		return p;
	}

	/**
	 * @author David
	 * @return
	 */
	public Collection<InstanciaCurso> getCursosProfesor() {
		return getCursosProfesor(cursoAcademicoService.findActual(), getProfesor());
	}
	
	/**
	 * @author David
	 * @return
	 */
	public Collection<InstanciaCurso> getCursosProfesor(CursoAcademico c, Profesor p) {
		return p.getMaterias().stream().filter(materia->{
			return materia.getCursoAcademico().equals(c);
		}).map(materia->{
			return materia.getCurso();
		}).collect(Collectors.toList());
	}

	/**
	 * @author David
	 * @param curso
	 * @return
	 */
	public Collection<Evento> getEventos(InstanciaCurso curso) {
		Profesor profe = getProfesor();
		Assert.isTrue(profe.equals(curso.getProfesor()));
		Set<Materia> materias = profe.getMaterias().stream().filter(materia->{
			return materia.getCurso().equals(curso);
		}).collect(Collectors.toSet());
		Assert.isTrue(materias.size()==1);
		
		return eventoService.findAllProfesorMateria(materias.stream().findFirst());
	}
	
	
	
}
