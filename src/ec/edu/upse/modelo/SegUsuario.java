package ec.edu.upse.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the seg_usuario database table.
 * 
 */

@Entity
@Table(name="seg_usuario")
@NamedQueries({
	@NamedQuery(name="SegUsuario.findAll", query="SELECT s FROM SegUsuario s"),
	@NamedQuery(name="SegUsuario.buscaUsuario", 
	query="SELECT s FROM SegUsuario s WHERE s.usuario = :nombreUsuario and s.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarPorCedula", query="SELECT s FROM SegUsuario s where s.cedula = :patron and s.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarPorUsuario", query="SELECT s FROM SegUsuario s where s.usuario = :patron and s.idUsuario <> :idUsuario and s.estado = 'A'"),
	@NamedQuery(name="SegUsuario.buscarPorPatron", query="SELECT s FROM SegUsuario s where (lower(s.nombres) like(:patron)"
			+ " or lower(s.apellidos) like(:patron)) and s.estado = 'A'")
})	
public class SegUsuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_usuario")
	private Integer idUsuario;

	private String apellidos;

	private String cedula;

	private String clave;

	private String estado;

	private String nombres;

	private String observacion;

	private String telefono;

	private String usuario;

	@Column(name="clave_desencriptada")
	private String claveDesencriptada;

	//bi-directional many-to-one association to SegPerfil
	@ManyToOne
	@JoinColumn(name="id_perfil")
	private SegPerfil segPerfil;

	public SegUsuario() {
	}

	public Integer getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getCedula() {
		return this.cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getClave() {
		return this.clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
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

	public String getUsuario() {
		return this.usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public SegPerfil getSegPerfil() {
		return this.segPerfil;
	}

	public void setSegPerfil(SegPerfil segPerfil) {
		this.segPerfil = segPerfil;
	}

	public String getClaveDesencriptada() {
		return claveDesencriptada;
	}

	public void setClaveDesencriptada(String claveDesencriptada) {
		this.claveDesencriptada = claveDesencriptada;
	}

	//bi-directional many-to-one association to PermisoCampania
	@OneToMany(mappedBy="segUsuario")
	private List<PermisoCampania> permisoCampania;

	public List<PermisoCampania> getPermisoCampanias() {
		return this.permisoCampania;
	}

	public void setPermisoCampania(List<PermisoCampania> permisoCampania) {
		this.permisoCampania = permisoCampania;
	}

	public PermisoCampania addPermisoCampania(PermisoCampania permisoCampania) {
		getPermisoCampanias().add(permisoCampania);
		permisoCampania.setSegUsuario(this);

		return permisoCampania;
	}

	public PermisoCampania removePermisoCampania(PermisoCampania permisoCampania) {
		getPermisoCampanias().remove(permisoCampania);
		permisoCampania.setSegUsuario(null);
		return permisoCampania;
	}
}