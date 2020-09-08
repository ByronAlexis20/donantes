package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the registro_donacion database table.
 * 
 */
@Entity
@Table(name="registro_donacion")
@NamedQueries({
	@NamedQuery(name="RegistroDonacion.buscarPorIdCampania", query="SELECT r FROM RegistroDonacion r where r.campania.idCampania = :patron and r.estado = 'A' order by r.persona.apellido asc"),
	@NamedQuery(name="RegistroDonacion.buscarPorNombre", query="SELECT r FROM RegistroDonacion r "
			+ "where LOWER(r.persona.nombre) LIKE(:patron) or LOWER(r.persona.apellido) LIKE(:patron)"),
	@NamedQuery(name="RegistroDonacion.buscarTodo", query="SELECT r FROM RegistroDonacion r where r.estado = 'A' and r.persona.estado = 'A' order by r.persona.apellido asc")
})
public class RegistroDonacion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_registro")
	private Integer idRegistro;

	private String estado;

	@Column(name="estado_donacion")
	private String estadoDonacion;

	private Timestamp fecha;

	private String observacion;

	//bi-directional many-to-one association to Campania
	@ManyToOne
	@JoinColumn(name="id_campania")
	private Campania campania;

	//bi-directional many-to-one association to Persona
	@ManyToOne
	@JoinColumn(name="id_persona")
	private Persona persona;

	@ManyToOne
	@JoinColumn(name="id_captado")
	private Parametrica parametricaCaptado;
	
	public RegistroDonacion() {
	}

	public Integer getIdRegistro() {
		return this.idRegistro;
	}

	public Parametrica getParametricaCaptado() {
		return parametricaCaptado;
	}

	public void setParametricaCaptado(Parametrica parametricaCaptado) {
		this.parametricaCaptado = parametricaCaptado;
	}

	public void setIdRegistro(Integer idRegistro) {
		this.idRegistro = idRegistro;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getEstadoDonacion() {
		return this.estadoDonacion;
	}

	public void setEstadoDonacion(String estadoDonacion) {
		this.estadoDonacion = estadoDonacion;
	}

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Campania getCampania() {
		return this.campania;
	}

	public void setCampania(Campania campania) {
		this.campania = campania;
	}

	public Persona getPersona() {
		return this.persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

}