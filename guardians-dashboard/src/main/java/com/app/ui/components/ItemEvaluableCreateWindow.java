/**
 * NotificacionCreateWindow.java
 * appEducacionalVaadin
 * 24/1/2015 0:18:38
 * Copyright David
 * com.app.ui.components
 */
package com.app.ui.components;

import com.app.domain.model.types.ItemEvaluable;
import com.app.ui.user.calificaciones.presenter.ItemsEvaluablesPresenter;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author David
 *
 */
public class ItemEvaluableCreateWindow extends Window {

	@PropertyId("titulo")
	private TextField titulo;
	@PropertyId("fecha")
	private DateField fecha;
	@PropertyId("contenido")
	private RichTextArea contenido;
	@PropertyId("alumno")
	private ComboBox comboAlumno;
	@PropertyId("asignatura")
	private ComboBox comboAsignatura;
	
	private ComboBox tipo;
	
	private ItemEvaluable item;
	
	private ItemsEvaluablesPresenter presenter;

	private final BeanFieldGroup<ItemEvaluable> fieldGroup;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1299573468494488377L;

	public ItemEvaluableCreateWindow(ItemEvaluable item,ItemsEvaluablesPresenter presenter) {
		this.item = item;
		this.presenter = presenter;
		Responsive.makeResponsive(this);

		

		setModal(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setHeight(80f, Unit.PERCENTAGE);
		setWidth(60, Unit.PERCENTAGE);
		VerticalLayout content = new VerticalLayout();
		content.addComponent(buildForm());
		content.addComponent(buildFooter());
		setContent(content);
		
		fieldGroup = new BeanFieldGroup<ItemEvaluable>(ItemEvaluable.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(item);
		fieldGroup.setBuffered(true);
		
	}

	/**
	 * @author David
	 * @return
	 */
	private Component buildFooter() {
		HorizontalLayout footer = new HorizontalLayout();
		footer.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		footer.setWidth(100.0f, Unit.PERCENTAGE);

		Button ok = new Button("OK");
		ok.addStyleName(ValoTheme.BUTTON_PRIMARY);
		ok.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -296792393671130920L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					fieldGroup.commit();

					String clazz = tipo.getValue().toString();
					
					
					presenter.save(item,clazz);

					Notification success = new Notification(
							clazz + " guardado satisfactoriamente");
					success.setDelayMsec(2000);
					success.setStyleName("bar success small");
					success.setPosition(Position.BOTTOM_CENTER);
					success.show(Page.getCurrent());

					close();
				} catch (CommitException e) {
					Notification.show("Error while updating profile",
							Type.ERROR_MESSAGE);
				}

			}
		});
		ok.focus();

		Button discard = new Button("Cancelar");
		discard.addStyleName(ValoTheme.BUTTON_PRIMARY);
		discard.addClickListener(new ClickListener() {
			/**
			 * 
			 */
			private static final long serialVersionUID = -2506358446761353801L;

			@Override
			public void buttonClick(ClickEvent event) {
				fieldGroup.discard();

				close();

			}
		});
		HorizontalLayout footersButtons = new HorizontalLayout();
		footersButtons.addComponent(ok);
		footersButtons.addComponent(discard);
		footersButtons.setSpacing(true);
		footer.addComponent(footersButtons);
		footer.setComponentAlignment(footersButtons, Alignment.TOP_RIGHT);
		return footer;
	}

	/**
	 * @author David
	 */
	private Component buildForm() {
		VerticalLayout root = new VerticalLayout();
		setIcon(FontAwesome.SEND);
		setCaption("Enviar Notificación");
		root.setSpacing(true);
		root.setSizeFull();
		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);
		Container containerTipos = presenter.getContainerTiposItems();
		tipo = new ComboBox("Tipo", containerTipos);
		tipo.setNullSelectionAllowed(false);
		tipo.setValue(Lists.newArrayList(containerTipos.getItemIds()).get(0));
		tipo.addValueChangeListener(new ValueChangeListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 8102139766913712976L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				String clazz = event.getProperty().getValue().toString();
				try {
					item = presenter.create(clazz);
				} catch (InstantiationException | IllegalAccessException
						| ClassNotFoundException e) {
					
					e.printStackTrace();
				}
				fieldGroup.setItemDataSource(item);
			}
		});
		details.addComponent(tipo);
		titulo = new TextField("Título");
		details.addComponent(titulo);
		fecha = new DateField("Fecha del Item");
		details.addComponent(fecha);

		IndexedContainer container =  
				presenter.getContainerAlumnos();
		
		IndexedContainer container2 =  
				presenter.getContainerAsignaturas();
		
		comboAlumno = new ComboBox("Alumno", container);
		comboAlumno.addStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
		comboAlumno.addStyleName(ValoTheme.COMBOBOX_LARGE);
			
		details.addComponent(comboAlumno);
		
		comboAsignatura = new ComboBox("Asignatura", container2);
		comboAsignatura.addStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
		comboAsignatura.addStyleName(ValoTheme.COMBOBOX_LARGE);
			
		details.addComponent(comboAsignatura);
		
		return root;
	}

}
