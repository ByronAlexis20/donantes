package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the seg_menu database table.
 * 
 */
@Entity
@Table(name="seg_menu")
@NamedQueries({
	@NamedQuery(name="SegMenu.findAll", query="SELECT s FROM SegMenu s where s.estado = 'A' order by s.posicion asc"),
	@NamedQuery(name="SegMenu.buscarHijos", query="SELECT s FROM SegMenu s where s.estado = 'A' and s.idMenuPadre = :idPadre order by s.posicion asc"),
})	
public class SegMenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_menu")
	private Integer idMenu;

	private String descripcion;

	private String estado;

	private String icono;

	@Column(name="id_menu_padre")
	private Integer idMenuPadre;

	private Integer posicion;

	private String url;

	@Column(name="url_asociado")
	private double urlAsociado;

	//bi-directional many-to-one association to SegPermiso
	@OneToMany(mappedBy="segMenu")
	private List<SegPermiso> segPermisos;

	public SegMenu() {
	}

	public Integer getIdMenu() {
		return this.idMenu;
	}

	public void setIdMenu(Integer idMenu) {
		this.idMenu = idMenu;
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

	public String getIcono() {
		return this.icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}

	public Integer getIdMenuPadre() {
		return this.idMenuPadre;
	}

	public void setIdMenuPadre(Integer idMenuPadre) {
		this.idMenuPadre = idMenuPadre;
	}

	public Integer getPosicion() {
		return this.posicion;
	}

	public void setPosicion(Integer posicion) {
		this.posicion = posicion;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public double getUrlAsociado() {
		return this.urlAsociado;
	}

	public void setUrlAsociado(double urlAsociado) {
		this.urlAsociado = urlAsociado;
	}

	public List<SegPermiso> getSegPermisos() {
		return this.segPermisos;
	}

	public void setSegPermisos(List<SegPermiso> segPermisos) {
		this.segPermisos = segPermisos;
	}

	public SegPermiso addSegPermiso(SegPermiso segPermiso) {
		getSegPermisos().add(segPermiso);
		segPermiso.setSegMenu(this);

		return segPermiso;
	}

	public SegPermiso removeSegPermiso(SegPermiso segPermiso) {
		getSegPermisos().remove(segPermiso);
		segPermiso.setSegMenu(null);

		return segPermiso;
	}

}