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

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Window;

import ec.edu.upse.modelo.CampaniaDAO;
import ec.edu.upse.modelo.Empresa;
import ec.edu.upse.modelo.EmpresaDAO;
import ec.edu.upse.modelo.Parametrica;
import ec.edu.upse.modelo.ParametricaDAO;
import ec.edu.upse.modelo.Persona;
import ec.edu.upse.modelo.PersonaDAO;
import ec.edu.upse.util.PrintReport;

@SuppressWarnings("serial")
public class ConsultaDonantesTipoSangreC extends SelectorComposer<Component>{
	@Wire private Window winConsultaDonantes;
	@Wire private Combobox cboGrupoSanguineo;
	@Wire private Checkbox chkAll;
	@Wire private Combobox cboGenero;
	@Wire private Combobox cboProvincia;
	@Wire private Combobox cboCiudad;
	@Wire private Listbox lstDonantes;
	List<Persona> donante;
	ParametricaDAO parametricaDAO = new ParametricaDAO();
	CampaniaDAO campaniaDAO = new CampaniaDAO();
	EmpresaDAO empresaDAO = new EmpresaDAO();
	PersonaDAO personaDAO = new PersonaDAO();
	
	@Override
	public void doAfterCompose(Component comp) throws Exception {
		// TODO Auto-generated method stub
		super.doAfterCompose(comp);
	}
	
	@Listen("onClick = #btnImprimir")
	public void imprimir() {
		try {
			if(cboCiudad.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Ciudad","info",cboCiudad,"end_center",2000);
				return;
			}
			if(cboGrupoSanguineo.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Grupo Sanguineo","info",cboGrupoSanguineo,"end_center",2000);
				return;
			}
			Parametrica pSangre = (Parametrica) cboGrupoSanguineo.getSelectedItem().getValue();//tipo sangre seleccionado
			PrintReport obj = new PrintReport();
			Map<String, Object> param = new HashMap<String, Object>();
			List<Empresa> listEmpresa = empresaDAO.getListaEmpresas();
			if(listEmpresa.size() > 0) {
				param.put("TITULO", listEmpresa.get(0).getNombreInstitucion());
				param.put("FACULTAD", listEmpresa.get(0).getFacultad());
				param.put("FACSISTEL", "FACULTAD DE SISTEMAS Y TELECOMUNICACIONES");
				param.put("PROYECTO", listEmpresa.get(0).getNombreProyecto());
				SimpleDateFormat formateador = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy", new Locale("es","ES"));
				param.put("FECHA_EMISION", formateador.format(new Date()));
				param.put("LOGO_FACSISTEL", createImageFromBytes(listEmpresa.get(0).getLogoFacsistel()));
				param.put("LOGO_ENFERMERIA", createImageFromBytes(listEmpresa.get(0).getLogoEnfermeria()));
			}
			if(chkAll.isChecked()) {//todos los aptos y los no aptos
				if(cboCiudad.getSelectedIndex() == 0) {//todas las ciudades de las provincias
					Parametrica pProvincia = (Parametrica)cboProvincia.getSelectedItem().getValue();
					param.put("CIUDAD", "TODAS CIUDADES DE PROVINCIA - " + pProvincia.getDescripcion());
					param.put("TIPO_SANGRE", pSangre.getDescripcion());
					param.put("ID_PROVINCIA", pProvincia.getIdParametrica());
					param.put("ID_SANGRE", pSangre.getIdParametrica());
					obj.crearReporte("/recursos/reportes/rptDonantesTodoCiudadAptosNoAptos.jasper", empresaDAO, param);
				}else {//caso contrario muestra solo de la ciudad seleccionada
					Parametrica pCanton = (Parametrica)cboCiudad.getSelectedItem().getValue();
					param.put("CIUDAD", pCanton.getDescripcion());
					param.put("TIPO_SANGRE", pSangre.getDescripcion());
					param.put("ID_CIUDAD", pCanton.getIdParametrica());
					param.put("ID_SANGRE", pSangre.getIdParametrica());
					obj.crearReporte("/recursos/reportes/rptDonantesCiudadAptosNoAptos.jasper", empresaDAO, param);
				}
			}else {//caso contrario muestra solo los donantes que estan aptos
				if(cboCiudad.getSelectedIndex() == 0) {//primero pregunta si son de todas las ciudades
					Parametrica pProvincia = (Parametrica)cboProvincia.getSelectedItem().getValue();
					param.put("CIUDAD", "TODAS CIUDADES DE PROVINCIA - " + pProvincia.getDescripcion());
					param.put("TIPO_SANGRE", pSangre.getDescripcion());
					param.put("ID_PROVINCIA", pProvincia.getIdParametrica());
					param.put("ID_SANGRE", pSangre.getIdParametrica());
					obj.crearReporte("/recursos/reportes/rptDonantesTodoCiudadAptos.jasper", empresaDAO, param);
				}else {//solo de una ciudad en comun
					Parametrica pCanton = (Parametrica)cboCiudad.getSelectedItem().getValue();
					param.put("CIUDAD", pCanton.getDescripcion());
					param.put("TIPO_SANGRE", pSangre.getDescripcion());
					param.put("ID_CIUDAD", pCanton.getIdParametrica());
					param.put("ID_SANGRE", pSangre.getIdParametrica());
					obj.crearReporte("/recursos/reportes/rptDonantesCiudadAptos.jasper", empresaDAO, param);
				}
			}	
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Listen("onClick = #btnBuscar")
	public void buscar() {
		try {
			if(cboCiudad.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Ciudad","info",cboCiudad,"end_center",2000);
				return;
			}
			if(cboGrupoSanguineo.getSelectedIndex() == -1) {
				Clients.showNotification("Debe seleccionar Grupo Sanguineo","info",cboGrupoSanguineo,"end_center",2000);
				return;
			}
			if(donante != null)
				donante.clear();//limpia la lista
			
			if(chkAll.isChecked()) {//todos los aptos y los no aptos
				if(cboCiudad.getSelectedIndex() == 0) {//todas las ciudades de las provincias
					System.out.println("todas las ciudades de la provincia.. consultar");
					List<Persona> donanteN = new ArrayList<Persona>();//esta lista es la que sera retornada en la tabla
					Parametrica pProvincia = (Parametrica) cboProvincia.getSelectedItem().getValue();//provincia seleccionada
					Parametrica pSangre = (Parametrica) cboGrupoSanguineo.getSelectedItem().getValue();//tipo sangre seleccionado
					List<Persona> donateSel; //obtiene los donates de cada ciudad
					
					for(Parametrica pp : pProvincia.getParametricas()) {
						if(pp.getIdParametrica() != null) {
							donateSel = new ArrayList<>();
							donateSel = personaDAO.getPersonaBuscar(pp.getIdParametrica(), pSangre.getIdParametrica());
							System.out.println("Ciudad: " + pp.getDescripcion());
							System.out.println("cantidad de donantes con sangre: " + pSangre.getDescripcion() + ": " + donateSel.size());
							for(Persona p : donateSel) {
								if(p.getRegistroDonacions().size() > 0) {
									if(calcularMesesAFecha(new Date(p.getRegistroDonacions().get(p.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3)
										p.setEstado("APTA");
									else
										p.setEstado("NO APTA");	
								}else
									p.setEstado("APTA");
								donanteN.add(p);
							}
						}
					}
					Collections.sort(donanteN, new Comparator<Persona>() {
						public int compare(Persona obj1, Persona obj2) {
							return obj1.getApellido().compareTo(obj2.getApellido());
						}
					});
					int cont = 1;
					for(Persona per : donanteN) {
						per.setIdPersona(cont);
						cont = cont + 1;
					}
					System.out.println("total: " + donanteN.size());
					lstDonantes.setModel(new ListModelList(donanteN));
				}else {//caso contrario muestra solo de la ciudad seleccionada
					Parametrica pCanton = (Parametrica)cboCiudad.getSelectedItem().getValue();
					Parametrica pSangre = (Parametrica)cboGrupoSanguineo.getSelectedItem().getValue();
					donante = personaDAO.getPersonaBuscar(pCanton.getIdParametrica(), pSangre.getIdParametrica());
					System.out.println("Ciudad: " + pCanton.getDescripcion());
					System.out.println("cantidad de donantes con sangre: " + pSangre.getDescripcion() + ": " + donante.size());
					for(Persona p : donante) {
						if(p.getRegistroDonacions().size() > 0) {//se elije el ultimo registro de donacion para comparar la fecha
							if(calcularMesesAFecha(new Date(p.getRegistroDonacions().get(p.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3)
								p.setEstado("APTA");
							else
								p.setEstado("NO APTA");	
						}else
							p.setEstado("APTA");
					}
					Collections.sort(donante, new Comparator<Persona>() {
						public int compare(Persona obj1, Persona obj2) {
							return obj1.getApellido().compareTo(obj2.getApellido());
						}
					});
					int cont = 1;
					for(Persona per : donante) {
						per.setIdPersona(cont);
						cont = cont + 1;
					}
					System.out.println("total: " + donante.size());
					lstDonantes.setModel(new ListModelList(donante));	
				}
			}else {//caso contrario muestra solo los donantes que estan aptos
				if(cboCiudad.getSelectedIndex() == 0) {//primero pregunta si son de todas las ciudades
					System.out.println("solo donantes aptos.. de todas las provincias");
					List<Persona> donanteN = new ArrayList<Persona>();
					Parametrica pProvincia = (Parametrica)cboProvincia.getSelectedItem().getValue();
					Parametrica pSangre = (Parametrica)cboGrupoSanguineo.getSelectedItem().getValue();
					List<Persona> donateSel;
					for(Parametrica pp : pProvincia.getParametricas()) {//para obtener los hijos.. en este caso son las ciudades
						donateSel = new ArrayList<Persona>();
						donateSel = personaDAO.getPersonaBuscar(pp.getIdParametrica(), pSangre.getIdParametrica());
						System.out.println("Ciudad: " + pp.getDescripcion());
						System.out.println("cantidad de donantes con sangre: " + pSangre.getDescripcion() + ": " + donateSel.size());
						for(Persona p : donateSel) {
							if(p.getRegistroDonacions().size() > 0) {
								if(calcularMesesAFecha(new Date(p.getRegistroDonacions().get(p.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3) {
									p.setEstado("APTA");
									donanteN.add(p);
								}
							}else {
								p.setEstado("APTA");
								donanteN.add(p);
							}
						}
					}
					System.out.println("total: " + donanteN.size());
					Collections.sort(donanteN, new Comparator<Persona>() {
						public int compare(Persona obj1, Persona obj2) {
							return obj1.getApellido().compareTo(obj2.getApellido());
						}
					});
					int cont = 1;
					for(Persona per : donanteN) {
						per.setIdPersona(cont);
						cont = cont + 1;
					}
					lstDonantes.setModel(new ListModelList(donanteN));
				}else {//solo de una ciudad en comun
					List<Persona> donanteN = new ArrayList<Persona>();
					Parametrica pCanton = (Parametrica)cboCiudad.getSelectedItem().getValue();
					Parametrica pSangre = (Parametrica)cboGrupoSanguineo.getSelectedItem().getValue();
					donante = personaDAO.getPersonaBuscar(pCanton.getIdParametrica(), pSangre.getIdParametrica());
					System.out.println("Ciudad: " + pCanton.getDescripcion());
					System.out.println("cantidad de donantes con sangre: " + pSangre.getDescripcion() + ": " + donante.size());
					for(Persona p : donante) {
						if(p.getRegistroDonacions().size() > 0) {
							if(calcularMesesAFecha(new Date(p.getRegistroDonacions().get(p.getRegistroDonacions().size() -1).getCampania().getFecha().getTime()),new Date()) >=  3) {
								p.setEstado("APTA");
								donanteN.add(p);
							}
						}else {
							p.setEstado("APTA");
							donanteN.add(p);
						}
					}
					Collections.sort(donanteN, new Comparator<Persona>() {
						public int compare(Persona obj1, Persona obj2) {
							return obj1.getApellido().compareTo(obj2.getApellido());
						}
					});
					int cont = 1;
					for(Persona per : donanteN) {
						per.setIdPersona(cont);
						cont = cont + 1;
					}
					System.out.println("total: " + donanteN.size());
					lstDonantes.setModel(new ListModelList(donanteN));
				}
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
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Listen("onChange = #cboProvincia")
	public void cambiaProvincia() {
		
		cboCiudad.setText("");
		Parametrica p = (Parametrica)cboProvincia.getSelectedItem().getValue();
		List<Parametrica> ciudades = parametricaDAO.getCiudades(p.getIdParametrica()).get(0).getParametricas();
		cboCiudad.setModel(new ListModelList(ciudades));
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
	public List<Parametrica> getCiudades(){
		return null;
	}
	public List<Parametrica> getGrupoSanguineo(){
		return parametricaDAO.getGrupoSanguineo().get(0).getParametricas();
	}
	public List<Parametrica> getProvincias(){
		return parametricaDAO.getProvincias().get(0).getParametricas();
	}
}
