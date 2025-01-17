/**
* ItemsEvaluablesPresenter.java
* appEducacionalVaadin
* 1/4/2015 20:08:19
* Copyright David
* com.app.ui.user.calificaciones.presenter
*/
package com.app.ui.user.calificaciones.presenter;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.app.applicationservices.services.AlumnoService;
import com.app.applicationservices.services.AsignaturaService;
import com.app.applicationservices.services.ProfesorService;
import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.Profesor;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.data.Container;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class ItemsEvaluablesPresenter {

	@Autowired
	private ProfesorService profesorService;
	@Autowired
	private AlumnoService alumnoService;
	@Autowired
	private AsignaturaService asignaturaService;
	@Autowired
	private ApplicationContext applicationContext;

	public ItemsEvaluablesPresenter(){
	}
	
	public Profesor getProfesor() {
		UserAccount account = AppUI.getCurrentUser();
		Profesor p = profesorService.findByUserAccount(account);
		return p;
	}
	
	/**
	 * @author David
	 * @return
	 */
	public IndexedContainer getContainerAlumnos() {
		List<Alumno> alum = Lists.newArrayList(profesorService.getTodosLosAlumnosProfesor(getProfesor()));
		return new IndexedContainer(alum);
	}

	/**
	 * @author David
	 * @return
	 */
	public IndexedContainer getContainerAsignaturas() {
		List<Materia> alum = Lists.newArrayList(asignaturaService.findAllByProfesor(getProfesor()));
		return new IndexedContainer(alum);
	}

	/**
	 * @author David
	 * @return
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public ItemEvaluable create() throws InstantiationException, IllegalAccessException {
		Reflections reflections = new Reflections("com.app.domain.model.types.itemsevaluables");

		 Set<Class<? extends ItemEvaluable>> allClasses = 
		     reflections.getSubTypesOf(ItemEvaluable.class);
		 ItemEvaluable item = null;
		 for ( Class<? extends ItemEvaluable> clazz : allClasses ){
			 item = clazz.newInstance();
			 item.setTitulo("Nuevo " + clazz.getSimpleName());
			 item.setFecha(new Date());
			 break;
		 }
		return item;
	}

	/**
	 * @author David
	 * @return
	 */
	public Container getContainerTiposItems() {
		Reflections reflections = new Reflections("com.app.domain.model.types.itemsevaluables");

		 Set<Class<? extends ItemEvaluable>> allClasses = 
		     reflections.getSubTypesOf(ItemEvaluable.class);
		 List<String> classNames = Lists.newArrayList();
		 for ( Class<? extends ItemEvaluable> clazz : allClasses ){
			 classNames.add(clazz.getSimpleName());
		 }
		 return new IndexedContainer(classNames);
	}

	/**
	 * @author David
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public ItemEvaluable create(String clazz) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		ItemEvaluable item = Class.forName("com.app.domain.model.types.itemsevaluables"+"."+clazz).asSubclass(ItemEvaluable.class).newInstance();
		item.setTitulo("Nuevo " + clazz);
		item.setFecha(new Date());
		return item;
	}

	/**
	 * @author David
	 * @param item
	 * @param clazz
	 */
	public void save(ItemEvaluable item, String clazz) {
		com.app.applicationservices.services.Service servicio = (com.app.applicationservices.services.Service) applicationContext
				.getBean(clazz + "Service");
		servicio.save(item);
	}
}
