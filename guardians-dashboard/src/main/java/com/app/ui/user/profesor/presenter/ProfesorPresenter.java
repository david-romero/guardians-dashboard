/**
* ProfesorPresenter.java
* guardians-dashboard
* 18/4/2015 17:49:21
* Copyright David
* com.app.ui.user.profesor.view
*/
package com.app.ui.user.profesor.presenter;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.applicationservices.services.PersonaService;
import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.Profesor;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class ProfesorPresenter {

	
	@Autowired
	private ProfesorService profesorService;
	
	@Autowired
	private PersonaService personaService;


	public Profesor getProfesor() {
		UserAccount account = AppUI.getCurrentUser();
		com.app.domain.model.types.Profesor p = profesorService
				.findByUserAccount(account);
		return p;
	}
	
}