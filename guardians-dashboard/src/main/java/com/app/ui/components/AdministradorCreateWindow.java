/**
 * NotificacionCreateWindow.java
 * appEducacionalVaadin
 * 24/1/2015 0:18:38
 * Copyright David
 * com.app.ui.components
 */
package com.app.ui.components;

import org.apache.log4j.Logger;

import com.app.domain.model.types.Administrador;
import com.app.ui.user.admin.presenter.AdminPresenter;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.shared.Position;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PasswordField;
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
public class AdministradorCreateWindow extends Window {

	@PropertyId("nombre")
	private TextField nombre;
	@PropertyId("apellidos")
	private TextField apellidos;
	@PropertyId("DNI")
	private TextField dni;
	@PropertyId("email")
	private TextField email;
	@PropertyId("telefono")
	private TextField telefono;
	
	@PropertyId("userAccount.username")
	private TextField username;
	
	@PropertyId("userAccount.password")
	private PasswordField password;
	
	private Administrador admin;
	//DRA - No es Autowired porque se produce un ciclo
	private AdminPresenter presenter;

	private BeanFieldGroup<Administrador> fieldGroup;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1299573468494488377L;
	
	
	private static final Logger log = Logger.getLogger(AdministradorCreateWindow.class);

	public AdministradorCreateWindow() {		
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
				log.error("Error guardando el administrador",e1);
				Notification.show("Error guardando el administrador",
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
		setIcon(FontAwesome.COGS);
		setCaption("Crear Administrador");
		root.setSpacing(true);
		root.setSizeFull();
		FormLayout details = new FormLayout();
		details.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		root.addComponent(details);
		root.setExpandRatio(details, 1);

		nombre = new TextField("Nombre");
		details.addComponent(nombre);
		
		apellidos = new TextField("Apellidos");
		details.addComponent(apellidos);
		
		dni = new TextField("D.N.I.");
		details.addComponent(dni);
		
		email = new TextField("Email");
		details.addComponent(email);
		
		telefono = new TextField("Teléfono");
		details.addComponent(telefono);
		
		username = new TextField("Username");
		details.addComponent(username);
		
		password = new PasswordField("Contraseña");
		details.addComponent(password);
		
		return root;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.AbstractComponent#attach()
	 */
	@Override
	public void attach() {
		super.attach();
		if ( presenter == null ){
			throw new IllegalArgumentException("Presenter no establecido");
		}
		admin = presenter.create();
		Responsive.makeResponsive(this);

		setModal(true);
		setCloseShortcut(KeyCode.ESCAPE, null);
		setResizable(false);
		setHeight(70f, Unit.PERCENTAGE);
		setWidth(50, Unit.PERCENTAGE);
		VerticalLayout content = new VerticalLayout();
		content.addComponent(buildForm());
		content.addComponent(buildFooter());
		setContent(content);
		
		fieldGroup = new BeanFieldGroup<Administrador>(Administrador.class);
		fieldGroup.bindMemberFields(this);
		fieldGroup.setItemDataSource(admin);
		fieldGroup.setBuffered(true);
	}

	/**
	 * @author David
	 * @throws CommitException
	 */
	private void save() throws CommitException {
		fieldGroup.commit();

		presenter.save(admin);

		Notification success = new Notification(
				"Administrador creado correctamente");
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
	public void setPresenter(AdminPresenter presenter) {
		this.presenter = presenter;
	}
	
	

}
