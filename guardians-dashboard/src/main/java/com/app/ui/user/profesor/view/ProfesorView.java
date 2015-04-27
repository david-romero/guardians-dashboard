/**
 * ProfesorView.java
 * appEducacionalVaadin
 * 06/12/2014 14:05:37
 * Copyright David
 * com.app.ui.user.tutores
 */
package com.app.ui.user.profesor.view;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Profesor;
import com.app.infrastructure.security.Authority;
import com.app.ui.AppUI;
import com.app.ui.NavigatorUI;
import com.app.ui.user.UserView;
import com.app.ui.user.menu.MenuComponent;
import com.app.ui.user.profesor.presenter.ProfesorPresenter;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;

@SpringView(name=Authority.PROFESOR)
/**
 * @author David
 *
 */
public class ProfesorView extends HorizontalLayout implements UserView {

	private NavigatorUI navigator;
	
	@Autowired
	private MenuComponent menu;
	
	private Profesor currentUser;
	
	private Authority rol;
	
	@Autowired
	private ProfesorPresenter presenter;
	
	public static final String NAME = "profesor";
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4830300048826230639L;

	public void enter(ViewChangeListener.ViewChangeEvent event) {
		
	}
	
	@PostConstruct
    void init() {
		currentUser = presenter.getProfesor();
		generateRol();
		removeAllComponents();
		generateView();
	}
	
	/**
	 * @author David
	 */
	private void generateView() {
		setSizeFull();
        addStyleName("mainview");
        menu.setCurrentUser(currentUser);
        addComponent(menu);

        ComponentContainer content = new CssLayout();
        content.addStyleName("view-content");
        content.setSizeFull();
        addComponent(content);
        
        setExpandRatio(content, 1.0f);
        
        navigator = new NavigatorUI(AppUI.getCurrent(), content);
        navigator.addProvider(( (AppUI) AppUI.getCurrent()).getSpringViewProvider());
        navigator.navigateTo("PaneldeControl");
	}

	/* (non-Javadoc)
	 * @see com.app.ui.user.UserView#getRol()
	 */
	@Override
	public Authority getRol() {
		if (this.rol==null){
			generateRol();
		}
		return rol;
	}


	public void generateRol() {
		this.rol = new Authority();
		this.rol.setAuthority(Authority.PROFESOR);
	}

	


}
