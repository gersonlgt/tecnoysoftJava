package gt.com.itSoftware.framework.vaadin.components;

import java.util.LinkedList;
import java.util.List;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Item;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import gt.com.itSoftware.framework.core.services.IServiceLocator;

public class ITSListaDeValoresWindow extends Window
//CustomComponent 
{

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private ITSTable resultados;
	@AutoGenerated
	private VerticalLayout verticalLayout_2;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_2;
	@AutoGenerated
	private TextField busqueda8;
	@AutoGenerated
	private TextField busqueda7;
	@AutoGenerated
	private TextField busqueda6;
	@AutoGenerated
	private TextField busqueda5;
	@AutoGenerated
	private HorizontalLayout horizontalLayout_1;
	@AutoGenerated
	private Button buscar;
	@AutoGenerated
	private TextField busqueda4;
	@AutoGenerated
	private TextField busqueda3;
	@AutoGenerated
	private TextField busqueda2;
	@AutoGenerated
	private TextField busqueda1;
	private Object[] componentes  = null;
	private Object codigo = null;
	private Integer indiceComponentePrincipal = null;
	private ITSDataBaseDataProvider provider = null;
	private IServiceLocator serviceLocator = null;
	private String filtro = "";
	private Integer columnaCodigo = 0;
	private List<TextField> listaComponentesBusqueda = new LinkedList<TextField>();
	
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ITSListaDeValoresWindow() {
		buildMainLayout();
		listaComponentesBusqueda.add(busqueda1);
		listaComponentesBusqueda.add(busqueda2);
		listaComponentesBusqueda.add(busqueda3);
		listaComponentesBusqueda.add(busqueda4);
		listaComponentesBusqueda.add(busqueda5);
		listaComponentesBusqueda.add(busqueda6);
		listaComponentesBusqueda.add(busqueda7);
		listaComponentesBusqueda.add(busqueda8);
		// se ocultan los componentes.
		for (TextField c : listaComponentesBusqueda){
			c.setVisible(false);
		}
		busqueda1.setVisible(true);
		setContent(mainLayout);
		addComponents();
		busqueda1.setTabIndex(1001);
		resultados.setTabIndex(1002);
		buscar.setTabIndex(1003);
		horizontalLayout_1.setComponentAlignment(buscar, Alignment.BOTTOM_CENTER);
		
		
		
	}
	
	/**
	 * Permite obtener el componente de la posici�n indicada por el indice para utilizar un tipo diferente de busqueda al normal.
	 * @param index
	 * @return
	 */
	public TextField obtenerComponenteBusqueda(Integer index){
		return listaComponentesBusqueda.get(index);
	}
	
	public List<TextField> getListaComponentesBusqueda() {
		return listaComponentesBusqueda;
	}

	public void setColumnaCodigo(Integer columnaCodigo) {
		this.columnaCodigo = columnaCodigo;
	}
	
	public Integer getColumnaCodigo() {
		return columnaCodigo;
	}
	
	public void habilitarBusqueda(boolean habilitar){
		verticalLayout_2.setVisible(habilitar);
	}
	
	
	public void setVisibleColumns(Object... columnas){
		resultados.setVisibleColumns(columnas);
	}
	
	public Object[] getVisibleColumns(){
		return resultados.getVisibleColumns();
	}
	
	// define el tama�o de la pantalla, de lo contrario, se autoajusta.
	public void setLovTableWidth(String width){
		resultados.setWidth(width);
	}
	
	
	@SuppressWarnings("serial")
	public void addComponents(){
		
		setModal(true);
		mainLayout.addComponent(new Label("*Para seleccionar haga doble clic sobre el registro seleccionado."));
		mainLayout.addComponent(new Label("*F10 para seleccionar el primer registro"));
		center();
		// se le asigna la funcionalidad a la ventana de cancelar la operaci�n con escape
		addShortcutListener(new ShortcutListener("", KeyCode.ESCAPE, new int[] {}) 
		{
			
			@Override
			public void handleAction(Object sender, Object target) {
				close();
			}
			
		});
		
		// Asignar la acci�n de seleccion al shorcut listener.
		busqueda1.addShortcutListener(new ShortcutListener("", KeyCode.F10, new int[] {}) 
		{
			
			@Override
			public void handleAction(Object sender, Object target) {
				try {
					// selecciona el primer registro que exista.
					if (resultados.getItemIds().size() > 0){
						if (resultados.getItem(resultados.getValue()) == null){
							seleccionarRegistro(resultados.getItem(0));
						}else{
							seleccionarRegistro(resultados.getItem(resultados.getValue()));
						}
					}
				} catch (Exception e) {
					System.out.println("No existe ningun registro para seleccionar.");
					e.printStackTrace();
				}
			}
			
		});
		
		// Asignar la acci�n de moverse entre los registros..
		resultados.setSelectable(true);
		resultados.setNullSelectionAllowed(false);		
		/*resultados.addShortcutListener(new ShortcutListener("", KeyCode.ENTER, new int[] {}) 
		{
			
			@Override
			public void handleAction(Object sender, Object target) {
				try {
					// selecciona el registro seleccionado.
					if (resultados.getItemIds().size() > 0){
						seleccionarRegistro((Item)resultados.getValue());
					}
				} catch (Exception e) {
					System.out.println("No existe ningun registro para seleccionar.");
					e.printStackTrace();
				}
			}
			
		});*/
		
		busqueda1.focus();
		
		// asignacion de la accion para la seleccion de un registro con doble clic.
		resultados.addItemClickListener(new ItemClickListener() {
			
			@Override
			public void itemClick(ItemClickEvent event) {
				if (event.isDoubleClick()){
					// permite seleccionar un registro con doble clic
					seleccionarRegistro(event.getItem());					
				}
			}
		});
		
		buscar.setClickShortcut(KeyCode.ENTER);		
		buscar.setIcon(new ThemeResource("img/verDetalle16_3.png"));
		buscar.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {				
				
				try {
					// si tiene columnas asignadas, no utiliza las del provider.
					if (resultados.getVisibleColumns().length == 0){
						resultados.setEncabezadoTabla(provider.getColumnNames(), provider.getColumnDataType());
					}
					String tmpFiltro = filtro;
					int indice= 1;
					// se modifica el filtro con los datos.
					for (TextField filtro: listaComponentesBusqueda){						
						tmpFiltro = tmpFiltro.replaceAll(":F"+indice, filtro.getValue().trim());
						indice ++;
					}
					
					provider.setFiltro(tmpFiltro);
					Object[][] data = serviceLocator.getConsultaDataBaseProviderConFiltro(provider);
					
					ITSListaDeValoresWindow.this.resultados.llenarTabla(data);
					if (data.length > 0){	
						resultados.select(0);
					}
					
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		});
	}
	
	public void seleccionarRegistro(Item item){
		int contador = 0;
		// asigna el codigo que siempre deber� estar en la primer posici�n.
		if (indiceComponentePrincipal != null)
			codigo = item.getItemProperty(columnaCodigo).getValue();
		// se obtenen los componentes que se ha definido que se van a asignar valores.					
		for (Object componente : componentes){
			
			if (componente != null){
				
				// asigna el codigo al componente principal
				if (contador == indiceComponentePrincipal){
					((AbstractField)componente).setData(item.getItemProperty(columnaCodigo).getValue());
				}
				/*if (componente instanceof TextField ){
					((TextField)componente).setValue(event.getItem().getItemProperty(contador).getValue().toString());								
				}if (componente instanceof TextArea ){
					((TextArea)componente).setValue(event.getItem().getItemProperty(contador).getValue().toString());
				}else	
				{*/
					((AbstractField)componente).setValue((item.getItemProperty(contador).getValue()==null?"":item.getItemProperty(contador).getValue().toString()));
					((AbstractField)componente).setDescription((item.getItemProperty(contador).getValue()==null?"":item.getItemProperty(contador).getValue().toString()));
				//}
			}
			contador++;
		}
		close();
	}
	
	public void setTitulosResultados(String[] nombres, Class[] tiposDeDato){
		resultados.setEncabezadoTabla(nombres, tiposDeDato);
	}
	
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	public void setServiceLocator(IServiceLocator serviceLocator) {
		this.serviceLocator = serviceLocator;
	}
	
	public void setProvider(ITSDataBaseDataProvider provider) {
		this.provider = provider;
	}
	
	/*
	 * Retrona el codigo seleccionado de un registro.
	 * Si no se ha seleccionado este es nulo;
	 */
	public Object getCodigo() {
		return codigo;
	}
	
	public void setComponentePrincipal(Integer indice){
		this.indiceComponentePrincipal = indice;
	}
	
	public void llenarDatos(Object[][] data) throws Exception{
		this.resultados.llenarTabla(data);
	}
	
	public void llenarDatosProvider() throws Exception{
		Object[][] data = serviceLocator.getConsultaDataBaseProvider(provider);
		this.resultados.actualizarTabla(data);
	}
	
	/*
	 * Permite definir el orden en el que se van a asignar los valores seleccinados
	 * de la tabla de acuerdo al orden de los componentes
	 */
	public void setComponentesAsignacion (Object ... componentes){
		this.componentes = componentes;
	}

	public void mostrarRegistros(){
		buscar.click();
	}


	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("-1px");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		
		// top-level component properties
		setWidth("-1px");
		setHeight("-1px");
		
		// verticalLayout_2
		verticalLayout_2 = buildVerticalLayout_2();
		mainLayout.addComponent(verticalLayout_2);
		
		// resultados
		resultados = new ITSTable();
		resultados.setImmediate(false);
		resultados.setWidth("-1px");
		resultados.setHeight("100.0%");
		mainLayout.addComponent(resultados);
		mainLayout.setExpandRatio(resultados, 0.1f);
		
		return mainLayout;
	}

	@AutoGenerated
	private VerticalLayout buildVerticalLayout_2() {
		// common part: create layout
		verticalLayout_2 = new VerticalLayout();
		verticalLayout_2.setImmediate(false);
		verticalLayout_2.setWidth("-1px");
		verticalLayout_2.setHeight("-1px");
		verticalLayout_2.setMargin(true);
		
		// horizontalLayout_1
		horizontalLayout_1 = buildHorizontalLayout_1();
		verticalLayout_2.addComponent(horizontalLayout_1);
		
		// horizontalLayout_2
		horizontalLayout_2 = buildHorizontalLayout_2();
		verticalLayout_2.addComponent(horizontalLayout_2);
		
		return verticalLayout_2;
	}
	
	
	public void focusBusqueda() {
		busqueda1.focus();
	}
	
	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_1() {
		// common part: create layout
		horizontalLayout_1 = new HorizontalLayout();
		horizontalLayout_1.setImmediate(false);
		horizontalLayout_1.setWidth("-1px");
		horizontalLayout_1.setHeight("-1px");
		horizontalLayout_1.setMargin(false);
		horizontalLayout_1.setSpacing(true);
		
		// busqueda1
		busqueda1 = new TextField();
		busqueda1.setCaption("B�squeda :");
		busqueda1.setImmediate(false);
		busqueda1.setWidth("227px");
		busqueda1.setHeight("-1px");
		horizontalLayout_1.addComponent(busqueda1);
		
		// busqueda2
		busqueda2 = new TextField();
		busqueda2.setImmediate(false);
		busqueda2.setWidth("-1px");
		busqueda2.setHeight("-1px");
		horizontalLayout_1.addComponent(busqueda2);
		
		// busqueda3
		busqueda3 = new TextField();
		busqueda3.setImmediate(false);
		busqueda3.setWidth("-1px");
		busqueda3.setHeight("-1px");
		horizontalLayout_1.addComponent(busqueda3);
		
		// busqueda4
		busqueda4 = new TextField();
		busqueda4.setImmediate(false);
		busqueda4.setWidth("-1px");
		busqueda4.setHeight("-1px");
		horizontalLayout_1.addComponent(busqueda4);
		
		// buscar
		buscar = new Button();
		buscar.setCaption("Buscar");
		buscar.setImmediate(true);
		buscar.setWidth("-1px");
		buscar.setHeight("-1px");
		horizontalLayout_1.addComponent(buscar);
		
		return horizontalLayout_1;
	}

	@AutoGenerated
	private HorizontalLayout buildHorizontalLayout_2() {
		// common part: create layout
		horizontalLayout_2 = new HorizontalLayout();
		horizontalLayout_2.setImmediate(false);
		horizontalLayout_2.setWidth("-1px");
		horizontalLayout_2.setHeight("-1px");
		horizontalLayout_2.setMargin(false);
		horizontalLayout_2.setSpacing(true);
		
		// busqueda5
		busqueda5 = new TextField();
		busqueda5.setImmediate(false);
		busqueda5.setWidth("-1px");
		busqueda5.setHeight("-1px");
		horizontalLayout_2.addComponent(busqueda5);
		
		// busqueda6
		busqueda6 = new TextField();
		busqueda6.setImmediate(false);
		busqueda6.setWidth("-1px");
		busqueda6.setHeight("-1px");
		horizontalLayout_2.addComponent(busqueda6);
		
		// busqueda7
		busqueda7 = new TextField();
		busqueda7.setImmediate(false);
		busqueda7.setWidth("-1px");
		busqueda7.setHeight("-1px");
		horizontalLayout_2.addComponent(busqueda7);
		
		// busqueda8
		busqueda8 = new TextField();
		busqueda8.setImmediate(false);
		busqueda8.setWidth("-1px");
		busqueda8.setHeight("-1px");
		horizontalLayout_2.addComponent(busqueda8);
		
		return horizontalLayout_2;
	}

}