package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the empresa database table.
 * 
 */
@Entity
@NamedQuery(name="Empresa.findAll", query="SELECT e FROM Empresa e")
public class Empresa implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_empresa")
	private Integer idEmpresa;

	private String carrera;

	private String direccion;

	private String email;

	private String estado;

	private String facultad;

	@Column(name="logo_enfermeria")
	private byte[] logoEnfermeria;
	
	
	@Column(name="logo_ministerio")
	private byte[] logoMinisterio;
	
	@Column(name="logo_facsistel")
	private byte[] logoFacsistel;

	@Column(name="nombre_institucion")
	private String nombreInstitucion;
	
	@Column(name="nombre_proyecto")
	private String nombreProyecto;
	
	
	private String ruc;

	public Empresa() {
	}

	public Integer getIdEmpresa() {
		return this.idEmpresa;
	}

	public String getNombreProyecto() {
		return nombreProyecto;
	}

	public void setNombreProyecto(String nombreProyecto) {
		this.nombreProyecto = nombreProyecto;
	}

	public void setIdEmpresa(Integer idEmpresa) {
		this.idEmpresa = idEmpresa;
	}

	public String getCarrera() {
		return this.carrera;
	}

	public void setCarrera(String carrera) {
		this.carrera = carrera;
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

	public String getFacultad() {
		return this.facultad;
	}

	public void setFacultad(String facultad) {
		this.facultad = facultad;
	}

	public String getNombreInstitucion() {
		return this.nombreInstitucion;
	}

	public void setNombreInstitucion(String nombreInstitucion) {
		this.nombreInstitucion = nombreInstitucion;
	}

	public String getRuc() {
		return this.ruc;
	}

	public void setRuc(String ruc) {
		this.ruc = ruc;
	}

	public byte[] getLogoEnfermeria() {
		return logoEnfermeria;
	}

	public void setLogoEnfermeria(byte[] logoEnfermeria) {
		this.logoEnfermeria = logoEnfermeria;
	}

	public byte[] getLogoFacsistel() {
		return logoFacsistel;
	}

	public void setLogoFacsistel(byte[] logoFacsistel) {
		this.logoFacsistel = logoFacsistel;
	}

	public byte[] getLogoMinisterio() {
		return logoMinisterio;
	}

	public void setLogoMinisterio(byte[] logoMinisterio) {
		this.logoMinisterio = logoMinisterio;
	}
	
	
}