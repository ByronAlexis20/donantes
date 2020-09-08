package ec.edu.upse.util;

import ec.edu.upse.modelo.SegUsuario;

public class Context {
	private final static Context instance = new Context();
	public static Context getInstance() {
		return instance;
	}
	private SegUsuario usuarioLogeado;

	public SegUsuario getUsuarioLogeado() {
		return usuarioLogeado;
	}

	public void setUsuarioLogeado(SegUsuario usuarioLogeado) {
		this.usuarioLogeado = usuarioLogeado;
	}
	
}
