/**
 * NotaPorEvaluacionService.java
 * appEducacional
 * 15/02/2014 20:25:28
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Cita;
import com.app.domain.model.types.Notificacion;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Persona;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.NotificacionRepository;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class NotificacionService {

	/**
	 * Constructor
	 */
	public NotificacionService() {
		super();
	}

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private NotificacionRepository notificacionRepositorio;

	// Servicios gestionados

	@Autowired
	private ProfesorService profesorService;

	@Autowired
	private PadreMadreOTutorService tutorService;

	// Metrodos CRUD

	public Notificacion create() {
		Notificacion nota = new Notificacion();
		nota.setTitulo("");
		nota.setPadreMadreOTutor(new PadreMadreOTutor());
		nota.setLeida(false);
		nota.setContenido("");
		nota.setFecha(new Date());
		return nota;
	}

	public Notificacion findOne(int notificacionId) {
		Assert.isTrue(notificacionId > 0);
		return notificacionRepositorio.findOne(notificacionId);
	}

	public Collection<Notificacion> findAll() {
		return notificacionRepositorio.findAll();
	}

	public void save(Notificacion notificacion) {
		Assert.notNull(notificacion);
		Assert.notNull(notificacion.getPadreMadreOTutor());
		Assert.notNull(notificacion.getProfesor());
		notificacionRepositorio.save(notificacion);
	}

	public void delete(Notificacion notificacion) {
		Assert.notNull(notificacion);
		Assert.isTrue(notificacion.getId() > 0);
		notificacionRepositorio.delete(notificacion);
	}

	// Other business methods

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Notificacion> findTutorEmitidas(PadreMadreOTutor tutor) {
		//Assert.notNull(tutorService.findPrincipal());
		Collection<Notificacion> emitidas = notificacionRepositorio
				.findTutorEmitidas(tutor.getId());
		Iterable<Notificacion> emitidasSinCitas = Iterables.filter(emitidas,
				new Predicate<Notificacion>() {
					public boolean apply(Notificacion p) {
						return !(p instanceof Cita);
					}
				});
		return Lists.newArrayList(emitidasSinCitas);
	}

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Notificacion> findTutorRecibidas(PadreMadreOTutor tutor) {
		//Assert.notNull(tutorService.findPrincipal());
		Collection<Notificacion> emitidas = notificacionRepositorio
				.findTutorRecibidas(tutor.getId());
		Iterable<Notificacion> emitidasSinCitas = Iterables.filter(emitidas,
				new Predicate<Notificacion>() {
					public boolean apply(Notificacion p) {
						return !(p instanceof Cita);
					}
				});
		return Lists.newArrayList(emitidasSinCitas);
	}

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Notificacion> findProfesorEmitidas(Profesor tutor) {
		//Assert.notNull(profesorService.findPrincipal());
		Collection<Notificacion> emitidas = notificacionRepositorio
				.findProfesorEmitidas(tutor.getId());
		Iterable<Notificacion> emitidasSinCitas = Iterables.filter(emitidas,
				new Predicate<Notificacion>() {
					public boolean apply(Notificacion p) {
						return !(p instanceof Cita);
					}
				});
		return Lists.newArrayList(emitidasSinCitas);
	}

	/**
	 * @author David Romero Alcaide
	 * @param tutor
	 */
	public Collection<Notificacion> findProfesorRecibidas(Profesor tutor) {
		//Assert.notNull(profesorService.findPrincipal());
		Collection<Notificacion> emitidas = notificacionRepositorio
				.findProfesorRecibidas(tutor.getId());
		Iterable<Notificacion> emitidasSinCitas = Iterables.filter(emitidas,
				new Predicate<Notificacion>() {
					public boolean apply(Notificacion p) {
						return !(p instanceof Cita);
					}
				});
		return Lists.newArrayList(emitidasSinCitas);
	}
	
	public int getNotificacionesNoLeidas(Persona persona){
		if ( persona instanceof Profesor ){
			Profesor profe = (Profesor) persona;
			List<Notificacion> l1 = Lists.newArrayList(findProfesorEmitidas(profe));
			l1.addAll(findProfesorRecibidas(profe));
			return Lists.newArrayList(Iterables.filter(l1, new Predicate<Notificacion>() {

				@Override
				public boolean apply(Notificacion input) {
					return input.isLeida();
				}
			})).size();
		}else if ( persona instanceof PadreMadreOTutor ){
			PadreMadreOTutor profe = (PadreMadreOTutor) persona;
			List<Notificacion> l1 = Lists.newArrayList(findTutorEmitidas(profe));
			l1.addAll(findTutorRecibidas(profe));
			return Lists.newArrayList(Iterables.filter(l1, new Predicate<Notificacion>() {

				@Override
				public boolean apply(Notificacion input) {
					return input.isLeida();
				}
			})).size();
		}
		return 0;
	}

}
