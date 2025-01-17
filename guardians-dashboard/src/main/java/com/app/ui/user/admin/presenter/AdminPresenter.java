/**
 * AdminPresenter.java
 * appEducacionalVaadin
 * 21/3/2015 20:09:18
 * Copyright David
 * com.app.ui.user
 */
package com.app.ui.user.admin.presenter;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.app.applicationservices.services.AdministradorService;
import com.app.applicationservices.services.PersonaService;
import com.app.domain.model.types.Administrador;
import com.app.domain.model.types.Persona;
import com.app.infrastructure.security.UserAccount;
import com.app.ui.AppUI;
import com.vaadin.addon.jpacontainer.EntityProvider;
import com.vaadin.addon.jpacontainer.JPAContainer;
import com.vaadin.addon.jpacontainer.provider.CachingMutableLocalEntityProvider;
import com.vaadin.data.util.filter.Compare;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.ViewScope;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Table;

@SpringComponent
@ViewScope
/**
 * @author David
 *
 */
public class AdminPresenter implements ClickListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3168752163649288162L;
	@Autowired
	private AdministradorService adminService;
	
	@Autowired
	private PersonaService personaService;
	
	private EntityProvider<Persona> entityProvider;
	
	@Autowired
	/**
	 * Manager
	 */
	private PlatformTransactionManager transactionManager;
	/**
	 * State of transaction
	 */
	protected TransactionStatus txStatus;


	public Administrador getAdministrador() {
		UserAccount account = AppUI.getCurrentUser();
		com.app.domain.model.types.Administrador p = adminService
				.findByUserAccount(account);
		return p;
	}

	

	/* (non-Javadoc)
	 * @see com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		//Id de la persona
		String idString = event.getButton().getId();
		@SuppressWarnings("unused")
		Integer id = new Integer(idString);
		
		Table table = (Table) event.getButton().getParent();
		table.refreshRowCache();
	}



	/**
	 * @author Administrador
	 * @return
	 */
	public Collection<Persona> getTodasPersonas() {
		return personaService.findAll();
	}



	/**
	 * @author David
	 * @return
	 */
	public JPAContainer<Persona> getTodasPersonasSinIdentidadConfirmada() {
		entityProvider = buldEntityProvider();
		JPAContainer<Persona> items = new JPAContainer<Persona>(
				Persona.class);
		items.setEntityProvider(entityProvider);
		items.addContainerFilter(new Compare.Equal("identidadConfirmada", Boolean.TRUE));
		return  items;
	}
	
	/**
	 * @author David
	 * @return
	 */
	private CachingMutableLocalEntityProvider<Persona> buldEntityProvider() {
		// We need a factory to create entity manager
		Map<String, String> properties = new HashMap<String, String>();
		properties
				.put("javax.persistence.jdbc.driver", "com.mysql.jdbc.Driver");
		properties.put("javax.persistence.jdbc.url",
				"jdbc:mysql://localhost:3306/testspring");
		properties.put("javax.persistence.jdbc.user", "rootApp");
		properties.put("javax.persistence.jdbc.password", "root123");

		properties.put("hibernate.format_sql", "true");
		properties.put("hibernate.show_sql", "false");
		properties.put("hibernate.hbm2ddl.auto", "verify");
		properties.put("hibernate.cglib.use_reflection_optimizer", "true");
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(
				"demo", properties);

		// We need an entity manager to create entity provider
		EntityManager em = emf.createEntityManager();

		// We need an entity provider to create a container
		CachingMutableLocalEntityProvider<Persona> entityProvider = new CachingMutableLocalEntityProvider<Persona>(
				Persona.class, em);
		return entityProvider;
	}



	/**
	 * @author David
	 * @param bean
	 */
	public void savePersona(Persona bean) {
		
		try{
			beginTransaction(true);
			personaService.save(bean);
			commitTransaction();
		}catch (Exception e){
			rollbackTransaction();
		}
	}
	
	/**
	 * Begin a transaction with the database
	 * 
	 * @author David Romero Alcaide
	 * @param readOnly
	 */
	protected void beginTransaction(boolean readOnly) {
		assert txStatus == null;

		DefaultTransactionDefinition definition;

		definition = new DefaultTransactionDefinition();
		definition
				.setIsolationLevel(DefaultTransactionDefinition.ISOLATION_DEFAULT);
		definition
				.setPropagationBehavior(DefaultTransactionDefinition.PROPAGATION_REQUIRES_NEW);
		definition.setReadOnly(readOnly);
		txStatus = transactionManager.getTransaction(definition);
	}
	
	/**
	 * Commit a transaction
	 * 
	 * @author David Romero Alcaide
	 * @throws Throwable
	 */
	protected void commitTransaction() throws TransactionException {
		assert txStatus != null;

		try {
			transactionManager.commit(txStatus);
			txStatus = null;
		} catch (TransactionException oops) {
			throw oops;
		}
	}

	/**
	 * Rollback a transaction
	 * 
	 * @author David Romero Alcaide
	 * @throws Throwable
	 */
	protected void rollbackTransaction() throws TransactionException {
		assert txStatus != null;
		try {
			if (!txStatus.isCompleted()) {
				transactionManager.rollback(txStatus);
			}
			txStatus = null;
		} catch (TransactionException oops) {
			throw oops;
		}
	}



	/**
	 * @author David
	 * @return
	 */
	public Administrador create() {
		Administrador admin = adminService.create();
		admin.setNombre("");
		admin.setApellidos("");
		admin.setNotas("");
		admin.setEmail("");
		admin.setTelefono("");
		return admin;
	}



	/**
	 * @author David
	 * @param admin
	 */
	public void save(Administrador admin) {
		adminService.save(admin);
	}

}
