/**
* ItemEvaluableComparator.java
* guardians-dashboard
* 16/4/2015 20:31:44
* Copyright David
* com.app.domain.model.types.util
*/
package com.app.domain.model.types.util;

import java.util.Comparator;

import com.app.domain.model.types.ItemEvaluable;

/**
 * @author David
 *
 */
public class ItemEvaluableComparator implements Comparator<ItemEvaluable>{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(ItemEvaluable o1, ItemEvaluable o2) {
		return o1.getFecha().compareTo(o2.getFecha());
	}

}
