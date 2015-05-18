package com.app.ui.user.spreadsheet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.InstanciaCurso;
import com.app.ui.AppUI;
import com.app.ui.user.profesor.presenter.ProfesorPresenter;
import com.google.common.collect.Lists;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.SpreadsheetComponentFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

@SpringView(name = "Anotar")
public class SpreadSheet2View extends HorizontalLayout implements View,
		SpreadsheetComponentFactory {

	protected Component layout;

	protected Spreadsheet spreadSheet;

	protected List<String> meses;
	@Autowired
	protected ProfesorPresenter presenter;

	protected List<Alumno> alumnos;

	@Override
	public void enter(ViewChangeEvent event) {
		alumnos = presenter.getAlumnosProfesor().stream().collect(Collectors.toList());
		meses = Lists.newArrayList("Septiembre", "Octubre", "Noviembre",
				"Diciembre", "Enero", "Febrero", "Marzo", "Abril", "Mayo",
				"Junio");
		setSizeFull();
		buildSpreadSheet();
		buildVerticalLayout();

		addComponent(spreadSheet);
		addComponent(layout);
		setExpandRatio(layout, 0.2f);
		setExpandRatio(spreadSheet, 0.8f);
	}

	private void buildSpreadSheet() {
		spreadSheet = new Spreadsheet();
		spreadSheet.setSpreadsheetComponentFactory(this);
		spreadSheet.setImmediate(true);
		spreadSheet.setColumnWidth(0, 40);
		spreadSheet.setColumnWidth(1, 80);
		spreadSheet.setColumnWidth(2, 60);
		spreadSheet.setColumnWidth(3, 80);
		spreadSheet.setColumnWidth(4, 80);
		spreadSheet.setColumnWidth(5, 60);
		spreadSheet.setColumnWidth(6, 60);
		spreadSheet.setColumnWidth(7, 60);
		spreadSheet.setColumnWidth(8, 60);

	}

	private void buildVerticalLayout() {
		layout = new VerticalLayout();
		layout.addStyleName("valo-menu");

		List<InstanciaCurso> cursos = presenter.getCursosProfesor().stream().collect(Collectors.toList());

		ComboBox box = new ComboBox(null, cursos);
		box.setInputPrompt("Filtre por Curso");

		box.addValueChangeListener(e -> {
			if ( e.getProperty().getValue() != null ){
				InstanciaCurso curso = (InstanciaCurso) e.getProperty().getValue();
				
				alumnos = curso.getAlumnos().stream().collect(Collectors.toList());
			}else{
				alumnos = presenter.getAlumnosProfesor().stream().collect(Collectors.toList());
			}
			buildVerticalLayout();
		});

		((VerticalLayout) layout).addComponent(box);

		for (Alumno alum : alumnos) {
			HorizontalLayout l = new HorizontalLayout();
			MarginInfo marg = new MarginInfo(true, false, false, false);
			l.setMargin(marg);
			l.setSizeFull();
			l.setSpacing(false);
			ThemeResource r = new ThemeResource("img/profile-pic-300px.jpg");
			Image img = new Image(null, r);
			img.removeStyleName("v-caption-on-top");
			img.addStyleName("v-icon");
			img.setWidth(40.0f, Unit.PIXELS);
			img.setHeight(100, Unit.PERCENTAGE);
			l.addComponent(img);
			String title = alum.getNombre() + " " + alum.getApellidos();
			Label lbl = new Label(title.length() > 25 ? title.substring(0, 25)
					: title);
			l.addComponent(lbl);
			l.addLayoutClickListener(e -> {

				String STYLE_SELECTED = "colored";

				Iterator<Component> components = ( (VerticalLayout) layout).iterator();
				while (components.hasNext()) {
					Component c = components.next();
					if (c instanceof HorizontalLayout) {
						if (((HorizontalLayout) c).getComponentCount() >= 1) {
							((HorizontalLayout) c).getComponent(1)
									.removeStyleName(STYLE_SELECTED);
						}
					}
				}
				lbl.addStyleName(STYLE_SELECTED);
				updateSpreadSheet(alum);
			});
			l.setExpandRatio(img, 0.2f);
			l.setExpandRatio(lbl, 0.8f);
			((VerticalLayout) layout).addComponent(l);
		}
	}

	private void updateSpreadSheet(Alumno alum) {
		if (!spreadSheet.getWorkbook().getSheetName(0)
				.equals(alum.getNombre() + " " + alum.getApellidos())) {
			spreadSheet.setSheetName(0,
					alum.getNombre() + " " + alum.getApellidos());
		}
		spreadSheet.refreshAllCellValues();
		int inicio = 1;
		for (int mes = 0; mes < meses.size(); mes++) {
			spreadSheet.createCell(0, inicio + mes, meses.get(mes));
		}
		for (int i = 1; i <= 31; i++) {
			spreadSheet.createCell(i, 0, i);
			spreadSheet.setRowHeight(i, 24);
		}
		for (int mes = 0; mes < meses.size(); mes++) {
			for (int i = 1; i <= 31; i++) {
				spreadSheet.createCell(i, inicio + mes, "Item");
			}
		}
		spreadSheet.refreshAllCellValues();
	}

	@Override
	public Component getCustomComponentForCell(Cell cell, int rowIndex,
			int columnIndex, Spreadsheet spreadsheet, Sheet sheet) {
		if (cell != null &&  cell.getStringCellValue().equals("Item")){
			Button b = new Button(FontAwesome.EDIT);
			b.addStyleName("tiny");
			String mes = meses.get(columnIndex-1);
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MMMM/yyyy",new Locale("ES","es"));
			sdf.setLenient(false);
			Calendar c = Calendar.getInstance();
			
			int year = c.get(Calendar.YEAR);
			
			int month = c.get(Calendar.MONTH);
			
			if ( month > 7 ){
				if ( columnIndex-1 > 3 ){
					year++;
				}
			}else{
				if ( columnIndex-1 <= 3 ){
					year--;
				}
			}
			Date fecha =  new Date();
			boolean fechaValida = false;
			try{
				fecha = sdf.parse(rowIndex + "/" + mes + "/" + year);
				b.setDescription(rowIndex + " de " + mes + " de " + year);
				fechaValida = true;
			}catch( ParseException e ){
				fechaValida = false;
			}
			
			
			c.setTime(fecha);
			
			if (!fechaValida  || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
				b.setReadOnly(true);
				b.setEnabled(false);
				b.setIcon(FontAwesome.TIMES_CIRCLE_O);
			}else{
				b.addClickListener(e->{	AppUI.getCurrent().addWindow(new Window());	});
			}
			
			return b;
		}
                
		return null;
	}

	@Override
	public Component getCustomEditorForCell(Cell cell, int rowIndex,
			int columnIndex, Spreadsheet spreadsheet, Sheet sheet) {
		return null;
	}

	@Override
	public void onCustomEditorDisplayed(Cell cell, int rowIndex,
			int columnIndex, Spreadsheet spreadsheet, Sheet sheet,
			Component customEditor) {

	}

}
