/**
* CitaLayout.java
* appEducacionalVaadin
* 30/3/2015 17:47:31
* Copyright David
* com.app.ui.user.citas.view
*/
package com.app.ui.user.citas.view;

import java.util.Date;
import java.util.Iterator;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.Responsive;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author David
 *
 */
public class EventsLayout extends VerticalLayout {

	@PropertyId("title")
	private TextField titulo;
	@PropertyId("description")
	private RichTextArea contenido;
	@PropertyId("date")
	private DateField datepicker;
	
	private com.app.utility.Event event;
	
	protected HorizontalLayout footer;

	private final BeanFieldGroup<com.app.utility.Event> fieldGroup;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1299573468494488377L;

	public EventsLayout(com.app.utility.Event event) {
		this.event = event;
		Responsive.makeResponsive(this);

		setSizeFull();
		VerticalLayout content = new VerticalLayout();
		content.addComponent(buildForm());
		
		addComponent(content);
		
		fieldGroup = new BeanFieldGroup<com.app.utility.Event>(com.app.utility.Event.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(event);
		fieldGroup.setBuffered(true);
		
	}

	
	/**
	 * @author David
	 */
	private Component buildForm() {
		VerticalLayout root = new VerticalLayout();
		root.setSpacing(true);
		root.setMargin(true);
		root.setSizeFull();
		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);

		titulo = new TextField("Título");
		details.addComponent(titulo);
		datepicker = new DateField("Fecha", event.getDate());
		datepicker.setSizeFull();
		datepicker.setRangeStart(new Date());
		datepicker.setResolution(Resolution.MINUTE);
		datepicker.setDateFormat("dd/MM/yyyy HH:mm");
		details.addComponent(datepicker);
		contenido = new RichTextArea("Contenido");
		contenido.setSizeFull();
		
		details.addComponent(contenido);
		return root;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.AbstractComponent#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		setReadOnly(iterator(),readOnly);
	}
	
	public void setReadOnly(Iterator<Component> componentes,boolean readOnly){
		while (componentes.hasNext()){
			Component component = componentes.next();
			component.setReadOnly(readOnly);
			if ( component instanceof AbstractOrderedLayout && ( (AbstractOrderedLayout) component ).getComponentCount() > 0 ){
				setReadOnly(( (AbstractOrderedLayout) component ).iterator(), readOnly);
			}
		}
	}

	/**
	 * @return footer
	 */
	public HorizontalLayout getFooter() {
		return footer;
	}
	
}

