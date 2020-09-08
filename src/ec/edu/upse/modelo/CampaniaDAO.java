package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;


public class CampaniaDAO extends ClaseDAO{
	
	@SuppressWarnings("unchecked")
	public List<Campania> getListaCampania(){
		List<Campania> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Campania.findAll");
		opciones = (List<Campania>) query.getResultList();		
		return opciones;
	}
	@SuppressWarnings("unchecked")
	public List<Campania> getListaTodasCampanias(){
		List<Campania> opciones = new ArrayList<>();
		List<Campania> opcion = new ArrayList<>();
		
		Campania nuevo = new Campania();
		nuevo.setEstado("A");
		nuevo.setFecha(null);
		nuevo.setLugar("Todo");
		nuevo.setNombreCampania("Todo");
		
		opcion.add(nuevo);
		
		
		Query query = getEntityManager().createNamedQuery("Campania.buscarTodo");
		opciones = (List<Campania>) query.getResultList();
		
		for(Campania c : opciones)
			opcion.add(c);
		return opcion;
	}
	@SuppressWarnings("unchecked")
	public List<Campania> getCampaniaById(Integer value) {
		List<Campania> resultado = new ArrayList<Campania>(); 
		Query query = getEntityManager().createNamedQuery("Campania.buscarPorId");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", value);
		resultado = (List<Campania>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<Campania> getCampania(String value) {
		List<Campania> resultado; 
		String patron = value;
		if (value == null || value.length() == 0) {
			patron = "%";
		}else{
			patron = "%" + patron.toLowerCase() + "%";
		}
		Query query = getEntityManager().createNamedQuery("Campania.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<Campania>) query.getResultList();
		return resultado;
	}
}
