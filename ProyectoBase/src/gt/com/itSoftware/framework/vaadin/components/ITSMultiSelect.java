package gt.com.itSoftware.framework.vaadin.components;

import java.util.Collection;
import java.util.List;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;

public class ITSMultiSelect extends CustomComponent implements Button.ClickListener, ClickListener{

	
	private static final long serialVersionUID = 1L;
	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	private Panel contenedor = new Panel();
	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private OptionGroup opciones;
	@AutoGenerated
	private CheckBox todos;
	
	private boolean modificado = false;
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ITSMultiSelect() {
		buildMainLayout();
		contenedor.setWidth("100%");		
		contenedor.setContent(mainLayout);
		setCompositionRoot(contenedor);		
		setDefaults();
		
	}

	public boolean isModificado() {
		return modificado;
	}
	
	@Override
	public void setWidth(String width) {		
		contenedor.setWidth(width);
		mainLayout.setWidth(width);
		super.setWidth(width);
	}
	
	
	@Override
	public void setHeight(String height) {
		contenedor.setHeight(height);
	}
	
	public void focus(){
		todos.focus();
	}
	
	private void setDefaults(){	
		
		opciones.setMultiSelect(true);
		todos.setValue(false);
		todos.focus();
		opciones.addValueChangeListener(new ValueChangeListener() {
			
			
			private static final long serialVersionUID = 1L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				if (isSelectedAll()){
					todos.setValue(true);
				}
				
			}
		});
		
		mainLayout.addLayoutClickListener(new LayoutClickListener() {
		
			private static final long serialVersionUID = 1L;

			@Override
			public void layoutClick(LayoutClickEvent event) {
				
				if (event.getClickedComponent() == todos){
					/// Es false porque se ejecuta primero el listener del layout
					if (((CheckBox)event.getClickedComponent()).getValue() == false){
						selectAll();
						return;
					}else{
						unSelectAll();
						return;
					}
				}				
				todos.setValue(false);	
				modificado = true;// verifica si se ha realizado algun cambio
			}
		});	
		
	}

	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(true);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("-1px");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("-1px");
		
		// todos
		todos = new CheckBox();
		todos.setCaption("Todos");
		todos.setImmediate(true);
		todos.setWidth("100.0%");
		todos.setHeight("-1px");
		mainLayout.addComponent(todos);
		
		// opciones
		opciones = new OptionGroup();
		opciones.setImmediate(true);
		opciones.setWidth("100.0%");
		opciones.setHeight("-1px");
		mainLayout.addComponent(opciones);
		
		return mainLayout;
	}
	
	
	public void removeAllItems(){
		this.opciones.removeAllItems();
	}
	
	public void removeItem(Object itemId ){
		this.opciones.removeItem(itemId);
	}
	public void setTextoOpcionTodos (String texto){
		this.todos.setCaption(texto);
	}
	
	public void addItem(Object item){
		opciones.addItem(item);
	}
	
	public void addItem(List<Object> items){
		for (Object item: items){
			opciones.addItem(item);			
		}
	}
	
	public Collection<?> getAllItems(){
		return opciones.getItemIds();
	}
	
	public Collection<?>  getSelectedItems(){
		return (Collection<?>)opciones.getValue();
	}
	
	public void setSelectedItems (Object ... itemIds){	
		for (Object o : itemIds){
			opciones.select(o);
		}
	}
	
	public void selectAll (){
		List<Object> listado = (List<Object>) opciones.getItemIds();
		for (Object o : listado){
			opciones.select(o);
		}
		todos.setValue(true);
	}
	
	public void unSelectAll (){
		List<Object> listado = (List<Object>) opciones.getItemIds();
		for (Object o : listado){
			opciones.unselect(o);
		}
		todos.setValue(false);
	}
	
	private boolean isSelectedAll(){
		//System.out.println(opciones.getItemIds().size()+" " +((Collection)opciones.getValue()).size() );
		return (opciones.getItemIds().size() == ((Collection)opciones.getValue()).size());
	}


	@Override
	public void buttonClick(ClickEvent event) {

		
	}


	@Override
	public void click(com.vaadin.event.MouseEvents.ClickEvent event) {

		
	}		
	

}