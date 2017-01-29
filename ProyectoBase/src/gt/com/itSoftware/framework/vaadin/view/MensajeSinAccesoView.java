package gt.com.itSoftware.framework.vaadin.view;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class MensajeSinAccesoView extends CustomComponent implements View{

	
	private static final long serialVersionUID = 1L;
	@AutoGenerated
	private VerticalLayout layoutPanel;
	private Panel panelPrincipal = null;	
	private VerticalLayout contentedor = null;
	private VerticalLayout contentedorTextoEncabezado = null;
	private Label textoEncabezado = new Label();
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public MensajeSinAccesoView() {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setCompositionRoot(layout);
		setStyleName("borde");
		buildMainLayout();
		setSizeFull();
		//layoutPanel.setStyleName("borde");
		layoutPanel.setSpacing(true);
		layoutPanel.setMargin(true);
		layoutPanel.setSizeFull();
		// permite generar el panel de desplazamiento si la encuesta sobrepasa la pagina.
		panelPrincipal = new Panel();
		panelPrincipal.setSizeFull();						
		panelPrincipal.setContent(layoutPanel);
		layout.addComponent(panelPrincipal);
		
		
		
		// Aqui va el contenido de la encuesta
		contentedor = new VerticalLayout();		
		layoutPanel.addComponent(contentedor);
		layoutPanel.setComponentAlignment(contentedor, Alignment.MIDDLE_CENTER);
		contentedor.setWidth("800px");
		contentedor.setStyleName("agradecimiento");	
		// Se crea el encabezado de la encuesta
		contentedor.addComponent(builtEncabezadoEncuesta("Su usuario no tiene acceso al sistema,\n comuniquese con el administrador para solicitar acceso."));
		
		
	}
	
	
	public Component builtEncabezadoEncuesta(String encabezado){
		
		HorizontalLayout encabezadoEncuesta = new HorizontalLayout();
		encabezadoEncuesta.setWidth("100%");		
		// para la imagen
		HorizontalLayout panelIzquierdo = new HorizontalLayout();
		panelIzquierdo.setMargin(true);
		panelIzquierdo.setWidth("200px");
		// para el espacio derecho
		HorizontalLayout panelDerecho = new HorizontalLayout();
		panelDerecho.setWidth("200px");
		// el texto del encabezado de la encuesta.
		contentedorTextoEncabezado = new VerticalLayout();
		//contentedorTextoEncabezado.setSizeFull();
		// Se agrega la imagen
		Image logoEncuesta = new Image(null, new ThemeResource("img/logo.png"));
		logoEncuesta.setWidth("100px");
		logoEncuesta.setHeight("100px");
		panelIzquierdo.addComponent(logoEncuesta);
		panelIzquierdo.setComponentAlignment(logoEncuesta, Alignment.MIDDLE_CENTER);
		
		encabezadoEncuesta.addComponent(panelIzquierdo);
		contentedorTextoEncabezado.setWidth("100%");
		contentedorTextoEncabezado.setMargin(true);
		encabezadoEncuesta.addComponent(contentedorTextoEncabezado);
		encabezadoEncuesta.setComponentAlignment(contentedorTextoEncabezado,Alignment.MIDDLE_CENTER);
		encabezadoEncuesta.setExpandRatio(contentedorTextoEncabezado, 1);
		encabezadoEncuesta.addComponent(panelDerecho);
		
		textoEncabezado.setValue(encabezado);
		textoEncabezado.setStyleName("encabezado");
		textoEncabezado.setContentMode(ContentMode.HTML);
		//texto.setSizeFull();
		
		contentedorTextoEncabezado.addComponent(textoEncabezado);		
		contentedorTextoEncabezado.setComponentAlignment(textoEncabezado,Alignment.MIDDLE_CENTER);
		
		return encabezadoEncuesta; 
	}
	
	

	@AutoGenerated
	private void buildMainLayout() {
		// the main layout and components will be created here
		layoutPanel = new VerticalLayout();
		//layoutPanel.setSizeFull();
		
	}

	@Override
	public void enter(ViewChangeEvent event) {
		
	}

}

