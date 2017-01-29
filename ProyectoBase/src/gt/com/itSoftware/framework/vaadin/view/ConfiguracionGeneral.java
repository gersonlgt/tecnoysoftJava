package gt.com.itSoftware.framework.vaadin.view;

import java.net.InetAddress;

import com.vaadin.ui.UI;

import gt.com.itSoftware.framework.core.services.BaseServiceLocator;
import gt.com.itSoftware.framework.vaadin.control.ITSUI;

public class ConfiguracionGeneral {
	
	public enum Servidor {JASPER, FORMS, REPORTS , APEX , EWS};
	private static InetAddress servidorActual = null;
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static boolean produccion = ((ITSUI<BaseServiceLocator>)UI.getCurrent()).isProduccion(); 
	
	public ConfiguracionGeneral() throws Exception {
		//ServiceLocator servidorDesaProd = new ServiceLocator();
		//valorServidorDesaProd = servidorDesaProd.enProduccion();	
	}
	/*
	public static String getServidor (Servidor server){
		if (isWindows()){
			if (server == Servidor.JASPER)
				return "http://appdesa.sib.gob.gt/";
			else
			if (server == Servidor.REPORTS ||server == Servidor.APEX ||server == Servidor.FORMS)
				return "http://desa.sib.gob.gt:7778/";
			else
			return ".";
		}else{
			// si es desarrollo
			if (valorServidorDesaProd == "Desa"){
				if (server == Servidor.JASPER)
					return "http://appdesa.sib.gob.gt/";
				else
				if (server == Servidor.REPORTS ||server == Servidor.APEX ||server == Servidor.FORMS)
					return "http://desa.sib.gob.gt:7778/";
				else
				return ".";
				
			}else{ // si es producción.
				
				if (server == Servidor.JASPER)
					return "http://appjava.sib.gob.gt/";
				else
				if (server == Servidor.REPORTS ||server == Servidor.APEX ||server == Servidor.FORMS)
					return "http://aplicaciones.sib.gob.gt:7778/";
				else
				return ".";
				
			}
						
		}

	}*/
	
	public static String getServidor (Servidor server) {
		System.out.println("produccion: "+produccion + " servidor " + server);
		
		if (!produccion){
			//return "Estamos en Desarrollo";
			if (server == Servidor.JASPER)
				return "http://ews-desa.sib.gob.gt/";
			else
			if (server == Servidor.EWS)
					return "http://ews-desa.sib.gob.gt:8080/";
			else
			if (server == Servidor.REPORTS ||server == Servidor.APEX ||server == Servidor.FORMS)
				return "http://desa.sib.gob.gt:7778/";
			else
			return "uno/";			
		}else{
			if (produccion){
				//return "Estamos en Producción";
				if (server == Servidor.JASPER)
					return "http://appjava.sib.gob.gt/";
				else
				if (server == Servidor.EWS)
					return "http://ews.sib.gob.gt:8080/";
				else
				if (server == Servidor.REPORTS ||server == Servidor.APEX ||server == Servidor.FORMS)
					return "http://aplicaciones.sib.gob.gt:7778/";
				else
				return "dos/";
			}
			return "tres/";
		}
	}
	
	public static String getReportServer(){
		
		if (isWindows())
			return "rep_desa_mtier";
		else
			if (!produccion){// si es desa
				return "rep_desa_mtier";
			}else{ // Si es producción.
				return "rep_aplicaciones";
			}
			
		 
	}
	
	public static boolean isWindows() {
		 
		return (OS.indexOf("win") >= 0);
 
	}
 
	public static boolean isMac() {
 
		return (OS.indexOf("mac") >= 0);
 
	}

	public static boolean isUnix() {
		 
		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
 
	}
 
	public static boolean isSolaris() {
 
		return (OS.indexOf("sunos") >= 0);
 
	}


}
