/**
* CalificarAlumnoView.java
* guardians-dashboard
* 1/5/2015 12:24:02
* Copyright David
* com.app.ui.user
*/
package com.app.ui.user.calificar.alumno.view;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.alump.lazylayouts.LazyComponentProvider;
import org.vaadin.alump.lazylayouts.LazyComponentRequestEvent;
import org.vaadin.alump.lazylayouts.LazyVerticalLayout;
import org.vaadin.jouni.animator.Animator;
import org.vaadin.jouni.dom.client.Css;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Notificacion;
import com.app.ui.user.alumno.presenter.AlumnoPresenter;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name=CalificarAlumnoView.NAME)
/**
 * @author David
 *
 */
public class CalificarAlumnoView extends HorizontalLayout implements View {

	//Layout con los alumnos
	protected LazyVerticalLayout layout;
	
	protected Spreadsheet spreadsheet;
	
	public static final String NAME = "CalificarAlumno";
	@Autowired
	protected AlumnoPresenter presenterAlumnos;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2771589687932199785L;

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	@PostConstruct
    void init() {
		initializeView();
		setSizeFull();
	}

	/**
	 * @author David
	 */
	protected void initializeView() {
		layout = new LazyVerticalLayout();
		layout.setSizeFull();
		spreadsheet = new Spreadsheet();
		spreadsheet.setSizeFull();
		addComponent(layout);
		addComponent(spreadsheet);
		initializeLayoutAlumnos();
		setExpandRatio(layout, 0.25f);
		setExpandRatio(spreadsheet, 0.75f);
	}

	/**
	 * @author David
	 */
	private void initializeLayoutAlumnos() {
		layout.enableLazyLoading(new LazyComponentProvider() {
			  @Override
			public void onLazyComponentRequest(LazyComponentRequestEvent event) {

			    // Load more data and add UI presentation of those to layout
			    Collection<Alumno> more = presenterAlumnos.getAlumnosProfesor();
			    for(Alumno alumno : more) {
			       event.getComponentContainer().addComponent(createAlumnoLayout(alumno));
			    }

			    // Disable lazy loading request when you run out of data
			    if(!presenterAlumnos.existenMasAlumnos()) {
			      event.getComponentContainer().disableLazyLoading();
			    }
			    
			    
			  }

			private Component createAlumnoLayout(Alumno alumno) {
				ThemeResource photoResource = new ThemeResource(
						"img/profile-pic-300px.jpg");
				Image img = new Image("",photoResource);
				img.addStyleName("v-icon");
				img.setWidth(40.0f, Unit.PIXELS);
				img.setHeight(100,Unit.PERCENTAGE);
				Label emisor = new Label(alumno.getNombre()+" "+alumno.getApellidos());
				emisor.addStyleName(ValoTheme.LABEL_H4);
				

				HorizontalLayout header = new HorizontalLayout(img,emisor);
				header.setComponentAlignment(emisor, Alignment.MIDDLE_CENTER);
				header.setComponentAlignment(img, Alignment.MIDDLE_LEFT);
				header.setExpandRatio(img, 0.25f);
				header.setExpandRatio(emisor, 0.75f);
				header.setMargin(false);
				header.setSizeFull();
				header.setSpacing(false);
				header.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
				
				header.addLayoutClickListener(e->{
					actualizarDatosSpreadSheet(alumno);
				});
				
				return header;
			}
			});
	}

	/**
	 * @author David
	 * @param alumno
	 */
	protected void actualizarDatosSpreadSheet(Alumno alumno) {
		
	}

}
