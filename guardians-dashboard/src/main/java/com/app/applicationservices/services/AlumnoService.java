/**
 * AlumnoService.java
 * appEducacional
 * 27/01/2014 09:06:00
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

/**
 * imports
 */
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.Set;

import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.Asignatura;
import com.app.domain.model.types.Curso;
import com.app.domain.model.types.InstanciaCurso;
import com.app.domain.model.types.ItemEvaluable;
import com.app.domain.model.types.Materia;
import com.app.domain.model.types.NotaPorEvaluacion;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Profesor;
import com.app.domain.model.types.itemsevaluables.Actividad;
import com.app.domain.model.types.itemsevaluables.Examen;
import com.app.domain.model.types.itemsevaluables.FaltaDeAsistencia;
import com.app.domain.model.types.itemsevaluables.Retraso;
import com.app.domain.model.types.itemsevaluables.Trabajo;
import com.app.domain.repositories.AlumnoRepository;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Transactional
@Service
/**
 * @author David Romero Alcaide
 *
 */
public class AlumnoService {

	/**
	 * Constructor
	 */
	public AlumnoService() {
		super();

	}
	
	/**
	 * Log
	 */
	private static final Logger LOGGER = Logger
			.getLogger(AlumnoService.class);

	@Autowired
	/**
	 * Repositorio para interactuar con la base de datos
	 */
	private AlumnoRepository alumnoRepositorio;

	// Servicios gestionados

	@Autowired
	/**
	 * Servicio para el curso del alumno
	 */
	private InstanciaCursoService cursoService;
	
	@Autowired
	/**
	 * Servicio para el curso del alumno
	 */
	private CursoAcademicoService cursoAcademicoService;

	@Autowired
	/**
	 * Servicio para el item evaluable falta de asistencia
	 */
	private RetrasoService retrasoService;

	@Autowired
	/**
	 * 
	 */
	private ProfesorService profesorService;

	@Autowired
	/**
	 * 
	 */
	private NotaPorEvaluacionService notaPorEvaluacionService;

	@Autowired
	/**
	 * 
	 */
	private EvaluacionService evaluacionService;

	@Autowired
	/**
	 * 
	 */
	private PadreMadreOTutorService tutorService;

	// Application context
	@Autowired
	/**
	 * 
	 */
	private ApplicationContext appContext;

	// Métodos CRUD

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Alumno create() {
		Alumno a = new Alumno();
		return a;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Alumno> findAll() {
		List<Alumno> ite;
		ite = alumnoRepositorio.findAll();

		return ite;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param alumnoId
	 * @return
	 */
	public Alumno findOne(int alumnoId) {
		Assert.isTrue(alumnoId > 0);
		Alumno a = alumnoRepositorio.findOne(alumnoId);
		Assert.notNull(a.getCursos());
		Assert.notEmpty(a.getCursos());
		Assert.notNull(a.getPadresMadresOTutores());
		Assert.notEmpty(a.getPadresMadresOTutores());
		Assert.notNull(a.getItemsEvaluables());
		return a;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param alumno
	 */
	public void save(Alumno alumno) {
		Assert.notNull(alumno);
		alumnoRepositorio.save(alumno);
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param alumno
	 */
	public void delete(Alumno alumno) {
		Assert.notNull(alumno);
		Assert.isTrue(alumno.getId() > 0);
		alumnoRepositorio.delete(alumno);
	}

	// Otros métodos de negocio

	/**
	 * @author David Romero Alcaide
	 * @param a
	 * @param alumno
	 */
	public void establecerRetraso(Materia a, Alumno alumno) {
		Assert.notNull(a, "pasarLista.asignatura");
		Assert.notNull(alumno, "pasarLista.idAlumno");
		String except = "pasarLista.cursoAlum";
		Assert.isTrue(a.getCurso().getAlumnos().contains(alumno),
				except);
		Assert.isTrue(!(existeRetrasoEnAsignaturaParaHoy(a, alumno)),
				"pasarLista.yaExiste");

		Assert.isTrue(a.getProfesor().isIdentidadConfirmada(),
				except);

		/*
		 *  Es necesario que este creado el dia
		 */

		ItemEvaluable item = retrasoService.create();

		item.setFecha(new Date(System.currentTimeMillis()));
		item.setMateria(a);
		item.setCalificacion(0.0);
		item.setEvaluacion(evaluacionService.findByDate(new Date(System
				.currentTimeMillis())));
		retrasoService.save(item);
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 * @param a
	 * @param asig
	 */
	private boolean existeRetrasoEnAsignaturaParaHoy(Materia asig, Alumno a) {
		boolean result = false;
		Assert.isTrue(asig.getProfesor().isIdentidadConfirmada());
		for (ItemEvaluable items : a.getItemsEvaluables()) {
			if (items instanceof Retraso && items.getMateria().equals(asig)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * Obtener los items evaluables de un alumno
	 * 
	 * @author David Romero Alcaide
	 * @param alum
	 * @return
	 */
	public List<ItemEvaluable> obtenerItemsAlumno(Alumno alum) {
		return Lists.newArrayList(alumnoRepositorio.getAllItemsEvaluables(alum
				.getId()));
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @return
	 */
	public List<NotaPorEvaluacion> obtenerNotasPorEvaluacion(Alumno alum,Materia asignatura) {
		Map<Class<? extends ItemEvaluable>, Integer> criterios = asignatura.getCriteriosDeEvaluacion();
		Assert.isTrue(criterios.size() > 0);
		List<NotaPorEvaluacion> notas = Lists.newArrayList();
		List<List<Integer>> contadores = new ArrayList<List<Integer>>();
		List<List<Double>> medias = new ArrayList<List<Double>>();
		iniciarContadores(medias, contadores);
		calcularMediasDeNotas(alum, asignatura, contadores, medias);
		List<NotaPorEvaluacion> notasDelAlumnoEnAsignatura = notaPorEvaluacionService
				.getNotasPorEvaluacion(alum, asignatura);
		Assert.isTrue(notasDelAlumnoEnAsignatura.size() == 3);
		double nota = -1;
		
		nota = calcularNotaEvaluacion(medias.get(0), contadores.get(0), criterios);
		NotaPorEvaluacion nota1 = notasDelAlumnoEnAsignatura.get(0);
		nota1.setNotaFinal(nota);
		nota1.setAlumno(alum);
		nota1.setMateria(asignatura);
		notaPorEvaluacionService.save(nota1);

		this.save(alum);
		notas.add(nota1);

		nota = calcularNotaEvaluacion(medias.get(1), contadores.get(1), criterios);
		NotaPorEvaluacion nota2 = notasDelAlumnoEnAsignatura.get(1);
		nota2.setNotaFinal(nota);
		nota2.setAlumno(alum);
		nota2.setMateria(asignatura);
		notaPorEvaluacionService.save(nota2);

		this.save(alum);
		notas.add(nota2);
		nota = calcularNotaEvaluacion(medias.get(2), contadores.get(2), criterios);
		NotaPorEvaluacion nota3 = notasDelAlumnoEnAsignatura.get(2);
		nota3.setNotaFinal(nota);
		nota3.setAlumno(alum);
		nota3.setMateria(asignatura);
		notaPorEvaluacionService.save(nota3);

		this.save(alum);
		notas.add(nota3);
		return notas;
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @param asignatura
	 * @param contadores
	 * @param medias
	 */
	private void calcularMediasDeNotas(Alumno alum, Materia
			asignatura,
			List<List<Integer>> contadores, List<List<Double>> medias) {
		for (ItemEvaluable item : obtenerItemsAlumnoAsignatura(alum, asignatura)) {
			calcularMediaItemsEvaluacion(medias, contadores, item, 0);
			calcularMediaItemsEvaluacion(medias, contadores, item, 1);
			calcularMediaItemsEvaluacion(medias, contadores, item, 2);
		}
	}

	/**
	 * Inicia los contadores y las sumas a 0
	 * 
	 * @author David Romero Alcaide
	 * @param medias
	 * @param contadores
	 */
	private void iniciarContadores(List<List<Double>> medias,
			List<List<Integer>> contadores) {
		for (int i = 0; i < 3; i++) {
			medias.add(i, new ArrayList<Double>());
			contadores.add(i, new ArrayList<Integer>());
			for (int ii = 0; ii < 8; ii++) {
				medias.get(i).add(ii, 0.0);
				contadores.get(i).add(ii, 0);
			}
		}
	}

	/**
	 * Obtiene las medias de los items
	 * 
	 * @author David Romero Alcaide
	 * @param medias
	 * @param contadores
	 * @param item
	 * @param evaluacion
	 */
	private void calcularMediaItemsEvaluacion(List<List<Double>> medias,
			List<List<Integer>> contadores, ItemEvaluable item, int evaluacion) {
		Assert.notNull(item.getEvaluacion());
		String pack = "com.app.domain.model.types.itemsevaluables.";
		if (item.getEvaluacion().getIndicador() == (evaluacion + 1)) {
			if (item.getClass()
					.getName()
					.compareTo(
							pack
									+ "Examen") == 0) {
				Double notaVieja = medias.get(evaluacion).get(4);
				medias.get(evaluacion).set(4,
						notaVieja + item.getCalificacion());
				int contAntiguo = contadores.get(evaluacion).get(4);
				contadores.get(evaluacion).set(4, contAntiguo + 1);
			}
			if (item.getClass()
					.getName()
					.compareTo(
							pack
									+ "Actividad") == 0) {
				Double notaVieja = medias.get(evaluacion).get(1);
				medias.get(evaluacion).set(1,
						notaVieja + item.getCalificacion());
				int contAntiguo = contadores.get(evaluacion).get(1);
				contadores.get(evaluacion).set(1, contAntiguo + 1);
			}
			if (item.getClass()
					.getName()
					.compareTo(
							pack
									+ "Trabajo") == 0) {
				Double notaVieja = medias.get(evaluacion).get(7);
				medias.get(evaluacion).set(7,
						notaVieja + item.getCalificacion());
				int contAntiguo = contadores.get(evaluacion).get(7);
				contadores.get(evaluacion).set(7, contAntiguo + 1);
			}
		}
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @param asignatura
	 * @return
	 */
	public Collection<ItemEvaluable> obtenerItemsAlumnoAsignatura(Alumno alum,
			Materia asignatura) {
		Assert.notNull(asignatura);
		Assert.notNull(alum);
		return alumnoRepositorio.getAllItemsEvaluablesByAsignatura(
				alum.getId(), asignatura.getId());
	}

	/**
	 * Calcula la nota para una asignatura
	 * 
	 * @author David Romero Alcaide
	 * @param medias
	 * @param contadores
	 * @param criterios
	 * @param nota
	 * @return
	 */
	private double calcularNotaEvaluacion(List<Double> medias,
			List<Integer> contadores, Map<Class<? extends ItemEvaluable>, Integer> criterios) {
		double mediaExamen, mediaTrabajo, mediaActividad;
		if (contadores.get(4) == 0) {
			mediaExamen = 0.0;
		} else {
			mediaExamen = medias.get(4) / contadores.get(4);
		}
		if (contadores.get(7) == 0) {
			mediaTrabajo = 0.0;
		} else {
			mediaTrabajo = medias.get(7) / contadores.get(7);
		}
		if (contadores.get(1) == 0) {
			mediaActividad = 0.0;
		} else {
			mediaActividad = medias.get(1) / contadores.get(1);
		}
		double notaTotal = 0.0;

		Set<Entry<Class<? extends ItemEvaluable>, Integer>> conjuntoPares = criterios.entrySet();
		for (Entry<Class<? extends ItemEvaluable>, Integer> entry : conjuntoPares) {
			if (entry.getKey().equals(Examen.class)) {
				notaTotal += mediaExamen * entry.getValue() / 100;
			}

			if (entry.getKey().equals(Trabajo.class)) {
				notaTotal += mediaTrabajo * entry.getValue() / 100;
			}

			if (entry.getKey().equals(Actividad.class)) {
				notaTotal += mediaActividad * entry.getValue() / 100;
			}
		}
		
		return notaTotal;
	}

	/**
	 * @author David Romero Alcaide
	 * @param alumnoId
	 * @return
	 */
	public List<PadreMadreOTutor> getTutores(int alumnoId) {
		Assert.isTrue(alumnoId > 0);
		Alumno alum = findOne(alumnoId);
		Assert.notNull(alum);
		return Lists.newArrayList(alum.getPadresMadresOTutores());
	}

	/**
	 * @author David Romero Alcaide
	 * @param item
	 * @param alumn
	 * @param tipoNota
	 * @param calificacion
	 */
	public void establecerItemEvaluacion(Date fecha, Alumno alumn,
			Materia asig, String tipoNota, int calificacion) {
		com.app.applicationservices.services.Service servicio = (com.app.applicationservices.services.Service) appContext
				.getBean(tipoNota + "Service");

		ItemEvaluable item = servicio.create();
		if (calificacion < 5) {
			item.setTitulo(tipoNota + ", No trabaja");
		} else {
			item.setTitulo(tipoNota + ", Si trabaja");
		}

		item.setFecha(fecha);
		item.setMateria(asig);
		item.setCalificacion(calificacion);
		item.setEvaluacion(evaluacionService.findByDate(fecha));
		servicio.save(item);
	}

	/**
	 * @author David Romero Alcaide
	 * @param a
	 * @return
	 */
	public Collection<Profesor> getProfesores(Alumno alum) {
		return alum.getCursos().stream().filter(curso->{
			return curso.getCursoAcademico().equals(cursoAcademicoService.findActual());
		}).map(curso->{ return curso.getProfesor(); }).collect(Collectors.toSet());
	}

	/**
	 * @author David Romero Alcaide
	 * @param alumnosImportados
	 * @throws ParseException
	 */
	public void guardarAlumnosImportados(List<String[]> alumnosImportados,Profesor p)
			throws ParseException {
		for (String[] alumnoParseado : alumnosImportados) {
			Alumno a = this.create(alumnoParseado,p);

			throw new NotImplementedException();
		}
	}

	/**
	 * @author David Romero Alcaide
	 * @param alumnoParseado
	 * @return
	 * @throws ParseException
	 */
	private Alumno create(String[] alumnoParseado,Profesor p) throws ParseException {
		Alumno a = create();
		a.setNombre(alumnoParseado[0].trim());
		a.setApellidos(alumnoParseado[1].trim());
		String curso = alumnoParseado[2].trim();
		int nivel = Integer.valueOf(curso.split(" ")[0]);
		String nivelE = curso.split(" ")[1];
		char iden = curso.split(" ")[2].charAt(0);
		InstanciaCurso c = cursoService.find(nivel, nivelE, iden);
		Assert.notNull(c);
		Assert.isTrue(profesorService.getCursosImparteDocencia(p).contains(c));
		a.getCursos().add(c);
		SimpleDateFormat sp = new SimpleDateFormat("mm/dd/yyyy");
		a.setFechaNacimiento(sp.parse(alumnoParseado[3]));
		try {
			a.setPendiente(alumnoParseado[4].trim());
		} catch (ArrayIndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(),e);
			a.setPendiente(" ");
		}
		try {
			a.setRepiteCurso(alumnoParseado[5].trim());
		} catch (ArrayIndexOutOfBoundsException e) {
			LOGGER.error(e.getMessage(),e);
			a.setRepiteCurso(" ");
		}
		return a;
	}

	/**
	 * @author David Romero Alcaide
	 * @param item
	 * @param alumn
	 * @param tipoNota
	 * @param calificacion
	 */
	public void crearItemEvaluacion(Date fecha, Alumno alumn, Materia asig,
			String tipoNota, String titulo) {
		com.app.applicationservices.services.Service servicio = (com.app.applicationservices.services.Service) appContext
				.getBean(tipoNota + "Service");
		ItemEvaluable item = servicio.create();
		item.setTitulo(titulo);
		item.setFecha(fecha);
		item.setMateria(asig);
		item.setCalificacion(0);
		item.setEvaluacion(evaluacionService.findByDate(fecha));
		servicio.save(item);
	}

	/**
	 * @author David Romero Alcaide
	 * @param nombre
	 * @param apellidos
	 * @param c
	 * @param fechaNacimiento
	 * @return
	 */
	public Alumno findAlumno(String nombre, String apellidos, Curso c,
			Date fechaNacimiento) {
		Alumno alumnoBuscado = null;
		PadreMadreOTutor tutor = tutorService.findPrincipal();
		Assert.notNull(tutor);
		Assert.isTrue(tutor.isIdentidadConfirmada());
		Collection<Alumno> primerFiltro = alumnoRepositorio
				.findByCursoYFechaNacimiento(c.getId(), fechaNacimiento);
		for (Alumno a : primerFiltro) {
			if (a.getApellidos().equals(apellidos)
					&& a.getApellidos().compareTo(apellidos) == 0
					&& a.getNombre().equals(nombre)
					&& a.getNombre().compareTo(nombre) == 0
					&& !a.getPadresMadresOTutores().contains(tutor)
					&& a.getPadresMadresOTutores().size() <= 2) {
				alumnoBuscado = a;
			}
		}
		return alumnoBuscado;
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @param tutor
	 */
	public void vincularConProfesor(Alumno alum, PadreMadreOTutor tutor) {
		Assert.notNull(tutor);
		Assert.isTrue(!alum.getPadresMadresOTutores().contains(tutor));
		alum.addPadreMadreOTutor(tutor);
		tutor.addTutorando(alum);
		save(alum);
		tutorService.save(tutor);
	}

	/**
	 * @author David Romero Alcaide
	 * @param alum
	 * @return
	 */
	public Collection<FaltaDeAsistencia> getFaltasAlumno(Alumno alum) {
		List<FaltaDeAsistencia> faltas = Lists.newArrayList();
		Collection<ItemEvaluable> items = alumnoRepositorio
				.getAllItemsEvaluables(alum.getId());
		for (ItemEvaluable item : items) {
			if (item instanceof FaltaDeAsistencia) {
				FaltaDeAsistencia falta = (FaltaDeAsistencia) item;
				faltas.add(falta);
			}
		}
		return faltas;
	}

}
