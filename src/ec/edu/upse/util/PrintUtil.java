package ec.edu.upse.util;

public class PrintUtil {
	public static final String FORMATO_PDF = "PDF";
	public static final String FORMATO_XLS = "XLS";
/*
	public static void ejecutaReporte(ClaseDAO claseDAO, 
			                          String pathReporte, 
			                          Map<String, Object> parametros, 
			                          String formato,String tituloReporte) {
		// Almacena el nombre de archivo resultante a descargar.
		String nombreArchivo = null;

		// Obtiene la conexion con la base de datos para pasarsela a Jasper
		Connection cn = claseDAO.abreConexion();
		
		try {
			
			// Obtiene el path de la aplicacion.
			String pathAbsoluto = Executions.getCurrent()
					              .getDesktop().getWebApp()
					              .getRealPath("/");
			
			// Arma el path del reporte basado en el path de la aplicacion.
			String archivoReporte = pathAbsoluto + pathReporte;
			
			// Obtiene un nombre aleatorio para el reporte
			nombreArchivo = pathAbsoluto + "/temp/" + UUID.randomUUID().toString();
			System.out.println("Nombre: " + nombreArchivo);
			if (parametros == null) {
				parametros = new HashMap<String, Object>();
			}
			
			// Coloca los parametros por defecto
			//parametros.put("__NOMBRE_INSTITUCION__", "Curso de JAVA");
			System.out.println(parametros + " " + formato);
			// Selecciona el formato.
			if (formato.equals(FORMATO_PDF)) {
				nombreArchivo += ".pdf";
				System.out.println("Archivo: " + nombreArchivo);
				System.out.println("entra 1");
				JasperPrint jasperprint=JasperFillManager.fillReport(archivoReporte,parametros,cn);
				System.out.println("entra 2");
				JasperViewer visor = new JasperViewer(jasperprint,false);
				visor.setTitle(tituloReporte);
	            visor.setVisible(true);
				System.out.println("ejecuto eporte");
			}else{
				// Si se genera en hoja electrónica.
				nombreArchivo += ".xls";
		        JasperPrint jasperPrint = JasperFillManager.fillReport(archivoReporte, parametros, cn);
				JRXlsExporter exporter = new JRXlsExporter();
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(nombreArchivo));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);
				exporter.exportReport();
				System.out.println("ejecuto eporte");
			}
			
			// Descarga el archivo.
			System.out.println("Descarga el archivo");
			Filedownload.save(new File(nombreArchivo), formato); 
			System.out.println("Descargo el archivo");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
	}
*/
}
