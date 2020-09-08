package ec.edu.upse.modelo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The persistent class for the seg_perfil database table.
 * 
 */
@Entity
@Table(name="seg_perfil")
@NamedQuery(name="SegPerfil.findAll", query="SELECT s FROM SegPerfil s where s.estado = 'A'")
public class SegPerfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_perfil")
	private Integer idPerfil;
	private String descripcion;
	private String estado;
	private String nombre;


	//bi-directional many-to-one association to SegPermiso
	@OneToMany(mappedBy="segPerfil")
	private List<SegPermiso> segPermisos;

	//bi-directional many-to-one association to SegUsuario
	@OneToMany(mappedBy="segPerfil")
	private List<SegUsuario> segUsuarios;

	public SegPerfil() {
	}

	public Integer getIdPerfil() {
		return this.idPerfil;
	}

	public void setIdPerfil(Integer idPerfil) {
		this.idPerfil = idPerfil;
	}


	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}



	public List<SegPermiso> getSegPermisos() {
		return this.segPermisos;
	}

	public void setSegPermisos(List<SegPermiso> segPermisos) {
		this.segPermisos = segPermisos;
	}

	public SegPermiso addSegPermiso(SegPermiso segPermiso) {
		getSegPermisos().add(segPermiso);
		segPermiso.setSegPerfil(this);

		return segPermiso;
	}

	public SegPermiso removeSegPermiso(SegPermiso segPermiso) {
		getSegPermisos().remove(segPermiso);
		segPermiso.setSegPerfil(null);

		return segPermiso;
	}

	public List<SegUsuario> getSegUsuarios() {
		return this.segUsuarios;
	}

	public void setSegUsuarios(List<SegUsuario> segUsuarios) {
		this.segUsuarios = segUsuarios;
	}

	public SegUsuario addSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().add(segUsuario);
		segUsuario.setSegPerfil(this);

		return segUsuario;
	}

	public SegUsuario removeSegUsuario(SegUsuario segUsuario) {
		getSegUsuarios().remove(segUsuario);
		segUsuario.setSegPerfil(null);

		return segUsuario;
	}

}