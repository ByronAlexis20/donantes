package ec.edu.upse.controlador;

import java.util.HashMap;
import java.util.Map;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;

import ec.edu.upse.modelo.Campania;
import ec.edu.upse.modelo.ClaseDAO;
import ec.edu.upse.util.PrintReport;

public class ReporteEstadisticoC extends SelectorComposer<Component>{
	ClaseDAO claseDAO = new ClaseDAO();
	@Listen("onClick=#btnVisualizar")
	public void generarReporte(){
		try {
			PrintReport reporte = new PrintReport();
			//reporte.crearReporte("/recursos/reportes/rptEstadisticoSangre.jasper", claseDAO, null);
			//reporte.showReport("li");
		}catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
