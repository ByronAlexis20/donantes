package ec.edu.upse.controlador;

import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import ec.edu.upse.modelo.Campania;
import ec.edu.upse.modelo.CampaniaDAO;
import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.RegistroDonacion;
import ec.edu.upse.modelo.RegistroDonacionDAO;

public class EliminarRegistroCampaniaC extends SelectorComposer<Component>{
	@Wire private Window winReporteCampania;
	@Wire private Listbox lstRegistro;
	@Wire private Combobox cboCampania;
	CampaniaDAO campaniaDAO = new CampaniaDAO();
	
	
	RegistroDonacionDAO registroDAO = new RegistroDonacionDAO();
	List<RegistroDonacion> registro;
	RegistroDonacion registroSeleccionado;
	
	
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		if (registro != null)
			registro = null; 
		llenarDatos();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void llenarDatos() {
		Campania pCampania = (Campania)cboCampania.getSelectedItem().getValue();
		registro = registroDAO.getRegistroByIdCampania(pCampania.getIdCampania());
		lstRegistro.setModel(new ListModelList(registro));
		registroSeleccionado = null;
	}
	@Listen("onClick=#btnEliminar")
	public void eliminar() {
		if(registroSeleccionado != null) {
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
	            public void onEvent(ClickEvent event) throws Exception {
	                if(Messagebox.Button.YES.equals(event.getButton())) {
	                	registroDAO.getEntityManager().getTransaction().begin();
	        			registroDAO.getEntityManager().remove(registroSeleccionado);
	        			registroDAO.getEntityManager().getTransaction().commit();
	        			Clients.showNotification("Datos eliminado");
	        			llenarDatos();
	                }
	            }
			};
			Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
	                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}else {
			Clients.showNotification("Debe seleccionar dato a eliminar");
		}
	}
	public List<Campania> getListaCampania(){
		return campaniaDAO.getListaCampania();
	}
	public List<RegistroDonacion> getRegistro() {
		return registro;
	}
	public void setRegistro(List<RegistroDonacion> registro) {
		this.registro = registro;
	}
	public RegistroDonacion getRegistroSeleccionado() {
		return registroSeleccionado;
	}
	public void setRegistroSeleccionado(RegistroDonacion registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}
	
	
}
