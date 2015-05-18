/**
* UserMenuHelper.java
* appEducacionalVaadin
* 22/12/2014 22:30:04
* Copyright David
* com.app.ui.user
*/
package com.app.ui.user.menu;

import java.util.Arrays;
import java.util.List;

import com.app.infrastructure.security.Authority;
import com.app.ui.user.admin.view.banear.usuarios.BanearUsuarioView;
import com.app.ui.user.admin.view.confirmar.identidad.ConfirmarIdentidadView;
import com.app.ui.user.alumno.view.ListarView;
import com.app.ui.user.alumno.view.PerfilView;
import com.app.ui.user.calendario.view.CalendarioView;
import com.app.ui.user.calificar.alumno.view.CalificarAlumnoView;
import com.app.ui.user.panelControl.view.PanelControlView;
import com.app.ui.user.spreadsheet.ExcelView;
import com.app.ui.user.spreadsheet.SpreadSheet2View;
import com.app.ui.user.spreadsheet.SpreadSheetView;
import com.vaadin.navigator.View;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;

/**
 * @author David
 *
 */
public enum UserMenuHelper {

	
	
	PANELCONTROL("PaneldeControl", PanelControlView.class, FontAwesome.HOME, true,Arrays.asList(new Authority(Authority.ADMINISTRADOR),new Authority(Authority.TUTOR),new Authority(Authority.PROFESOR))),
	BANEAR("BanearUsuario", BanearUsuarioView.class, FontAwesome.BAN, true,Arrays.asList(new Authority(Authority.ADMINISTRADOR))),
	CONFIRMARIDENTIDAD("Confirmar", ConfirmarIdentidadView.class, FontAwesome.CHECK_SQUARE, true,Arrays.asList(new Authority(Authority.ADMINISTRADOR))),
	CALENDARIO("Calendario", CalendarioView.class, FontAwesome.CALENDAR, true,Arrays.asList(new Authority(Authority.TUTOR),new Authority(Authority.PROFESOR))),
	CALIFICARALUMNO("CalificarAlumno", CalificarAlumnoView.class, FontAwesome.EDIT, true,Arrays.asList(new Authority(Authority.PROFESOR))),
	ALUMNO("Perfil", PerfilView.class, FontAwesome.EDIT, true,Arrays.asList(new Authority(Authority.PROFESOR))),
	LISTAR("Listar", ListarView.class, FontAwesome.EDIT, true,Arrays.asList(new Authority(Authority.PROFESOR))),
	CALIFICAR("Calificar", ExcelView.class, FontAwesome.FILE_EXCEL_O, true,Arrays.asList(new Authority(Authority.PROFESOR))),
	EVALUAR("Evaluar", SpreadSheetView.class, FontAwesome.FILE_EXCEL_O, true,Arrays.asList(new Authority(Authority.PROFESOR))),
	ANOTAR("Anotar", SpreadSheet2View.class, FontAwesome.FILE_EXCEL_O, true,Arrays.asList(new Authority(Authority.PROFESOR)));
	

    private final String viewName;
    private final Class<? extends View> viewClass;
    private final Resource icon;
    private final boolean stateful;
    private List<com.app.infrastructure.security.Authority> roles;

    private UserMenuHelper(final String viewName,
            final Class<? extends View> viewClass, final Resource icon,
            final boolean stateful,List<com.app.infrastructure.security.Authority> roles) {
        this.viewName = viewName;
        this.viewClass = viewClass;
        this.icon = icon;
        this.stateful = stateful;
        this.roles = roles;
    }

    /**
	 * @return roles
	 */
	public List<com.app.infrastructure.security.Authority> getRoles() {
		return roles;
	}



	public boolean isStateful() {
        return stateful;
    }

    public String getViewName() {
        return viewName;
    }

    public Class<? extends View> getViewClass() {
        return viewClass;
    }

    public Resource getIcon() {
        return icon;
    }

    public static UserMenuHelper getByViewName(final String viewName) {
    	UserMenuHelper result = null;
        for (UserMenuHelper viewType : values()) {
            if (viewType.getViewName().equals(viewName)) {
                result = viewType;
                break;
            }
        }
        return result;
    }
	
}
