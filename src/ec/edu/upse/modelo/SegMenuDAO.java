package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegMenuDAO extends ClaseDAO{

	@SuppressWarnings("unchecked")
	public List<SegMenu> getOpciones(){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegMenu.findAll");
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegMenu> getListaMenuHijo(Integer idPadre){
		List<SegMenu> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("SegMenu.buscarHijos");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idPadre", idPadre);
		opciones = (List<SegMenu>) query.getResultList();		
		return opciones;
	}
}
