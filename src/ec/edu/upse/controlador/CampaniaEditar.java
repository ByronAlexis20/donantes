package ec.edu.upse.controlador;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.Campania;
import ec.edu.upse.modelo.CampaniaDAO;
import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.ParametricaDAO;
import ec.edu.upse.modelo.SegAuditoria;
import ec.edu.upse.modelo.SegAuditoriaDAO;
import ec.edu.upse.util.Context;



public class CampaniaEditar extends SelectorComposer<Component>{
	// Enlaza a la ventana para poderla cerrar
	@Wire private Window winCampaniaEditar;
	@Wire private Datebox dtpFecha;
	@Wire private Textbox txtHorario;
	@Wire private Textbox txtNombreCampania;
	@Wire private Textbox txtLugar;
	@Wire private Combobox cboEstadoCampania;
	@Wire private Row rowEstadoCampaña;
	// Contiene la ventana padre para invocar a la actualizacion de la misma cuando 
	// se cierra esta ventana.
	private CampaniaListaC campaniaLista; 

	ParametricaDAO parametricaDAO = new ParametricaDAO();
	
	// Instancia el objeto para acceso a datos.
	CampaniaDAO campaniaDAO = new CampaniaDAO();

	// Objeto que contiene la persona con la que se esta trabajando
	Campania campania;

	@Override
	public void doAfterCompose(Component comp) throws Exception{
		super.doAfterCompose(comp);
		// Recupera la ventana padre.
		campaniaLista = (CampaniaListaC)Executions.getCurrent().getArg().get("VentanaPadre");
		//System.out.println("lista " + listaResponsables.getResponsable().get(0).getIdResponsable());
		// Recupera el objeto pasado como parametro. Si no lo recibe, crea uno
		campania = null; 
		if (Executions.getCurrent().getArg().get("Campania") != null) {
			campania = (Campania) Executions.getCurrent().getArg().get("Campania");
			dtpFecha.setValue(new Date(campania.getFecha().getTime()));
			txtHorario.setText(campania.getHorario());
			txtNombreCampania.setText(campania.getNombreCampania());
			txtLugar.setText(campania.getLugar());
			cboEstadoCampania.setText(campania.getEstadoCampania());
			rowEstadoCampaña.setVisible(true);
		}else{
			campania = new Campania(); 
			rowEstadoCampaña.setVisible(false);
		}

	}


	/**
	 * Escucha el evento "onClick" del objeto "grabar"
	 */
	@Listen("onClick=#btnGrabar")
	public void grabar(){
		System.out.println("entra a grabar");
		try {
			SegAuditoria auditoria = new SegAuditoria();
			auditoria.setEstado("A");
			Date date = new Date();
			auditoria.setFecha(new Timestamp(date.getTime()));
			auditoria.setIdAuditoria(null);
			// Inicia la transaccion
			campaniaDAO.getEntityManager().getTransaction().begin();
			// Almacena los datos.
			// Si es nuevo utiliza el metodo "persist" de lo contrario usa el metodo "merge"
			if (campania.getIdCampania() == null) {
				auditoria.setTablaAfectada("Tabla Campania: Registro");
				auditoria.setDescripcion("Usuario " + Context.getInstance().getUsuarioLogeado().getUsuario() + " ha "
						+ "registrado campaña " + txtNombreCampania.getText());
				auditoria.setUsuarioCrea(Context.getInstance().getUsuarioLogeado().getIdUsuario());
				campania.setEstado("A");
				campania.setEstadoCampania("EN PROCESO");
				Timestamp fecha = new Timestamp(dtpFecha.getValue().getTime());
				campania.setFecha(fecha);
				campaniaDAO.getEntityManager().persist(campania);
				campaniaDAO.getEntityManager().persist(auditoria);
			}else{
				auditoria.setDescripcion("Usuario " + Context.getInstance().getUsuarioLogeado().getUsuario() + " ha "
						+ "modificado campaña " + txtNombreCampania.getText());
				auditoria.setTablaAfectada("Tabla Campania: Modificación");
				Timestamp fecha = new Timestamp(dtpFecha.getValue().getTime());
				campania.setFecha(fecha);
				Parametrica p = (Parametrica)cboEstadoCampania.getSelectedItem().getValue();
				campania.setEstadoCampania(p.getDescripcion());

				campaniaDAO.getEntityManager().merge(campania);
				campaniaDAO.getEntityManager().persist(auditoria);
			}
			// Cierra la transaccion.
			campaniaDAO.getEntityManager().getTransaction().commit();
			Clients.showNotification("Proceso Ejecutado con exito.");
			// Actualiza la lista
			campaniaLista.buscar();
			// Cierra la ventana
			salir();
		} catch (Exception e) {
			e.printStackTrace();
			// Si hay error, reversa la transaccion.
			campaniaDAO.getEntityManager().getTransaction().rollback();
		}

	}

	/**
	 * Escucha el evento "onClick" del objeto "salir"
	 */
	@Listen("onClick=#btnSalir")
	public void salir(){
		winCampaniaEditar.detach();
	}


	public Campania getCampania() {
		return campania;
	}


	public void setCampania(Campania campania) {
		this.campania = campania;
	}

	public List<Parametrica> getEstadoCampania(){
		return parametricaDAO.getEstadoCampania().get(0).getParametricas();
	}



	/**
	 * Retorna una lista de cargos, se deberia recuperar de una tabla.
	 * @return
	 */
	
}
