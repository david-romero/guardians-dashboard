/**
 * TablaItemsEvaluables.java
 * appEducacionalVaadin
 * 20/3/2015 17:29:00
 * Copyright David
 * com.app.ui.components
 */
package com.app.ui.components;

import com.app.domain.model.types.NotaPorEvaluacion;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.data.Container;
import com.vaadin.data.validator.DoubleRangeValidator;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Table;
import com.vaadin.ui.TableFieldFactory;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author David
 *
 */
public class TablaNotasPorEvaluacion extends Table {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3196656315566638918L;

	public TablaNotasPorEvaluacion() {

	}

	public TablaNotasPorEvaluacion(JPAContainer<NotaPorEvaluacion> container) {
		setCaption("Notas por Evaluación");
		setContainerDataSource(container);
		setVisibleColumns("evaluacion", "notaFinal");
		setColumnHeaders("Evaluación", "Calificación");
		setColumnExpandRatio("evaluacion", 2);
		setColumnExpandRatio("notaFinal", 1);
		setColumnAlignment("notaFinal", Align.RIGHT);
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
				if ("notaFinal".equals(propertyId)){
					NotaPorEvaluacion item = (NotaPorEvaluacion) container.getContainerProperty(itemId, propertyId).getValue();
					TextField field = new TextField(container.getContainerProperty(itemId, propertyId));
					field.setValue(item.getNotaFinal()+"");
					field.addValidator(new DoubleRangeValidator("Calificación no válida", 0.0, Double.MAX_VALUE));
					field.setMaxLength(5);
					return field;
				}
				return null;
			}
		});
	}

}
