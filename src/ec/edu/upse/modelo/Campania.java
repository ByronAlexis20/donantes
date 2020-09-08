package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the campania database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Campania.findAll", query="SELECT c FROM Campania c where c.estado = 'A' and c.estadoCampania = 'EN PROCESO'"),
	@NamedQuery(name="Campania.buscarTodo", query="SELECT c FROM Campania c where c.estado = 'A'"),
	@NamedQuery(name="Campania.buscarPorPatron", 
	query="SELECT c FROM Campania c "
			+ "WHERE LOWER(c.lugar) LIKE LOWER(:patron) and c.estado = 'A'"),
	@NamedQuery(name="Campania.buscarPorId", query="SELECT c FROM Campania c where c.estado = 'A' and c.idCampania = :patron"),

})
public class Campania implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_campania")
	private Integer idCampania;

	private String estado;

	private Timestamp fecha;

	private String nombreCampania;

	@Column(name="estado_campania")
	private String estadoCampania;

	/*
	@Column(name="nuevo_nn")
	private String nuevo;
	 */
	private String horario;

	private String lugar;


	//bi-directional many-to-one association to RegistroDonacion
	@OneToMany(mappedBy="campania",cascade = CascadeType.ALL)
	private List<RegistroDonacion> registroDonacions;

	//bi-directional many-to-one association to PermisoCampania
	@OneToMany(mappedBy="campania")
	private List<PermisoCampania> permisoCampania;

	public Campania() {
	}

	public Integer getIdCampania() {
		return this.idCampania;
	}

	public void setIdCampania(Integer idCampania) {
		this.idCampania = idCampania;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getHorario() {
		return this.horario;
	}

	public void setHorario(String horario) {
		this.horario = horario;
	}

	public String getLugar() {
		return this.lugar;
	}

	public void setLugar(String lugar) {
		this.lugar = lugar;
	}

	public List<RegistroDonacion> getRegistroDonacions() {
		return this.registroDonacions;
	}

	public void setRegistroDonacions(List<RegistroDonacion> registroDonacions) {
		this.registroDonacions = registroDonacions;
	}

	public RegistroDonacion addRegistroDonacion(RegistroDonacion registroDonacion) {
		getRegistroDonacions().add(registroDonacion);
		registroDonacion.setCampania(this);

		return registroDonacion;
	}

	public RegistroDonacion removeRegistroDonacion(RegistroDonacion registroDonacion) {
		getRegistroDonacions().remove(registroDonacion);
		registroDonacion.setCampania(null);

		return registroDonacion;
	}

	public String getEstadoCampania() {
		return estadoCampania;
	}

	public void setEstadoCampania(String estadoCampania) {
		this.estadoCampania = estadoCampania;
	}

	public String getNombreCampania() {
		return nombreCampania;
	}

	public void setNombreCampania(String nombreCampania) {
		this.nombreCampania = nombreCampania;
	}

	public List<PermisoCampania> getPermisoCampanias() {
		return this.permisoCampania;
	}

	public void setPermisoCampania(List<PermisoCampania> permisoCampania) {
		this.permisoCampania = permisoCampania;
	}

	public PermisoCampania addPermisoCampania(PermisoCampania permisoCampania) {
		getPermisoCampanias().add(permisoCampania);
		permisoCampania.setCampania(this);

		return permisoCampania;
	}

	public PermisoCampania removePermisoCampania(PermisoCampania permisoCampania) {
		getPermisoCampanias().remove(permisoCampania);
		permisoCampania.setCampania(null);
		return permisoCampania;
	}
}