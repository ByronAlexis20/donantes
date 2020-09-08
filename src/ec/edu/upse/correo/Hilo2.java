package ec.edu.upse.correo;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.MessagingException;
import javax.swing.JOptionPane;

import org.zkoss.zk.ui.util.Clients;

import ec.edu.upse.util.Constantes;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class Hilo2 {
	private boolean continuar = true;
	public static  boolean  envioExito= false;
	private String adjunto;
	private String[] adjuntos;
	private String[] destinatarios;
	private int servidor;
	private String asunto;
	private String mensaje;

/*	private ImageView ivEnviandoMensaje;
	private Button btnEnviarCorreo;
	*/
/*	public Hilo2(String adjunto, String[] adjuntos, String[] destinatarios, int servidor, String destinatario,
			String asunto, String mensaje,ImageView ivEnviandoMensaje, Button btnEnviarCorreo,String cliente, String medidor) {
		this.adjunto = adjunto;
		this.adjuntos = adjuntos;
		this.destinatarios = destinatarios;
		this.servidor = servidor;
		this.destinatario = destinatario;
		this.asunto = asunto;
		this.mensaje = mensaje;
		this.ivEnviandoMensaje = ivEnviandoMensaje;
		this.btnEnviarCorreo = btnEnviarCorreo;
	}*/
	public Hilo2(String adjunto, String[] adjuntos, String[] destinatarios, int servidor,
			String asunto, String mensaje) {
		this.adjunto = adjunto;
		this.adjuntos = adjuntos;
		this.destinatarios = destinatarios;
		this.servidor = servidor;
		this.asunto = asunto;
		this.mensaje = mensaje;
	}
	public void detenElHilo(){
		this.continuar = false;
	}
	int i = 0;

	public void enviarCorreoCumpleanios() {
		while (this.continuar) {
			i = i + 1;
			//adjunto es la direccion del archivo adjunto
			if (this.adjunto.isEmpty()) {
				EnviarMail propio = new EnviarMail(Constantes.CORREO_ORIGEN, Constantes.CONTRASENIA_ORIGEN, 
						this.destinatarios, this.asunto, this.mensaje, this.servidor);
				try {
					propio.enviar();
					
				}
				catch (MessagingException ex){
					JOptionPane.showMessageDialog(null, "Error de envio de correos" + ex.getMessage());
					Logger.getLogger(Hilo2.class.getName()).log(Level.SEVERE, null, ex);
					detenElHilo();
				}
			}
			else {
				EnviarMailComplejo propio = new EnviarMailComplejo(Constantes.CORREO_ORIGEN, Constantes.CONTRASENIA_ORIGEN, this.destinatarios, this.asunto, this.mensaje, this.adjuntos, this.servidor);
				try {
					propio.Enviar();
				}
				catch (MessagingException ex){
					JOptionPane.showMessageDialog(null, "Error" + ex.getMessage());
					Logger.getLogger(Hilo2.class.getName()).log(Level.SEVERE, null, ex);
					detenElHilo();
				}
			}
			detenElHilo();
			System.out.println(i);
		}
	}
	public String getAdjunto() {
		return adjunto;
	}

	public void setAdjunto(String adjunto) {
		this.adjunto = adjunto;
	}

	public String[] getAdjuntos() {
		return adjuntos;
	}

	public void setAdjuntos(String[] adjuntos) {
		this.adjuntos = adjuntos;
	}

	public String[] getDestinatarios() {
		return destinatarios;
	}

	public void setDestinatarios(String[] destinatarios) {
		this.destinatarios = destinatarios;
	}

	public int getServidor() {
		return servidor;
	}

	public void setServidor(int servidor) {
		this.servidor = servidor;
	}

	public String getAsunto() {
		return asunto;
	}

	public void setAsunto(String asunto) {
		this.asunto = asunto;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

/*	public ImageView getIvEnviandoMensaje() {
		return ivEnviandoMensaje;
	}

	public void setIvEnviandoMensaje(ImageView ivEnviandoMensaje) {
		this.ivEnviandoMensaje = ivEnviandoMensaje;
	}

	public Button getBtnEnviarCorreo() {
		return btnEnviarCorreo;
	}

	public void setBtnEnviarCorreo(Button btnEnviarCorreo) {
		this.btnEnviarCorreo = btnEnviarCorreo;
	} */
}