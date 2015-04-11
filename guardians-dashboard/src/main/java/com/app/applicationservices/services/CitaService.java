/**
 * NotaPorEvaluacionService.java
 * appEducacional
 * 15/02/2014 20:25:28
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;

import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.TimeZone;
import net.fortuna.ical4j.model.TimeZoneRegistry;
import net.fortuna.ical4j.model.TimeZoneRegistryFactory;
import net.fortuna.ical4j.model.ValidationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VTimeZone;
import net.fortuna.ical4j.model.parameter.Cn;
import net.fortuna.ical4j.model.parameter.Role;
import net.fortuna.ical4j.model.property.Attendee;
import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Method;
import net.fortuna.ical4j.model.property.Organizer;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.Status;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.model.property.Version;
import net.fortuna.ical4j.util.UidGenerator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Cita;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Persona;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.CitaRepository;
import com.app.infrastructure.CustomMailSender;
import com.app.infrastructure.exceptions.GeneralException;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class CitaService implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7186451063200256750L;

	/**
	 * Constructor
	 */
	public CitaService() {
		super();
	}

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private CitaRepository citaRepositorio;

	// Servicios gestionados

	@Autowired
	private ProfesorService profesorService;

	@Autowired
	private PadreMadreOTutorService tutorService;

	// Metrodos CRUD

	public Cita create() {
		Cita nota = new Cita();
		nota.setFecha(new Date());
		nota.setConfirmadoProfesor(false);
		nota.setConfirmadoTutor(false);
		nota.setTitulo("");
		nota.setContenido("");
		return nota;
	}

	public Cita findOne(int citaId) {
		Assert.isTrue(citaId > 0);
		return citaRepositorio.findOne(citaId);
	}

	public Collection<Cita> findAll() {
		return citaRepositorio.findAll();
	}

	public void save(Cita cita, Persona organizador) throws GeneralException {
		Assert.notNull(cita);
		Assert.notNull(cita.getPadreMadreOTutor());
		Assert.notNull(cita.getProfesor());
		Assert.isTrue(cita.getFecha().after(
				new Date(System.currentTimeMillis())));
		checkOrganizador(cita, organizador);
		if (cita.getPadreMadreOTutor().getEmail() != null
				&& cita.getProfesor().getEmail() != null
				&& cita.getPadreMadreOTutor().getEmail().trim().compareTo("") != 0
				&& cita.getProfesor().getEmail().trim().compareTo("") != 0) {
			sendCitaViaEmail(cita, organizador);
		}
		citaRepositorio.save(cita);
	}

	/**
	 * @author David Romero Alcaide
	 * @param cita
	 * @param organizador
	 */
	private void checkOrganizador(Cita cita, Persona organizador) {
		if (organizador instanceof PadreMadreOTutor) {
			cita.setConfirmadoTutor(true);
		} else {
			cita.setConfirmadoProfesor(true);
		}
	}

	/**
	 * @author David Romero Alcaide
	 * @param cita
	 * @param organizador
	 * @throws GeneralException
	 */
	private void sendCitaViaEmail(Cita cita, Persona organizador)
			throws GeneralException {

		if (organizador instanceof PadreMadreOTutor) {

			enviarCitaAProfesor(cita);
		} else {

			enviarCitaATutor(cita);
		}
	}

	public void delete(Cita cita) {
		Assert.notNull(cita);
		Assert.isTrue(cita.getId() > 0);
		citaRepositorio.delete(cita);
	}

	// Other business methods

	/**
	 * @author David Romero Alcaide
	 * @param cita
	 * @throws ParserException
	 * @throws IOException
	 * @throws ValidationException
	 * @throws MessagingException
	 */
	private void enviarCitaATutor(Cita cita) throws GeneralException {
		File file;
		try {
			file = crearICS(cita, cita.getProfesor(),
					cita.getPadreMadreOTutor());
		} catch (IOException e) {

			throw new GeneralException(e);
		} catch (ParserException e) {

			throw new GeneralException(e);
		} catch (ValidationException e) {

			throw new GeneralException(e);
		}
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"Spring-Mail.xml");
		CustomMailSender mm = (CustomMailSender) context.getBean("mailMail");
		String emailBody = "<body><h1>Solicitud de Cita</h1><br>"
				+ "<p>El Profesor "
				+ cita.getProfesor().getNombre()
				+ " "
				+ cita.getProfesor().getApellidos()
				+ " quiere tener una cita con usted acerca de"
				+ " un hijo o tutorando suyo</p>. <br><p> La Cita se desea concertar para "
				+ cita.getFecha()
				+ ". Si desea contactar con el profesor este es su email: "
				+ cita.getProfesor().getEmail() + " .</p></body>";
		mm.sendMail("El Profesor " + cita.getProfesor().getNombre() + " "
				+ cita.getProfesor().getApellidos()
				+ " quiere tener una cita con usted.", emailBody, cita
				.getProfesor().getEmail(), cita.getPadreMadreOTutor()
				.getEmail(), file);
	}

	/**
	 * @author David Romero Alcaide
	 * @param cita
	 * @throws ParserException
	 * @throws IOException
	 * @throws ValidationException
	 * @throws MessagingException
	 */
	private void enviarCitaAProfesor(Cita cita) throws GeneralException {
		List<Alumno> alumnos = profesorService
				.getTodosLosAlumnosProfesorConTutor(cita.getProfesor(),
						cita.getPadreMadreOTutor());
		String alumnosPadre = "";

		for (Alumno alum : alumnos) {
			alumnosPadre += "\n" + alum.getNombre() + " " + alum.getApellidos();
		}

		File file;
		try {
			file = crearICS(cita, cita.getPadreMadreOTutor(),
					cita.getProfesor());
		} catch (IOException e) {

			throw new GeneralException(e);
		} catch (ParserException e) {

			throw new GeneralException(e);
		} catch (ValidationException e) {

			throw new GeneralException(e);
		}
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
				"Spring-Mail.xml");
		CustomMailSender mm = (CustomMailSender) context.getBean("mailMail");
		mm.sendMail(
				"El Padre,Madre o Tutor "
						+ cita.getPadreMadreOTutor().getNombre() + " "
						+ cita.getPadreMadreOTutor().getApellidos()
						+ " quiere tener una cita con usted.",
				"<body><h1>Solicitud de Cita</h1><br>"
						+ "<p>El Padre,Madre o Tutor "
						+ cita.getPadreMadreOTutor().getNombre()
						+ " "
						+ cita.getPadreMadreOTutor().getApellidos()
						+ " quiere tener una cita con usted acerca de"
						+ " un alumno suyo</p>.<br><p>Los alumnos son: "
						+ alumnosPadre
						+ " .</p>"
						+ "<br><p> La Cita se desea concertar para "
						+ cita.getFecha()
						+ ". Si desea contactar con el padre, madre o tutor este es su email: "
						+ cita.getPadreMadreOTutor().getEmail()
						+ " .</p></body>", cita.getPadreMadreOTutor()
						.getEmail(), cita.getProfesor().getEmail(), file);
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 * @throws ParserException
	 * @throws IOException
	 * @throws ValidationException
	 */
	private File crearICS(Cita cita, Persona organizador, Persona from)
			throws IOException, ParserException, ValidationException {
		Calendar calendar = new Calendar();
		/*
		 * Establecemos el timezone
		 */
		TimeZoneRegistry registry = TimeZoneRegistryFactory.getInstance()
				.createRegistry();
		TimeZone timezone = registry.getTimeZone("Europe/Madrid");
		VTimeZone tz = ((net.fortuna.ical4j.model.TimeZone) timezone)
				.getVTimeZone();
		calendar.getProperties().add(
				new ProdId("-//Google Inc//Google Calendar 70.9054//EN"));
		calendar.getProperties().add(Version.VERSION_2_0);
		calendar.getProperties().add(CalScale.GREGORIAN);
		calendar.getProperties().add(Method.REQUEST);

		DateTime start = new DateTime(cita.getFecha());
		DateTime end = new DateTime(new Date(
				cita.getFecha().getTime() + 3600000));

		VEvent event = new VEvent(start, end, "Cita entre "
				+ organizador.getNombre() + " y " + from.getNombre());

		/*
		 * initialise as an all-day event..
		 */

		/*
		 * create Organizer object and add it to vEvent
		 */
		Organizer organizer = new Organizer(URI.create("mailto:"
				+ organizador.getEmail()));
		organizer.getParameters().add(
				new Cn(organizador.getNombre() + " "
						+ organizador.getApellidos()));
		event.getProperties().add(organizer);

		/*
		 * add timezone to vEvent
		 */
		event.getProperties().add(tz.getTimeZoneId());

		/*
		 * generate unique identifier and add it to vEvent
		 */
		UidGenerator ug;
		ug = new UidGenerator("uidGen");
		Uid uid = ug.generateUid();
		event.getProperties().add(uid);

		/*
		 * add attendees..
		 */
		Attendee dev1 = new Attendee(URI.create("mailto:" + from.getEmail()));
		dev1.getParameters().add(Role.REQ_PARTICIPANT);
		dev1.getParameters().add(
				new Cn(from.getNombre() + " " + from.getApellidos()));
		event.getProperties().add(dev1);

		event.getProperties().add(
				new Description(organizador.getNombre() + " "
						+ organizador.getApellidos()
						+ " solicita tener una cita con Usted."));

		event.getProperties().add(Status.VEVENT_TENTATIVE);

		calendar.getComponents().add(event);

		File calFile = new File("invite.ics");

		CalendarOutputter outputter = new CalendarOutputter();

		FileOutputStream fout = new FileOutputStream(calFile);
		outputter.output(calendar, fout);

		return calFile;

	}

	// Other business methods

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Cita> findTutorEmitidas(PadreMadreOTutor tutor) {
		//Assert.isTrue(tutorService.findPrincipal().equals(tutor));
		//Assert.notNull(tutorService.findPrincipal());
		Collection<Cita> emitidas = citaRepositorio.findTutorEmitidas(tutor
				.getId());
		return emitidas;
	}

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Cita> findTutorRecibidas(PadreMadreOTutor tutor) {
		//Assert.isTrue(tutorService.findPrincipal().equals(tutor));
		//Assert.notNull(tutorService.findPrincipal());
		Collection<Cita> emitidas = citaRepositorio.findTutorRecibidas(tutor
				.getId());
		return emitidas;
	}

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Cita> findProfesorEmitidas(Profesor tutor) {
		//Assert.notNull(profesorService.findPrincipal());
		//Assert.isTrue(profesorService.findPrincipal().equals(tutor));
		Collection<Cita> emitidas = citaRepositorio.findProfesorEmitidas(tutor
				.getId());
		return emitidas;
	}

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Cita> findProfesorRecibidas(Profesor tutor) {
		//Assert.notNull(profesorService.findPrincipal());
		//Assert.isTrue(profesorService.findPrincipal().equals(tutor));
		Collection<Cita> emitidas = citaRepositorio.findProfesorRecibidas(tutor
				.getId());
		return emitidas;
	}

	/**
	 * @author David Romero Alcaide
	 * @param citaBuscada
	 * @throws GeneralException
	 */
	public void confirmarPorTutor(Cita cita) throws GeneralException {
		Assert.notNull(cita);
		Assert.isTrue(cita.getFecha().after(
				new Date(System.currentTimeMillis())));
		cita.setConfirmadoTutor(true);
		this.save(cita, cita.getPadreMadreOTutor());
	}

	/**
	 * @author David Romero Alcaide
	 * @param citaBuscada
	 * @throws GeneralException
	 */
	public void confirmarPorProfesor(Cita cita) throws GeneralException {
		Assert.notNull(cita);
		Assert.isTrue(cita.getFecha().after(
				new Date(System.currentTimeMillis())));
		cita.setConfirmadoProfesor(true);
		this.save(cita, cita.getProfesor());
	}

}
