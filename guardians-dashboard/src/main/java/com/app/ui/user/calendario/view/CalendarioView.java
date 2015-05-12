/**
* CalendarioView.java
* appEducacionalVaadin
* 27/12/2014 14:13:33
* Copyright David
* com.app.ui.user.calendario
*/
package com.app.ui.user.calendario.view;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.app.domain.model.types.Cita;
import com.app.domain.model.types.Evento;
import com.app.domain.model.types.Profesor;
import com.app.infrastructure.exceptions.GeneralException;
import com.app.presenter.event.EventComunicationBus;
import com.app.ui.user.calendario.presenter.CalendarioPresenter;
import com.app.ui.user.citas.view.CitaLayout;
import com.app.ui.user.citas.view.EventsLayout;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.eventbus.Subscribe;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.shared.MouseEventDetails.MouseButton;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventResize;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.MoveEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;
import com.vaadin.ui.components.calendar.handler.BasicEventMoveHandler;
import com.vaadin.ui.components.calendar.handler.BasicEventResizeHandler;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name=CalendarioView.NAME)
public final class CalendarioView extends CssLayout implements View {

    /**
	 * 
	 */
	private static final long serialVersionUID = -818498578306802869L;
	private Calendar calendar;
    private Component tray;
    @Autowired
    private CalendarioPresenter presenter;
    @Autowired
    private CitaLayout layout;
	
	private TabSheet tabs;
	
	private Cita citaAModificar;
    
    public static final String NAME = "Calendario";
    
    private static final Logger log = Logger.getLogger(CalendarioView.class);

    public CalendarioView() {
    }
    
    @Override
    public void attach(){
    	setSizeFull();
        addStyleName("schedule");
        EventComunicationBus.register(this);

        tabs = new TabSheet();
        tabs.setSizeFull();
        tabs.addStyleName(ValoTheme.TABSHEET_PADDED_TABBAR);

        tabs.addComponent(buildCalendarView());
        tabs.addComponent(buildCatalogView());
	    Cita cita = presenter.create(Profesor.class);
	    cita.setFecha(new Date());
	    layout.setCita(cita);
        tabs.addTab(layout, null, FontAwesome.PLUS);

        addComponent(tabs);

        tray = buildTray();
        addComponent(tray);

        injectMovieCoverStyles();
    }

    @Override
    public void detach() {
        super.detach();
        // A new instance of ScheduleView is created every time it's navigated
        // to so we'll need to clean up references to it on detach.
        EventComunicationBus.unregister(this);
    }

    private void injectMovieCoverStyles() {
        // Add all movie cover images as classes to CSSInject
        String styles = "";
        /*for (NotaPorEvaluacion m : AppUI.getDataProvider().getMovies()) {
            WebBrowser webBrowser = Page.getCurrent().getWebBrowser();

            String bg = "url(VAADIN/themes/" + UI.getCurrent().getTheme()
                    + "/img/event-title-bg.png), url(" + m.toString() + ")";

            // IE8 doesn't support multiple background images
            if (webBrowser.isIE() && webBrowser.getBrowserMajorVersion() == 8) {
                bg = "url(" + m.toString() + ")";
            }

            styles += ".v-calendar-event-" + m.getId()
                    + " .v-calendar-event-content {background-image:" + bg
                    + ";}";
        }*/

        Page.getCurrent().getStyles().add(styles);
    }

    private Component buildCalendarView() {
        VerticalLayout calendarLayout = new VerticalLayout();
        Component c = createHeaderToolbar();
        calendarLayout.addComponent(c);
        calendarLayout.setCaption("Calendario");
        calendarLayout.setMargin(true);

        calendar = new Calendar(new EventProvider());
        calendar.setFirstVisibleDayOfWeek(1);
        calendar.setWidth(100.0f, Unit.PERCENTAGE);
        calendar.setHeight(530.0f, Unit.PIXELS);
        calendar.setHandler(new EventClickHandler() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 3737924168453802999L;

			@Override
            public void eventClick(final EventClick event) {
                setTrayVisible(false);
                CitaEvent movieEvent = (CitaEvent) event.getCalendarEvent();
                CitaLayout layout = new CitaLayout();
                layout.setCita(movieEvent.getCita());
                layout.setReadOnly(true);
                layout.getFooter().setVisible(false);
                Window w = new Window("Ver Evento", layout);
                w.setModal(true);
                w.setHeight(70f, Unit.PERCENTAGE);
                w.setWidth(60, Unit.PERCENTAGE);
                getUI().addWindow(w);
            }
        });
        calendarLayout.addComponent(calendar);

        calendar.setFirstVisibleHourOfDay(7);
        calendar.setLastVisibleHourOfDay(23);

        calendar.setHandler(new BasicEventMoveHandler() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 3564440530947198403L;

			@Override
            public void eventMove(final MoveEvent event) {
                CalendarEvent calendarEvent = event.getCalendarEvent();
                if (calendarEvent instanceof CitaEvent) {
                    CitaEvent editableEvent = (CitaEvent) calendarEvent;

                    Date newFromTime = event.getNewStart();

                    // Update event dates
                    long length = editableEvent.getEnd().getTime()
                            - editableEvent.getStart().getTime();
                    setDates(editableEvent, newFromTime,
                            new Date(newFromTime.getTime() + length));
                    citaAModificar = editableEvent.cita;
                    setTrayVisible(true);
                }
            }

            protected void setDates(final CitaEvent event, final Date start,
                    final Date end) {
                event.start = start;
                event.end = end;
                event.getCita().setFecha(start);
            }
        });
        calendar.setHandler(new BasicEventResizeHandler() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -304268054993586332L;

			@Override
            public void eventResize(final EventResize event) {
                Notification
                        .show("You're not allowed to change the movie duration");
            }
        });

        java.util.Calendar initialView = java.util.Calendar.getInstance();
        initialView.add(java.util.Calendar.DAY_OF_WEEK,
                -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
        
        int primerDiaMes = 1;
        int mesActual  = initialView.get(java.util.Calendar.MONTH)+1;
        int anioActual = initialView.get(java.util.Calendar.YEAR);
        int ultimoDiaMes = initialView.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
			Date inicioMes = sdf.parse(primerDiaMes+"/"+mesActual +"/"+anioActual);
			Date finMes = sdf.parse(ultimoDiaMes+"/"+mesActual +"/"+anioActual);
	        
	        calendar.setStartDate(inicioMes);

	        calendar.setEndDate(finMes);
		} catch (ParseException e) {
			
			e.printStackTrace();
		}

        

        return calendarLayout;
    }
    
    /**
	 * @author David
	 */
	private Component createHeaderToolbar() {
		HorizontalLayout header = new HorizontalLayout();
		header.addStyleName(ValoTheme.WINDOW_TOP_TOOLBAR);
		header.setWidth(100,Unit.PERCENTAGE);
		Button monthView = new Button("Vista Mensual",
				new ClickListener() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 5594631503767611801L;

					@Override
					public void buttonClick(final ClickEvent event) {
						java.util.Calendar initialView = java.util.Calendar.getInstance();
				        initialView.add(java.util.Calendar.DAY_OF_WEEK,
				                -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
						int primerDiaMes = 1;
				        int mesActual  = initialView.get(java.util.Calendar.MONTH)+1;
				        int anioActual = initialView.get(java.util.Calendar.YEAR);
				        int ultimoDiaMes = initialView.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
				        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				        try {
							Date inicioMes = sdf.parse(primerDiaMes +"/"+mesActual +"/"+anioActual);
							Date finMes = sdf.parse(ultimoDiaMes + "/"+mesActual +"/"+anioActual);
					        
					        calendar.setStartDate(inicioMes);

					        calendar.setEndDate(finMes);
						} catch (ParseException e) {
							
							e.printStackTrace();
						}
					}
				});
		monthView.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		monthView.addStyleName(ValoTheme.BUTTON_SMALL);
		Button weeklyView = new Button("Vista Semanal");
		weeklyView.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6566647235327528652L;

			@Override
			public void buttonClick(ClickEvent event) {
		        java.util.Calendar initialView = java.util.Calendar.getInstance();
		        initialView.add(java.util.Calendar.DAY_OF_WEEK,
		                -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
		        int mesActual  = initialView.get(java.util.Calendar.MONTH);
		        int anioActual = initialView.get(java.util.Calendar.YEAR);
		        int primerDiaSemana = initialView.getActualMinimum(java.util.Calendar.DAY_OF_WEEK);
		        int ultimoDiaSemana = initialView.getActualMaximum(java.util.Calendar.DAY_OF_WEEK);
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        try {
					Date inicioMes = sdf.parse(+primerDiaSemana+"/"+mesActual +"/"+anioActual);
					Date finMes = sdf.parse(ultimoDiaSemana+"/"+mesActual +"/"+anioActual);
			        
			        calendar.setStartDate(inicioMes);

			        calendar.setEndDate(finMes);
				} catch (ParseException e) {
					
					e.printStackTrace();
				}
			}
		});
				
		weeklyView.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		weeklyView.addStyleName(ValoTheme.BUTTON_SMALL);
		Button nextMonth = new Button(FontAwesome.ARROW_RIGHT);
		nextMonth.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		nextMonth.addStyleName(ValoTheme.BUTTON_SMALL);
		nextMonth.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -4200605888795359120L;

			@Override
			public void buttonClick(ClickEvent event) {
				java.util.Calendar initialView = java.util.Calendar.getInstance();
				initialView.setTime(calendar.getStartDate());
		        initialView.add(java.util.Calendar.DAY_OF_WEEK,
		                -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
		        initialView.add(java.util.Calendar.MONTH, 1);
				int primerDiaMes = 1;
		        int mesActual  = initialView.get(java.util.Calendar.MONTH)+1;
		        int anioActual = initialView.get(java.util.Calendar.YEAR);
		        int ultimoDiaMes = initialView.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        try {
					Date inicioMes = sdf.parse(primerDiaMes +"/"+mesActual +"/"+anioActual);
					Date finMes = sdf.parse(ultimoDiaMes + "/"+mesActual +"/"+anioActual);
			        
			        calendar.setStartDate(inicioMes);

			        calendar.setEndDate(finMes);
				} catch (ParseException e) {
					log.error(e.getMessage());
				}
			}
		});
		Button prevMonth = new Button(FontAwesome.ARROW_LEFT);
		prevMonth.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		prevMonth.addStyleName(ValoTheme.BUTTON_SMALL);
		prevMonth.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 6490136372196944676L;

			@Override
			public void buttonClick(ClickEvent event) {
				java.util.Calendar initialView = java.util.Calendar.getInstance();
				initialView.setTime(calendar.getStartDate());
		        initialView.add(java.util.Calendar.DAY_OF_WEEK,
		                -initialView.get(java.util.Calendar.DAY_OF_WEEK) + 1);
				int primerDiaMes = 1;
		        int mesActual  = initialView.get(java.util.Calendar.MONTH);
		        int anioActual = initialView.get(java.util.Calendar.YEAR);
		        int ultimoDiaMes = initialView.getActualMaximum(java.util.Calendar.DAY_OF_MONTH);
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        try {
					Date inicioMes = sdf.parse(primerDiaMes +"/"+mesActual +"/"+anioActual);
					Date finMes = sdf.parse(ultimoDiaMes + "/"+mesActual +"/"+anioActual);
			        
			        calendar.setStartDate(inicioMes);

			        calendar.setEndDate(finMes);
				} catch (ParseException e) {
					log.error(e.getMessage());
				}
			}
		});
		HorizontalLayout footersButtons = new HorizontalLayout();
		footersButtons.addComponent(prevMonth);
		footersButtons.addComponent(monthView);
		footersButtons.addComponent(weeklyView);
		footersButtons.addComponent(nextMonth);
		footersButtons.setSpacing(true);
		header.addComponent(footersButtons);
		header.setComponentAlignment(footersButtons, Alignment.TOP_RIGHT);
		header.setHeightUndefined();
		return header;
		
	}

    private Component buildCatalogView() {
        CssLayout catalog = new CssLayout();
        catalog.setCaption("Catalogo");
        catalog.addStyleName("catalog");


        List<Cita> allCitas = presenter.findAllCitas();
		
		List<com.app.utility.Event> events = Lists.newArrayList();
		events.addAll(Lists.newArrayList(Iterables.transform(allCitas, new Function<Cita, com.app.utility.Event>() {

			@Override
			public com.app.utility.Event apply(Cita input) {
				com.app.utility.Event event = new com.app.utility.Event();
				event.setDate(input.getFecha());
				event.setDescription(input.getContenido());
				event.setTitle(input.getContenido());
				event.setType("Cita");
				return event;
			}

			
		})));
		List<Evento> eventos = presenter.findAllEventos();
		events.addAll(Lists.newArrayList(Iterables.transform(eventos, new Function<Evento, com.app.utility.Event>() {

			@Override
			public com.app.utility.Event apply(Evento input) {
				com.app.utility.Event event = new com.app.utility.Event();
				event.setDate(input.getFecha());
				event.setDescription(input.getMateria().getAsignatura().getNombre());
				event.setTitle(" Nuevo Evento ");
				event.setType("Evento");
				return event;
			}

			
		})));
        
        
        for (final com.app.utility.Event evento : events) {
            VerticalLayout frame = new VerticalLayout();
            frame.addStyleName("frame");
            frame.setWidthUndefined();

            // Create an instance of our stream source.
            StreamResource.StreamSource imagesource = new StreamSource() {
				
				/**
				 * 
				 */
				private static final long serialVersionUID = 8397701244024814903L;

				@Override
				public InputStream getStream() {
					InputStream myInputStream = new ByteArrayInputStream(presenter.getProfesor().getImagen()); 
					return myInputStream;
				}
			};
            
            StreamResource resource = new StreamResource(imagesource, "foto");
            
            Image poster = new Image(null, resource);
            poster.setWidth(100.0f, Unit.PIXELS);
            poster.setHeight(145.0f, Unit.PIXELS);
            frame.addComponent(poster);

            Label titleLabel = new Label(evento.getTitle());
            titleLabel.setContentMode(ContentMode.HTML);
            titleLabel.setWidth(120.0f, Unit.PIXELS);
            frame.addComponent(titleLabel);

            frame.addLayoutClickListener(new LayoutClickListener() {
                /**
				 * 
				 */
				private static final long serialVersionUID = 2854032716967906972L;

				@Override
                public void layoutClick(final LayoutClickEvent event) {
                    if (event.getButton() == MouseButton.LEFT) {
                    	setTrayVisible(false);
                        EventsLayout layout = new EventsLayout(evento);
                        layout.setReadOnly(true);
                        Window w = new Window("Ver Evento", layout);
                        w.setModal(true);
                        w.setHeight(70f, Unit.PERCENTAGE);
                        w.setWidth(60, Unit.PERCENTAGE);
                        getUI().addWindow(w);
                    }
                }
            });
            catalog.addComponent(frame);
        }
        return catalog;
    }

    private Component buildTray() {
        final HorizontalLayout tray = new HorizontalLayout();
        tray.setWidth(100.0f, Unit.PERCENTAGE);
        tray.addStyleName("tray");
        tray.setSpacing(true);
        tray.setMargin(true);

        Label warning = new Label(
                "Tienes cambios sin guardar");
        warning.addStyleName("warning");
        warning.addStyleName("icon-attention");
        tray.addComponent(warning);
        tray.setComponentAlignment(warning, Alignment.MIDDLE_LEFT);
        tray.setExpandRatio(warning, 1);

        ClickListener close = new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = -6213698005799918841L;

			@Override
            public void buttonClick(final ClickEvent event) {
                setTrayVisible(false);
            }
        };

        Button confirm = new Button("Guardar");
        confirm.addStyleName(ValoTheme.BUTTON_PRIMARY);
        confirm.addClickListener(close);
        confirm.addClickListener(new ClickListener() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 7032067703526302114L;

			@Override
			public void buttonClick(ClickEvent event) {
				try {
					presenter.save(citaAModificar, Profesor.class);
					showTab(0);
				} catch (GeneralException e) {
					Notification.show("Error " + e.getMessage() , Notification.Type.ERROR_MESSAGE);
					e.printStackTrace();
				}
			}
		});
        tray.addComponent(confirm);
        tray.setComponentAlignment(confirm, Alignment.MIDDLE_LEFT);

        Button discard = new Button("Cancelar");
        discard.addClickListener(close);
        discard.addClickListener(new ClickListener() {
            /**
			 * 
			 */
			private static final long serialVersionUID = 214543289675793525L;

			@Override
            public void buttonClick(final ClickEvent event) {
                calendar.markAsDirty();
            }
        });
        tray.addComponent(discard);
        tray.setComponentAlignment(discard, Alignment.MIDDLE_LEFT);
        return tray;
    }

    private void setTrayVisible(final boolean visible) {
        final String styleReveal = "v-animate-reveal";
        if (visible) {
            tray.addStyleName(styleReveal);
        } else {
            tray.removeStyleName(styleReveal);
        }
    }

    @Subscribe
    public void browserWindowResized(com.app.presenter.event.EventComunication.BrowserResizeEvent event) {
        if (Page.getCurrent().getBrowserWindowWidth() < 800) {
            calendar.setEndDate(calendar.getStartDate());
        }
    }

    @Override
    public void enter(final ViewChangeEvent event) {
    }

    private class EventProvider implements CalendarEventProvider {

        /**
		 * 
		 */
		private static final long serialVersionUID = 2424308919744567587L;

		@Override
        public List<CalendarEvent> getEvents(final Date startDate,
                final Date endDate) {
            // Transactions are dynamically fetched from the backend service
            // when needed.
        	JPAContainer<Cita> container = (JPAContainer<Cita>) presenter.findAllBetweenDates(startDate,endDate);
            List<CalendarEvent> result = new ArrayList<CalendarEvent>();
            for (Object itemId : container.getItemIds()) {
            	Cita cita = container.getItem(itemId).getEntity();
            	Date end = cita.getFecha();
            	GregorianCalendar calendar = new GregorianCalendar();
            	calendar.setTime(end);
            	calendar.add(GregorianCalendar.HOUR, 2);
            	end = new Date(calendar.getTimeInMillis());
                result.add(new CitaEvent(cita.getFecha(), end , cita));
            }
            return result;
        }
    }
    // Create a dynamically updating content for the popup
    public class PopupTextFieldContent implements PopupView.Content {
        /**
		 * 
		 */
		private static final long serialVersionUID = 3224954237082927252L;
		private final HorizontalLayout layout;
        private final TextField textField = new TextField(
                "Minimized HTML content", "Click to edit");
 
        public PopupTextFieldContent(Date fecha) {
        	textField.setValue(fecha.toString());
            layout = new HorizontalLayout(textField);
        }
 
        @Override
        public final Component getPopupComponent() {
            return layout;
        }
 
        @Override
        public final String getMinimizedValueAsHTML() {
            return textField.getValue();
        }
    };

    public final class CitaEvent implements CalendarEvent {

        /**
		 * 
		 */
		private static final long serialVersionUID = 7764193022529576405L;
		private Date start;
        private Date end;
        private Cita cita;

        public CitaEvent(final Date start, final Date end, final Cita cita) {
            this.start = start;
            this.end = end;
            this.cita = cita;
        }

        @Override
        public Date getStart() {
            return start;
        }

        @Override
        public Date getEnd() {
            return end;
        }

        @Override
        public String getDescription() {
            return "";
        }

        @Override
        public String getStyleName() {
            return String.valueOf(cita.getId());
        }

        @Override
        public boolean isAllDay() {
            return false;
        }

        public Cita getCita() {
            return cita;
        }

        public void setCita(final Cita cita) {
            this.cita = cita;
        }

        public void setStart(final Date start) {
            this.start = start;
        }

        public void setEnd(final Date end) {
            this.end = end;
        }

        @Override
        public String getCaption() {
            return cita.getContenido();
        }

    }
    
    public void showTab(Integer tab){
    	tabs.setSelectedTab(tab);
    }

}
