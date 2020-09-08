package ec.edu.upse.modelo;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

public class EmpresaDAO extends ClaseDAO{
	@SuppressWarnings("unchecked")
	public List<Empresa> getListaEmpresas(){
		List<Empresa> opciones = new ArrayList<>();		
		Query query = getEntityManager().createNamedQuery("Empresa.findAll");
		opciones = (List<Empresa>) query.getResultList();		
		return opciones;
	}
}
