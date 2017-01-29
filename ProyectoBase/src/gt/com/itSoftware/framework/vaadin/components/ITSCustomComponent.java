package gt.com.itSoftware.framework.vaadin.components;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.JavaScript;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import gt.com.itSoftware.framework.core.model.Usuario;
import gt.com.itSoftware.framework.core.services.BaseServiceLocator;
import gt.com.itSoftware.framework.core.utils.string.StringUtils;
import gt.com.itSoftware.framework.vaadin.control.ITSUI;
import gt.com.itSoftware.framework.vaadin.view.ConfiguracionGeneral;
import gt.com.itSoftware.framework.vaadin.view.ConfiguracionGeneral.Servidor;


public class ITSCustomComponent<T extends BaseServiceLocator> extends CustomComponent{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final Integer ACCION_CREAR = 1;
	public static final Integer ACCION_MODIFICAR = 2;
	public static final Integer ACCION_VISUALIZAR = 3;
	
	private Integer accionActual = ACCION_CREAR;
	
	private List<Component> componentesDePantalla = new LinkedList<Component>();
	
	
	public List<Component> getComponentesDePantalla() {
		return componentesDePantalla;
	}
	
	public Integer getAccionActual() {
		return accionActual;
	}
	
	public void setAccionActual(Integer accionActual) {
		this.accionActual = accionActual;
	}
	
	@SuppressWarnings("unchecked")
	public T getServiceLocator (){
		return (T) getPrincipalUI().getServiceLocator();
	}

	public String  getUserName (){
		return getPrincipalUI().getUserName();
	}
	
	@SuppressWarnings("unchecked")
	public ITSUI<BaseServiceLocator> getPrincipalUI(){
		return ((ITSUI<BaseServiceLocator>)UI.getCurrent());
	}
	
	public Usuario getUsuarioAutenticado (){
		return getPrincipalUI().getUsuarioAutenticado();
	}
	
	
	public void errorMessage(Exception e){
		Notification.show("Error al realizar acción", "\n"+StringUtils.depurarMensajeError(e.getMessage()), Type.ERROR_MESSAGE);
	}
	
	public void errorMessage(String mensaje ){
		Notification.show("Error al realizar acción", "\n"+StringUtils.depurarMensajeError(mensaje), Type.ERROR_MESSAGE);
	}
	
	public void successMessage(String mensaje){
		Notification.show("Acción realizada exitosamente", "\n"+mensaje, Type.TRAY_NOTIFICATION);
	}
	public void warnginMessage(String titulo, String mensaje){
		Notification.show(titulo, "\n"+mensaje, Type.WARNING_MESSAGE);
	}
	
	
	
	/**
	 * Permite cerrar el explorador actual mediante la utilización de una instrucción javascript.
	 * 
	 */
	public void closeCurrentPage(){
		JavaScript.getCurrent().execute("window.open('','_self').close();");
	}
	
	
	public String getServidor(){
		return ConfiguracionGeneral.getServidor(Servidor.FORMS);
		
	}
	
	public String getServidor( Servidor server){
		return ConfiguracionGeneral.getServidor(server);
	}
	
	public String getReportServer(){
		return ConfiguracionGeneral.getReportServer();
	}
}
