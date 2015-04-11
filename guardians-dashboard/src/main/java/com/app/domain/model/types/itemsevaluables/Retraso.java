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
public class Retraso extends ItemEvaluable {


	
	/**
	 * Constructor
	 */
	public Retraso() {
		super();
		
	}

	/**
	 * 
	 */
	private boolean justificado;

	/**
	 * @return the justificada
	 */
	public boolean isJustificado() {
		return justificado;
	}

	/**
	 * @param justificada the justificada to set
	 */
	public void setJustificado(boolean justificado) {
		this.justificado = justificado;
	}

	

}
