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
public class Examen extends ItemEvaluable {


	
	/**
	 * Constructor
	 */
	public Examen() {
		super();
		
	}

	

	
}
