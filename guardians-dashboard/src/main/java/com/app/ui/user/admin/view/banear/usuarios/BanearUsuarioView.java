/**
 * BanearUsuarioView.java
 * appEducacionalVaadin
 * 21/3/2015 23:07:16
 * Copyright David
 * com.app.ui.user.admin.view.banear.usuarios
 */
package com.app.ui.user.admin.view.banear.usuarios;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Persona;
import com.app.presenter.event.EventComunication.BrowserResizeEvent;
import com.app.presenter.event.EventComunicationBus;
import com.app.ui.user.admin.presenter.AdminPresenter;
import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.server.Responsive;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "BanearUsuario")
/**
 * @author David
 *
 */
public class BanearUsuarioView extends VerticalLayout implements View {

	private Table table;
	private Button createReport;

	@Autowired
	private AdminPresenter presenter;

	/**
	 * 
	 */
	private static final long serialVersionUID = -7560486752156416103L;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener
	 * .ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
	}

	@PostConstruct
	void init() {
		setSizeFull();
		addStyleName("transactions");
		EventComunicationBus.register(this);

		addComponent(buildToolbar());

		table = buildTable();
		addComponent(table);
		setExpandRatio(table, 1);
	}

	private Component buildToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName("viewheader");
		header.setSpacing(true);
		Responsive.makeResponsive(header);

		Label title = new Label("Personas");
		title.setSizeUndefined();
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		header.addComponent(title);

		createReport = buildCreateReport();
		HorizontalLayout tools = new HorizontalLayout(buildFilter(),
				createReport);
		tools.setSpacing(true);
		tools.addStyleName("toolbar");
		header.addComponent(tools);

		return header;
	}

	private Button buildCreateReport() {
		final Button createReport = new Button("Banear Seleccionados");
		createReport
				.setDescription("Banea a los usuarios seleccionados de la aplicación.");
		createReport.addClickListener(e -> {
				Object persona = table.getValue();
				if ( persona != null && persona instanceof Persona ){
					Persona p = (Persona) persona;
					p.setUserAccount(null);
					presenter.savePersona(p);
					table.refreshRowCache();
				}
			
			});
		createReport.setEnabled(true);
		return createReport;
	}

	private Component buildFilter() {
		TextField filter = new TextField();
		filter.addTextChangeListener(e -> {
			Filterable data = (Filterable) table.getContainerDataSource();
			data.removeAllContainerFilters();
			data.addContainerFilter(new Filter() {
				/**
				 * 
				 */
				private static final long serialVersionUID = -3899997423103680023L;

				@Override
				public boolean passesFilter(final Object itemId, final Item item) {

					if (e.getText() == null || e.getText().equals("")) {
						return true;
					}
					return false;

				}

				@Override
				public boolean appliesToProperty(final Object propertyId) {
					if (propertyId.equals("country")
							|| propertyId.equals("city")
							|| propertyId.equals("title")) {
						return true;
					}
					return false;
				}
			});
		});

		filter.setInputPrompt("Filtrar");
		filter.setIcon(FontAwesome.SEARCH);
		filter.addStyleName(ValoTheme.TEXTFIELD_INLINE_ICON);
		filter.addShortcutListener(new ShortcutListener("Clear",
				KeyCode.ESCAPE, null) {
			/**
					 * 
					 */
			private static final long serialVersionUID = -3681283114635453722L;

			@Override
			public void handleAction(final Object sender, final Object target) {
				filter.setValue("");
				((Filterable) table.getContainerDataSource())
						.removeAllContainerFilters();
			}
		});
		return filter;
	}

	private Table buildTable() {
		// Have a container of some type to contain the data
		BeanItemContainer<Persona> container = new BeanItemContainer<Persona>(
				Persona.class, presenter.getTodasPersonas());
		container.addNestedContainerProperty("userAccount.username");
		Table table = new Table();
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);
		table.setSelectable(true);

		table.setColumnCollapsingAllowed(true);

		table.setColumnReorderingAllowed(true);
		table.setContainerDataSource(container);

		table.setImmediate(true);
		
		table.setVisibleColumns("DNI","apellidos","nombre","email","identidadConfirmada","imagen","telefono","userAccount.username");
		
		table.addGeneratedColumn("imagen", getImageColumnGenerator());

		return table;
	}
	
	public ColumnGenerator getImageColumnGenerator(){
		 ColumnGenerator imageColumnGenerator = new ColumnGenerator()
		  {
		    /**
			 * 
			 */
			private static final long serialVersionUID = 8387232622265687219L;

			public Object generateCell(final Table source, final Object itemId,
		        final Object columnId)
		    {
			  @SuppressWarnings("unchecked")
			  BeanItem<Persona> alumnoBean = (BeanItem<Persona>) source.getItem(itemId);
			  Persona alumno = alumnoBean.getBean();
			  VerticalLayout pic = new VerticalLayout();
				pic.setSizeUndefined();
				pic.setSpacing(true);
				Resource resource;
				if (alumno.getImagen() != null
						&& alumno.getImagen().length > 0) {
					StreamSource source2 = new StreamResource.StreamSource() {

						private static final long serialVersionUID = -3823582436185258502L;

						public InputStream getStream() {
							InputStream reportStream = null;
							reportStream = new ByteArrayInputStream(
									alumno.getImagen());
							return reportStream;
						}
					};
					resource = new StreamResource(source2, "profile-picture.png");
				} else {
					resource = new ThemeResource(
							"img/profile-pic-300px.jpg");
				}
				Image profilePic = new Image("", resource);
				profilePic.setWidth(40.0f, Unit.PIXELS);
				profilePic.markAsDirty();
				pic.addComponent(profilePic);
		      return pic;
		    }
		  };
		  return imageColumnGenerator;
		}

	@Subscribe
	public void browserResized(final BrowserResizeEvent event) {
		// Some columns are collapsed when browser window width gets small
		// enough to make the table fit better.
		for (Object propertyId : table.getContainerPropertyIds()) {
			table.setColumnCollapsed(propertyId, Page.getCurrent()
					.getBrowserWindowWidth() < 800);
		}
	}
}
