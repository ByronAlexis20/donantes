package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class RegistroDonacionDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<RegistroDonacion> getRegistroByIdCampania(Integer value) {
		List<RegistroDonacion> resultado = new ArrayList<RegistroDonacion>(); 
		Query query = getEntityManager().createNamedQuery("RegistroDonacion.buscarPorIdCampania");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", value);
		resultado = (List<RegistroDonacion>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<RegistroDonacion> getRegistroAll() {
		List<RegistroDonacion> resultado = new ArrayList<RegistroDonacion>(); 
		Query query = getEntityManager().createNamedQuery("RegistroDonacion.buscarTodo");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		resultado = (List<RegistroDonacion>) query.getResultList();
		return resultado;
	}
}
