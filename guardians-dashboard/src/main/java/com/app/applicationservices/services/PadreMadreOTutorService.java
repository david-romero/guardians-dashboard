/**
 * PadreMadreOTutorService.java
 * appEducacional
 * 16/01/2014 11:36:24
 * Copyright David Romero Alcaide
 * com.app.applicationservices.services
 */
package com.app.applicationservices.services;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.app.domain.domainservices.Valida;
import com.app.domain.model.types.Alumno;
import com.app.domain.model.types.PadreMadreOTutor;
import com.app.domain.model.types.Profesor;
import com.app.domain.repositories.PadreMadreOTutorRepository;
import com.app.infrastructure.security.Authority;
import com.app.infrastructure.security.LoginService;
import com.app.infrastructure.security.UserAccount;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

@Service
@Transactional
/**
 * @author David Romero Alcaide
 *
 */
public class PadreMadreOTutorService {

	// Repositorios gestionados

	@Autowired
	/**
	 * 
	 */
	private PadreMadreOTutorRepository padreMadreOTutorRepositorio;

	// Servicios gestionados

	@Autowired
	private AlumnoService alumnoService;

	/**
	 * Constructor
	 */
	public PadreMadreOTutorService() {
		super();

	}

	// Métodos CRUD
	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public PadreMadreOTutor create() {
		PadreMadreOTutor tutor = new PadreMadreOTutor();
		tutor.setIdentidadConfirmada(false);
		UserAccount account = new UserAccount();
		List<Authority> authorities = Lists.newArrayList();
		Authority auth = new Authority();
		auth.setAuthority(Authority.TUTOR);
		authorities.add(auth);
		account.setAuthorities(authorities);
		tutor.setUserAccount(account);

		return tutor;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<PadreMadreOTutor> findAll() {
		List<PadreMadreOTutor> ite;
		ite = padreMadreOTutorRepositorio.findAll();

		return ite;
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param PadreMadreOTutorId
	 * @return
	 */
	public PadreMadreOTutor findOne(int padreMadreOTutorId) {
		return padreMadreOTutorRepositorio.findOne(padreMadreOTutorId);
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param PadreMadreOTutor
	 */
	public void save(PadreMadreOTutor padreMadreOTutor) {
		Assert.notNull(padreMadreOTutor);
		Assert.isTrue(Valida.validaDNI(padreMadreOTutor.getDNI()), "pasarLista.error");
		Assert.isTrue(padreMadreOTutor.getApellidos().length() >= 4, "pasarLista.error");
		Assert.isTrue(padreMadreOTutor.getNombre().length() > 2, "pasarLista.error");
		padreMadreOTutorRepositorio.save(padreMadreOTutor);
	}

	/**
	 * 
	 * @author David Romero Alcaide
	 * @param PadreMadreOTutor
	 */
	public void delete(PadreMadreOTutor padreMadreOTutor) {
		Assert.notNull(padreMadreOTutor);
		Assert.isTrue(padreMadreOTutor.getId()>0);
		padreMadreOTutorRepositorio.delete(padreMadreOTutor);
	}

	// Otros métodos de negocio

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public PadreMadreOTutor findPrincipal() {
		PadreMadreOTutor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);
		result = findByUserAccount(userAccount);
		Assert.notNull(result);

		return result;
	}

	public PadreMadreOTutor findByUserAccount(UserAccount userAccount) {
		Assert.notNull(userAccount);

		PadreMadreOTutor result;

		result = padreMadreOTutorRepositorio.findByUserAccountId(userAccount
				.getId());

		return result;
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Alumno> getTodosLosAlumnosPadreMadreOTutor() {
		PadreMadreOTutor p = findPrincipal();
		Assert.notNull(p);
		return p.getTutorandos();
	}

	/**
	 * @author David Romero Alcaide
	 * @return
	 */
	public Collection<Profesor> getTodosProfesores() {
		Set<Profesor> todosProfesores = Sets.newHashSet();
		for (Alumno a : getTodosLosAlumnosPadreMadreOTutor()) {
			todosProfesores.addAll(alumnoService.getProfesores(a));
		}
		return todosProfesores;
	}

	/**
	 * @author David Romero Alcaide
	 * @param hash1
	 */
	public void updatePassword(String hash1) {
		Assert.notNull(hash1);
		Assert.isTrue(hash1.length() > 5);
		PadreMadreOTutor p = findPrincipal();
		Assert.notNull(p);
		p.getUserAccount().setPassword(hash1);
		save(p);
	}

}
