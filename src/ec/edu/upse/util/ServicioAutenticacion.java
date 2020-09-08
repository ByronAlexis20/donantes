package ec.edu.upse.util;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.zkoss.zk.au.out.AuDownload;

import ec.edu.upse.modelo.SegAuditoria;
import ec.edu.upse.modelo.SegAuditoriaDAO;
import ec.edu.upse.modelo.SegPerfil;
import ec.edu.upse.modelo.SegPermiso;
import ec.edu.upse.modelo.SegUsuario;
import ec.edu.upse.modelo.SegUsuarioDAO;



/**
 * Detalle de datos de los usuarios que acceden al sistema.
 * Debe implementar UserDetailsService para poder usarse como fuente de datos de Spring
 * security.
 */
public class ServicioAutenticacion implements UserDetailsService {

	/**
	 * Este metodo es invocado en el momento de la autenticacion paraa recuperar 
	 * los datos del usuario.
	 * 
	 */
	SegAuditoriaDAO auditoriaDAO = new SegAuditoriaDAO();
	@Override
	public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
		try {
			Context.getInstance().setUsuarioLogeado(null);
			SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
			SegUsuario usuario; 
			User usuarioSpring;
			List<GrantedAuthority> privilegios; 
			usuario = usuarioDAO.getUsuario(nombreUsuario);
			privilegios = obtienePrivilegios(usuario.getSegPerfil());
			
			SegAuditoria auditoria = new SegAuditoria();
			auditoria.setEstado("A");
			Date date = new Date();
			auditoria.setFecha(new Timestamp(date.getTime()));
			auditoria.setIdAuditoria(null);
			auditoria.setTablaAfectada("Tabla usuario: Consulta");
			auditoria.setDescripcion("Usuario " + usuario.getUsuario() + " ha iniciado sesion");
			auditoria.setUsuarioCrea(usuario.getIdUsuario());
			auditoriaDAO.getEntityManager().getTransaction().begin();
			auditoriaDAO.getEntityManager().persist(auditoria);
			auditoriaDAO.getEntityManager().getTransaction().commit();
			Context.getInstance().setUsuarioLogeado(usuario);
			// Construye un objeto de Spring en base a los datos del usuario de la base de datos.
			usuarioSpring = new User(usuario.getUsuario(), usuario.getClave(), privilegios);
			
			return usuarioSpring;
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
			auditoriaDAO.getEntityManager().getTransaction().rollback();
			return null;
		}
		
	}

	/**
	 * Elabora una lista de objetos del tipo GrantedAuthority en base a los permisos
	 * del usuario.
	 * 
	 * @param usuario
	 * @return
	 */
	private List<GrantedAuthority> obtienePrivilegios(SegPerfil perfil) {
		List<GrantedAuthority> listaPrivilegios = new ArrayList<GrantedAuthority>(); 
		
	    for(SegPermiso permiso  : perfil.getSegPermisos())
	    	listaPrivilegios.add(new SimpleGrantedAuthority(permiso.getSegPerfil().getDescripcion()));

		return listaPrivilegios;
	}

}
