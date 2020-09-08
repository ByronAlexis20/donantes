package ec.edu.upse.controlador;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.ParametricaDAO;
import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.PersonaDAO;
import ec.edu.upse.modelo.SegAuditoria;
import ec.edu.upse.util.Context;
import ec.edu.upse.util.ControllerHelper;

@SuppressWarnings("serial")
public class RegistroPersonaC extends SelectorComposer<Component>{
	@Wire private Window winRegistro;
	
	@Wire private Textbox txtCedula;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtCelular;
	@Wire private Textbox txtApellidos;
	@Wire private Datebox dtpFecha;
	@Wire private Textbox txtEmail;
	@Wire private Textbox txtDireccion;
	@Wire private Combobox cboGrupoSanguineo;
	@Wire private Combobox cboGenero;
	@Wire private Combobox cboProvincia;
	@Wire private Combobox cboCiudad;
	@Wire private Button btnRegistrar;
	@Wire private Label lblAviso;
	@Wire private Combobox cboTatuaje;
	@Wire private Datebox dtpFechaTatuaje;
	
	Persona persona;
	PersonaDAO personaDAO = new PersonaDAO();
	ParametricaDAO parametricaDAO = new ParametricaDAO();
	ControllerHelper helper = new ControllerHelper();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		dtpFechaTatuaje.setDisabled(true);
		persona = new Persona();
	}
	@Listen("onOK=#txtCedula")
	public void buscarPersona() {
		try {
			if(helper.validarDeCedula(txtCedula.getText()) == false) {
				showNotify("Cédula incorrecta",txtCedula);
				limpiar();
				return;
			}
			
			List<Persona> personaLista = personaDAO.getPersona(String.valueOf(txtCedula.getValue()));
			if(personaLista.size() != 0) {
				persona = new Persona();
				persona = personaLista.get(0);
				cargarDatos(persona);
				if(persona.getRegistroDonacions().size() > 0) {
					if(calcularMesesAFecha(new Date(persona.getRegistroDonacions().get(persona.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3) {
						btnRegistrar.setDisabled(false);
						lblAviso.setValue("Persona apta para realizar donación");
					}
					else {
						lblAviso.setValue("Persona no apta para realizar donación");
						btnRegistrar.setDisabled(true);	
					}
				}else {
					lblAviso.setValue("Persona apta para realizar donación");
					btnRegistrar.setDisabled(false);
				}
			}
			else {
				limpiar();
				persona = new Persona();
				showNotify("Persona no existe.. debe registrarla",txtCedula);
				lblAviso.setValue("Persona aun no registrada");
				btnRegistrar.setDisabled(false);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static int calcularMesesAFecha(Date fechaInicio, Date fechaFin) {
        try {
            //Fecha inicio en objeto Calendar
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(fechaInicio);
            //Fecha finalización en objeto Calendar
            Calendar endCalendar = Calendar.getInstance();
            endCalendar.setTime(fechaFin);
            //Cálculo de meses para las fechas de inicio y finalización
            int startMes = (startCalendar.get(Calendar.YEAR) * 12) + startCalendar.get(Calendar.MONTH);
            int endMes = (endCalendar.get(Calendar.YEAR) * 12) + endCalendar.get(Calendar.MONTH);
            //Diferencia en meses entre las dos fechas
            int diffMonth = endMes - startMes;
            return diffMonth;
        } catch (Exception e) {
            return 0;
        }
 }
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void cargarDatos(Persona persona) {
		try {
			txtCedula.setText(persona.getCedula());
			txtTelefono.setText(persona.getTelefono());
			txtNombres.setText(persona.getNombre());
			txtCelular.setText(persona.getCelular());
			txtApellidos.setText(persona.getApellido());
			dtpFecha.setValue(new Date(persona.getFechaNacimiento().getTime()));
			if(persona.getFechaTatuaje() != null) 
				dtpFechaTatuaje.setValue(new Date(persona.getFechaTatuaje().getTime()));
			else
				dtpFechaTatuaje.setValue(null);
			
			txtEmail.setText(persona.getEmail());
			txtDireccion.setText(persona.getDireccion());
			
			for(int i = 0; i < cboGrupoSanguineo.getItems().size(); i++){
				Parametrica p = (Parametrica)cboGrupoSanguineo.getItems().get(i).getValue();
				if(p.getDescripcion().equals(persona.getParametricaSangre().getDescripcion()))
					cboGrupoSanguineo.setSelectedIndex(i);
			}
			for(int i = 0; i < cboTatuaje.getItems().size(); i++){
				Parametrica p = (Parametrica)cboTatuaje.getItems().get(i).getValue();
				if(persona.getParametricaTatuaje().getDescripcion() != null)
					if(p.getDescripcion().equals(persona.getParametricaTatuaje().getDescripcion()))
						cboTatuaje.setSelectedIndex(i);
			}
			for(int i = 0; i < cboGenero.getItems().size(); i++){
				Parametrica p = (Parametrica)cboGenero.getItems().get(i).getValue();
				if(persona.getParametricaGenero().getDescripcion() != null)
					if(p.getDescripcion().equals(persona.getParametricaGenero().getDescripcion()))
						cboGenero.setSelectedIndex(i);
			}
			for(int i = 0; i < cboTatuaje.getItems().size(); i++){
				Parametrica p = (Parametrica)cboTatuaje.getItems().get(i).getValue();
				if(persona.getParametricaGenero().getDescripcion() != null)
					if(p.getDescripcion().equals(persona.getParametricaGenero().getDescripcion()))
						cboTatuaje.setSelectedIndex(i);
			}
			
			for(int i = 0; i < cboProvincia.getItems().size(); i++){
				Parametrica p = (Parametrica)cboProvincia.getItems().get(i).getValue();
				if(p.getDescripcion().equals(persona.getParametricaCanton().getParametrica().getDescripcion())) { //pregunto x la provincia
					cboProvincia.setSelectedIndex(i);
					cboCiudad.setText("");
					List<Parametrica> ciudades = parametricaDAO.getCiudades(p.getIdParametrica()).get(0).getParametricas();
					cboCiudad.setModel(new ListModelList(ciudades));
					cboCiudad.setText(persona.getParametricaCanton().getDescripcion());					
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChange = #cboProvincia")
	public void cambiaProvincia() {
		
		cboCiudad.setText("");
		Parametrica p = (Parametrica)cboProvincia.getSelectedItem().getValue();
		List<Parametrica> ciudades = parametricaDAO.getCiudades(p.getIdParametrica()).get(0).getParametricas();
		cboCiudad.setModel(new ListModelList(ciudades));
	}
	@Listen("onChange = #cboTatuaje")
	public void cambiaTatuaje() {
		Parametrica pTatuaje = (Parametrica)cboTatuaje.getSelectedItem().getValue();
		if(pTatuaje.getDescripcion().equals("NO"))
			dtpFechaTatuaje.setDisabled(true);
		else 
			dtpFechaTatuaje.setDisabled(false);
	}
	
	@Listen("onClick=#btnRegistrar")
	public void registrarDonacion(){
		copiarDatos();
		if(validarDatos() == false)
			return;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("Persona", persona);
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/registroDonacion.zul", winRegistro, params);
		ventanaCargar.doModal();
	}
	private boolean validarDatos() {
		try {
			if(txtCedula.getText() == "") {
				Clients.showNotification("Debe registrar cédula","info",txtCedula,"end_center",2000);
				return false;
			}
			if(dtpFecha.getValue() == null) {
				Clients.showNotification("Debe seleccionar fecha de nacimiento","info",cboGenero,"end_center",2000);
				return false;
			}
			if(cboGenero.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar genero","info",cboGenero,"end_center",2000);
				return false;
			}
			if(cboGrupoSanguineo.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Grupo Sanguineo","info",cboGrupoSanguineo,"end_center",2000);
				return false;
			}
			if(cboTatuaje.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar si tiene tatuaje","info",cboTatuaje,"end_center",2000);
				return false;
			}
			if(cboCiudad.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Ciudad","info",cboCiudad,"end_center",2000);
				return false;
			}
			if(helper.validarDeCedula(txtCedula.getText()) == false) {
				Clients.showNotification("Cédula no valida","info",txtCedula,"end_center",2000);
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	private void copiarDatos() {
		try {
			if(persona.getIdPersona() != null) {//persona recuperada
				persona.setApellido(txtApellidos.getText());
				persona.setCedula(txtCedula.getText());
				persona.setCelular(txtCelular.getText());
				persona.setDireccion(txtDireccion.getText());
				persona.setEmail(txtEmail.getText());
				persona.setEstado("A");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				String dateAsString = sdf.format(dtpFecha.getValue());
				Timestamp ts = new Timestamp(((java.util.Date)sdf.parse(dateAsString)).getTime());
				persona.setFechaNacimiento(ts);
				persona.setNombre(txtNombres.getText());
				persona.setTelefono(txtTelefono.getText());
				Parametrica pGenero = (Parametrica)cboGenero.getSelectedItem().getValue();
				persona.setParametricaGenero(pGenero);
				Parametrica pCanton = (Parametrica)cboCiudad.getSelectedItem().getValue();
				persona.setParametricaCanton(pCanton);
				Parametrica pSangre = (Parametrica)cboGrupoSanguineo.getSelectedItem().getValue();
				persona.setParametricaSangre(pSangre);
				Parametrica pTatuaje = (Parametrica)cboTatuaje.getSelectedItem().getValue();
				persona.setParametricaTatuaje(pTatuaje);
				if(pTatuaje.getDescripcion().equals("NO")) {
					persona.setFechaTatuaje(null);
					System.out.println("no tiene tatuaje");
				}
				else {
					Timestamp fechaTatuaje = new Timestamp(dtpFechaTatuaje.getValue().getTime());
					persona.setFechaTatuaje(fechaTatuaje);
					System.out.println("si tiene tatuaje");
				}
			}else {//nueva persona
				persona = new Persona();
				persona.setIdPersona(null);
				persona.setApellido(txtApellidos.getText());
				persona.setCedula(txtCedula.getText());
				persona.setCelular(txtCelular.getText());
				persona.setDireccion(txtDireccion.getText());
				persona.setEmail(txtEmail.getText());
				persona.setEstado("A");
				Timestamp date = new Timestamp(dtpFecha.getValue().getTime());
				persona.setFechaNacimiento(date);
				persona.setNombre(txtNombres.getText());
				persona.setTelefono(txtTelefono.getText());
				Parametrica pGenero = (Parametrica)cboGenero.getSelectedItem().getValue();
				persona.setParametricaGenero(pGenero);
				Parametrica pCanton = (Parametrica)cboCiudad.getSelectedItem().getValue();
				persona.setParametricaCanton(pCanton);
				Parametrica pSangre = (Parametrica)cboGrupoSanguineo.getSelectedItem().getValue();
				persona.setParametricaSangre(pSangre);
				Parametrica pTatuaje = (Parametrica)cboTatuaje.getSelectedItem().getValue();
				persona.setParametricaTatuaje(pTatuaje);
				if(pTatuaje.getDescripcion().equals("NO"))
					persona.setFechaTatuaje(null);
				else {
					Timestamp fechaTatuaje = new Timestamp(dtpFechaTatuaje.getValue().getTime());
					persona.setFechaTatuaje(fechaTatuaje);
				}
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Listen("onClick=#btnNuevo")
	public void nuevo() {
		limpiar();
		txtCedula.setText("");
	}
	public void limpiar() {
		txtApellidos.setText("");
		txtCedula.setText("");
		txtCelular.setText("");
		txtDireccion.setText("");
		txtEmail.setText("");
		dtpFecha.setValue(null);
		dtpFechaTatuaje.setValue(null);
		txtNombres.setText("");
		txtTelefono.setText("");
		cboGenero.setSelectedIndex(-1);
		cboProvincia.setSelectedIndex(-1);
		cboCiudad.setSelectedIndex(-1);
		cboGrupoSanguineo.setSelectedIndex(-1);
		cboTatuaje.setSelectedIndex(-1);
		persona = null;
	}
	@Listen("onClick=#btnGrabar")
	public void grabarPersona() {
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
            public void onEvent(ClickEvent event) throws Exception {
                if(Messagebox.Button.YES.equals(event.getButton())) {
                	if(validarDatos() == true) {//graba o modifica
                		copiarDatos();
                		SegAuditoria auditoria = new SegAuditoria();
            			auditoria.setEstado("A");
            			Date date = new Date();
            			auditoria.setFecha(new Timestamp(date.getTime()));
            			auditoria.setIdAuditoria(null);
                		personaDAO.getEntityManager().getTransaction().begin();
                		if(persona.getIdPersona() != null) {//modifica
                			auditoria.setTablaAfectada("Tabla Persona: Modificación");
                			auditoria.setDescripcion("Usuario " + Context.getInstance().getUsuarioLogeado().getUsuario() + " ha "
            						+ "modificado persona con ci " + persona.getCedula());
            				auditoria.setUsuarioCrea(Context.getInstance().getUsuarioLogeado().getIdUsuario());
                			personaDAO.getEntityManager().merge(persona);
                			personaDAO.getEntityManager().persist(auditoria);
                			}
                		else {
                			auditoria.setTablaAfectada("Tabla Persona: Registro");
                			auditoria.setDescripcion("Usuario " + Context.getInstance().getUsuarioLogeado().getUsuario() + " ha "
            						+ "registrado persona con ci " + persona.getCedula());
            				auditoria.setUsuarioCrea(Context.getInstance().getUsuarioLogeado().getIdUsuario());
                			personaDAO.getEntityManager().persist(persona);
                			personaDAO.getEntityManager().persist(auditoria);
                		}
                			
                		personaDAO.getEntityManager().getTransaction().commit();
                		Clients.showNotification("Datos Grabados con exito");
                		limpiar();
                		txtCedula.setText("");
                	}
                }
            }
		};
		Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
	}
	public List<Parametrica> getCiudades(){
		return null;
	}
	private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "info", ref, "end_center", 2000);
    }
	public List<Parametrica> getGrupoSanguineo(){
		return parametricaDAO.getGrupoSanguineo().get(0).getParametricas();
	}
	public List<Parametrica> getGenero(){
		return parametricaDAO.getGenero().get(0).getParametricas();
	}
	public List<Parametrica> getTatuajes(){
		return parametricaDAO.getTatuajes().get(0).getParametricas();
	}
	public List<Parametrica> getProvincias(){
		return parametricaDAO.getProvincias().get(0).getParametricas();
	}
	public Persona getPersona() {
		return persona;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}


}
