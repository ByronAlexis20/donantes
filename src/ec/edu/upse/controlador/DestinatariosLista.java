package ec.edu.upse.controlador;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.ParametricaDAO;
import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.PersonaDAO;


@SuppressWarnings({ "serial", "rawtypes" })
public class DestinatariosLista extends SelectorComposer{
	@Wire private Combobox cboProvincia;
	@Wire private Window winDestinatarioLista;
	@Wire private Listbox lstDonantes;
	@Wire private Textbox txtBuscar;
	@Wire private Label lblNumSeleccionados;
	
	List<Persona> donante;
	List<Persona> donantesSeleccionados;
	
	PersonaDAO personaDAO = new PersonaDAO();
	ParametricaDAO parametricaDAO = new ParametricaDAO();
	
	EnvioCorreo envioCorreo;
	
	@SuppressWarnings("unchecked")
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		envioCorreo = (EnvioCorreo)Executions.getCurrent().getArg().get("VentanaPadre");
		lblNumSeleccionados.setValue("0");
		
	}
	
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		if (donante != null) 
			donante = null; 
		buscarDonante(txtBuscar.getValue());
	}
	
	@SuppressWarnings({ "unchecked" })
	private void buscarDonante(String dato){
		try {
			if(cboProvincia.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Provincia","info",cboProvincia,"end_center",2000);
				return;
			}
			lstDonantes.setCheckmark(false);
			lstDonantes.setMultiple(false);
			Parametrica pProvincia = (Parametrica)cboProvincia.getSelectedItem().getValue();
			donante = personaDAO.getPersonaBuscarProvincia(pProvincia.getIdParametrica(), dato);
			lstDonantes.setModel(new ListModelList(donante));
			lstDonantes.setCheckmark(true);
			lstDonantes.setMultiple(true);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
		
	}

	
	@Listen("onClick=#btnSalir")
	public void salir(){
		winDestinatarioLista.detach();
	}
	
	
	@Listen("onClick=#btnAceptar")
	public void aceptar(){
		try {
			List<String> listaSeleccionada = new ArrayList<String>();
			if(lstDonantes.getSelectedItems().size() == lstDonantes.getItems().size()) {
				Parametrica pProvincia = (Parametrica)cboProvincia.getSelectedItem().getValue();
				List<Persona> lista = personaDAO.getPersonaBuscarProvincia(pProvincia.getIdParametrica(), txtBuscar.getText());
				for(Persona ls : lista)
					listaSeleccionada.add(ls.getEmail());
			}else {
				for(Listitem item : lstDonantes.getSelectedItems()) {
					Persona per = (Persona)item.getValue();
					listaSeleccionada.add(per.getEmail());
				}
			}
			System.out.println("Cantidad seleccionada pantalla modal: "+ listaSeleccionada.size());
			
			envioCorreo.recuperarDatos(listaSeleccionada);
			salir();
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Listen("onSelect = #lstDonantes")
	public void seleccionar() {
		try {
			Integer contador = lstDonantes.getSelectedItems().size();
			lblNumSeleccionados.setValue(String.valueOf(contador));
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	
	 public List<Parametrica> getProvincias(){
		return parametricaDAO.getProvincias().get(0).getParametricas();
	}

	
	public List<Persona> getDonante() {
		return donante;
	}


	public void setDonante(List<Persona> donante) {
		this.donante = donante;
	}

	public List<Persona> getDonantesSeleccionados() {
		return donantesSeleccionados;
	}

	public void setDonantesSeleccionados(List<Persona> donantesSeleccionados) {
		this.donantesSeleccionados = donantesSeleccionados;
	}	
	
	
}
