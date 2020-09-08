	package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the persona database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Persona.findAll", query="SELECT p FROM Persona p"),
	@NamedQuery(name="Personas.buscarPorFechaNacimiento", 
    query="SELECT p FROM Persona p WHERE FUNC('MONTH', p.fechaNacimiento) = :fCumpleaniosMes AND FUNC('DAY', p.fechaNacimiento) = :fCumpleaniosDia"),
	@NamedQuery(name="Persona.buscarPorCedula", query="SELECT p FROM Persona p where p.cedula = :patron and p.estado = 'A'"),
	@NamedQuery(name="Persona.buscarPorPatron", query="SELECT p FROM Persona p where p.estado = 'A'"
			+ " and p.parametricaCanton.idParametrica = :ciudad and p.parametricaSangre.idParametrica = :sangre order by p.apellido asc"),
	@NamedQuery(name="Persona.buscarPorProvincia", query="SELECT p FROM Persona p where p.estado = 'A'"
			+ " and p.parametricaCanton.parametrica.idParametrica = :provincia and p.parametricaSangre.idParametrica = :sangre"),
	@NamedQuery(name="Persona.buscarPersonaPorProvincia", query="SELECT p FROM Persona p where p.estado = 'A'"
			+ " and p.parametricaCanton.parametrica.idParametrica = :provincia and  (lower(p.nombre) like(:patron) or lower(p.apellido) like(:patron))")	
})
public class Persona implements Serializable, Cloneable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_persona")
	private Integer idPersona;

	private String apellido;

	private String cedula;

	private String celular;

	private String direccion;

	private String email;

	private String estado;

	
	
	@Column(name="fecha_nacimiento")
	private Timestamp fechaNacimiento;

	@Column(name="fecha_tatuaje")
	private Timestamp fechaTatuaje;

	private String nombre;

	private String observacion;

	private String telefono;

	//bi-directional many-to-one association to Parametrica
	@ManyToOne
	@JoinColumn(name="id_canton")
	private Parametrica parametricaCanton;

	//bi-directional many-to-one association to Parametrica
	@ManyToOne
	@JoinColumn(name="id_tatuaje")
	private Parametrica parametricaTatuaje;

	//bi-directional many-to-one association to Parametrica
	@ManyToOne
	@JoinColumn(name="id_grupo_sanguineo")
	private Parametrica parametricaSangre;

	//bi-directional many-to-one association to Parametrica
	@ManyToOne
	@JoinColumn(name="id_sexo")
	private Parametrica parametricaGenero;

	//bi-directional many-to-one association to RegistroDonacion
	@OneToMany(mappedBy="persona",cascade=CascadeType.ALL)
	private List<RegistroDonacion> registroDonacions;

	public Persona() {
	}

	public Integer getIdPersona() {
		return this.idPersona;
	}

	public void setIdPersona(Integer idPersona) {
		this.idPersona = idPersona;
	}

	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Timestamp getFechaNacimiento() {
		return this.fechaNacimiento;
	}

	public void setFechaNacimiento(Timestamp fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getObservacion() {
		return this.observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Parametrica getParametricaCanton() {
		return this.parametricaCanton;
	}

	public void setParametricaCanton(Parametrica parametricaCanton) {
		this.parametricaCanton = parametricaCanton;
	}

	public Parametrica getParametricaSangre() {
		return this.parametricaSangre;
	}

	public void setParametricaSangre(Parametrica parametricaSangre) {
		this.parametricaSangre = parametricaSangre;
	}

	public Parametrica getParametricaTatuaje() {
		return parametricaTatuaje;
	}

	public void setParametricaTatuaje(Parametrica parametricaTatuaje) {
		this.parametricaTatuaje = parametricaTatuaje;
	}

	public Parametrica getParametricaGenero() {
		return this.parametricaGenero;
	}

	public void setParametricaGenero(Parametrica parametricaGenero) {
		this.parametricaGenero = parametricaGenero;
	}

	public List<RegistroDonacion> getRegistroDonacions() {
		return this.registroDonacions;
	}

	public void setRegistroDonacions(List<RegistroDonacion> registroDonacions) {
		this.registroDonacions = registroDonacions;
	}

	public RegistroDonacion addRegistroDonacion(RegistroDonacion registroDonacion) {
		getRegistroDonacions().add(registroDonacion);
		registroDonacion.setPersona(this);

		return registroDonacion;
	}

	public RegistroDonacion removeRegistroDonacion(RegistroDonacion registroDonacion) {
		getRegistroDonacions().remove(registroDonacion);
		registroDonacion.setPersona(null);

		return registroDonacion;
	}

	public Timestamp getFechaTatuaje() {
		return fechaTatuaje;
	}

	public void setFechaTatuaje(Timestamp fechaTatuaje) {
		this.fechaTatuaje = fechaTatuaje;
	}

	 public Persona clone() throws CloneNotSupportedException{
		 Persona clonmalefico = (Persona) super.clone();
         return clonmalefico;
    }
}