/**
* CursosFilterPopup.java
* appEducacionalVaadin
* 21/3/2015 17:25:04
* Copyright David
* com.app.ui.components
*/
package com.app.ui.components;

import java.util.List;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopup;
import org.tepi.filtertable.numberfilter.NumberInterval;
import org.vaadin.hene.popupbutton.PopupButton;
import org.vaadin.hene.popupbutton.PopupButton.PopupVisibilityEvent;

import com.app.domain.model.types.Curso;
import com.google.gwt.thirdparty.guava.common.collect.Lists;
import com.vaadin.data.Property;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.ObjectProperty;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.VerticalLayout;

/**
 * @author David
 *
 */
public class CursosFilterPopup extends CustomField<List<Curso>>{

	protected PopupButton content;
	protected FilterDecorator decorator;
	protected boolean settingValue;
	protected String valueMarker;
    private List<Curso> valores;

    private static final String DEFAULT_OK_CAPTION = "Establecer";
    private static final String DEFAULT_RESET_CAPTION = "Limpiar";
    private static final String DEFAULT_VALUE_MARKER = "[x]";

    /* Buttons */
    private Button ok;
    private Button reset;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3097878848594378427L;

	public CursosFilterPopup(FilterDecorator decorator) {
        this.decorator = decorator;
        /* This call is needed for the value setting to function before attach */
        getContent();
    }
	
	private void initPopup() {
        final VerticalLayout content = new VerticalLayout();
        content.setStyleName("numberfilterpopupcontent");
        content.setSpacing(true);
        content.setMargin(true);
        content.setSizeUndefined();

        for ( Curso c : valores ){
        	CheckBox check = new CheckBox(c.toString());
        	@SuppressWarnings("unchecked")
			ObjectProperty property =
        		    new ObjectProperty(c, Curso.class);
        	check.setPropertyDataSource(property);
        	content.addComponent(check);
        }


        ok = new Button(DEFAULT_OK_CAPTION, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                // users inputs
            	List<Curso> cursos = Lists.newArrayList();
            	for ( int i = 0; i < content.getComponentCount(); i++ ) {
            		Component component = content.getComponent(i);
            		if (component instanceof CheckBox){
            			@SuppressWarnings("unchecked")
						Property<Curso> p = ((CheckBox) component).getPropertyDataSource();
            			Curso c = p.getValue();
            			cursos.add(c);
            		}
            	}
                setValue(cursos);
                CursosFilterPopup.this.content.setPopupVisible(false);
            }
        });

        reset = new Button(DEFAULT_RESET_CAPTION, new ClickListener() {
            @Override
            public void buttonClick(ClickEvent event) {
                setValue(null);
                CursosFilterPopup.this.content.setPopupVisible(false);
            }
        });

        HorizontalLayout buttons = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.setSpacing(true);
        buttons.addComponent(ok);
        buttons.addComponent(reset);
        buttons.setExpandRatio(ok, 1);
        buttons.setComponentAlignment(ok, Alignment.MIDDLE_RIGHT);
        content.addComponent(buttons);

        this.content.setContent(content);
    }
	
	@Override
    public void setValue(List<Curso> newFieldValue)
            throws com.vaadin.data.Property.ReadOnlyException,
            ConversionException {
        settingValue = true;
        valores = newFieldValue;
        super.setValue(newFieldValue);
        settingValue = false;
    }
	
	@Override
    protected Component initContent() {
        if (content == null) {
            content = new PopupButton();
            content.setWidth(100, Unit.PERCENTAGE);
            setImmediate(true);
            setStyleName("numberfilterpopup");
            initPopup();
            content.addPopupVisibilityListener(new PopupButton.PopupVisibilityListener() {

                @Override
                public void popupVisibilityChange(PopupVisibilityEvent event) {
                    /*if (settingValue) {
                        settingValue = false;
                    } else if (interval == null) {
                        ltInput.setValue("");
                        gtInput.setValue("");
                        eqInput.setValue("");
                    } else {
                        ltInput.setValue(interval.getLessThanValue());
                        gtInput.setValue(interval.getGreaterThanValue());
                        eqInput.setValue(interval.getEqualsValue());
                    }*/
                }
            });
        }
        return content;
    }

	/* (non-Javadoc)
	 * @see com.vaadin.ui.AbstractField#getType()
	 */
	@Override
	public Class<? extends List<Curso>> getType() {
		return (Class<? extends List<Curso>>) List.class;
	}

}
