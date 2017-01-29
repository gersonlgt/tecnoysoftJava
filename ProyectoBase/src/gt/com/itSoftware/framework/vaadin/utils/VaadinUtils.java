package gt.com.itSoftware.framework.vaadin.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.AbstractSelect;
import com.vaadin.ui.AbstractTextField;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import gt.com.itSoftware.framework.core.utils.string.DateUtils;
import gt.com.itSoftware.framework.vaadin.components.ITSListaDeValores;
import gt.com.itSoftware.framework.vaadin.components.ITSMultiSelectComboBox;
import gt.com.itSoftware.framework.vaadin.components.data.ITSItem;


public class VaadinUtils {
	
	
	
	
	
	/***
	 * Permite formatear el contenido de un comoponente  de un formato de entrada a uno de salida.
	 * 
	 * @param componente	El Textfield que se desea formatear
	 * @param formatoEntrada	El formato de entrada para una fecha
	 * @param formatoSalida		El formato de salida para una fecha
	 * @throws Exception 
	 */

	
	public static void formatTexfieldToDateFormat(TextField componente ,String formatoEntrada, String formatoSalida) throws  Exception{
		if (componente != null && componente.getValue() != null && !componente.getValue().isEmpty()){
			if (componente.getValue().matches("\\d\\d/\\d\\d/\\d\\d\\d\\d")){
				return;
			}
			SimpleDateFormat entrada = new SimpleDateFormat(formatoEntrada);
			SimpleDateFormat salida = new SimpleDateFormat(formatoSalida);
			componente.setValue(salida.format(entrada.parse(componente.getValue().trim())));
		}
	}
	
	/***
	 * Permite formatear el contenido de un comoponente  de un formato ddMMyyyy de entreda a uno de salida dd/MM/yyyy.
	 * 
	 * @param componente	El Textfield que se desea formatear
	 * @throws Exception 
	 */
	public static void formatTexfieldToDateFormat(TextField componente ) throws  Exception{
		formatTexfieldToDateFormat(componente, "ddMMyyyy", "dd/MM/yyyy");		
	}

	/**
	 * Permite agregar al componente textfield 
	 * @param componente
	 */
	public static void addTextfieldDateFormat(final TextField componente ) throws RuntimeException{

		componente.setImmediate(true);		
		componente.setInputPrompt("DD/MM/YYYY");
		componente.setNullRepresentation("");
		componente.setMaxLength(10);
		componente.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -9187643453883775221L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					if (componente.getValue() == null){
						return;
					}
					if (componente.getValue().trim().length() == 0 || componente.getValue().trim().length() == 8 || componente.getValue().trim().length() == 10){
						if ( componente.getValue().trim().length() != 0)
							VaadinUtils.formatTexfieldToDateFormat(componente);
					}else
						throw new Exception("La longitud de la fecha ingresada no es correcta");
					
				} catch (Exception e) {
					componente.setValue("");
					componente.focus();
					Notification.show("Error de formato de fecha.", "\nEl formato de fecha es incorrecto.", Type.ERROR_MESSAGE);
					e.printStackTrace();
				//	throw new RuntimeException("Se ha ingrasado una fecha invalida");
				}				
			}
		});
	}
	
	/**
	 * Permite agregar al componente textfield 
	 * @param componente
	 */
	public static void addTextfieldDateFormatTiempo(final TextField componente ) throws RuntimeException{

		componente.setImmediate(true);		
		componente.setInputPrompt("DD/MM/YYYY hh:mi:ss");
		componente.setNullRepresentation("");
		componente.setMaxLength(19);
		componente.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -9187643453883775221L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					if (componente.getValue() == null){
						return;
					}
					//if (componente.getValue().trim().length() == 0 || componente.getValue().trim().length() == 8 || componente.getValue().trim().length() == 10){
						if ( componente.getValue().trim().length() != 0)
							VaadinUtils.formatTexfieldToDateFormat(componente, "dd/MM/yyyy HH:mm:ss", "dd/MM/yyyy HH:mm:ss");
					//}else
						//throw new Exception("La longitud de la fecha ingresada no es correcta");
					
				} catch (Exception e) {
					componente.setValue("");
					componente.focus();
					Notification.show("Error de formato de fecha.", "\nEl formato de fecha es incorrecto.", Type.ERROR_MESSAGE);
					e.printStackTrace();
				//	throw new RuntimeException("Se ha ingrasado una fecha invalida");
				}				
			}
		});
	}
	
	/**
	 * Permite agregar al componente textfield 
	 * @param componente
	 */
	public static void addTextfieldFormat(final TextField componente, final String formato ) throws RuntimeException{

		componente.setImmediate(true);		
		componente.setInputPrompt(formato);
		componente.setNullRepresentation("");
		componente.setMaxLength(19);
		componente.addValueChangeListener(new ValueChangeListener() {
			private static final long serialVersionUID = -9187643453883775221L;

			@Override
			public void valueChange(ValueChangeEvent event) {
				try {
					if (componente.getValue() == null){
						return;
					}
					//if (componente.getValue().trim().length() == 0 || componente.getValue().trim().length() == 8 || componente.getValue().trim().length() == 10){
						if ( componente.getValue().trim().length() != 0)
							VaadinUtils.formatTexfieldToDateFormat(componente, formato, formato);
					//}else
						//throw new Exception("La longitud de la fecha ingresada no es correcta");
					
				} catch (Exception e) {
					componente.setValue("");
					componente.focus();
					Notification.show("Error de formato.", "\nEl formato es incorrecto.(" + formato+ ")", Type.ERROR_MESSAGE);
					e.printStackTrace();
				//	throw new RuntimeException("Se ha ingrasado una fecha invalida");
				}				
			}
		});
	}
	
	

	

	public static Map<String,Class<?>> getPropiedadesDeClase(Class<?> clase){
		Map<String,Class<?>> listado = new HashMap<String,Class<?>>();
		for (Field f:clase.getDeclaredFields()){			
			if ( 
				 //f.getModifiers()!= Modifier.STATIC || //si no es una constante statica
				 f.getModifiers()!= 9  // si no es una constante publica
				 //!f.isEnumConstant() // Si no es enumeracion
				)				
			listado.put(f.getName(),f.getType());
		}
		return listado;
	}
	
	
	public static List<Field> getPropiedadesDeClaseList(Class<?> clase){
		List<Field>listado = new LinkedList<Field>();
		for (Field f:clase.getDeclaredFields()){			
			if ( 
				 //f.getModifiers()!= Modifier.STATIC || //si no es una constante statica
				 f.getModifiers()!= 9  // si no es una constante publica
				 //!f.isEnumConstant() // Si no es enumeracion
				)				
			listado.add(f);
		}
		return listado;
	}
	
	
	public static Object parseString (String valor , Class<?> clase){
		if (clase == Integer.class){
			return Integer.parseInt(valor);
		}else if (clase == Double.class){
			
			if(valor.contains(","))
			{
				valor = valor.replace(",", "");
			}
			
			return Double.parseDouble(valor);
		}else if (clase == BigDecimal.class){
			
			if(valor.contains(","))
			{
				valor = valor.replace(",", "");
			}
			
			return new BigDecimal(valor);
		}else if (clase == Date.class){
			try {
				try{
					return DateUtils.parseStringToDateTiempo(valor);
				}catch(Exception e){
					return DateUtils.parseStringToDate(valor);
				}
				//return DateUtils.parseStringToDate(valor);				
			} catch (Exception e) {				
				e.printStackTrace();
				throw new RuntimeException(e.getMessage());
			}
		}
		
		return valor;
		
	}
	
	public static Object parseBoolean (Boolean valor , Class<?> clase){
		if (clase == BigDecimal.class){
			return valor == true?new BigDecimal(1):new BigDecimal(0);
		}
		if (clase == Integer.class){
			return valor == true?1:0;
		}else if (clase == String.class){
			return valor == true ?"S":"N" ;
		}
		return valor;
		
	}
	
	
		
	/**
	 * Permite llenar ComboBox y NativeSelec a partir de un arreglo de datos.
	 * @param data			Es un arreglo de Object que contiene la información con la cual se llenaran los componentes.
	 * @param indiceId		Es el indice de la columna del arreglo de datos que se usará para el id del SIBItem.
	 * @param descripcion	Es el indice de la columna del arreglo de datos que se usará para la descripción del SIBItem.
	 * @param componente	Es un componente de tipo ComboBox y NativeSelect.
	 * @throws Exception
	 */
	public static void llenarComponente(Object[][] data, int indiceId, int descripcion, Component componente) throws Exception
    {
		if(componente!=null)
		{
		if(componente.getClass().equals(ITSMultiSelectComboBox.class))
	          {
			ITSMultiSelectComboBox combo = (ITSMultiSelectComboBox)componente;
		          int contador = 0;
		          for (Object[] registro : data)
		          {
		                contador=0;
		                ITSItem<Object> item = new ITSItem<Object>();
		                for (Object campo :registro)
		                {
							if (contador == indiceId) {
								item.setId(campo);
							} 
							if (contador == descripcion) {
								item.setDescripcion(String.valueOf(campo));
							}
		                    contador++;
		                }
		                combo.addItem(item);
		          }
	          }else 
          if(componente.getClass().equals(ComboBox.class))
          {
        	  ComboBox combo = (ComboBox)componente;
	          int contador = 0;
	          for (Object[] registro : data)
	          {
	                contador=0;
	                ITSItem<Object> item = new ITSItem<Object>();
	                for (Object campo :registro)
	                {
						if (contador == indiceId) {
							item.setId(campo);
						} 
						if (contador == descripcion) {
							item.setDescripcion(String.valueOf(campo));
						}
	                    contador++;
	                }
	                combo.addItem(item);
	          }
          }
          else if(componente.getClass().equals(NativeSelect.class))
          {
        	  NativeSelect combo = (NativeSelect)componente;
	          int contador = 0;
	          for (Object[] registro : data)
	          {
	                contador=0;
	                ITSItem<Object> item = new ITSItem<Object>();
	                for (Object campo :registro)
	                {
						if (contador == indiceId) {
							item.setId(campo);
						} 
						if (contador == descripcion) {
							item.setDescripcion(String.valueOf(campo));
						}
	                    contador++;
	                }
	                combo.addItem(item);
	          }
          }
		}
    }
	
	/**
	 * Permite validar un conjunto de componentes como requeridos.
	 * @param campos		Campos que se desea que se valide que son requeridos.
	 * @throws Exception	Error que se lanzará si ocurre que un campo no se le han ingresado valores.
	 */
	@SuppressWarnings("rawtypes")
	public static void  validarCamposRequeridos (List<Component> campos ) throws Exception{
		for (Component campo : campos){
			if (campo instanceof ComboBox){
					if ( ((ComboBox) campo).getValue() == null ){
						((ComboBox) campo).focus();
						throw new Exception ("El campo "+campo.getCaption()+" es requerido.");						
					}
				}
			else
			if (campo instanceof AbstractTextField){
				if ( ((AbstractTextField) campo).getValue() == null || ((AbstractTextField) campo).getValue().trim().isEmpty()){
					((AbstractTextField) campo).focus();
					throw new Exception ("El campo "+campo.getCaption()+" es requerido.");
					
				}
			}else
				if (campo instanceof ITSListaDeValores){
					if ( ((ITSListaDeValores) campo).getData() == null ){
						((ITSListaDeValores) campo).showLov();
						throw new Exception ("El campo "+campo.getCaption()+" es requerido.");						
					}
			}
			else
				if (campo instanceof AbstractField){
					if ( ((AbstractField) campo).getValue() == null || (((AbstractField) campo).getValue() != null && ((AbstractField) campo).getValue().toString().isEmpty())){
						((AbstractField) campo).focus();
						throw new Exception ("El campo "+campo.getCaption()+" es requerido.");
					}
			}
			
		}
		
	}
	
	
	/**
	 * Permite activar o desactivar un conjunt conjunto de componentes que han sido asignados en una lista.
	 * 
	 * @param componentes	Listado de componentes quee se desea activar/desactivar.
	 * @param enabled		true = activa componentes, false = desactiva componentes.
	 */
	public static void activarComponentes(List<Component> componentes, boolean enabled){
		
		for (Component componente:componentes){
			componente.setEnabled(enabled);			
		}
	}
	
	
	/**
	 * Permite limpiar un conjunto de campos, que han sido asignados previamente en la pantalla.
	 * 
	 * @param campos	Listado de componentes que se desea limpiar.
	 * 
	 */
	public static void limpiarCampos (List<Component> campos ) {
		
		for (Component campo : campos){
			
			if (campo instanceof ComboBox){				
				((ComboBox) campo).setValue(null);
				((ComboBox) campo).select(null);
		
			}else
			if (campo instanceof CheckBox){				
					((CheckBox) campo).setValue(false);
			
			}else
				if (campo instanceof PopupDateField){					
					((PopupDateField) campo).setValue(null);
					
			}else
			if (campo instanceof AbstractField){					
				((AbstractField) campo).setValue("");
				
			}else
				if (campo instanceof ITSListaDeValores){					
					((ITSListaDeValores) campo).clearComponent();											
				}			
			else
				if (campo instanceof AbstractSelect){					
						((AbstractSelect) campo).setValue(null);
				
				}
		}
	}
	
	
	
}
