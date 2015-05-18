/**
 * 
 */
package com.app.ui.user.spreadsheet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.InstanciaCurso;
import com.app.ui.AppUI;
import com.app.ui.user.profesor.presenter.ProfesorPresenter;
import com.app.ui.util.CustomStreamSource;
import com.google.common.collect.Sets;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vaadin.addon.spreadsheet.Spreadsheet;
import com.vaadin.addon.spreadsheet.Spreadsheet.CellValueChangeListener;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.BrowserWindowOpener;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;


@SpringView(name="Evaluar")
/**
 * @author David
 *
 */
public class SpreadSheetView extends CssLayout implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = -711210545781688994L;

	String [] abecedario = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	protected Component layout;

	protected Spreadsheet spreadSheet;

	protected Component tray;

	protected HorizontalLayout layoutGeneral;

	protected Set<Cell> celdasPropias;

	protected Set<Cell> celdasModificadas;
	
	protected int inicioFilas = 7;
	
	@Autowired
	protected ProfesorPresenter presenter;

	protected List<Alumno> alumnos;

	/* (non-Javadoc)
	 * @see com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		celdasPropias = Sets.newHashSet();
		celdasModificadas = Sets.newHashSet();
		alumnos = presenter.getAlumnosProfesor().stream().collect(Collectors.toList());
		layoutGeneral = new HorizontalLayout();
		layoutGeneral.setSizeFull();
		setSizeFull();
		buildSpreadSheet();
		try {
			buildVerticalLayout();
		} catch (IOException e) {
			
			e.printStackTrace();
		}

		layoutGeneral.addComponent(spreadSheet);
		layoutGeneral.addComponent(layout);
		layoutGeneral.setExpandRatio(layout, 0.2f);
		layoutGeneral.setExpandRatio(spreadSheet, 0.8f);
		addStyleName("schedule");;
		addComponent(layoutGeneral);
		tray = buildTray();
		addComponent(tray);
	}

	private void buildVerticalLayout() throws IOException{
		layout = new VerticalLayout();
		layout.addStyleName("valo-menu");


		HorizontalLayout layoutExport = new HorizontalLayout();
		Button exportExcelButton = new Button(FontAwesome.FILE_EXCEL_O);
		exportExcelButton.setDescription("Excel");
		CustomStreamSource source = new CustomStreamSource(spreadSheet);
		String filename = "Cursos.xls";
		StreamResource resource = new StreamResource(source, filename);
		resource.setMIMEType("application/xls");
		resource.getStream().setParameter("Content-Disposition", "attachment; filename=" + filename);
		BrowserWindowOpener opener = new BrowserWindowOpener(resource);
		opener.extend(exportExcelButton);
		Button exportPDFButton = new Button(FontAwesome.FILE_PDF_O);
		exportPDFButton.setDescription("PDF");

		FileDownloader downloader = new FileDownloader(new FileResource(File.createTempFile("temp", "txt")));

		exportPDFButton.addClickListener(e->{
						downloader.extend(exportPDFButton);
						StreamResource resource2 = new StreamResource(new StreamSource(){

									public java.io.InputStream getStream(){
										try {
											return createPDF();
										} catch (DocumentException
												| IOException e) {
											
											e.printStackTrace();
											return new ByteArrayInputStream(new byte[0]);
										}
									}
								}, "Informe.pdf");
						resource2.setMIMEType("application/pdf");
						resource2.getStream().setParameter("Content-Disposition", "attachment; filename="+"Informe.pdf");
						downloader.setFileDownloadResource(resource2);
				});
		exportPDFButton.click();

		layoutExport.addComponents(exportExcelButton,exportPDFButton);

		( (VerticalLayout) layout).addComponent(layoutExport);

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
			try {
				buildVerticalLayout();
			} catch (Exception e1) {
				
				e1.printStackTrace();
			}
		});

		((VerticalLayout) layout).addComponent(box);

		


		for (Alumno alum : alumnos) {
			HorizontalLayout l = new HorizontalLayout();
			MarginInfo marg = new MarginInfo(true, false, false, false);
			l.setMargin(marg);
			l.setSizeFull();
			l.setSpacing(false);
			ThemeResource r = new ThemeResource(
					"img/profile-pic-300px.jpg");
			Image img = new Image(null,r);
			img.removeStyleName("v-caption-on-top");
			img.addStyleName("v-icon");
			img.setWidth(40.0f, Unit.PIXELS);
			img.setHeight(100,Unit.PERCENTAGE);
			l.addComponent(img);
			Label lbl = new Label(alum.getNombre() + " " + alum.getApellidos());
			l.addComponent(lbl);
			l.addLayoutClickListener(e->{
							Iterator<Component> components = ( (VerticalLayout) layout).iterator();
							while ( components.hasNext() ){
								Component c = components.next();
								if ( c instanceof HorizontalLayout ){
									if ( ((HorizontalLayout) c).getComponentCount() >= 1 ){
										((HorizontalLayout) c).getComponent(1).removeStyleName("colored");
									}
								}
							}
							lbl.addStyleName("colored");
							updateSpreadSheet(alum);
					});
			l.setExpandRatio(img, 0.2f);
			l.setExpandRatio(lbl, 0.8f);
			( (VerticalLayout) layout).addComponent(l);
		}
	}

	private InputStream createPDF() throws DocumentException, IOException{
		spreadSheet.refreshAllCellValues();
		String titulo =  "TEST";
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		//We will create output PDF document objects at this point
		Document iText_xls_2_pdf = new Document(PageSize.A4);
		PdfWriter writer = PdfWriter.getInstance(iText_xls_2_pdf,output );
		iText_xls_2_pdf.open();

		Paragraph para=new Paragraph();
		Font font = FontFactory.getFont("Open Sans", 24, Font.BOLD);
		font.setColor(64,67,70);
		para.setFont(font);
		para.setAlignment(Element.ALIGN_CENTER);
		para.add(titulo);

		iText_xls_2_pdf.add(para);

		//Dos lineas en blanco
		iText_xls_2_pdf.add( Chunk.NEWLINE);
		iText_xls_2_pdf.add( Chunk.NEWLINE);

		//-----------------------------------------------------------------------------------------------
		PdfPTable my_table = new PdfPTable(6);

		my_table.setWidthPercentage(100);
		my_table.setSpacingBefore(0f);
		my_table.setSpacingAfter(0f);
		my_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		my_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		font = FontFactory.getFont("Times-Roman", 10, Font.NORMAL);
		Font fontString = FontFactory.getFont("Times-Roman", 8, Font.NORMAL);
		//We will use the object below to dynamically add new data to the table
		PdfPCell table_cell;
		PdfPCell table_cell2;
		for (int ii = 0; ii <= inicioFilas; ii ++){
			if ( ii == 0 ){
				table_cell = new PdfPCell(new Phrase("Evaluacion ",font));
				table_cell2 = new PdfPCell(new Phrase(1+"",font));
				my_table.addCell(table_cell);
				my_table.addCell(table_cell2);
				table_cell = new PdfPCell(new Phrase("Evaluacion ",font));
				table_cell2 = new PdfPCell(new Phrase(2+"",font));
				my_table.addCell(table_cell);
				my_table.addCell(table_cell2);
				table_cell = new PdfPCell(new Phrase("Evaluacion ",font));
				table_cell2 = new PdfPCell(new Phrase(3+"",font));
				my_table.addCell(table_cell);
				my_table.addCell(table_cell2);
			}else{
				for ( int i = 0; i < 6; i += 2 ){
					Cell cell1 = spreadSheet.getCell(inicioFilas+ii,i+1);
					String valueString = "";
					if ( cell1 != null ){
						if ( cell1.getCellType() == Cell.CELL_TYPE_NUMERIC ){
							valueString = cell1.getNumericCellValue() + "";
						} else if ( cell1.getCellType() == Cell.CELL_TYPE_STRING ){
							valueString = cell1.getStringCellValue();
						}
					}
					table_cell = new PdfPCell(new Phrase("Examen " + ii,font));
					table_cell2 = new PdfPCell(new Phrase(valueString,font));
					my_table.addCell(table_cell);
					my_table.addCell(table_cell2);
				}
			}
		}
		
		iText_xls_2_pdf.add(my_table);
		
		//Dos lineas en blanco
		iText_xls_2_pdf.add( Chunk.NEWLINE);
		iText_xls_2_pdf.add( Chunk.NEWLINE);
		
		my_table = new PdfPTable(2);
		my_table.setWidthPercentage(100);
		my_table.setSpacingBefore(0f);
		my_table.setSpacingAfter(0f);
		my_table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		my_table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		table_cell = new PdfPCell(new Phrase("Evaluacion 1",font));
		Cell cell = spreadSheet.getCell("H8");
		FormulaEvaluator evaluator= spreadSheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		CellValue value=null;
		try {
			value=evaluator.evaluate(cell);
		}
		catch (RuntimeException ignore) {
			value = new CellValue(0.0);
		}
		if ( value == null ){
			value = new CellValue(0.0);
		}
		table_cell2 = new PdfPCell(new Phrase(value.getNumberValue()+"",font));
		my_table.addCell(table_cell);
		my_table.addCell(table_cell2);
		table_cell = new PdfPCell(new Phrase("Evaluacion 2",font));
		cell = spreadSheet.getCell("H9");
		evaluator= spreadSheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		value=null;
		try {
			value=evaluator.evaluate(cell);
		}
		catch (RuntimeException ignore) {
			value = new CellValue(0.0);
		}
		if ( value == null ){
			value = new CellValue(0.0);
		}
		table_cell2 = new PdfPCell(new Phrase(value.getNumberValue()+"",font));
		my_table.addCell(table_cell);
		my_table.addCell(table_cell2);
		table_cell = new PdfPCell(new Phrase("Evaluacion 3",font));
		cell = spreadSheet.getCell("H10");
		evaluator= spreadSheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
		value=null;
		try {
			value=evaluator.evaluate(cell);
		}
		catch (RuntimeException ignore) {
			value = new CellValue(0.0);
		}
		if ( value == null ){
			value = new CellValue(0.0);
		}
		table_cell2 = new PdfPCell(new Phrase(value.getNumberValue()+"",font));
		my_table.addCell(table_cell);
		my_table.addCell(table_cell2);

		iText_xls_2_pdf.add(my_table);

		output.flush();
		iText_xls_2_pdf.close();
		output.close();

		return new ByteArrayInputStream(output.toByteArray());
	}

	private void buildSpreadSheet(){
		spreadSheet = new Spreadsheet();
		spreadSheet.setColumnWidth(0, 100);
		spreadSheet.setColumnWidth(2, 100);
		spreadSheet.setColumnWidth(4, 100);
		spreadSheet.setColumnWidth(6, 100);
		//TODO - Añadir las celdas de alumnos
		spreadSheet.addCellValueChangeListener(e->{
						for ( CellReference  cell : e.getChangedCells() ){
							Cell cell2 = spreadSheet.getCell(cell.getRow(), cell.getCol());
							if ( cell2.getCellType() == Cell.CELL_TYPE_NUMERIC ){
								celdasModificadas.add(cell2);
							}
							if ( cell2.getCellType() == Cell.CELL_TYPE_FORMULA ){
								celdasModificadas.add(cell2);
							}
						}
						if ( celdasModificadas.size() > 0 )
							setTrayVisible(true);
				});
		spreadSheet.setImmediate(true);
	}

	private void updateSpreadSheet(Alumno alum){
		String title = alum.getNombre() + " " + alum.getApellidos();
		spreadSheet.setSheetName(0, title);
		for ( Cell cell : celdasPropias ){
			cell.setCellValue("");
		}
		spreadSheet.refreshAllCellValues();
		celdasPropias = Sets.newHashSet();
		celdasModificadas = Sets.newHashSet();
		Random rd = new Random();
		int columnas = 2;
		int evaluaciones = 3;
		int numeroItems = 7;
		int columna = 0;

		for ( int evaluacion = 0; evaluacion < evaluaciones; evaluacion ++ ){
			org.apache.poi.ss.usermodel.Cell cell = spreadSheet.createCell(inicioFilas, columna, "Evaluacion");
			celdasPropias.add(cell);
			org.apache.poi.ss.usermodel.Cell cellEval = spreadSheet.createCell(inicioFilas, columna+1,(evaluacion+1));
			celdasPropias.add(cellEval);
			for ( int fila = 1; fila <= numeroItems; fila ++ ){
				org.apache.poi.ss.usermodel.Cell cell2 = spreadSheet.createCell(inicioFilas+fila, columna, "Examen  " + fila);
				celdasPropias.add(cell2);
				Double nota = ExcelView.randDouble(0,10);
				org.apache.poi.ss.usermodel.Cell cell3 = spreadSheet.createCell(inicioFilas+fila, columna+1, nota);
				celdasPropias.add(cell3);
			}
			String columnaIndicadorInicio = abecedario[columna+1];
			String columnaIndicadorFin = abecedario[columna+1];
			columna +=2;
			int filaInicio = inicioFilas+2;
			int filaFin = inicioFilas+1+numeroItems;

			spreadSheet.createCell(inicioFilas+evaluacion, 6, "Evaluacion "+ (evaluacion+1) );
			Cell celda = spreadSheet.createCell(inicioFilas+evaluacion, 7, 0);
			celda.setCellFormula("ROUND(SUM(" + columnaIndicadorInicio + filaInicio+":"+columnaIndicadorFin+filaFin+")/"+numeroItems+",2)");
			celdasPropias.add(celda);
		}
		Cell celda = spreadSheet.createCell(inicioFilas+evaluaciones, 7, 0);
		celda.setCellFormula("ROUND(SUM(H8:H10)/3,2)");
		celdasPropias.add(celda);
		spreadSheet.refreshCells(celdasPropias);
	}

	private Component buildTray() {
		HorizontalLayout tray = new HorizontalLayout();
		tray.setWidth(100.0f, Unit.PERCENTAGE);
		tray.addStyleName("tray");
		tray.setSpacing(true);
		tray.setMargin(true);

		Label warning = new Label(
				"You have unsaved changes made to the spreadsheet");
		warning.addStyleName("warning");
		warning.addStyleName("icon-attention");
		tray.addComponent(warning);
		tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
		tray.setExpandRatio(warning, 1);

		ClickListener close = new ClickListener() {
					/**
			 * 
			 */
			private static final long serialVersionUID = 8583867471621538991L;

					@Override
					public void buttonClick(final ClickEvent event) {
						setTrayVisible(false);
					}
				};

		Button confirm = new Button("Confirm");
		confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
		confirm.addClickListener(close);
		confirm.addClickListener(new ClickListener(){
					/**
			 * 
			 */
			private static final long serialVersionUID = 4636805930772312381L;

					@Override
					public void buttonClick(final ClickEvent event) {
						Set<Cell> intersection = new HashSet<Cell>(celdasPropias);
						Set<Cell> modificadas = new HashSet<Cell>(celdasModificadas);

						intersection.retainAll(modificadas);
						for ( Cell cell : intersection ){
							if ( cell.getCellType() == Cell.CELL_TYPE_NUMERIC ){
								Notification.show(cell.getNumericCellValue() + "");
							}
							if ( cell.getCellType() == Cell.CELL_TYPE_FORMULA ){
								FormulaEvaluator evaluator= spreadSheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
								CellValue value=null;
								try {
									value=evaluator.evaluate(cell);
								}
								catch (RuntimeException ignore) {
									value = new CellValue(0.0);
								}
								Notification.show(value.getStringValue());
							}
						}
						celdasModificadas = Sets.newHashSet();
					}

				});
		tray.addComponent(confirm);
		tray.setComponentAlignment(confirm, Alignment.MIDDLE_LEFT);

		Button discard = new Button("Discard");
		discard.addClickListener(close);
		discard.addClickListener(e->{AppUI.getCurrent().getNavigator().navigateTo("SpreadSheet2");});
		tray.addComponent(discard);
		tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
		return tray;
	}

	private void setTrayVisible(boolean visible) {
		String styleReveal = "v-animate-reveal";
		if (visible) {
			tray.addStyleName(styleReveal);
		} else {
			tray.removeStyleName(styleReveal);
		}
	}

}
