package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class ParametricaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Parametrica> getGrupoSanguineo() {
		List<Parametrica> retorno = new ArrayList<Parametrica>();
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarGrupoSanguineo");
		retorno = (List<Parametrica>) query.getResultList();
		return retorno;
	}
	@SuppressWarnings("unchecked")
	public List<Parametrica> getCaptado() {
		List<Parametrica> retorno = new ArrayList<Parametrica>();
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarCaptado");
		retorno = (List<Parametrica>) query.getResultList();
		return retorno;
	}
	@SuppressWarnings("unchecked")
	public List<Parametrica> getGenero() {
		List<Parametrica> retorno = new ArrayList<Parametrica>();
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarGenero");
		retorno = (List<Parametrica>) query.getResultList();
		return retorno;
	}
	@SuppressWarnings("unchecked")
	public List<Parametrica> getEstadoCampania() {
		List<Parametrica> retorno = new ArrayList<Parametrica>();
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarEstadoCampania");
		retorno = (List<Parametrica>) query.getResultList();
		return retorno;
	}
	@SuppressWarnings("unchecked")
	public List<Parametrica> getProvincias() {
		List<Parametrica> retorno = new ArrayList<Parametrica>();
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarProvincia");
		retorno = (List<Parametrica>) query.getResultList();
		return retorno;
	}
	@SuppressWarnings("unchecked")
	public List<Parametrica> getTatuajes() {
		List<Parametrica> retorno = new ArrayList<Parametrica>();
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarTatuaje");
		retorno = (List<Parametrica>) query.getResultList();
		return retorno;
	}
	@SuppressWarnings("unchecked")
	public List<Parametrica> getCiudades(Integer patron) {
		List<Parametrica> retorno = new ArrayList<>();
		List<Parametrica> ciudadRetorno = new ArrayList<>();
		
		Parametrica pp = new Parametrica();
		pp.setDescripcion("Todo");
		pp.setCodigo(null);
		pp.setEstado("A");
		pp.setIdParametrica(null);
		pp.setParametrica(null);
		pp.setParametricas(null);
		pp.setPersonasCanton(null);
		pp.setPersonasGenero(null);
		pp.setPersonasSangre(null);
		pp.setPersonasTatuaje(null);
		
		Query query = getEntityManager().createNamedQuery("Parametrica.buscarCiudad");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		retorno = (List<Parametrica>) query.getResultList();
		List<Parametrica> ciudades = new ArrayList<>();
		ciudades.add(pp);
		for(Parametrica p : retorno.get(0).getParametricas()){
			ciudades.add(p);
		}
		retorno.get(0).setParametricas(ciudades);
		return retorno;
	}
}