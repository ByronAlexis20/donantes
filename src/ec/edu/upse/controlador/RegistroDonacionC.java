package ec.edu.upse.controlador;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Messagebox.ClickEvent;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.Campania;
import ec.edu.upse.modelo.CampaniaDAO;
import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.ParametricaDAO;
import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.RegistroDonacion;
import ec.edu.upse.modelo.RegistroDonacionDAO;
import ec.edu.upse.modelo.SegAuditoria;
import ec.edu.upse.util.Constantes;
import ec.edu.upse.util.Context;


public class RegistroDonacionC extends SelectorComposer<Component>{
	@Wire private Window winRegistroDonacion;
	@Wire private Combobox cboCampania;
	@Wire private Combobox cboCaptado;
	@Wire private Textbox txtObservaciones;
	private RegistroPersonaC registroPersona;
	Persona persona;
	RegistroDonacion donacion;
	RegistroDonacionDAO donacionDAO = new RegistroDonacionDAO();
	ParametricaDAO parametricaDAO = new ParametricaDAO();	
	CampaniaDAO campaniaDAO = new CampaniaDAO();
	
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		registroPersona = (RegistroPersonaC)Executions.getCurrent().getArg().get("VentanaPadre");
		persona = null; 
		if (Executions.getCurrent().getArg().get("Persona") != null) {
			persona = (Persona) Executions.getCurrent().getArg().get("Persona");
		}else{
			persona = new Persona(); 
		}
	}
	@Listen("onClick=#btnGrabar")
	public void grabar(){
		try {
			if(validarDatos() == false)
				return;
			EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
	            public void onEvent(ClickEvent event) throws Exception {
	                if(Messagebox.Button.YES.equals(event.getButton())) {
	                	SegAuditoria auditoria = new SegAuditoria();
            			auditoria.setEstado("A");
            			Date fechaAu = new Date();
            			auditoria.setFecha(new Timestamp(fechaAu.getTime()));
            			auditoria.setIdAuditoria(null);
            			
	                	donacion = new RegistroDonacion();
	        			donacion.setIdRegistro(null);
	        			Campania campania = (Campania)cboCampania.getSelectedItem().getValue();
	        			donacion.setCampania(campania);
	        			Parametrica p = (Parametrica)cboCaptado.getSelectedItem().getValue();
	        			donacion.setParametricaCaptado(p);
	        			donacion.setEstado("A");
	        			donacion.setEstadoDonacion(Constantes.ULTIMA_DONACION);
	        			Date date = new Date();
	        			Timestamp fecha = new Timestamp(date.getTime());
	        			donacion.setFecha(fecha);
	        			donacion.setObservacion(txtObservaciones.getText());
	        			if(persona.getIdPersona() != null) {
	        				auditoria.setTablaAfectada("Tabla Registro donacion: Registro");
                			auditoria.setDescripcion("Usuario " + Context.getInstance().getUsuarioLogeado().getUsuario() + " ha "
            						+ "registrado la donacion de persona con ci " + persona.getCedula());
            				auditoria.setUsuarioCrea(Context.getInstance().getUsuarioLogeado().getIdUsuario());
	        				donacion.setPersona(persona);
	        				persona.addRegistroDonacion(donacion);
	        			}	        				
	        			else {
	        				auditoria.setTablaAfectada("Tabla Registro donacion y persona: Registro");
                			auditoria.setDescripcion("Usuario " + Context.getInstance().getUsuarioLogeado().getUsuario() + " ha "
            						+ "registrado la donacion y la persona con ci " + persona.getCedula());
            				auditoria.setUsuarioCrea(Context.getInstance().getUsuarioLogeado().getIdUsuario());
	        				donacion.setPersona(persona);
	        				List<RegistroDonacion> listaRegistro = new ArrayList<RegistroDonacion>();
	        				listaRegistro.add(donacion);
	        				persona.setRegistroDonacions(listaRegistro);
	        			}
	        			
	        			donacionDAO.getEntityManager().getTransaction().begin();
	        			if(persona.getIdPersona() != null) {
	        				donacionDAO.getEntityManager().merge(persona);
	        				donacionDAO.getEntityManager().persist(auditoria);
	        			}
	        			else {
	        				donacionDAO.getEntityManager().persist(auditoria);
	        				donacionDAO.getEntityManager().persist(persona);
	        			}
	        			donacionDAO.getEntityManager().getTransaction().commit();
	        			
	        			System.out.println(donacion.getObservacion());
	        			Clients.showNotification("Datos Grabados con exito");
	        			registroPersona.limpiar();
	        			winRegistroDonacion.detach();
	                }
	            }
	        };
	        Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
	                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			donacionDAO.getEntityManager().getTransaction().rollback();
		}
	}
	private boolean validarDatos() {
		try {
			if(cboCampania.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Campaña","info",cboCampania,"end_center",2000);
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	@Listen("onClick=#btnCancelar")
	public void salir(){
		winRegistroDonacion.detach();
	}
	public List<Parametrica> getListaCaptado(){
		return parametricaDAO.getCaptado().get(0).getParametricas();
	}
	public List<Campania> getListaCampania(){
		return campaniaDAO.getListaCampania();
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}
	public RegistroDonacion getDonacion() {
		return donacion;
	}
	public void setDonacion(RegistroDonacion donacion) {
		this.donacion = donacion;
	}
	
	
}
