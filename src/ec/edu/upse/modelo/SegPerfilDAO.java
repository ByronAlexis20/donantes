package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class SegPerfilDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<SegPerfil> getListaPerfiles() {
		List<SegPerfil> retorno = new ArrayList<SegPerfil>();
		Query query = getEntityManager().createNamedQuery("SegPerfil.findAll");
		retorno = (List<SegPerfil>) query.getResultList();
		return retorno;
	}
}
