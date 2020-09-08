package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 * The persistent class for the seg_auditoria database table.
 * 
 */
@Entity
@Table(name="seg_auditoria")
@NamedQuery(name="SegAuditoria.findAll", query="SELECT s FROM SegAuditoria s")
public class SegAuditoria implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_auditoria")
	private Integer idAuditoria;

	@Lob
	private String descripcion;

	private String estado;

	private Timestamp fecha;

	@Lob
	@Column(name="tabla_afectada")
	private String tablaAfectada;

	@Column(name="usuario_crea")
	private Integer usuarioCrea;

	public SegAuditoria() {
	}

	public Integer getIdAuditoria() {
		return this.idAuditoria;
	}

	public void setIdAuditoria(Integer idAuditoria) {
		this.idAuditoria = idAuditoria;
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

	public Timestamp getFecha() {
		return this.fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getTablaAfectada() {
		return this.tablaAfectada;
	}

	public void setTablaAfectada(String tablaAfectada) {
		this.tablaAfectada = tablaAfectada;
	}

	public Integer getUsuarioCrea() {
		return this.usuarioCrea;
	}

	public void setUsuarioCrea(Integer usuarioCrea) {
		this.usuarioCrea = usuarioCrea;
	}

}