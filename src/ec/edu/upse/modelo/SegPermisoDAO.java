package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegPermisoDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SegPermiso> getListaPermisosPadre(Integer idPerfil) {
		List<SegPermiso> resultado = new ArrayList<SegPermiso>(); 
		Query query = getEntityManager().createNamedQuery("SegPermiso.buscarPadrePorPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idperfil", idPerfil);
		resultado = (List<SegPermiso>) query.getResultList();
		return resultado;
	}
	
	@SuppressWarnings("unchecked")
	public List<SegPermiso> getListaPermisosTodos(Integer idPerfil) {
		List<SegPermiso> resultado = new ArrayList<SegPermiso>(); 
		Query query = getEntityManager().createNamedQuery("SegPermiso.buscarTodosPorPerfil");
		query.setHint("javax.persistence.cache.storeMode", "REFRESH");
		query.setParameter("idperfil", idPerfil);
		resultado = (List<SegPermiso>) query.getResultList();
		return resultado;
	}
}
