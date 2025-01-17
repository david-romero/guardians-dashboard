/**
* NotificacionesPresenter.java
* appEducacionalVaadin
* 21/1/2015 19:52:38
* Copyright David
* com.app.ui.user.notificaciones.view
*/
package com.app.ui.user.notificaciones.presenter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.applicationservices.services.NotificacionService;
import com.app.applicationservices.services.PadreMadreOTutorService;
import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.Notificacion;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Persona;
import com.app.domain.model.types.Profesor;
import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.app.ui.components.NotificacionCreateWindow;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class NotificacionesPresenter implements ClickListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6189842576913112726L;

	@Autowired
	private NotificacionService notificacionService;
	@Autowired
	private ProfesorService profesorService;
	@Autowired
	private PadreMadreOTutorService tutorService;
	@Autowired
	private NotificacionCreateWindow window;
	
	protected int inicio = 0;
	
	protected int salto = 20;
	
	public NotificacionesPresenter(){
	}


	/**
	 * @author David
	 * @return
	 */
	public List<Notificacion> getNotificacines() {
		List<Notificacion> notifications = Lists.newArrayList();
		UserAccount account = AppUI.getCurrentUser();
			switch ( Lists.newArrayList(account.getAuthorities()).get(0).getAuthority() ) {
			case Authority.PROFESOR:
				Profesor p = profesorService.findByUserAccount(account);
				notifications.addAll(p.getNotificaciones());
				break;
			case Authority.TUTOR:
				PadreMadreOTutor tutor = tutorService.findByUserAccount(account);
				notifications.addAll(tutor.getNotificaciones());
				break;

			default:
				break;
			}
		return notifications;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		window.setPresenter(this);
		AppUI.getCurrent().addWindow(window);
	}
	
	public Notificacion create(){
		Notificacion notificacion = notificacionService.create();
		notificacion.setProfesor(getProfesor());
		return notificacion;
	}
	
	
	private Profesor getProfesor(){
		UserAccount account = AppUI.getCurrentUser();
		Profesor p = profesorService.findByUserAccount(account);
		return p;
	}


	/**
	 * @author David
	 * @param notificacion
	 */
	public void save(Notificacion notificacion,Class<? extends Persona> clazz) {
		notificacion.setEmisor(clazz.getSimpleName());
		notificacionService.save(notificacion);
	}

	/**
	 * @author David
	 * @param class1
	 * @return
	 */
	public IndexedContainer getContainer(
			Class<PadreMadreOTutor> clazz) {
		IndexedContainer container = null;
		if ( clazz.equals(PadreMadreOTutor.class) ){
			List<PadreMadreOTutor> tutores = profesorService.getTutoresAlumnosPertenecientesProfesor(getProfesor());
			container = new IndexedContainer(tutores);
		}
		return container;
	}
	
	public List<Notificacion> obtenerMasNotificaciones(){
		List<Notificacion> notificaciones =
				getNotificacines();
		Collections.sort(notificaciones, new Comparator<Notificacion>(){

			@Override
			public int compare(Notificacion arg0, Notificacion arg1) {
				return arg0.getFecha().compareTo(arg1.getFecha());
			}
			
		});
		Collections.reverse(notificaciones);
		if ( notificaciones.size() > inicio + salto ){
			notificaciones =
					getNotificacines().subList(inicio, inicio+salto);
			inicio = inicio + salto;
		}
		return notificaciones;
	}

	public boolean existenMasNotificaciones(){
		return getNotificacines().size() > inicio + salto;
	}

}
