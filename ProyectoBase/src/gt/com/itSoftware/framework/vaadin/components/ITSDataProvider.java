package gt.com.itSoftware.framework.vaadin.components;
/**
 * 
 * @author jmoralesa
 *
 */
public interface ITSDataProvider {
	/**
	 * Permite obtener los datos para la implementación de un 
	 * proveedor de datos.	
	 * @return Object[][] que contiene las columnas y las filas
	 *  con los datos enviados por el proveedor.
	 */
	Object[][] getData();
	
	/**
	 * Permite ejecutar el proceso de refrescado de datos.
	 * 
	 * Ej: Permite recargar los datos de una tabla, un servicio web, etc.
	 * 	
	 */
	void refresh()throws Exception;
	
	/**
	 * Permite identificar el tipo de datos de la columnas.
	 * 
	 * @param dataTypes
	 */
	void setColumnDataType(Class<?>[] dataTypes);
	
}
