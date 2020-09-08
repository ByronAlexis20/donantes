package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;


public class SegUsuarioDAO extends ClaseDAO{
	
	public SegUsuario getUsuario(String nombreUsuario) {
		SegUsuario usuario; 
		Query consulta;
		consulta = getEntityManager().createNamedQuery("SegUsuario.buscaUsuario");
		consulta.setParameter("nombreUsuario", nombreUsuario);
		usuario = (SegUsuario) consulta.getSingleResult();
		return usuario;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListaUsuario(String value) {
		List<SegUsuario> resultado = new ArrayList<SegUsuario>(); 
		String patron = value;
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarPorCedula");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", patron);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getListausuarioBuscar(String value) {
		List<SegUsuario> resultado = new ArrayList<SegUsuario>(); 
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarPorPatron");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron","%" + value.toLowerCase() + "%");
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
	@SuppressWarnings("unchecked")
	public List<SegUsuario> getBuscarUsuario(String value,Integer idUsuario) {
		List<SegUsuario> resultado = new ArrayList<SegUsuario>(); 
		Query query = getEntityManager().createNamedQuery("SegUsuario.buscarPorUsuario");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("patron", value.toLowerCase());
		query.setParameter("idUsuario", idUsuario);
		resultado = (List<SegUsuario>) query.getResultList();
		return resultado;
	}
}
