/**
* UserView.java
* appEducacional
* 6/4/2015 16:59:24
* Copyright Administrador
* com.app.ui.user
*/
package com.app.ui.user;

import com.app.infrastructure.security.Authority;
import com.vaadin.navigator.View;

/**
 * @author DRA
 *
 */
public interface UserView extends View{

	public Authority getRol();
	
}
