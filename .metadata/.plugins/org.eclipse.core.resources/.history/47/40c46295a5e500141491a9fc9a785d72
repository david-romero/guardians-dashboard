/**
* AlumnosTable.java
* appEducacionalVaadin
* 21/3/2015 11:27:09
* Copyright David
* com.app.ui.components
*/
package com.app.ui.components.alumnos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tepi.filtertable.FilterTable;

import com.app.domain.model.types.Alumno;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.JPAContainerItem;
import com.vaadin.data.Property;
import com.vaadin.server.Resource;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomTable;
import com.vaadin.ui.Image;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author David
 *
 */
public class AlumnosTable extends FilterTable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3877027841830033880L;
	
	public AlumnosTable(JPAContainer<Alumno> container){
		setContainerDataSource(container);

        addStyleName(ValoTheme.TABLE_BORDERLESS);
        addStyleName(ValoTheme.TABLE_NO_STRIPES);
        addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
        setSortEnabled(false);
        setFilterDecorator(new AlumnosFilterGenerator());
        setRowHeaderMode(RowHeaderMode.INDEX);
        setSizeFull();
        setVisibleColumns("imagen","nombre", "apellidos","fechaNacimiento","curso");
        setColumnHeaders("Alumno","Nombre", "Apellido","Fecha de Nacimiento","Curso");

        setSortContainerPropertyId("nombre");
        
        addGeneratedColumn("imagen", getImageColumnGenerator() );
	}
	
	@Override
    protected String formatPropertyValue(final Object rowId,
            final Object colId, final Property<?> property) {
        String result = super.formatPropertyValue(rowId, colId, property);
        if (colId.equals("fechaNacimiento")) {
            if (property != null && property.getValue() != null) {
                Date r = (Date) property.getValue();
                String ret = new SimpleDateFormat("dd/MM/yyyy").format(r);
                result = ret;
            } else {
                result = "";
            }
        }
        return result;
    }
	public ColumnGenerator getImageColumnGenerator(){
	 ColumnGenerator imageColumnGenerator = new ColumnGenerator()
	  {
	    /**
		 * 
		 */
		private static final long serialVersionUID = 8387232622265687219L;

		public Object generateCell(final CustomTable source, final Object itemId,
	        final Object columnId)
	    {
		  @SuppressWarnings("unchecked")
		  JPAContainerItem<Alumno> alumnoBean = (JPAContainerItem<Alumno>) source.getItem(itemId);
		  Alumno alumno = alumnoBean.getEntity();
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
			profilePic.setWidth(100.0f, Unit.PIXELS);
			profilePic.markAsDirty();
			pic.addComponent(profilePic);
	      return pic;
	    }
	  };
	  return imageColumnGenerator;
	}
}
