package gt.com.itSoftware.framework.vaadin.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.net.URLConnection;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.RequestHandler;
import com.vaadin.server.SystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import com.vaadin.server.SystemMessagesProvider;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;
import com.vaadin.server.VaadinService;
import com.vaadin.server.VaadinServletService;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;

import gt.com.itSoftware.framework.core.model.Usuario;
import gt.com.itSoftware.framework.core.services.BaseServiceLocator;
import gt.com.itSoftware.framework.vaadin.view.MensajeSinAccesoView;


public class ITSUI<T extends BaseServiceLocator> extends UI {

	
	protected boolean produccion = true;
	protected Navigator navigator = null;		
	private T serviceLocator;
	protected String userName = "userName no identificado";
	protected Usuario usuarioAutenticado = null;
	protected boolean hasSecurity = true;
	
	
	
	
	@SuppressWarnings("unchecked")
	protected void init(VaadinRequest request) {
		
		System.out.println("Conectando con usuario :"+request.getRemoteUser());
		try {
			// utilizando refleccion se crea un tipo de datos en tiempo de ejecucion
			/*((Class)((ParameterizedType)this.getClass().
				       getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();*/

			serviceLocator = (T) ((Class)((ParameterizedType)this.getClass().
				       getGenericSuperclass()).getActualTypeArguments()[0]).newInstance();;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();			
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		VaadinService.getCurrent().setSystemMessagesProvider(
                new SystemMessagesProvider() {
                @Override 
                public SystemMessages getSystemMessages(
                    SystemMessagesInfo systemMessagesInfo) {
                	
                	//System.out.println("Paso por aqui");
                    CustomizedSystemMessages messages = new CustomizedSystemMessages();
                    // sesion expirada
                    messages.setSessionExpiredCaption("Su sesión ha expirado");
                    messages.setSessionExpiredMessage("Haga <i>click aquí</i> para continuar.");
                    messages.setSessionExpiredNotificationEnabled(true);
                    messages.setSessionExpiredURL("");
                    
                    messages.setCommunicationErrorCaption("Ha ocurrido un error de comunicación");
                    messages.setCommunicationErrorMessage("Comuníquese con el administrador del sistema para verificar su conectividad.");
                    messages.setCommunicationErrorNotificationEnabled(true);
                    messages.setCommunicationErrorURL("");                    
                    return messages;
                }
            });
		
		
		VaadinSession.getCurrent().addRequestHandler(
		        new RequestHandler() {
		    @Override
		    public boolean handleRequest(VaadinSession session,
		                                 VaadinRequest request,
		                                 VaadinResponse response)
		            throws IOException {
		    	
		    	
		    	System.out.println(request.getPathInfo());
		    	
		    	if ("/SIBDescarga".equals(request.getPathInfo())) {	
			    		URL url;
			        	System.out.println("Iniciar descarga");
			        	response.setHeader("Expires", "0");
			            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
			            response.setHeader("Pragma", "public");
			            OutputStream os = response.getOutputStream();
						String consultando = "http://ews.sib.gob.gt:8080/DescargaArchivosInterno/DescargarArchivoGenerico?"+VaadinServletService.getCurrentServletRequest().getQueryString();
						System.out.println("Consultando :"+consultando);
						url = new URL(consultando);
						URLConnection conn = url.openConnection();
						// se obtiene de la coneccion el nombre del archivo.
						response.setHeader("Content-Disposition", conn.getHeaderField("Content-Disposition"));
						//Se obtiene el tipo de contenido de la petición
						response.setContentType(conn.getContentType());
						byte[] buffer = new byte[1024];			
							InputStream is = conn.getInputStream();
							int len;
					        while ((len = is.read(buffer)) > 0) {
					           	os.write(buffer, 0, len);
					        }
					        is.close();		           
					        os.flush();
					        os.close();
			           
			            return true; 
			        }
		    	else
		            return false; // No hay respuesta
		    }
		});
		
		
		  
				/// se manejan los errores
			setErrorHandler(new DefaultErrorHandler(){
				@Override
				public void error(com.vaadin.server.ErrorEvent event) {			
					//super.error(event);
					//Notification.show(event.getThrowable().getMessage());
					System.out.println("Error desde handler");
					event.getThrowable().printStackTrace();
				}
			});
		
		navigator = new Navigator (this,this);
		
		if (hasSecurity){
			// si está en produccion lee el usurio del servidor
			if (produccion)
				this.userName = request.getRemoteUser();
			
			// si tiene usuario elimina el .
			if (userName != null)
				this.userName = this.userName.toUpperCase().replaceAll("@SIB.GOB.GT", "");
			// se crea el usuario sin acceso.
			navigator.addView("", new MensajeSinAccesoView());			
			//navigator.addView("sinAcceso", new MensajeSinAccesoView());
			// si no es un userName de red.		
			try {
				usuarioAutenticado  = getServiceLocator().obtenerInformacionUsuario(this.userName);
				System.out.println(userName+" "+usuarioAutenticado.getPersPerona()+" "+usuarioAutenticado.getUsuarioSistema()+" "+usuarioAutenticado.getNombre()+": unidad:"+usuarioAutenticado.getUnidadQuePertence()+" - Unidad Padre:"+usuarioAutenticado.getUnidadPadre()+" - Año periodo actual: "+usuarioAutenticado.getAnioPeriodoActual());			
			} catch (Exception e1) {
				Notification.show("Error al identificar al usuario ",e1.getMessage(),Type.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
		
		System.out.println("Ingresando con usuario: "+ userName);
		
		if (userName == null){
			navigator.navigateTo("");
			return;
		}
		
		
	}
	
	public T getServiceLocator() {
		return serviceLocator;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public Usuario getUsuarioAutenticado() {
		return usuarioAutenticado;
	} 
	
	
	public boolean isProduccion() {
		return produccion;
	}


}