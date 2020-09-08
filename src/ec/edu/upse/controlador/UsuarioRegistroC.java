package ec.edu.upse.controlador;

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
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Datebox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox.ClickEvent;

import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.SegPerfil;
import ec.edu.upse.modelo.SegPerfilDAO;
import ec.edu.upse.modelo.SegUsuario;
import ec.edu.upse.modelo.SegUsuarioDAO;
import ec.edu.upse.util.ControllerHelper;

public class UsuarioRegistroC extends SelectorComposer<Component>{
	@Wire private Window winRegistroUsuario;
	
	@Wire private Textbox txtCedula;
	@Wire private Checkbox chkEstado;
	@Wire private Textbox txtTelefono;
	@Wire private Textbox txtNombres;
	@Wire private Textbox txtApellidos;
	@Wire private Textbox txtUsuario;
	@Wire private Combobox cboPerfil;
	@Wire private Textbox txtClave;
	@Wire private Textbox txtObservacion;
	
	SegUsuario usuario;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	SegPerfilDAO perfilDAO = new SegPerfilDAO();
	ControllerHelper helper = new ControllerHelper();
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		usuario = new SegUsuario();
	}
	@Listen("onOK=#txtCedula")
	public void buscarusuario() {
		try {
			if(helper.validarDeCedula(txtCedula.getText()) == false) {
				showNotify("Cédula incorrecta",txtCedula);
				limpiar();
				return;
			}
			
			List<SegUsuario> usuarioLista = usuarioDAO.getListaUsuario(txtCedula.getValue());
			if(usuarioLista.size() != 0) {
				usuario = new SegUsuario();
				usuario = usuarioLista.get(0);
				cargarDatos(usuario);
			}
			else {
				limpiar();
				usuario = new SegUsuario();
				showNotify("Usuario no existe.. debe ser registrado",txtCedula);
			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@Listen("onClick=#btnBuscarUsuario")
	public void buscarListadoUsuarios() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/usuarioLista.zul", winRegistroUsuario, params);
		ventanaCargar.doModal();
	}
	public void cargarDatos(SegUsuario usuarioRecuperado) {
		try {
			if(usuarioRecuperado.getEstado().equals("A"))
				chkEstado.setChecked(true);
			else
				chkEstado.setChecked(false);
			txtCedula.setText(usuarioRecuperado.getCedula());
			txtTelefono.setText(usuarioRecuperado.getTelefono());
			txtNombres.setText(usuarioRecuperado.getNombres());
			txtApellidos.setText(usuarioRecuperado.getApellidos());
			txtUsuario.setText(usuarioRecuperado.getUsuario());
			cboPerfil.setText(usuarioRecuperado.getSegPerfil().getNombre());
			txtClave.setText("");
			txtObservacion.setText(usuarioRecuperado.getObservacion());
			
			
			usuario = usuarioRecuperado;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private void copiarDatos() {
		usuario.setCedula(txtCedula.getText());
		usuario.setTelefono(txtTelefono.getText());
		usuario.setNombres(txtNombres.getText());
		usuario.setApellidos(txtApellidos.getText());
		usuario.setUsuario(txtUsuario.getText());
		SegPerfil pPerfil = (SegPerfil)cboPerfil.getSelectedItem().getValue();
		usuario.setSegPerfil(pPerfil);
		usuario.setClave(helper.encriptar(txtClave.getText()));
		usuario.setClaveDesencriptada(txtClave.getText());
		usuario.setObservacion(txtObservacion.getText());
		if(chkEstado.isChecked())
			usuario.setEstado("A");
		else
			usuario.setEstado("I");
		
	}
	@Listen("onClick=#btnLimpiar")
	public void nuevo() {
		limpiar();
		txtCedula.setText("");
	}
	@Listen("onClick=#btnGrabar")
	public void grabar() {
		
		EventListener<ClickEvent> clickListener = new EventListener<Messagebox.ClickEvent>() {
            public void onEvent(ClickEvent event) throws Exception {
                if(Messagebox.Button.YES.equals(event.getButton())) {
                	if(validarDatos() == true) {
                		copiarDatos();
                		usuarioDAO.getEntityManager().getTransaction().begin();
                		if(usuario.getIdUsuario() != null)//modifica
                			usuarioDAO.getEntityManager().merge(usuario);
                		else
                			usuarioDAO.getEntityManager().persist(usuario);
                		usuarioDAO.getEntityManager().getTransaction().commit();
                		Clients.showNotification("Datos Grabados con exito");
                		txtCedula.setText("");
                		limpiar();
                	}
                }
            }
		};
		Messagebox.show("Desea Grabar los Datos?", "Confirmación", new Messagebox.Button[]{
                Messagebox.Button.YES, Messagebox.Button.NO }, Messagebox.QUESTION, clickListener);
	}
	private boolean validarDatos() {
		try {
			if(txtCedula.getText() == "") {
				Clients.showNotification("Debe registrar cédula","info",txtCedula,"end_center",2000);
				return false;
			}
			if(helper.validarDeCedula(txtCedula.getText()) == false) {
				Clients.showNotification("Cédula no valida","info",txtCedula,"end_center",2000);
				return false;
			}
			if(txtUsuario.getText() == "") {
				Clients.showNotification("Campo obligatorio: usuario","info",txtUsuario,"end_center",2000);
				return false;
			}
			if(txtClave.getText() == "") {
				Clients.showNotification("Campo obligatorio: clave","info",txtClave,"end_center",2000);
				return false;
			}
			int idUsuario;
			if(usuario.getIdUsuario() != null)
				idUsuario = usuario.getIdUsuario();
			else
				idUsuario = 0;
			if(usuarioDAO.getBuscarUsuario(txtUsuario.getText(),idUsuario).size() > 0) {
				Clients.showNotification("Usuario ya existe","info",txtUsuario,"end_center",2000);
				return false;
			}
			return true;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	public List<SegPerfil> getPerfiles(){
		return perfilDAO.getListaPerfiles();
	}
	private void limpiar() {
		txtTelefono.setText("");
		txtNombres.setText("");
		txtApellidos.setText("");
		txtUsuario.setText("");
		cboPerfil.setSelectedIndex(-1);
		txtClave.setText("");
		txtObservacion.setText("");
		usuario = null;
	}
	private void showNotify(String msg, Component ref) {
        Clients.showNotification(msg, "info", ref, "end_center", 2000);
    }
}
