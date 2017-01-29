/**
 * 
 */
package gt.com.itSoftware.framework.vaadin.components;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.XMLGregorianCalendar;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.Converter;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.BaseTheme;

import gt.com.itSoftware.framework.core.utils.string.DateUtils;
import gt.com.itSoftware.framework.vaadin.components.data.ITSItem;
import gt.com.itSoftware.framework.vaadin.utils.VaadinUtils;

public class ITSTable extends Table {

	private static final long serialVersionUID = 1L;
	
	private String[] nombreColumnas = null;	
	@SuppressWarnings("rawtypes")
	private Class[] tiposDeDato = null;
	private List<Field> columnasClase = null ;
	private Class<?> clase = null;
	//private Map<Object, Object> columnasClase = new HashMap<Object, Object>(); 
	private HashMap<Object, Object> columnasTooltip = new HashMap<Object, Object>();
	private HashMap<Object, Boolean> filasModificadas = new HashMap<Object, Boolean>();
	private HashMap<Object, Boolean> filasNuevas = new HashMap<Object, Boolean>();
	private HashMap<Object, Boolean> columnasDeshabilitadas = new HashMap<Object, Boolean>();
	private Integer[] idsColumnasDeshabilitadas = null;
	private HashMap<Object, Boolean> columnasEventoModificacion = new HashMap<Object, Boolean>();
	private Integer[] idsColumnasEventoModificacion = null;
	private boolean ingorarPrimerRegistro = false;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy"); 
	private Object[] todasColumnasVisibles = null;
	private Converter<?, ?>[] formatosColumnas = null;
	private Integer[] totalesEnColumnas = null;
	private boolean[] edicionComponentesEnColumnas = null;
	private int numeroFilasTextArea=2;
	private boolean visualizarFilasModificadasColoreadas = false;
	private String estiloCeldaModificada="frmFilaModificada";
	// define si es necesario limpiar la tabla al llenarla
	// se utiliza en el metodo llenar tabla.
	private boolean banderaLimpiarTabla = true;// definido para poder agregar un registro sin limpiar la tabla
	private boolean banderaNuevoRegistroObjetoTabla = false;// definido para manejar los nuevos registros atraves de un objeto 
	private Map<Object, Listener> columnasValueChange = new HashMap<Object, Listener>();
	private Map<Object, Object[][]> listasDeValores = new HashMap<Object,Object[][]>();
	
	
	//formato default para numericos
	private NumberFormat formato = new DecimalFormat("#,###,###,##0.00");
	
	
	
	public ITSTable() {
		
		setItemDescriptionGenerator(new ItemDescriptionGenerator() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public String generateDescription(Component source, Object itemId, Object propertyId) {
				String tooltip = "";
				if (propertyId != null)
					if ((tooltip = (String) columnasTooltip.get(propertyId)) != null) {
						return tooltip;
					} else if (columnasTooltip.containsKey(propertyId)) {
						Item item = getItem(itemId);
						return "" + item.getItemProperty(propertyId).getValue();
					}
				return null;
			}

		});
		
		//alwaysRecalculateColumnWidths = true;		
	}
	
	
	public void addListenerToColumn(Object id , Listener valueChange){
		columnasValueChange.put(id, valueChange);
	}
	public void removeListenerToColumn(Object id ){
		columnasValueChange.remove(id);
	}
	/**
	 * Permite reacalcular en tiempo de presentacion el tamaño de las columnas.
	 * 
	 * @param always	Valor boolean
	 */
	public void setAlwaysRecalculateColumnWidths (boolean always){
		alwaysRecalculateColumnWidths = always;
	}
	/**
	 * Si la columna es de tipo Combobox de valores, se utiliza los valores enviados para crear un combo box con los valores indicados, en la columna 1 el codigo, y el la columna 2 la descripcion
	 * 
	 * @param id	id de la columan iniciando en 0
	 * @param combo	Arreglo de objetos que contiene los datos a incluir en la lista de valores.
	 */
	public void addComboboxToColumn(Object id, Object[][] combo){
		listasDeValores.put(id, combo);
	}
	
	public boolean setIgnorarPrimerRegistro(boolean ignorar){
		return this.ingorarPrimerRegistro = ignorar;
	}
	/**
	 * eramirez
	 * Procedimiento que permite actualizar el estado de modificación de una fila de la tabla
	 * @param itemId es el indice de la fila de la tabla a la cual se le desea establecer el estado de modificación
	 * @param estadoModificacion true: indica que la fila se modifico.  false: indica que la fila no se modifico
	 */
	
	public void setEstadoModificacionFila(Object itemId, boolean estadoModificacion) {
		filasModificadas.put(itemId, estadoModificacion);
		if (visualizarFilasModificadasColoreadas)
		{
			colorearFilasModificadas();
		}
	}
	
	/**
	 * Procedimiento que permite enviar que componentes de columnas serán editables
	 * @param valores
	 */
	@Deprecated
	public void setEdicionComponentesEnColumnas(boolean... valores)
	{
		edicionComponentesEnColumnas = valores;
	}
	
	
	/**
	 * Procedimiento que permite especificar las columnas que estaran deshabilitadas
	 * @param valores
	 */
	public void setIdsColumnasDeshabilitadas(Integer... idsColDeshabilitadas) {
		this.idsColumnasDeshabilitadas = idsColDeshabilitadas;
	}
	
	/**
	 * Procedimiento que permite especificar las columnas en las que el componente de la misma
	 * implementara el evento de ValueChange
	 * @param valores
	 */
	public void setIdsColumnasEventoModificacion(Integer... idsColEventoModificacion) {
		this.idsColumnasEventoModificacion = idsColEventoModificacion;
	}

	
	/**
	 * Procedimiento que unifica la funcionalidad de la edicion de componentes y la nueva funcionalidad
	 * para deshabilitar los componentes de las columnas especificadas
	 */
	private void cargarMapColumnasDeshabilitadas()
	{
		columnasDeshabilitadas.clear();
		
		if(edicionComponentesEnColumnas!=null)
		{
			for(int i=0; i<edicionComponentesEnColumnas.length; i++)
			{
				columnasDeshabilitadas.put(i,edicionComponentesEnColumnas[i]);
			}
		}
		
		
		if(idsColumnasDeshabilitadas!=null)
		{
			for(int i=0; i<idsColumnasDeshabilitadas.length; i++)
			{
				columnasDeshabilitadas.put(idsColumnasDeshabilitadas[i],false);
			}
		}
	}
	
	
	
	/**
	 * Procedimiento que unifica la funcionalidad de la edicion de componentes y la nueva funcionalidad
	 * para implementar los eventos de valueChange en los componentes de las columnas especificadas
	 */
	private void cargarMapColumnasEventoModificacion()
	{
		columnasEventoModificacion.clear();
		
		if(edicionComponentesEnColumnas!=null)
		{
			for(int i=0; i<edicionComponentesEnColumnas.length; i++)
			{
				columnasEventoModificacion.put(i,edicionComponentesEnColumnas[i]);
			}
		}
		
		
		if(idsColumnasEventoModificacion!=null)
		{
			for(int i=0; i<idsColumnasEventoModificacion.length; i++)
			{
				columnasEventoModificacion.put(idsColumnasEventoModificacion[i],true);
			}
		}
	}
	
	
	
	/**
	 * Procedimiento que establece si se colorea las filas modificadas o no
	 * @param valor
	 */
	public void setVisualizarFilasModificadasColoreadas(boolean valor) 
	{				
		visualizarFilasModificadasColoreadas = valor;
	}
	
	/**
	 * eramirez
	 * Función que devuelve el estado de modificación de la fila de la tabla
	 * @param itemId es el indice la fila de la tabla de la cual se desea saber si tuvo cambios o modificaciones
	 * @return
	 */
	public boolean getEstadoModificacionFila(Object itemId){
		return filasModificadas.get(itemId);
	}
	
	/**
	 * Procedimiento que establece el numero de filas que se visuallizan en los TextArea de la tabla
	 * @param numeroFilas
	 */
	public void setNumeroFilasTextArea(int numeroFilas)
	{
		numeroFilasTextArea=numeroFilas;
	}
	
	/**
	 * eramirez
	 * Función que retorna los indices de las filas modificadas
	 * @return retorna la lista de itemsId de las filas que fueron modificadas 
	 */
	public Collection getFilasModificadas()
	{
		return filasModificadas.keySet();
	}
	
	
	public Collection getFilasNuevas()
	{
		return filasNuevas.keySet();
	}
	

	@SuppressWarnings("rawtypes")
	public void setEncabezadoTabla(String[] nombreColumnas, Class[] tiposDeDato) {		
		this.nombreColumnas = nombreColumnas;		
		this.tiposDeDato = tiposDeDato;
		
		if ( nombreColumnas == null || tiposDeDato == null)
			throw new RuntimeException ("Ninguno de los encabezados puede ser nulo.");
		// Se establecen los encabezados a la tabla en conjunto con los tipos de datos
		for (int x = 0; x < tiposDeDato.length; x++){
			this.addContainerProperty(x, this.tiposDeDato[x],null);
			this.setColumnHeader(x, this.nombreColumnas[x]);
		}
		
		this.todasColumnasVisibles = getVisibleColumns();
		 
	}
	
	@SuppressWarnings("rawtypes")
	public void setEncabezadoTabla(String[] nombreColumnas, Class[] tiposDeDato,Class<?> clase) {
		this.clase = clase;
		this.nombreColumnas = nombreColumnas;		
		this.tiposDeDato = tiposDeDato;
		this.columnasClase = VaadinUtils.getPropiedadesDeClaseList(clase);
		
		if ( nombreColumnas == null || tiposDeDato == null)
			throw new RuntimeException ("Ninguno de los encabezados puede ser nulo.");
		// Se establecen los encabezados a la tabla en conjunto con los tipos de datos
		for (int x = 0; x < tiposDeDato.length; x++){
			this.addContainerProperty(x, this.tiposDeDato[x],null);
			this.setColumnHeader(x, this.nombreColumnas[x]);
		}
		this.todasColumnasVisibles = getVisibleColumns();
		 
	}
	
	
	public void setEncabezadoTabla(String[] nombreColumnas, Class<?> clase) {
		if ( nombreColumnas == null )
			throw new RuntimeException ("Ninguno de los encabezados puede ser nulo.");
		
		this.columnasClase = VaadinUtils.getPropiedadesDeClaseList(clase);
		
		if (columnasClase.size() > nombreColumnas.length)
			throw new RuntimeException ("La cantidad de propiedades de la clase es menor a la de los titulos.");
		
		this.clase = clase;
		this.nombreColumnas = nombreColumnas;		
		this.tiposDeDato = new Class[nombreColumnas.length];
				
		
		// Se establecen los encabezados a la tabla en conjunto con los tipos de datos
		for (int x = 0; x < nombreColumnas.length; x++){
			this.tiposDeDato[x]=columnasClase.get(x).getType();
			this.addContainerProperty(x, this.tiposDeDato[x],null);
			this.setColumnHeader(x, this.nombreColumnas[x]);
			
		}
		this.todasColumnasVisibles = getVisibleColumns();
		 
	}
	
	
	
	/**
	 * Se utiliza para definir los encabezados de las tablas sin indicar el tipo de datos.
	 * Se asume por defecto que los tipos de datos datos de la tabla son String.class
	 * 
	 * @param nombreColumnas Los nombres que se les desea asignar a las columnas.
	 */
	public  void setEncabezadoTabla(String[] nombreColumnas) {		
		
		
		if (nombreColumnas == null) {
			throw new RuntimeException ("Ninguno de los encabezados puede ser nulo.");
		}	
		this.nombreColumnas = nombreColumnas;		
		this.tiposDeDato = new Class[nombreColumnas.length];
		// Se establecen los encabezados a la tabla en conjunto con 
		//los tipos de datos
		for (int x = 0; x < nombreColumnas.length; x++) {
			this.addContainerProperty(x, String.class, null); 
			this.setColumnHeader(x, this.nombreColumnas[x]);
			this.tiposDeDato[x] = String.class;
		}
		
		this.todasColumnasVisibles = getVisibleColumns();
		 
	}
	
	/**
	 * @author gdeleon
	 * @param rowId
	 *            Número de fila
	 * @param colId
	 *            Número de columna
	 * @param valor
	 *            valor de la celda
	 * @return El valor formateado segun el formato correspondiente a la lista
	 *         de formatos agregados a la tabla Funcion que formatea el valor
	 *         que se envia como parametro en el parametro valor segun el
	 *         formato correspondiente a la lista de formatos agregados a la
	 *         tabla.
	 */
	public String formatoPersonalizado(Object rowId, Object colId, Object valor) {
		if (valor != null) {
			if (formatosColumnas != null && formatosColumnas[(Integer) colId] != null) {
				TextField textField = new TextField(valor.toString());
				textField.setValue(valor.toString());
				textField.setConverter((Converter<String, ?>) formatosColumnas[(Integer) colId]);
				String retorna = (String) textField.getConvertedValue();
				textField = null;
				return retorna;
			} else if (valor instanceof Date) {
				Date dateValue = (Date) valor;
				return new SimpleDateFormat("dd/MM/yyyy").format(dateValue);
			} else if (valor instanceof XMLGregorianCalendar){				
				Date dateValue = ((XMLGregorianCalendar) valor).toGregorianCalendar().getTime();
				return new SimpleDateFormat("dd/MM/yyyy").format(dateValue); 
			}
		}
		return null;
	}
	
	@Override
	protected String formatPropertyValue(Object rowId, Object colId,
			Property<?> property) {
		Object v = property.getValue();
		String retorna = formatoPersonalizado(rowId, colId, v);
		if (retorna != null) {
			return retorna;
		}

		return super.formatPropertyValue(rowId, colId, property);
	}

	public void actualizarTabla(Object[][] data) throws Exception{
		// obtenemos las columnas visibles actuales;
		Object[] columnasVisibles = getVisibleColumns();	
		// activamos todas las columnas previo a llenarse.
		setVisibleColumns(this.todasColumnasVisibles);
		llenarTabla(data);
		// se muestran solo las que se encontraban visibles antes de la actualizacion.
		setVisibleColumns(columnasVisibles);
	}
	
	
	@Override
	public Object addItem(Object[] cells, Object itemId)
			throws UnsupportedOperationException {
				// obtenemos las columnas visibles actuales;
				Object[] columnasVisibles = getVisibleColumns();	
				// activamos todas las columnas previo a llenarse.
				setVisibleColumns(this.todasColumnasVisibles);
				Object objeto = super.addItem(cells, itemId);
				//si es un nuevo registro, se agrega a la opcion de nuevos registro.s
				if (banderaNuevoRegistroObjetoTabla == true){
					filasNuevas.put(itemId, true);
				}
				// se muestran solo las que se encontraban visibles antes de la actualizacion.
				setVisibleColumns(columnasVisibles);

		return objeto;
	}
	
	
	public Object addItemNuevo(Object[] cells, Object itemId)
	{
		//Se inicializa la fila nueva con estado true
		filasNuevas.put(itemId, true);
		return addItem(cells, itemId);
	}
	
	/**
	 * Permite agregar un nuevo item a la tabla, pero tomando en cuenta que la
	 * generación del ID del registro será un correlativo.
	 * 
	 * @param cells
	 *            Datos que se van a agregar a la celda
	 * @return el item que se va ha creado
	 */
	public Object addItemNuevo(Object[] cells) {
		int size = getItemIds().size() + 1;
		// Se inicializa la fila nueva con estado true
		filasNuevas.put(size, true);
		return addItem(cells, size);
	}
	
	/**
	 * Permite agregar un grupo de objetos a la tabla. El orden de los campos
	 * defindos en la tabla, debe coincidir con el orden declarados en la clase.
	 * 
	 * @param objeto
	 *            Listado de objetos separados por coma.
	 * @throws Exception
	 */
	public void addItemObject(final Object ... objeto) throws Exception {
		Object[] dato = null;
		int contador = -1; // define las columans.
		Method[] metodos = new Method[tiposDeDato.length];
		for (Field field : columnasClase) {			
			contador++;
			metodos[contador] = clase.getMethod("get" 
												+ field.getName().toUpperCase().charAt(0) 
												+ field.getName().substring(1));
			metodos[contador].setAccessible(true);			
			//System.out.println(field.getName() + " "+field.getType());
		}		
		//System.out.println(tiposDeDato);
		//Se crean el contenedor de objetos[] 
		Object[][] o = new Object[objeto.length][tiposDeDato.length];
		contador = 0;
		// se obtienen todos los objetos
		for (Object reg:objeto){
			// se crea un nuevo registro.
			dato = new Object[tiposDeDato.length];
			// por cada columna.
			for (int x = 0; x < columnasClase.size(); x++){				
				// se obtiene el dato de cada campo.
				dato[x] = metodos[x].invoke(reg);				
			}
			////addItem(dato, getItemIds().size()+1);
			o[contador] = dato;
			contador ++;
		}
		
		banderaLimpiarTabla = false;
		actualizarTabla(o);
		banderaLimpiarTabla = true;
	}
	
	public void addItemNuevoObject(final Object  objeto) throws Exception{
		//int size = getItemIds().size();
		// Se inicializa la fila nueva con estado true
		//filasNuevas.put(size, true);
		banderaNuevoRegistroObjetoTabla = true;
		addItemObject(objeto);
		banderaNuevoRegistroObjetoTabla = false;
	}
	
	public Object getItemAsObject(Object itemid) throws Exception {
			if (clase == null){
				throw new Exception("No se ha definido un tipo de clase para la tabla en el metodo setEncabezado");
			}
			Object o = clase.newInstance();
			Item i = getItem(itemid);
			Field field = null;
			Method metodo = null;
			Object valor = null; // puede ser un componente o un valor
			// para cada campo de clase, se revisa si existe en la pantalla.
			for (int x = 0; x < columnasClase.size(); x++) {
					
					
					field = columnasClase.get(x);
					
					metodo = clase.getMethod("set"+field.getName().toUpperCase().charAt(0)+field.getName().substring(1), field.getType());
					metodo.setAccessible(true);
					
					// se obtiene el valor y se verifica.
					valor = i.getItemProperty(x).getValue();
					
					if (valor instanceof RichTextArea){
						
						metodo.invoke(o, ((RichTextArea)valor).getValue());
							
					} else if (valor instanceof DateField) {
						metodo.invoke(o, ((DateField) valor).getValue());
							
					} else if (valor instanceof TextArea) {
						metodo.invoke(o, ((TextArea)valor).getValue());	
					} else if (valor instanceof AbstractTextField){
						if (((AbstractTextField)valor).getValue() != null && !((AbstractTextField)valor).getValue().isEmpty()){									
							metodo.invoke(o, VaadinUtils.parseString(((AbstractTextField)valor).getValue(), field.getType()));									
						}
					}else if (valor instanceof CheckBox){							
						metodo.invoke(o, VaadinUtils.parseBoolean(((CheckBox)valor).getValue(),field.getType()));
					}else 
						if (valor instanceof ComboBox){
							// si es instancia de sibitem.
							if ( ((ComboBox)valor).getValue() instanceof ITSItem){
								valor = ((ITSItem<?>)((ComboBox)valor).getValue()).getId();
							}else{
								valor = ((ComboBox)valor).getValue();
							}
							if (valor != null)
								metodo.invoke(o, field.getType().cast(valor));
							
						}else if (valor instanceof ITSListaDeValores){
							if (((ITSListaDeValores)valor).getData() != null)
								if (field.getType() == ITSItem.class){
									metodo.invoke(o, new ITSItem (((ITSListaDeValores)valor).getData(),(String)((ITSListaDeValores)valor).getValue()));										
								}else{
									metodo.invoke(o, ((ITSListaDeValores)valor).getData());
								}
						}else{
							metodo.invoke(o,field.getType().cast(valor));
						}
				}			
		return o;
	}
	
	
	
	/**
     * Limpia información de la tabla que se carga al utilizarla.   
      * Información a limpiar: 1. Remueve todos los items de la tabla. 
      *  2. Limpia la lista de filasModificadas. 3. Limpia la lista de filasNuevas.
     * @author gdeleon
     * @throws Exception
     */
     public void limpiarTabla() throws Exception{
          
           filasModificadas.clear();
           filasNuevas.clear();
           removeAllItems();
     }

	
	public void llenarTabla(Object[][] data) throws Exception{
		int indice = 0;
		// define si es neceario limpiar los registros al agregar uno nuevo
			
		cargarMapColumnasDeshabilitadas();
		cargarMapColumnasEventoModificacion();
		if (banderaLimpiarTabla){
			filasModificadas.clear();
			filasNuevas.clear();
			removeAllItems();
			indice = 0;
		}else{
			if (this.size() > 0)
				indice = this.size();
			else
				indice = 0;
		}
		
		int contador = 0;
		
		Object componente = null;
		boolean primerRegistro = true;
		double[] total =  null;		
		if (data  !=null && data.length>0){
			total = new double[data[0].length];
		}
		
		if(visualizarFilasModificadasColoreadas)
		{
			getTabla().setImmediate(true);
		}
		Listener valueChange = null;
		Object[] registroNuevo = null;
		for (Object[] registro : data){
			//Se crea una nueva instancia del registro que se va agregar al componente table, para evitar que se copien las 
			//referencias hacia los objetos de la matriz original, con este cambio se van por valor. 
			registroNuevo = new Object[registro.length];
			contador = 0;
			
			//Se suman las filas que se seleccionaron para colocar totales
			if (getTotalesEnColumnas()!=null){
				for (Integer sumaCampo :getTotalesEnColumnas()){
					if (registro[sumaCampo]!=null){
						total[sumaCampo]+=Double.parseDouble(registro[sumaCampo].toString());
					}
				}
			}
			
			for (Object campo :registro){
				valueChange = columnasValueChange.get(contador); 
				
				if (tiposDeDato[contador] == TextField.class){
					if ( campo == null)
						componente = new TextField(null, "");
					else{
						if (campo instanceof Date){
							componente = new TextField(null, DateUtils.parseDateToString((Date)campo));
						}else
						{
							componente = new TextField(null, ""+campo);
						}
						
					}
										
					((TextField)componente).setSizeFull();
					((TextField)componente).setImmediate(true);
					/// Permite  agregar el value change listerner.
					if (valueChange != null){
						((TextField)componente).addListener(valueChange);						
					}
					//Evento donde se captura el evento de moficación del registro
					if(componente!=null)
					{
					  if (!banderaNuevoRegistroObjetoTabla)
						if(columnasEventoModificacion.get(contador)!=null && (Boolean)columnasEventoModificacion.get(contador)==true)
						{
							final int indiceModificacion = indice;
							((TextField)componente).setImmediate(true);
							((TextField)componente).addValueChangeListener(new ValueChangeListener() {
								
								@Override
								public void valueChange(
										com.vaadin.data.Property.ValueChangeEvent event) {
									
									filasModificadas.put(indiceModificacion, true);
									if (visualizarFilasModificadasColoreadas)
									{
										colorearFilasModificadas();
									}
								}
							});
						}
						
						if (columnasDeshabilitadas.get(contador) != null && (Boolean) columnasDeshabilitadas.get(contador) == false)
						{
							((TextField) componente).setEnabled(false);
						}
					}
					
					//registro[contador]=componente ;
					registroNuevo[contador]=componente ;
				}else
					if (tiposDeDato[contador] == ComboBox.class){
						Object[][] c = listasDeValores.get(contador);							
						componente = new ComboBox();												
						
						if (c != null){
							VaadinUtils.llenarComponente(c, 0, 1, (ComboBox)componente);
						}
						
						if ( campo != null){							
							//Item i = ((ComboBox)componente).getItem(new SIBItem<String>((String)campo, ""));
							((ComboBox)componente).setValue(new ITSItem<Object>(campo, ""));
						}
						((ComboBox)componente).setFilteringMode(FilteringMode.CONTAINS);					
						((ComboBox)componente).setSizeFull();						
						((ComboBox)componente).setImmediate(true);
						
						if (valueChange != null){
							((ComboBox)componente).addListener(valueChange);
							
						}
						
						//Evento donde se captura el evento de moficación del registro
						if(componente!=null)
						{
						  if (!banderaNuevoRegistroObjetoTabla)
							if(columnasEventoModificacion.get(contador)!=null && (Boolean)columnasEventoModificacion.get(contador)==true)
							{
								final int indiceModificacion = indice;
								((ComboBox)componente).setImmediate(true);
								((ComboBox)componente).addValueChangeListener(new ValueChangeListener() {
									
									@Override
								public void valueChange(
										com.vaadin.data.Property.ValueChangeEvent event) {
									
									filasModificadas.put(indiceModificacion, true);
									if (visualizarFilasModificadasColoreadas)
									{
										colorearFilasModificadas();
									}
								}
								});
							}
							
							if (columnasDeshabilitadas.get(contador) != null && (Boolean) columnasDeshabilitadas.get(contador) == false)
							{
								((ComboBox) componente).setEnabled(false);
							}
						}
						
						//registro[contador]=componente ;
						registroNuevo[contador]=componente ;
				}			
				else
					if (tiposDeDato[contador] == TextArea.class){
						if ( campo == null)
							componente = new TextArea(null, "");	
						else
							componente = new TextArea(null, (String)campo);					
						((TextArea)componente).setSizeFull();
						((TextArea)componente).setRows(numeroFilasTextArea);
						((TextArea)componente).setImmediate(true);
						
						if (valueChange != null){
							((TextArea)componente).addListener(valueChange);
							
						}
						
						//Evento donde se captura el evento de moficación del registro
						if(componente!=null)
						{
						  if (!banderaNuevoRegistroObjetoTabla)
							if(columnasEventoModificacion.get(contador)!=null && (Boolean)columnasEventoModificacion.get(contador)==true)
							{
								final int indiceModificacion = indice;
								((TextArea)componente).setImmediate(true);
								((TextArea)componente).addValueChangeListener(new ValueChangeListener() {
									
									@Override
								public void valueChange(
										com.vaadin.data.Property.ValueChangeEvent event) {
									
									filasModificadas.put(indiceModificacion, true);
									if (visualizarFilasModificadasColoreadas)
									{
										colorearFilasModificadas();
									}
								}
								});
							}
							
						if (columnasDeshabilitadas.get(contador) != null && (Boolean) columnasDeshabilitadas.get(contador) == false)
						{
							((TextArea) componente).setEnabled(false);
						}
						}
						
						//registro[contador]=componente ;
						registroNuevo[contador]=componente ;
				} else
					if (tiposDeDato[contador] == CheckBox.class){						
						if (campo==null){							
							componente = new CheckBox(null, new Boolean(false));
							registroNuevo[contador]=componente ;
						}else{							
							if (campo instanceof String ){
							
								componente = new CheckBox(null, new Boolean((((String)campo).equals("1") || ((String)campo).equals("S"))?true:false));						
								//registro[contador]=componente ;
								registroNuevo[contador]=componente ;
							}else{
								componente = new CheckBox(null, (Boolean)campo);
								((CheckBox)componente).setImmediate(true);
								//registro[contador]=componente ;
								registroNuevo[contador]=componente ;
							}
						}
						if (valueChange != null){
							((CheckBox)componente).addListener(valueChange);
							
						}
						
						//Evento donde se captura el evento de moficación del registro
					if (componente != null)
					{
					 if (!banderaNuevoRegistroObjetoTabla)
						if (columnasEventoModificacion.get(contador) != null && (Boolean) columnasEventoModificacion.get(contador) == true)
						{
							final int indiceModificacion = indice;
							((CheckBox) componente).setImmediate(true);
							((CheckBox) componente).addValueChangeListener(new ValueChangeListener() {

								@Override
								public void valueChange(
										com.vaadin.data.Property.ValueChangeEvent event) {
									
									filasModificadas.put(indiceModificacion, true);
									if (visualizarFilasModificadasColoreadas)
									{
										colorearFilasModificadas();
									}
								}
							});
						}
							
							if(columnasDeshabilitadas.get(contador)!=null && (Boolean)columnasDeshabilitadas.get(contador)==false)
							{((CheckBox)componente).setEnabled(false);}
						}
				}
					 else
							if (tiposDeDato[contador] == Date.class){												
								if (campo != null){
									if (campo instanceof String)
										//registro[contador]= sdf.parse((String)campo);
										registroNuevo[contador]= sdf.parse((String)campo);
									else
										//registro[contador]= campo;
										registroNuevo[contador]= campo;
								}
						}
				else
					if (tiposDeDato[contador] == Button.class){
						componente = new Button((String)campo);
						((Button)componente).setStyleName(BaseTheme.BUTTON_LINK);
						((Button)componente).setSizeFull();						
						//registro[contador]=componente ;
						registroNuevo[contador]=componente ;
					}				
				else
					//registro[contador]= campo;				
					registroNuevo[contador]= campo;
				contador++;		
				valueChange = null;
				
			}
			
			if (this.ingorarPrimerRegistro ){
				if (!primerRegistro)
					//addItem(registro, indice++);
					addItem(registroNuevo, indice++);
			}else{
				if (banderaNuevoRegistroObjetoTabla){
					addItem(registroNuevo, indice++);	
				}else{
					addItem(registroNuevo, indice++);
				}
				
			}
			
			primerRegistro = false;			
		}
		//Muestro la fila de totales
		if (getTotalesEnColumnas()!=null){
			setFooterVisible(true);
			setColumnFooter(0, "Total:");
			for (Integer totalCols :getTotalesEnColumnas()){
				//setColumnFooter(totalCols, formato.format(total[totalCols]));
				double valorTotalDouble=0;
				String valorTotal = "0";
				if (total != null){									
					valorTotalDouble = total[totalCols];				
				}
				valorTotal = formatoPersonalizado((Object)1, (Object)totalCols,valorTotalDouble);
				if (valorTotal == null){
					valorTotal = formato.format(valorTotalDouble);
				}
				
				setColumnFooter(totalCols, valorTotal);
			}
		}
	}
	
	
	/**
	 * Procedimiento encargado de colorear la fila modificada
	 */
	public void colorearFilasModificadas()
	{
		//esto se comento porque al momento de cambiar el color a la fila modificada
		//pierde el focus del campo en el que se encuentra.
		/*getTabla().setCellStyleGenerator(
			    new CellStyleGenerator( ) {
				@Override
				public String getStyle(Table source,
						Object itemId, Object propertyId) {
					// TODO Auto-generated method stub
					if(filasModificadas.get(itemId)!=null)
					{
						return estiloCeldaModificada;
					}
					return "";
				}
			    } );*/
	}
	
	
		
	
	public void addTooltipColumn(Object columna){
		this.columnasTooltip.put(columna, null);
	}
	
	public void addTooltipColumn(Object columna, String mensaje){
		this.columnasTooltip.put(columna, mensaje);
	}

	public Integer[] getTotalesEnColumnas() {
		return totalesEnColumnas;
	}

	/**
	 *@author gdeleon 
	 * @param totalesEnColumnas listado de columnas que se necesita agregar un total al final de la tabla
	 * Procedimiento que le setea a la tabla una fila de total al final de la tabla
	 * recibiendo como parametro el listado de las columnas a calcular y mostrar el total. 
	 * 
	 */
	public void setTotalesEnColumnas(Integer... totalesEnColumnas) {
		this.totalesEnColumnas = totalesEnColumnas;
	}

	/**
	 * @author gdeleon
	 * @return retorna un vector con el listado de converters de cada columna
	 */
	public Converter<?, ?>[] getFormatosColumnas() {
		return formatosColumnas;
	}

	/**
	 * @author gdeleon
	 * @param formatosColumnas listado de converters para aplicacar en cada columna de la tabla
	 * Procedimiento que setea a la tabla una lista de converters(Clase Implementada) para cada columna
	 * de la tabla, si se desea que el converter a aplicar sea el default colocar null en la posicion 
	 * de la columna sino colocar el converter necesario
	 */
	public void setFormatosColumnas(Converter<?, ?>... formatosColumnas) {
		this.formatosColumnas = formatosColumnas;
	}
	
	private Table getTabla() {
		// TODO Auto-generated method stub
		return this;
	}
}

