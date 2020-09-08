package ec.edu.upse.controlador;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.zkoss.io.Files;
import org.zkoss.util.media.Media;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import ec.edu.upse.correo.Hilo2;
import ec.edu.upse.modelo.ParametricaDAO;
import ec.edu.upse.modelo.Persona;

@SuppressWarnings("serial")
public class EnvioCorreo extends SelectorComposer<Component> {
	@Wire private Label lblNombreArchivo;
	@Wire private Window winEnvioCorreos;
	@Wire private Combobox cboProvincia;
	@Wire private Textbox txDestinatario;
	@Wire private Textbox txtAsunto;
	@Wire private Textbox txtCuerpo;

	List<Persona> donante;
	ParametricaDAO parametricaDAO = new ParametricaDAO();


	@Listen("onClick=#btnBuscarDestinat")
	public void buscarListadoDestinatarios() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("VentanaPadre", this);
		Window ventanaCargar = (Window) Executions.createComponents("/principal/destinatarioLista.zul", winEnvioCorreos, params);
		ventanaCargar.doModal();
	}

	Media media;
	@Listen("onUpload = button#upload")
	public void onUpload(UploadEvent event) {
		System.out.println("ADJUNTAR");
		try {
			System.out.println("before upload " + event.getMedia().getName());
			media = event.getMedia();
			lblNombreArchivo.setValue(media.getName());

		} catch (Exception e) {
			e.printStackTrace();
			Messagebox.show("Upload failed");
		}
	}	
	List<String> listaCorreos;
	public void recuperarDatos(List<String> listaSeleccionada) {
		try {
			txDestinatario.setValue("");
			listaCorreos = new ArrayList<String>();
			System.out.println("Listado pantalla recuperada: " + listaSeleccionada.size());
			String correos = "";

			for(String per : listaSeleccionada) {

				correos = correos + per + ";";
				listaCorreos.add(per);
			}
			System.out.println(listaCorreos.size() + " correos liststststststsr");
			txDestinatario.setValue(correos);
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	Integer contadorEnviados = 0;
	Integer contadorNoEnviados = 0;
	Integer contadorNoValidos = 0;

	@Listen("onClick=#btnEnviar")
	public void enviarCorreo() {
		try {
			contadorEnviados = 0;
			contadorNoEnviados = 0;
			contadorNoValidos = 0;


			if(txDestinatario.getText() == "") {
				Clients.showNotification("Debe registrar destinatario","info",txDestinatario,"end_center",2000);
				return;
			}

			if(txtAsunto.getText() == "") {
				Clients.showNotification("Debe registrar asunto","info",txtAsunto,"end_center",2000);
				return;
			}
			List<String> correosValidos = new ArrayList<String>();
			List<String> correosAgregar = new ArrayList<String>();
			List<String[]> listaEnviar = new ArrayList<String[]>();
			Integer contArreglos = 0;
			Integer contCorreos = 0;
			System.out.println(listaCorreos.size() + " correos a enviar");
			for(String correoEnviar : listaCorreos) {
				if(validarEmail(correoEnviar) == true) {
					contadorEnviados ++;
					contCorreos ++;
					correosValidos.add(correoEnviar);
					correosAgregar.add(correoEnviar);

					if(contCorreos == 50) {
						String[] destinatarios = new String[50];

						for(int i = 0 ; i < correosAgregar.size() ; i++)
							destinatarios[i] = correosAgregar.get(i);

						listaEnviar.add(destinatarios);
						contCorreos = 0;
						contArreglos ++;
						correosAgregar = new ArrayList<String>();
					}
				}
				else {
					contadorNoValidos ++;
					contadorNoEnviados ++;
				}
			}
			if(contCorreos < 50) {
				String[] destinatarios = new String[contCorreos];

				for(int i = 0 ; i < correosAgregar.size() ; i++)
					destinatarios[i] = correosAgregar.get(i);

				listaEnviar.add(destinatarios);
				contCorreos = 0;
				contArreglos ++;
			}

			for(int i = 0 ; i < contArreglos ; i++)
				enviarCorreoPersonas(listaEnviar.get(i));


			Messagebox.show("Correos enviados exitosamente\n\nCorreos enviados: " + contadorEnviados + "\nCorreos no enviados: " + contadorNoEnviados + "\nCorreos no validos : " + contadorNoValidos);
			limpiar();
		}catch(Exception ex) {

		}
	}

	private void enviarCorreoPersonas(String[] correosValidos) {
		try {

			String adjunto = "";
			String[] adjuntos = adjunto.split(",");
			String asunto;
			String mensaje;
			int servidor;
			String[] destinatarios = correosValidos;
			asunto = txtAsunto.getText();
			System.out.println("Correos adjuntos: " + destinatarios.length);
			servidor = 0;
			mensaje = txtCuerpo.getText();
			if(media != null) {
				System.out.println("archivo");
				String nombreArchivo = "";
				String pathAbsoluto = Executions.getCurrent().getDesktop().getWebApp().getRealPath("/");

				nombreArchivo = pathAbsoluto + "temp";
				nombreArchivo = nombreArchivo + "\\" + lblNombreArchivo.getValue();


				String carpetaArchivo = pathAbsoluto + "temp";
				File folder = new File(carpetaArchivo);
				if (folder.exists()) {
				}else {
					folder.mkdir();
				}
				Files.copy(new File(nombreArchivo),media.getStreamData());

				System.out.println("archivo: " + nombreArchivo);
				adjunto = nombreArchivo;
				adjuntos = adjunto.split(",");

			}

			Hilo2 miHilo = new Hilo2(adjunto, adjuntos, destinatarios, servidor, asunto, mensaje);
			miHilo.enviarCorreoCumpleanios();	
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public void limpiar() {
		media = null;
		lblNombreArchivo.setValue("Sin archivo");
		txDestinatario.setText("");
		txtAsunto.setText("");
		txtCuerpo.setText("");

	}
	private static final String EMAIL_PATTERN = 
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public static boolean validarEmail(String email) {
		try{
			// Compiles the given regular expression into a pattern.
			Pattern pattern = Pattern.compile(EMAIL_PATTERN);
			// Match the given input against this pattern
			Matcher matcher = pattern.matcher(email);
			return matcher.matches();
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
}
