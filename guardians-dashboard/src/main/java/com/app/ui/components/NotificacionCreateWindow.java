/**
 * NotificacionCreateWindow.java
 * appEducacionalVaadin
 * 24/1/2015 0:18:38
 * Copyright David
 * com.app.ui.components
 */
package com.app.ui.components;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Notificacion;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.ui.user.notificaciones.presenter.NotificacionesPresenter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class NotificacionCreateWindow extends Window {

	@PropertyId("titulo")
	private TextField titulo;
	@PropertyId("contenido")
	private RichTextArea contenido;
	@PropertyId("padreMadreOTutor")
	private ComboBox combo;
	
	private Notificacion notificacion;
	//DRA - No es Autowired porque se produce un ciclo
	private NotificacionesPresenter presenter;

	private BeanFieldGroup<Notificacion> fieldGroup;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1299573468494488377L;
	
	
	private static final Logger log = Logger.getLogger(NotificacionCreateWindow.class);

	public NotificacionCreateWindow() {		
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
		ok.addClickListener(e->{
			try {
				save();
			} catch (Exception e1) {
				log.error("Error guardando la notificacion",e1);
				Notification.show("Error guardando la notificación",
						Type.ERROR_MESSAGE);
			}
		});
		ok.focus();

		Button discard = new Button("Cancelar");
		discard.addStyleName(ValoTheme.BUTTON_PRIMARY);
		discard.addClickListener(e->{
			fieldGroup.discard();
			close();
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

		titulo = new TextField("Título");
		details.addComponent(titulo);
		contenido = new RichTextArea("Contenido");
		contenido.setSizeFull();

		IndexedContainer container =  
				presenter.getContainer(PadreMadreOTutor.class);
		
		combo = new ComboBox("Tutor", container);
		combo.addStyleName(ValoTheme.COMBOBOX_ALIGN_CENTER);
		combo.addStyleName(ValoTheme.COMBOBOX_LARGE);
			
		details.addComponent(combo);
		
		details.addComponent(contenido);
		return root;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.AbstractComponent#attach()
	 */
	@Override
	public void attach() {
		super.attach();
		notificacion = presenter.create();
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
		
		fieldGroup = new BeanFieldGroup<Notificacion>(Notificacion.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(notificacion);
		fieldGroup.setBuffered(true);
	}

	/**
	 * @author David
	 * @throws CommitException
	 */
	private void save() throws CommitException {
		fieldGroup.commit();

		presenter.save(notificacion,com.app.domain.model.types.Profesor.class);

		Notification success = new Notification(
				"Notificación enviada correctamente");
		success.setDelayMsec(2000);
		success.setStyleName("bar success small");
		success.setPosition(Position.BOTTOM_CENTER);
		success.show(Page.getCurrent());

		close();
	}

	/**
	 * @param presenter the presenter to set
	 * Establecer el presenter
	 */
	public void setPresenter(NotificacionesPresenter presenter) {
		this.presenter = presenter;
	}
	
	

}
