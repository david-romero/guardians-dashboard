/**
*
*/
package com.app.domain.model.types.itemsevaluables;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;

import com.app.domain.model.types.ItemEvaluable;


@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 * 
 */
public class FaltaDeAsistencia extends ItemEvaluable {


	
	/**
	 * Constructor
	 */
	public FaltaDeAsistencia() {
		super();
		
	}

	/**
	 * 
	 */
	private boolean justificada;

	/**
	 * @return the justificada
	 */
	public boolean isJustificada() {
		return justificada;
	}

	/**
	 * @param justificada the justificada to set
	 */
	public void setJustificada(boolean justificada) {
		this.justificada = justificada;
	}

	

}
