/**
 * Curso.java
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 17:00
 */
package com.app.domain.model.types;

/**
 * imports
 */
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springframework.util.Assert;

import com.app.domain.model.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * Clase que representa a un curso determinado
 * Ej: 3 E.S.O. B
 */
public class Curso extends DomainEntity {

	/**
	 * Constructor para inicializar coleciones
	 */
	public Curso() {
		super();
		this.alumnos = new ArrayList<Alumno>();
		this.asignaturas = new ArrayList<Asignatura>();
	}

	/**
	 * cardinal que identifica al curso dentro de los niveles educativos Del 1
	 * al 6 para primaria Del 1 al 4 para E.S.O. Del 1 al 2 para Bachiller
	 */
	private int nivel;
	/**
	 * Cadena de texto que identifica el nivel educativo en el que se encuentra
	 * E.S.O. o Bachiller o Primaria
	 */
	private String nivelEducativo;

	/**
	 * Letra que diferencia a dos cursos con mismo nivel académico y nivel
	 */
	private char identificador;

	@Range(min = 1, max = 6)
	/**
	 * @return nivel
	 */
	public int getNivel() {
		return nivel;
	}

	/**
	 * @param nivel
	 *            the nivel to set establece el nivel
	 */
	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	@NotBlank
	/**
	 * @return the nivelAcademico Si pertenece a la ESO, nivel hasta 4 Si
	 *         pertenece a Bachiller, nivel hasta 2 Tres casos posibles
	 *         Primaria, Eso o Bachiller
	 */
	public String getNivelEducativo() {
		return nivelEducativo;
	}

	/**
	 * @param nivelAcademico
	 *            the nivelAcademico to set
	 */
	public void setNivelEducativo(String nivelEducativo) {
		this.nivelEducativo = nivelEducativo;
	}

	/**
	 * @return the identificador
	 */
	public char getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador
	 *            the identificador to set
	 */
	public void setIdentificador(char identificador) {
		this.identificador = identificador;
	}

	// Relaciones

	/**
	 * Curso academico al cual pertenece el alumno
	 */
	private CursoAcademico cursoAcademico;

	/**
	 * alumnos adscritos en este curso
	 */
	private Collection<Alumno> alumnos;

	/**
	 * Asignaturas vinculadas a este curso
	 */
	private Collection<Asignatura> asignaturas;

	@NotNull
	@Valid
	@ManyToOne()
	/**
	 * @return cursoAcademico
	 */
	public CursoAcademico getCursoAcademico() {
		return cursoAcademico;
	}

	/**
	 * @param cursoAcademico
	 *            the cursoAcademico to set Establecer el cursoAcademico
	 */
	public void setCursoAcademico(CursoAcademico cursoAcademico) {
		this.cursoAcademico = cursoAcademico;
	}

	@NotNull
	@OneToMany(mappedBy = "curso")
	/**
	 * @return the alumnos
	 */
	public Collection<Alumno> getAlumnos() {
		return alumnos;
	}

	/**
	 * @param alumnos
	 *            the alumnos to set
	 */
	public void setAlumnos(Collection<Alumno> alumnos) {
		this.alumnos = alumnos;
	}

	/**
	 * Añadir un alumno al curso
	 * 
	 * @author David Romero Alcaide
	 * @param alumno
	 */
	public void addAlumno(Alumno alumno) {
		Assert.isTrue(this.nivel > 0 && this.nivelEducativo.length() > 0
				&& this.identificador != ' ');
		Assert.isTrue(!(this.alumnos.contains(alumno)));
		this.alumnos.add(alumno);
		alumno.setCurso(this);
	}

	/**
	 * Eliminar al alumno recibido de un curso
	 * 
	 * @author David Romero Alcaide
	 * @param alumno
	 * @return
	 */
	public boolean removeAlumno(Alumno alumno) {
		Assert.isTrue(this.alumnos.contains(alumno));
		alumno.setCurso(null);
		return this.alumnos.remove(alumno);
	}

	@NotNull
	@OneToMany(mappedBy = "curso")
	/**
	 * @return the asignaturas
	 * Debería ser un collection que no se pudiera modificar
	 * pero con la tecnología actual es imposible
	 */
	public Collection<Asignatura> getAsignaturas() {
		return asignaturas;
	}

	/**
	 * @param asignaturas
	 *            the asignaturas to set
	 */
	public void setAsignaturas(Collection<Asignatura> asignaturas) {
		this.asignaturas = asignaturas;
	}

	/**
	 * Añade una asignatura al curso
	 * 
	 * @author David Romero Alcaide
	 * @param asignatura
	 */
	public void addAsignatura(Asignatura asignatura) {
		this.asignaturas.add(asignatura);
	}

	/**
	 * Elimina una asignatura
	 * 
	 * @author David Romero Alcaide
	 * @param asignatura
	 * @return
	 */
	public boolean removeAsignatura(Asignatura asignatura) {
		Assert.isTrue(this.asignaturas.contains(asignatura));
		asignatura.setCurso(null);
		return this.asignaturas.remove(asignatura);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return nivel + " " + nivelEducativo + " " + identificador;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + identificador;
		result = prime * result + nivel;
		result = prime * result
				+ ((nivelEducativo == null) ? 0 : nivelEducativo.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Curso other = (Curso) obj;
		if (identificador != other.identificador) {
			return false;
		}
		if (nivel != other.nivel) {
			return false;
		}
		if (nivelEducativo == null) {
			if (other.nivelEducativo != null) {
				return false;
			}
		} else if (!nivelEducativo.equals(other.nivelEducativo)) {
			return false;
		}
		return true;
	}

}
