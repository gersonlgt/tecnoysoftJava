package gt.com.itSoftware.framework.core.services;

import gt.com.itSoftware.framework.vaadin.components.ITSDataBaseDataProvider;

public interface IServiceLocator {

	/**
	 * Permite realizr una consulta generica a la base de datos, enviando unicamente la consulta y el listado deparametros separados por coma. Retornando el resultado en un arreglo de objetos.
	 * @param consulta		Alfanumerico que contiene la consulta que se va a realizar, incluyendo los parametros utilizando el signo ? representando la posicion de cada uno de ellos.
	 * @param params		Objetos separados por comas, que representarán el valor de cada parametro, en la posicion de cada simbolo ? en la consulta.
	 * @return				Listado de objetos en una matriz, cada uno será del tipo que java representa en la base de datos. Ej: las columnas numericas son de tipo BigDecimal.
	 * @throws Exception	Error lanzado por la base de datos al intentar realizar la consulta.
	 */
	public Object[][] getConsultaGenerica(String consulta, Object ... params) throws Exception;
	public ITSDataBaseDataProvider createDatabaseProvider (String query, Class[] columnDataType);
	//public SIBDataBaseDataProvider obtenerListadoInstituciones ();
	public Object[][] getConsultaDataBaseProvider (ITSDataBaseDataProvider provider) throws Exception;
	public Object[][] getConsultaDataBaseProviderConFiltro (ITSDataBaseDataProvider provider) throws Exception;
	
}
