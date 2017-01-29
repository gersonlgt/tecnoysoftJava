package gt.com.itSoftware.acmulti.core.control;

import java.io.File;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;

import gt.com.itSoftware.acmulti.core.model.ServiceLocator;
import gt.com.itSoftware.acmulti.crm.view.CrmPrincipalView;
import gt.com.itSoftware.framework.vaadin.control.ITSUI;

@SuppressWarnings("serial")
@Theme("proyectobase")
public class PrincipalUI extends ITSUI<ServiceLocator> {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = PrincipalUI.class)
	public static class Servlet extends VaadinServlet {
	}

	public PrincipalUI() {		
		if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0){
			produccion = false;
			userName = "gdeleon";			
		}				
		// se setea false para que no utilice el usuario
		hasSecurity=true;
	}
	
	@Override
	protected void init(VaadinRequest request) {
		// inicializa la funcionalidad general.
		super.init(request);
		getNavigator().addView("crm", new CrmPrincipalView());
		
	}
	
	public int getProporcionPantallaEnPixeles(Double pProporcion)
	{
		int pixeles=0;
		Double pixelesDouble = this.getPage().getBrowserWindowWidth()*pProporcion;
		pixeles = pixelesDouble.intValue();
		return pixeles;
	}
	
	public int getProporcionPantallaAltoEnPixeles(Double pProporcion)
	{
		int pixeles=0;
		Double pixelesDouble = this.getPage().getBrowserWindowHeight()*pProporcion;
		pixeles = pixelesDouble.intValue();
		return pixeles;
	}
	
	
	public int getProporcionEnPixeles(Double contenedor, Double proporcion){		
		Double pixelesDouble = contenedor * proporcion;
		return pixelesDouble.intValue();
	}
	
	public String getPathTemporal() {		
		return VaadinServlet.getCurrent()
			    .getServletConfig().getServletContext().getRealPath("")+File.separator+"tmpFiles"+File.separator;
	}

}