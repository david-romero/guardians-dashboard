/**
* ConfirmarIdentidadView.java
* guardians-dashboard
* 27/4/2015 20:48:39
* Copyright David
* com.app.ui.user.admin.view.confirmar.identidad
*/
package com.app.ui.user.admin.view.confirmar.identidad;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Persona;
import com.app.presenter.event.EventComunicationBus;
import com.app.ui.user.admin.presenter.AdminPresenter;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Container.Indexed;
import com.vaadin.data.Item;
import com.vaadin.data.util.BeanItem;
import com.vaadin.event.SelectionEvent;
import com.vaadin.event.SelectionEvent.SelectionListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Responsive;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.MultiSelectionModel;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name="Confirmar")
/**
 * @author David
 *
 */
public class ConfirmarIdentidadView extends VerticalLayout implements View,SelectionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1514746363585817751L;
	
	@Autowired
	private AdminPresenter presenter;
	
	private Grid table;
	
	private Button updateButton;
	private Button createReport;
    private final Set<BeanItem<Persona>> editedItems = new HashSet<BeanItem<Persona>>();
    private final Set<BeanItem<Persona>> oldEditedItems = new HashSet<BeanItem<Persona>>();

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
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
		// Create update button logic
        updateButton = new Button("Update", new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                    // Apply changes to all selected items
                    for (BeanItem<Persona> item : editedItems) {
                        // Set changed to true, so style name is changed
                        item.getItemProperty("identidadConfirmada").setValue(true);
                    }

                    oldEditedItems.removeAll(editedItems);

                    // Reset old edited items to revert the style name
                    for (BeanItem<Persona> item : oldEditedItems) {
                        item.getItemProperty("identidadConfirmada").setValue(false);
                        // Explicit refresh so row will actually show
                        // changes
                        presenter.savePersona(item.getBean());
                    }
                    oldEditedItems.clear();
                    oldEditedItems.addAll(editedItems);
                    // Remove all selected rows
                    ((MultiSelectionModel) table.getSelectionModel())
                            .deselectAll();

                }
        });
        updateButton.setWidth(100, Unit.PERCENTAGE);

        // Layout editor pieces
        addComponent(updateButton);
        setWidth(100, Unit.PERCENTAGE);
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
		final Button createReport = new Button("Crear Informe");
		createReport
				.setDescription("Create a new report from the selected transactions");
		createReport.addClickListener(e -> {
			// TODO
			});
		createReport.setEnabled(false);
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

	private Grid buildTable() {
		// Have a container of some type to contain the data
		Indexed container =  presenter.getTodasPersonasSinIdentidadConfirmada();
		Grid table = new Grid();
		table.setSizeFull();
		table.addStyleName(ValoTheme.TABLE_BORDERLESS);
		table.addStyleName(ValoTheme.TABLE_NO_HORIZONTAL_LINES);
		table.addStyleName(ValoTheme.TABLE_COMPACT);

		
		
		table.setContainerDataSource(container);

		table.setImmediate(true);

		return table;
	}

	/* (non-Javadoc)
	 * @see com.vaadin.event.SelectionEvent.SelectionListener#select(com.vaadin.event.SelectionEvent)
	 */
	@Override
	public void select(SelectionEvent event) {
		// Logic for handling selection changes from Grid
        boolean empty = event.getSelected().isEmpty();
        if (!empty) {
            // Some items are selected. Make sure Editor is enabled
            setEnabled(true);

            // Keep track of items currently being edited.
            editedItems.addAll(getItemIds(event.getAdded()));
            editedItems.removeAll(getItemIds(event.getRemoved()));
        } else {
            // No items are selected. Disable editor.
            setEnabled(false);
            editedItems.clear();
        }
	}
	
	 /**
     * Gets a list of BeanItems for given item IDs from the container of
     * Grid.
     *
     * @param itemIds
     *            set of item ids
     * @return a collection of BeanItems for given itemIds
     */
    private Collection<BeanItem<Persona>> getItemIds(Set<Object> itemIds) {
        Set<BeanItem<Persona>> items = new HashSet<BeanItem<Persona>>();
        for (Object id : itemIds) {
            items.add((BeanItem<Persona>) table.getContainerDataSource()
                    .getItem(id));
        }
        return items;
    }

}
