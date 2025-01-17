/**
 * PanelControlPresenter.java
 * appEducacionalVaadin
 * 7/2/2015 18:13:51
 * Copyright David
 * com.app.ui.user.panelControl.presenter
 */
package com.app.ui.user.panelControl.presenter;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.applicationservices.services.AdministradorService;
import com.app.applicationservices.services.NotificacionService;
import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.Notificacion;
import com.app.domain.model.types.Persona;
import com.app.domain.model.types.Profesor;
import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class PanelControlPresenter implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -3060765199303204446L;
	@Autowired
	private AdministradorService adminService;
	
	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private NotificacionService notificacionService;


	public PanelControlPresenter() {
	}

	/**
	 * @author David
	 * @return
	 */
	public int getNotificacionesNoLeidas() {
		Persona p = getCurrentPerson();
		if ( p instanceof Profesor){
			Profesor profe = (Profesor) p;
			return notificacionService
					.findProfesorEmitidas(profe).size() + notificacionService.findProfesorRecibidas(profe).size();
		}else{
			return 3;
		}
		
	}

	/**
	 * @author David
	 * @return
	 */
	public List<Notificacion> getListNotificacionesNoLeidas() {
		List<Notificacion> list = Lists.newArrayList(notificacionService
				.findProfesorEmitidas(getProfesor()));
		list.addAll(notificacionService.findProfesorRecibidas(getProfesor()));
		if ( list.size() > 2 ){
			list = list.subList(0, 2);
		}
		return list;
	}

	/**
	 * @author David
	 * @return
	 */
	public Persona getCurrentPerson() {
		UserAccount account = AppUI.getCurrentUser();
    	Persona p = null;
    	switch ( Lists.newArrayList(account.getAuthorities()).get(0).getAuthority()) {
		case Authority.PROFESOR:
			p = profesorService.findByUserAccount(account);
			break;
		case Authority.TUTOR:
			break;
		case Authority.ADMINISTRADOR:
			p = adminService.findByUserAccount(account);
			break;

		default:
			p = new Persona() {

				/**
				 * 
				 */
				private static final long serialVersionUID = -4532758104268148528L;
			};
			p.setUserAccount(account);
			break;
		}
    	return p;
	}

	public Profesor getProfesor() {
		UserAccount account = AppUI.getCurrentUser();
		Profesor p = profesorService.findByUserAccount(account);
		return p;
	}

	/**
	 * @author David
	 */
	public void savePerson(Persona p) {
		profesorService.save((Profesor) p);
	}

}
