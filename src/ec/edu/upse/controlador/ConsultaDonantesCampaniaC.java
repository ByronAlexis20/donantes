package ec.edu.upse.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.Campania;
import ec.edu.upse.modelo.CampaniaDAO;
import ec.edu.upse.modelo.Empresa;
import ec.edu.upse.modelo.EmpresaDAO;
import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.RegistroDonacion;
import ec.edu.upse.modelo.RegistroDonacionDAO;
import ec.edu.upse.util.PrintReport;

public class ConsultaDonantesCampaniaC extends SelectorComposer<Component>{
	@Wire private Window winReporteCampania;
	@Wire private Combobox cboCampania;
	CampaniaDAO campaniaDAO = new CampaniaDAO();
	EmpresaDAO empresaDAO = new EmpresaDAO();
	RegistroDonacionDAO registroDAO = new RegistroDonacionDAO();
	@Wire private Listbox lstDonantes;
	List<Persona> donante = new ArrayList<Persona>();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	public List<Campania> getListaCampania(){
		return campaniaDAO.getListaTodasCampanias();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onClick=#btnBuscar")
	public void buscarDonantesCampania() throws CloneNotSupportedException{
		donante.clear();//en cada iteracion.. osea en cada busqueda se limpia la lista nuevamente
		System.out.println("lista limpia se supone: " + donante.size());
		boolean band = false;//bandera que me sirve para saber si existe un donante repetido.. xq se va a mostrar todos los donantes. y si no se hace eso. la muestra varias veces
		if(cboCampania.getSelectedIndex() == -1) {
			Clients.showNotification("Debe seleccionar Campaña","info",cboCampania,"end_center",2000);
			return;
		}
		Campania pCampania = (Campania) cboCampania.getSelectedItem().getValue();
		if(pCampania.getNombreCampania().equals("Todo")) {
			System.out.println("entra a buscar todo");
			List<RegistroDonacion> registro = registroDAO.getRegistroAll();
			System.out.println(registro.size());
			for(RegistroDonacion r : registro) {
				band = false;
				for(Persona rg : donante) {
					if(r.getPersona().getIdPersona() == rg.getIdPersona())
						band = true;
				}
				if(band == false) {
					Persona pClon = r.getPersona().clone();
					donante.add(pClon);
				}
			}
			
			Collections.sort(donante, new Comparator<Persona>() {
				public int compare(Persona obj1, Persona obj2) {
					return obj1.getApellido().compareTo(obj2.getApellido());
				}
			});
			int cont = 1;
			for(Persona p : donante) {
				p.setIdPersona(cont);
				if(p.getRegistroDonacions().size() > 0) {
					if(calcularMesesAFecha(new Date(p.getRegistroDonacions().get(p.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3)
						p.setEstado("APTA");
					else
						p.setEstado("NO APTA");	
				}else
					p.setEstado("APTA");
				cont = cont + 1;
			}

		}else {
			int cont = 1;
			List<RegistroDonacion> registro = registroDAO.getRegistroByIdCampania(pCampania.getIdCampania());
			if(registro.size() > 0) {
				for(RegistroDonacion r : registro) {
					r.getPersona().setIdPersona(cont);
					donante.add(r.getPersona());
					cont = cont + 1;
				}
				for(Persona p : donante) {
					if(p.getRegistroDonacions().size() > 0) {
						if(calcularMesesAFecha(new Date(p.getRegistroDonacions().get(p.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3)
							p.setEstado("APTA");
						else
							p.setEstado("NO APTA");	
					}else
						p.setEstado("APTA");
				}
				System.out.println("tamaño: " + donante.size());
			}
		}
		lstDonantes.setModel(new ListModelList(donante));
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
	@Listen("onClick=#btnImprimir")
	public void generarReporte(){
		try {
			Campania pCampania = (Campania)cboCampania.getSelectedItem().getValue();
			if(lstDonantes.getItems().size() > 0) {
				Map<String, Object> param = new HashMap<String, Object>();
				List<Empresa> listEmpresa = empresaDAO.getListaEmpresas();
				if(listEmpresa.size() > 0) {
					if(cboCampania.getSelectedIndex() == 0) {
						param.put("TITULO", listEmpresa.get(0).getNombreInstitucion());
						param.put("FACULTAD", listEmpresa.get(0).getFacultad());
						param.put("FACSISTEL", "FACULTAD DE SISTEMAS Y TELECOMUNICACIONES");
						param.put("PROYECTO", listEmpresa.get(0).getNombreProyecto());
						param.put("CAMPANIA", "Todo");
						SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es","ES"));
						param.put("FECHA_CAMPANIA", formateador.format(new Date()));
						param.put("FECHA_EMISION", formateador.format(new Date()));
						param.put("LOGO_FACSISTEL", createImageFromBytes(listEmpresa.get(0).getLogoFacsistel()));
						param.put("LOGO_ENFERMERIA", createImageFromBytes(listEmpresa.get(0).getLogoEnfermeria()));
						PrintReport obj = new PrintReport();
						obj.crearReporte("/recursos/reportes/rptDonantesAptosTodos.jasper", empresaDAO, param);
					}else {
						param.put("TITULO", listEmpresa.get(0).getNombreInstitucion());
						param.put("FACULTAD", listEmpresa.get(0).getFacultad());
						param.put("FACSISTEL", "FACULTAD DE SISTEMAS Y TELECOMUNICACIONES");
						param.put("PROYECTO", listEmpresa.get(0).getNombreProyecto());
						
						List<Campania> listCampania = campaniaDAO.getCampaniaById(pCampania.getIdCampania());
						if(listCampania.size() > 0) {
							SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es","ES"));
							param.put("CAMPANIA", listCampania.get(0).getNombreCampania());
							param.put("FECHA_CAMPANIA", formateador.format(listCampania.get(0).getFecha()));
							param.put("FECHA_EMISION", formateador.format(new Date()));
							System.out.println("id campaña: " + listCampania.get(0).getIdCampania());
							param.put("ID_CAMPANIA", pCampania.getIdCampania());
						}else {
							param.put("CAMPANIA", "");
							param.put("FECHA_CAMPANIA", "");
							param.put("FECHA_EMISION", "");
							param.put("ID_CAMPANIA", null);
						}

						param.put("LOGO_FACSISTEL", createImageFromBytes(listEmpresa.get(0).getLogoFacsistel()));
						param.put("LOGO_ENFERMERIA", createImageFromBytes(listEmpresa.get(0).getLogoEnfermeria()));
						PrintReport obj = new PrintReport();
						obj.crearReporte("/recursos/reportes/rptDonantesAptosDonar.jasper", empresaDAO, param);
					}

				}else {

				}

			}
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	private BufferedImage createImageFromBytes(byte[] imageData) {
		ByteArrayInputStream bite = new ByteArrayInputStream(imageData);
		try {
			return ImageIO.read(bite);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	public List<Persona> getDonante() {
		return donante;
	}
	public void setDonante(List<Persona> donante) {
		this.donante = donante;
	}
}
