/**
 * @author David
 * @copyright David Romero Alcaide
 * 03/01/2014 17:30
 */
package com.app.domain.model.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import junit.framework.Assert;

import org.hibernate.validator.constraints.Range;

import com.app.domain.model.DomainEntity;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * 
 */
public class Evaluacion extends DomainEntity {

	/**
	 * Constructor vacio
	 */
	public Evaluacion() {
		super();
		this.itemsEvaluable = new ArrayList<ItemEvaluable>();
	}

	/**
	 * fecha inicio de la evaluación
	 */
	private Date inicio;
	/**
	 * fecha fin de la evaluación
	 */
	private Date fin;

	/**
	 * Indica en cual de las 3 evaluaciones nos encontramos
	 */
	private int indicador;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	/**
	 * @return the inicio
	 */
	public Date getInicio() {
		return inicio;
	}

	/**
	 * @param inicio
	 *            the inicio to set
	 */
	public void setInicio(Date inicio) {
		this.inicio = new Date(inicio.getTime());
	}

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	/**
	 * @return the fin
	 */
	public Date getFin() {
		return fin;
	}

	/**
	 * @param fin
	 *            the fin to set
	 */
	public void setFin(Date fin) {
		Assert.assertNotNull(fin);
		/*
		 * Fin debe ser mayor que inicio, Establecer regla de modelo de dominio
		 */
		this.fin = new Date(fin.getTime());
		
	}

	@Range(min = 1, max = 3)
	/**
	 * @return the indicador
	 */
	public int getIndicador() {
		return indicador;
	}

	/**
	 * @param indicador
	 *            the indicador to set
	 */
	public void setIndicador(int indicador) {
		this.indicador = indicador;
	}

	// Relaciones

	/**
	 * Items evaluables pertenecientes a una evaluación
	 */
	private Collection<ItemEvaluable> itemsEvaluable;

	@Valid
	@NotNull
	@OneToMany(mappedBy = "evaluacion", fetch = FetchType.EAGER)
	/**
	 * @return the itemsEvaluable
	 */
	public Collection<ItemEvaluable> getItemsEvaluable() {
		return itemsEvaluable;
	}

	/**
	 * @param itemsEvaluable
	 *            the itemsEvaluable to set
	 */
	public void setItemsEvaluable(Collection<ItemEvaluable> itemsEvaluable) {
		this.itemsEvaluable = itemsEvaluable;
	}

	/**
	 * Añade un item a una evaluación.
	 * 
	 * @author David Romero Alcaide
	 * @param item
	 */
	public void addItemEvaluable(ItemEvaluable item) {
		this.itemsEvaluable.add(item);
		item.setEvaluacion(this);
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
		result = prime * result + ((fin == null) ? 0 : fin.hashCode());
		result = prime * result + indicador;
		result = prime * result + ((inicio == null) ? 0 : inicio.hashCode());
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
		Evaluacion other = (Evaluacion) obj;
		if (fin == null) {
			if (other.fin != null) {
				return false;
			}
		} else if (!fin.equals(other.fin)) {
			return false;
		}
		if (indicador != other.indicador) {
			return false;
		}
		if (inicio == null) {
			if (other.inicio != null) {
				return false;
			}
		} else if (!inicio.equals(other.inicio)) {
			return false;
		}
		return true;
	}

}
