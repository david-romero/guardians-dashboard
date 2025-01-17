/**
* Instituto.java
* guardians-dashboard
* 16/4/2015 18:16:38
* Copyright David
* com.app.domain.model.types
*/
package com.app.domain.model.types;

import java.util.Collection;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.google.gwt.thirdparty.guava.common.collect.Sets;

@Entity
@Access(AccessType.PROPERTY)
/**
 * @author David
 *
 */
public class Instituto extends Persona{
	
	public Instituto(){
		super();
		this.matriculas = Sets.newHashSet();
		this.relacionesLaborales = Sets.newHashSet();
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5745625485915546117L;

	private String calle;
	
	private String complementoDireccion;
	
	private String ciudad;
	
	private int cp;
	
	private String provincia;
	
	private String numero;
	
	//Relaciones
	
	private Collection<Matricula> matriculas;
	
	private Collection<RelacionLaboral> relacionesLaborales;
	
	@NotNull
	@OneToMany(mappedBy = "instituto")
	/**
	 * @return relacionesLaborales
	 */
	public Collection<RelacionLaboral> getRelacionesLaborales() {
		return relacionesLaborales;
	}

	/**
	 * @param relacionesLaborales the relacionesLaborales to set
	 * Establecer el relacionesLaborales
	 */
	public void setRelacionesLaborales(
			Collection<RelacionLaboral> relacionesLaborales) {
		this.relacionesLaborales = relacionesLaborales;
	}

	@NotBlank
	/**
	 * @return calle
	 */
	public String getCalle() {
		return calle;
	}

	
	@NotBlank
	/**
	 * @return numero
	 */
	public String getNumero() {
		return numero;
	}

	/**
	 * @param numero the numero to set
	 * Establecer el numero
	 */
	public void setNumero(String numero) {
		this.numero = numero;
	}

	/**
	 * @param calle the calle to set
	 * Establecer el calle
	 */
	public void setCalle(String calle) {
		this.calle = calle;
	}

	@NotBlank
	/**
	 * @return complementoDireccion
	 */
	public String getComplementoDireccion() {
		return complementoDireccion;
	}

	/**
	 * @param complementoDireccion the complementoDireccion to set
	 * Establecer el complementoDireccion
	 */
	public void setComplementoDireccion(String complementoDireccion) {
		this.complementoDireccion = complementoDireccion;
	}

	@NotBlank
	/**
	 * @return ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 * Establecer el ciudad
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return cp
	 */
	public int getCp() {
		return cp;
	}

	/**
	 * @param cp the cp to set
	 * Establecer el cp
	 */
	public void setCp(int cp) {
		this.cp = cp;
	}

	@NotBlank
	/**
	 * @return provincia
	 */
	public String getProvincia() {
		return provincia;
	}

	/**
	 * @param provincia the provincia to set
	 * Establecer el provincia
	 */
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	@NotNull
	@OneToMany(mappedBy = "instituto")
	/**
	 * @return matriculas
	 */
	public Collection<Matricula> getMatriculas() {
		return matriculas;
	}

	/**
	 * @param matriculas the matriculas to set
	 * Establecer el matriculas
	 */
	public void setMatriculas(Collection<Matricula> matriculas) {
		this.matriculas = matriculas;
	}
	
	
	
}
