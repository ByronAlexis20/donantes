package ec.edu.upse.controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.Campania;
import ec.edu.upse.modelo.CampaniaDAO;

public class CampaniaListaC extends SelectorComposer<Component>{
	@Wire private Window winCampaniaLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstCampania;
	
	
	CampaniaDAO campaniaDAO = new CampaniaDAO();

	List<Campania> campania;
	Campania campaniaSeleccionado;
	
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		System.out.println("entra buscar");
		if (campania != null)
			campania = null; 
		campania = campaniaDAO.getCampania(txtBuscar.getValue());
		lstCampania.setModel(new ListModelList(campania));
		campaniaSeleccionado = null;
	}
	
	@Listen("onClick=#btnNuevo")
	public void nuevo(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Campania", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/campaniaEditar.zul", winCampaniaLista, params);
		ventanaCargar.doModal();
	}
	
	@Listen("onClick=#btnImprimir")
	public void imprimir(){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Campania", null);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/campaniaEditar.zul", winCampaniaLista, params);
		ventanaCargar.doModal();
	}
	
	
	@Listen("onClick=#btnEditar")
	public void editar(){
		if (campaniaSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una campania.");
			return; 
		}
		// Actualiza la instancia antes de enviarla a editar.
		campaniaDAO.getEntityManager().refresh(campaniaSeleccionado);

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Campania", campaniaSeleccionado);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/campaniaEditar.zul", winCampaniaLista, params);
		ventanaCargar.doModal();

	}
	
	@Command //esta esperando un evento del formulario
	@NotifyChange("formularioActual") 
	public void cargaUrl(@BindingParam("url") String url){ //recupero el parametro con BindingParam
		if (url.substring(0, 4).toLowerCase().equals("http")) {	//compara si la variabla url que llega como parametro en un http	
			Sessions.getCurrent().setAttribute("FormularioActual", null); //la variabla la setea a null
			Executions.getCurrent().sendRedirect(url, "_blank"); //manda a una pagina en blanco
		}else{ //caso contrario es un archivo de nuestro sistema
			Sessions.getCurrent().setAttribute("FormularioActual", url);
		}
	}

	@SuppressWarnings("unchecked")
	@Listen("onClick=#btnEliminar")
	public void eliminar(){
		if (campaniaSeleccionado == null) {
			Clients.showNotification("Debe seleccionar una persona.");
			return; 
		}
		Messagebox.show("Desea eliminar el registro seleccionado?", "Confirmación de Eliminación", Messagebox.YES | Messagebox.NO, Messagebox.QUESTION, new EventListener() {
			@Override
			public void onEvent(Event event) throws Exception {
				if (event.getName().equals("onYes")) {
					try {
						campaniaSeleccionado.setEstado("I");
						campaniaDAO.getEntityManager().getTransaction().begin();
						campaniaDAO.getEntityManager().persist(campaniaSeleccionado);
						campaniaDAO.getEntityManager().getTransaction().commit();;
						Clients.showNotification("Transaccion ejecutada con exito.");
						buscar();
					} catch (Exception e) {
						e.printStackTrace();
						campaniaDAO.getEntityManager().getTransaction().rollback();
					}
				}
			}
		});

	}

	public List<Campania> getCampania() {
		return campania;
	}

	public void setCampania(List<Campania> campania) {
		this.campania = campania;
	}

	public Campania getCampaniaSeleccionado() {
		return campaniaSeleccionado;
	}

	public void setCampaniaSeleccionado(Campania campaniaSeleccionado) {
		this.campaniaSeleccionado = campaniaSeleccionado;
	}
	
	
	//getters and setters
	
}
