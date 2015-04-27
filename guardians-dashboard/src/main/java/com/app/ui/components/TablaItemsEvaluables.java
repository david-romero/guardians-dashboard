/**
 * TablaItemsEvaluables.java
 * appEducacionalVaadin
 * 20/3/2015 17:29:00
 * Copyright David
 * com.app.ui.components
 */
package com.app.ui.components;

import java.util.Arrays;

import com.app.domain.model.types.ItemEvaluable;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author David
 *
 */
public class TablaItemsEvaluables extends Table {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3196656315566638918L;

	public TablaItemsEvaluables() {

	}

	public TablaItemsEvaluables(JPAContainer<ItemEvaluable> container) {
		setCaption("Items Evaluable");
		setContainerDataSource(container);
		setVisibleColumns("titulo", "calificacion");
		setColumnHeaders("Titulo", "Calificación");
		setColumnExpandRatio("titulo", 2);
		setColumnExpandRatio("calificacion", 1);
		setColumnAlignment("calificacion", Align.RIGHT);
		setSortContainerPropertyId("titulo");
		setSizeFull();
		addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        addStyleName(ValoTheme.TABLE_SMALL);
        setTableFieldFactory(new TableFieldFactory() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -4378979820492945926L;

			@Override
			public Field<?> createField(Container container, Object itemId,
					Object propertyId, Component uiContext) {
				if ("calificacion".equals(propertyId)){
					ItemEvaluable item = (ItemEvaluable) container.getContainerProperty(itemId, propertyId).getValue();
					TextField field = new TextField(container.getContainerProperty(itemId, propertyId));
					field.setValue(item.getCalificacion()+"");
					field.addValidator(new DoubleRangeValidator("Calificación no válida", 0.0, Double.MAX_VALUE));
					field.setMaxLength(5);
					return field;
				}
				return null;
			}
		});
	}

}
