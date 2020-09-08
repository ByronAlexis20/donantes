package ec.edu.upse.controlador;

import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.SegUsuario;
import ec.edu.upse.modelo.SegUsuarioDAO;

public class UsuarioListaC extends SelectorComposer<Component>{
	@Wire private Window winUsuarioLista;
	@Wire private Textbox txtBuscar;
	@Wire private Listbox lstUsuario;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	UsuarioRegistroC usuarioRegistro;
	List<SegUsuario> usuario;
	private SegUsuario usuarioSeleccionado;
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
		usuarioRegistro = (UsuarioRegistroC)Executions.getCurrent().getArg().get("VentanaPadre");
		
	}
	@Listen("onClick=#btnBuscar;onOK=#txtBuscar")
	public void buscar(){
		if (usuario != null) 
			usuario = null; 
		buscarUsuario(txtBuscar.getValue());
	}
	@Listen("onDoubleClick=#lstUsuario")
	public void doubleClick() throws Exception {
		if(usuarioSeleccionado != null) {
			System.out.println(usuarioSeleccionado.getApellidos());
			usuarioRegistro.cargarDatos(usuarioSeleccionado);
			winUsuarioLista.detach();
		}
	}
	@Listen("onClick=#btnSalir")
	public void salir(){
		winUsuarioLista.detach();
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void buscarUsuario(String dato){
		usuario = usuarioDAO.getListausuarioBuscar(dato);
		lstUsuario.setModel(new ListModelList(usuario));
		usuarioSeleccionado = null;
	}
	public List<SegUsuario> getUsuario() {
		return usuario;
	}
	public void setUsuario(List<SegUsuario> usuario) {
		this.usuario = usuario;
	}
	public SegUsuario getUsuarioSeleccionado() {
		return usuarioSeleccionado;
	}
	public void setUsuarioSeleccionado(SegUsuario usuarioSeleccionado) {
		this.usuarioSeleccionado = usuarioSeleccionado;
	}
}
