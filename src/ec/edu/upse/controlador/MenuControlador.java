package ec.edu.upse.controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;

import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.Sessions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Include;
import org.zkoss.zul.Tree;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treechildren;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.PersonaDAO;
import ec.edu.upse.modelo.SegMenu;
import ec.edu.upse.modelo.SegMenuDAO;
import ec.edu.upse.modelo.SegPermiso;
import ec.edu.upse.modelo.SegPermisoDAO;
import ec.edu.upse.modelo.SegUsuario;
import ec.edu.upse.modelo.SegUsuarioDAO;
import ec.edu.upse.util.LongOperation;
import ec.edu.upse.util.SecurityUtil;

public class MenuControlador {

	@Wire Tree menu;
	@Wire Include areaContenido;
	SegMenu opcionSeleccionado;
	SegUsuarioDAO usuarioDAO = new SegUsuarioDAO();
	SegPermisoDAO permisoDAO = new SegPermisoDAO();
	SegMenuDAO menuDAO = new SegMenuDAO();
	List<SegPermiso> listaPermisosPadre = new ArrayList<SegPermiso>();
	List<SegPermiso> listaPermisosTodos = new ArrayList<SegPermiso>();
	
	@AfterCompose
	public void aferCompose(@ContextParam(ContextType.VIEW) Component view) throws IOException, MessagingException{
		Selectors.wireComponents(view, this, false);
		loadTree();
		//startLongOperation();
	}
	
  /*  public void startLongOperation() {
    	String  d_email = "kattybarzola42@gmail.com",
	            d_password = "joseph2010",
	            d_host = "smtp.gmail.com",
	            d_port  = "465";
		Properties props = new Properties();
	    props.put("mail.smtp.user", d_email);
	    props.put("mail.smtp.host", d_host);
	    props.put("mail.smtp.port", d_port);
	    props.put("mail.smtp.starttls.enable","true");
	    props.put("mail.smtp.debug", "true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.socketFactory.port", d_port);
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    props.put("mail.smtp.socketFactory.fallback", "false");
		//Iniciar Sesion
	    System.out.println("iniciando sesion");
		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(d_email, d_password);
			}
		});
		//Para Consultar Las personas que cumplen años
		PersonaDAO personaDAO = new PersonaDAO();
    	
		new LongOperation() {
			
			@Override
			protected void execute() throws InterruptedException {
				while(true) {
					Calendar now = Calendar.getInstance(TimeZone.getTimeZone("America/Guayaquil"));
					System.out.println("Hora de envio");
    				//consulta si hay personas que cumplen años segun el mes y el dia
    				List<Persona> listaPersonas = personaDAO.getPersonas(now.get(Calendar.MONTH) + 1,now.get(Calendar.DAY_OF_MONTH));
    				System.out.println("Personas que cumplen años: " + listaPersonas.size());
    				for(Persona p:listaPersonas){
    					if(p.getEmail().equals(null) || p.getEmail().equals("")) {
    						System.out.println(p.getApellido() + " " + p.getNombre() + " no tiene direccion Email.");
    					}else {
    						try {
								enviarMensaje(session, p.getEmail(), d_email);
							} catch (MessagingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
    					}
    				}
    				//System.out.println("Envios hechos, Dormire por un dia");
    				//Thread.sleep(86400000);
    				Thread.sleep(200000);
				}
			}
		}.start();
		
    }
	
	public void enviarMensaje(Session session, String email, String d_email) throws MessagingException {
		String subject = "Feliz Cumpleaños";
		String mensaje = "Te deseamos lo mejor en este dia y que sigas cumpliendo muchos años mas.";
		
		// Se compone la parte del texto
        BodyPart texto = new MimeBodyPart();
        texto.setText(mensaje);
        //por si quieres enviar una imagen o algo.
		BodyPart adjunto = new MimeBodyPart();
        adjunto.setDataHandler(
        		new DataHandler(new javax.activation.FileDataSource("D:\\Deber.docx")));
        adjunto.setFileName("Deber.docx");
        BodyPart adjunto2 = new MimeBodyPart();
        adjunto2.setDataHandler(
        	new DataHandler(new javax.activation.FileDataSource("D:\\Programación Web\\web.docx")));
        adjunto2.setFileName("web.docx");
		
		MimeMultipart multiParte = new MimeMultipart();
        multiParte.addBodyPart(texto);
        multiParte.addBodyPart(adjunto);
        multiParte.addBodyPart(adjunto2);
		// Create a default MimeMessage object.
		Message message = new MimeMessage(session);

		// Set From: header field of the header.
		message.setFrom(new InternetAddress(d_email));

		// Set To: header field of the header.
		message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

		// Set Subject: header field
		message.setSubject(subject);

		// Now set the actual message
		
		message.setText(mensaje);
		
		//Enviar adjunto
        //message.setContent(multiParte);
		// Send message
		Transport.send(message);
		System.out.println("Mensaje enviado correctamente a "+email+".");
	}
	*/
	
	public void loadTree() throws IOException{		
		SegUsuario usuario = usuarioDAO.getUsuario(SecurityUtil.getUser().getUsername().trim()); 
		if (usuario != null){
			listaPermisosPadre = permisoDAO.getListaPermisosPadre(usuario.getSegPerfil().getIdPerfil());
			listaPermisosTodos = permisoDAO.getListaPermisosTodos(usuario.getSegPerfil().getIdPerfil());
			if (listaPermisosPadre.size() > 0) { //si tiene permisos el usuario
				//capturar solo los menus
				List<SegMenu> listaMenu = new ArrayList<SegMenu>();
				for(SegPermiso permiso : listaPermisosPadre) listaMenu.add(permiso.getSegMenu());
				menu.appendChild(getTreechildren(listaMenu));   
			}			
		}
		listaPermisosPadre = null;
	}
	private Treechildren getTreechildren(List<SegMenu> listaMenu) {
		Treechildren retorno = new Treechildren();
		for(SegMenu opcion : listaMenu) {
			Treeitem ti = getTreeitem(opcion);
			retorno.appendChild(ti);
			List<SegMenu> listaPadreHijo = new ArrayList<SegMenu>();
			for(SegPermiso permiso : listaPermisosTodos) {
				if(permiso.getSegMenu().getIdMenuPadre() == opcion.getIdMenu())
					listaPadreHijo.add(permiso.getSegMenu());
			}
			if (!listaPadreHijo.isEmpty()) {
				ti.appendChild(getTreechildren(listaPadreHijo));
			}
		}
		return retorno;
	}
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private Treeitem getTreeitem(SegMenu opcion) {
		Treeitem retorno = new Treeitem();
		Treerow tr = new Treerow();
		Treecell tc = new Treecell(opcion.getDescripcion());
		//System.out.println("titulomenu: " + tc);
		if (opcion.getIcono() != null) {
			//tc.setIconSclass(opcion.getImagen());
			tc.setSrc(opcion.getIcono());
		}
		tr.addEventListener(Events.ON_CLICK, new EventListener() {
			@Override
			public void onEvent(Event arg0) throws Exception {
				// TODO Auto-generated method stub
				opcionSeleccionado = opcion; 
				if(opcionSeleccionado.getUrl() != null){
					loadContenido(opcionSeleccionado);
				}
			}
		});
		
		tr.appendChild(tc);
		retorno.appendChild(tr);
		retorno.setOpen(false);
		return retorno;
	}
	@NotifyChange({"areaContenido"})
	public void loadContenido(SegMenu opcion) {
		
		if (opcion.getUrl().toLowerCase().substring(0, 2).toLowerCase().equals("http")) {
			Sessions.getCurrent().setAttribute("FormularioActual", null);
			Executions.getCurrent().sendRedirect(opcion.getUrl(), "_blank");			
		} else {
			Sessions.getCurrent().setAttribute("FormularioActual", opcion);	
			areaContenido.setSrc(opcion.getUrl());
		}	
		
	}
	public String getNombreUsuario() {
		return SecurityUtil.getUser().getUsername();
	}
}
