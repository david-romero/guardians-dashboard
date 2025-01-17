/**
 * Alumno.java
 * @author David Romero Alcaide
 * @copyright David Romero Alcaide
 * 03/01/2014 16:00
 */
package com.app.domain.model.types;

/**
 * imports
 */
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.hibernate.annotations.SortComparator;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.Assert;

import com.app.domain.model.DomainEntity;
import com.app.domain.model.types.util.ItemEvaluableComparator;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.collect.Sets;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * Clase que representa al alumno dentro del dominio.
 */
public class Alumno extends DomainEntity {

	/**
	 * Constructo de serie para inicializar las colecciones.
	 */
	public Alumno() {
		super();
		this.itemsEvaluables = Lists.newArrayList();
		this.padresMadresOTutores = Lists.newArrayList();
		this.notasPorEvaluacion = Lists.newArrayList();
		this.matriculas = Sets.newHashSet();
	}

	// Atributos

	/**
	 * Nombre del alumno
	 */
	private String nombre;
	/**
	 * Apellidos del alumno
	 */
	private String apellidos;
	/**
	 * Atributo que representa los cursos que ha repetido el alumno
	 */
	private String repiteCurso;
	/**
	 * Atributo que representa la o las asignaturas pendientes que pueda tener
	 * un alumno.
	 */
	private String pendiente;
	/**
	 * imagen del alumno
	 */
	private byte[] imagen;

	private Date fechaNacimiento;

	@Lob
	@Column(name = "imagen")
	/**
	 * @return imagen
	 */
	public byte[] getImagen() {
		if (this.imagen != null) {
			return imagen.clone();
		} else {
			return new byte[0];
		}
	}

	/**
	 * @param imagen
	 *            the imagen to set Establecer el imagen
	 */
	public void setImagen(final byte[] imagen) {
		if (imagen == null) {
			this.imagen = new byte[0];
		} else {
			this.imagen = Arrays.copyOf(imagen, imagen.length);
		}
	}

	/**
	 * @return la o las asignaturas pendientes
	 */
	public String getPendiente() {
		return pendiente;
	}

	/**
	 * @param la
	 *            o las asignaturas pendientes the pendiente subjects to set
	 */
	public void setPendiente(String pendiente) {
		this.pendiente = pendiente;
	}

	@NotBlank
	/**
	 * @return nombre del alumno
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            establecer el nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@NotBlank
	/**
	 * @return apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos
	 *            establecer los apellidos the apellidos to set
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return repiteCurso
	 */
	public String getRepiteCurso() {
		return repiteCurso;
	}

	/**
	 * @param repiteCurso
	 *            establecer si ha repetido curso the repiteCurso to set
	 */
	public void setRepiteCurso(String repiteCurso) {
		this.repiteCurso = repiteCurso;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Past
	/**
	 * @return fechaNacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento
	 *            the fechaNacimiento to set Establecer el fechaNacimiento
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	// Relaciones

	/**
	 * tutores del alumno
	 */
	private Collection<PadreMadreOTutor> padresMadresOTutores;
	/**
	 * Días del calendario lectivo del curso. Modificado para poder ordenarlo
	 * con eficiencia
	 */
	private List<ItemEvaluable> itemsEvaluables;
	/**
	 * Notas por evaluación y asignatura del alumno
	 */
	private Collection<NotaPorEvaluacion> notasPorEvaluacion;
	/**
	 * Notas por evaluación y asignatura del alumno
	 */
	private Collection<Matricula> matriculas;
	/**
	 * Curso al que pertenece el alumno
	 */
	private Collection<InstanciaCurso> cursos;

	@NotNull
	@ManyToMany(cascade = { CascadeType.PERSIST })
	@Size(min = 0, max = 2)
	/**
	 * @return the padresMadresOTutores
	 */
	public Collection<PadreMadreOTutor> getPadresMadresOTutores() {
		return padresMadresOTutores;
	}

	/**
	 * @param padresMadresOTutores
	 *            the padresMadresOTutores to set Se establece como máximo dos
	 *            tutores como regla de dominio.
	 */
	public void setPadresMadresOTutores(
			Collection<PadreMadreOTutor> padresMadresOTutores) {
		this.padresMadresOTutores = padresMadresOTutores;
	}
	
	@Valid
	@NotNull
	@ManyToMany()
	/**
	 * @return matriculas
	 */
	public Collection<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	 * @param matriculas the matriculas to set
	 * Establecer el matriculas
	 */
	public void setMatriculas(Collection<Matricula> matriculas) {
		this.matriculas = matriculas;
	}

	/**
	 * Añade un padre, madre o tutor
	 * 
	 * @author David Romero Alcaide
	 * @param padreMadreOTutor
	 */
	public void addPadreMadreOTutor(PadreMadreOTutor padreMadreOTutor) {
		Assert.isTrue(!padreMadreOTutor.getTutorandos().contains(this));
		Assert.isTrue(this.padresMadresOTutores.size() < 2);
		this.padresMadresOTutores.add(padreMadreOTutor);
	}

	/**
	 * Elimina a un padre, madre o tutor
	 * 
	 * @author David Romero Alcaide
	 * @param padreMadreOTutor
	 * @return
	 */
	public boolean removePadreMadreOTutor(PadreMadreOTutor padreMadreOTutor) {
		return this.padresMadresOTutores.remove(padreMadreOTutor);
	}

	@Valid
	@NotNull
	@ManyToMany
	/**
	 * @return the curso
	 */
	public Collection<InstanciaCurso> getCursos() {
		return cursos;
	}

	/**
	 * @param curso
	 *            the curso to set
	 */
	public void setCursos(Collection<InstanciaCurso> cursos) {
		this.cursos = cursos;
	}


	@NotNull
	@OneToMany(mappedBy = "alumno", cascade = { CascadeType.ALL })
	@SortComparator(value = ItemEvaluableComparator.class)
	/**
	 * Debería devolver una collection que no se pudise modificar pero
	 * con la tecnología actual es imposible
	 * @return the diasDelCalendario
	 */
	public List<ItemEvaluable> getItemsEvaluables() {
		return this.itemsEvaluables;
	}

	/**
	 * @param diasDelCalendario
	 *            the diasDelCalendario to set
	 */
	public void setItemsEvaluables(
			List<ItemEvaluable> itemsEvaluables) {
		this.itemsEvaluables = itemsEvaluables;
	}


	@NotNull
	@OneToMany(mappedBy = "alumno")
	/**
	 * @return the notasPorEvaluacion
	 */
	public Collection<NotaPorEvaluacion> getNotasPorEvaluacion() {
		return notasPorEvaluacion;
	}

	/**
	 * @param notasPorEvaluacion
	 *            the notasPorEvaluacion to set
	 */
	public void setNotasPorEvaluacion(
			Collection<NotaPorEvaluacion> notasPorEvaluacion) {
		this.notasPorEvaluacion = notasPorEvaluacion;
	}

	/**
	 * Añade una nota por evaluación
	 * 
	 * @author David Romero Alcaide
	 * @param notaPorEvaluacion
	 */
	public void addNotaPorEvaluacion(NotaPorEvaluacion notaPorEvaluacion) {
		Assert.notNull(notaPorEvaluacion);
		Assert.isTrue(this.getCursos().contains(notaPorEvaluacion.getMateria().getCurso()));
		this.notasPorEvaluacion.add(notaPorEvaluacion);
		notaPorEvaluacion.setAlumno(this);
	}

	/**
	 * Elimina una nota por evaluación
	 * 
	 * @author David Romero Alcaide
	 * @param notaPorEvaluacion
	 * @return
	 */
	public boolean removeNotaPorEvaluacion(NotaPorEvaluacion notaPorEvaluacion) {
		notaPorEvaluacion.setAlumno(null);
		return this.notasPorEvaluacion.remove(notaPorEvaluacion);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (nombre != null ? "" + nombre + " " : "")
				+ (apellidos != null ? "" + apellidos + ", " : "");
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result
				+ ((fechaNacimiento == null) ? 0 : fechaNacimiento.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	/* (non-Javadoc)
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
		if (!(obj instanceof Alumno)) {
			return false;
		}
		Alumno other = (Alumno) obj;
		if (apellidos == null) {
			if (other.apellidos != null) {
				return false;
			}
		} else if (!apellidos.equals(other.apellidos)) {
			return false;
		}
		if (fechaNacimiento == null) {
			if (other.fechaNacimiento != null) {
				return false;
			}
		} else if (!fechaNacimiento.equals(other.fechaNacimiento)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		return true;
	}

}
