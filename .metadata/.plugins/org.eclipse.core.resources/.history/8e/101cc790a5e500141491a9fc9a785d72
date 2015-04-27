/**
* AlumnosFilterGenerator.java
* appEducacionalVaadin
* 21/3/2015 15:35:54
* Copyright David
* com.app.ui.components.alumnos
*/
package com.app.ui.components.alumnos;

import java.text.DateFormat;
import java.util.Locale;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.FilterGenerator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.app.ui.components.CursosFilterPopup;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Field;

/**
 * @author David
 *
 */
public class AlumnosFilterGenerator implements FilterDecorator,FilterGenerator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4504226360424070467L;

	@Override
    public String getEnumFilterDisplayName(Object propertyId, Object value) {
        if ("nombre".equals(propertyId)) {
            return "Nombre: " + value.toString();
        }
        // returning null will output default value
        return null;
    }

    @Override
    public Resource getEnumFilterIcon(Object propertyId, Object value) {
        if ("state".equals(propertyId)) {
            /*State state = (State) value;
            switch (state) {
            case CREATED:
                return new ThemeResource("../runo/icons/16/document.png");
            case PROCESSING:
                return new ThemeResource("../runo/icons/16/reload.png");
            case PROCESSED:
                return new ThemeResource("../runo/icons/16/ok.png");
            case FINISHED:
                return new ThemeResource("../runo/icons/16/globe.png");
            }*/
        }
        return null;
    }

    @Override
    public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? "Validated" : "Not validated";
        }
        // returning null will output default value
        return null;
    }

    @Override
    public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
        if ("validated".equals(propertyId)) {
            return value ? new ThemeResource("../runo/icons/16/ok.png")
                    : new ThemeResource("../runo/icons/16/cancel.png");
        }
        return null;
    }

    @Override
    public String getFromCaption() {
        return "Fecha inicio:";
    }

    @Override
    public String getToCaption() {
        return "Fecha fin:";
    }

    @Override
    public String getSetCaption() {
        // use default caption
        return "Establecer";
    }

    @Override
    public String getClearCaption() {
        // use default caption
        return "Limpiar";
    }

    @Override
    public boolean isTextFilterImmediate(Object propertyId) {
        // use text change events for all the text fields
        return true;
    }

    @Override
    public int getTextChangeTimeout(Object propertyId) {
        // use the same timeout for all the text fields
        return 500;
    }

    @Override
    public String getAllItemsVisibleString() {
        return "Mostrar todos";
    }

    @Override
    public Resolution getDateFieldResolution(Object propertyId) {
        return Resolution.DAY;
    }

    public DateFormat getDateFormat(Object propertyId) {
        return DateFormat.getDateInstance(DateFormat.SHORT,getLocale());
    }

    @Override
    public boolean usePopupForNumericProperty(Object propertyId) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String getDateFormatPattern(Object propertyId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Locale getLocale() {
        // TODO Auto-generated method stub
        return new Locale("ES", "es");
    }

    @Override
    public NumberFilterPopupConfig getNumberFilterPopupConfig() {
        // TODO Auto-generated method stub
        return null;
    }


	/* (non-Javadoc)
	 * @see org.tepi.filtertable.FilterGenerator#generateFilter(java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter generateFilter(Object propertyId, Object value) {
		if ("nombre".equals(propertyId) || "apellidos".equals(propertyId) ) {
            /* Create an 'equals' filter for the ID field */
            if (value != null && value instanceof String) {
                   return new Like(propertyId, (String) value);
            }
        } else if ("checked".equals(propertyId)) {
            if (value != null && value instanceof Boolean) {
                if (Boolean.TRUE.equals(value)) {
                    return new Compare.Equal(propertyId, value);
                } else {
                    return new Or(new Compare.Equal(propertyId, true),
                            new Compare.Equal(propertyId, false));
                }
            }
        }
        // For other properties, use the default filter
        return null;
	}

	
	
	/* (non-Javadoc)
	 * @see org.tepi.filtertable.FilterGenerator#generateFilter(java.lang.Object, com.vaadin.ui.Field)
	 */
	@Override
	public Filter generateFilter(Object propertyId, Field<?> originatingField) {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.tepi.filtertable.FilterGenerator#getCustomFilterComponent(java.lang.Object)
	 */
	@Override
	public AbstractField<?> getCustomFilterComponent(Object propertyId) {
		if ("curso".equals(propertyId)){
			CursosFilterPopup popup = new CursosFilterPopup(this);
			return popup;
		}
		if ("nombre".equals(propertyId)) {
            CheckBox box = new CheckBox();
            return box;
        }
		return null;
	}

	/* (non-Javadoc)
	 * @see org.tepi.filtertable.FilterGenerator#filterRemoved(java.lang.Object)
	 */
	@Override
	public void filterRemoved(Object propertyId) {
	}

	/* (non-Javadoc)
	 * @see org.tepi.filtertable.FilterGenerator#filterAdded(java.lang.Object, java.lang.Class, java.lang.Object)
	 */
	@Override
	public void filterAdded(Object propertyId,
			Class<? extends Filter> filterType, Object value) {
	}

	/* (non-Javadoc)
	 * @see org.tepi.filtertable.FilterGenerator#filterGeneratorFailed(java.lang.Exception, java.lang.Object, java.lang.Object)
	 */
	@Override
	public Filter filterGeneratorFailed(Exception reason, Object propertyId,
			Object value) {
		return null;
	}

	
}
