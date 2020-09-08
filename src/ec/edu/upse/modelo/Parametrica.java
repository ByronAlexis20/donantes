package ec.edu.upse.modelo;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the parametrica database table.
 * 
 */
@Entity
@NamedQueries({
	@NamedQuery(name="Parametrica.findAll", query="SELECT p FROM Parametrica p"),
	@NamedQuery(name="Parametrica.buscarGrupoSanguineo", query="SELECT p FROM Parametrica p where p.codigo = 'GS'"),
	@NamedQuery(name="Parametrica.buscarGenero", query="SELECT p FROM Parametrica p where p.codigo = 'GN'"),
	@NamedQuery(name="Parametrica.buscarProvincia", query="SELECT p FROM Parametrica p where p.codigo = 'PR'"),
	@NamedQuery(name="Parametrica.buscarTatuaje", query="SELECT p FROM Parametrica p where p.codigo = 'TA'"),
	@NamedQuery(name="Parametrica.buscarEstadoCampania", query="SELECT p FROM Parametrica p where p.codigo = 'ES'"),
	@NamedQuery(name="Parametrica.buscarCaptado", query="SELECT p FROM Parametrica p where p.codigo = 'CP'"),
	@NamedQuery(name="Parametrica.buscarCiudad", query="SELECT p FROM Parametrica p where p.idParametrica = :patron")
})
public class Parametrica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_parametrica")
	private Integer idParametrica;

	private String codigo;

	private String descripcion;

	private String estado;

	//bi-directional many-to-one association to Parametrica
	@ManyToOne
	@JoinColumn(name="id_parametrica_padre")
	private Parametrica parametrica;

	//bi-directional many-to-one association to Parametrica
	@OneToMany(mappedBy="parametrica",cascade = CascadeType.ALL)
	private List<Parametrica> parametricas;

	//bi-directional many-to-one asociation to Persona
	@OneToMany(mappedBy="parametricaSangre",cascade = CascadeType.ALL)
	private List<Persona> parametricaSangre;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="parametricaGenero",cascade = CascadeType.ALL)
	private List<Persona> parametricaGenero;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="parametricaCanton",cascade = CascadeType.ALL)
	private List<Persona> parametricaCanton;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="parametricaTatuaje",cascade = CascadeType.ALL)
	private List<Persona> parametricaTatuaje;

	//bi-directional many-to-one association to Persona
	@OneToMany(mappedBy="parametricaCaptado",cascade = CascadeType.ALL)
	private List<RegistroDonacion> parametricaCaptado;

	public Parametrica() {
	}

	public Integer getIdParametrica() {
		return this.idParametrica;
	}

	public void setIdParametrica(Integer idParametrica) {
		this.idParametrica = idParametrica;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
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

	public Parametrica getParametrica() {
		return this.parametrica;
	}

	public void setParametrica(Parametrica parametrica) {
		this.parametrica = parametrica;
	}

	public List<Parametrica> getParametricas() {
		return this.parametricas;
	}

	public void setParametricas(List<Parametrica> parametricas) {
		this.parametricas = parametricas;
	}

	public Parametrica addParametrica(Parametrica parametrica) {
		getParametricas().add(parametrica);
		parametrica.setParametrica(this);

		return parametrica;
	}

	public Parametrica removeParametrica(Parametrica parametrica) {
		getParametricas().remove(parametrica);
		parametrica.setParametrica(null);

		return parametrica;
	}

	public List<Persona> getPersonasSangre() {
		return this.parametricaSangre;
	}

	public void setPersonasSangre(List<Persona> parametricaSangre) {
		this.parametricaSangre = parametricaSangre;
	}

	public Persona addPersonas1(Persona parametricaSangre) {
		getPersonasSangre().add(parametricaSangre);
		parametricaSangre.setParametricaSangre(this);

		return parametricaSangre;
	}

	public Persona removePersonasSangre(Persona parametricaSangre) {
		getPersonasSangre().remove(parametricaSangre);
		parametricaSangre.setParametricaSangre(null);

		return parametricaSangre;
	}

	public List<Persona> getPersonasGenero() {
		return this.parametricaGenero;
	}

	public void setPersonasGenero(List<Persona> parametricaGenero) {
		this.parametricaGenero = parametricaGenero;
	}

	public Persona addPersonasGenero(Persona parametricaGenero) {
		getPersonasGenero().add(parametricaGenero);
		parametricaGenero.setParametricaGenero(this);

		return parametricaGenero;
	}

	public Persona removePersonasGenero(Persona parametricaGenero) {
		getPersonasGenero().remove(parametricaGenero);
		parametricaGenero.setParametricaGenero(null);

		return parametricaGenero;
	}

	public List<Persona> getPersonasCanton() {
		return this.parametricaCanton;
	}

	public void setPersonasCanton(List<Persona> parametricaCanton) {
		this.parametricaCanton = parametricaCanton;
	}

	public Persona addPersonasCanton(Persona parametricaCanton) {
		getPersonasCanton().add(parametricaCanton);
		parametricaCanton.setParametricaCanton(this);

		return parametricaCanton;
	}

	public Persona removePersonasCanton(Persona parametricaCanton) {
		getPersonasCanton().remove(parametricaCanton);
		parametricaCanton.setParametricaCanton(null);

		return parametricaCanton;
	}


	public List<Persona> getPersonasTatuaje() {
		return this.parametricaTatuaje;
	}

	public void setPersonasTatuaje(List<Persona> parametricaTatuaje) {
		this.parametricaTatuaje = parametricaTatuaje;
	}

	public Persona addPersonasTatuaje(Persona parametricaTatuaje) {
		getPersonasTatuaje().add(parametricaTatuaje);
		parametricaTatuaje.setParametricaTatuaje(this);

		return parametricaTatuaje;
	}

	public Persona removePersonasTatuaje(Persona parametricaTatuaje) {
		getPersonasTatuaje().remove(parametricaTatuaje);
		parametricaTatuaje.setParametricaTatuaje(null);

		return parametricaTatuaje;
	}

	public List<RegistroDonacion> getParametricaCaptado() {
		return parametricaCaptado;
	}

	public void setParametricaCaptado(List<RegistroDonacion> parametricaCaptado) {
		this.parametricaCaptado = parametricaCaptado;
	}

	public RegistroDonacion addPersonasTatuaje(RegistroDonacion parametricaCaptado) {
		getParametricaCaptado().add(parametricaCaptado);
		parametricaCaptado.setParametricaCaptado(this);

		return parametricaCaptado;
	}

	public RegistroDonacion removePersonasTatuaje(RegistroDonacion parametricaCaptado) {
		getParametricaCaptado().remove(parametricaCaptado);
		parametricaCaptado.setParametricaCaptado(null);

		return parametricaCaptado;
	}
}