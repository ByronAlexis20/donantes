package ec.edu.upse.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="permiso_campania")
@NamedQueries({
	@NamedQuery(name="PermisoCampania.findAll", query="SELECT p FROM PermisoCampania p")
})	
public class PermisoCampania implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_permiso")
	private Integer idPermiso;

	private String estado;
	//bi-directional many-to-one association to SegUsuario
	@ManyToOne
	@JoinColumn(name="id_usuario")
	private SegUsuario segUsuario;

	//bi-directional many-to-one association to Campania
	@ManyToOne
	@JoinColumn(name="id_campania")
	private Campania campania;
	
	public PermisoCampania() {
		
	}

	public Integer getIdPermiso() {
		return idPermiso;
	}

	public void setIdPermiso(Integer idPermiso) {
		this.idPermiso = idPermiso;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public SegUsuario getSegUsuario() {
		return segUsuario;
	}

	public void setSegUsuario(SegUsuario segUsuario) {
		this.segUsuario = segUsuario;
	}

	public Campania getCampania() {
		return campania;
	}

	public void setCampania(Campania campania) {
		this.campania = campania;
	}
	
}
