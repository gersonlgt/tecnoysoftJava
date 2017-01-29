package gt.com.itSoftware.framework.vaadin.components;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public class ITSDataBaseDataProvider  implements ITSDataProvider{
	private String query= null;
	private String where = "";
	private String filtro = "";
	private Connection conection = null;
	private Object[][] data = null;
	private Class[] columnDataType = null;	
	private String [] columnNames = null;
	
	
	public ITSDataBaseDataProvider(Connection conection) {
		this.conection = conection;
	}
	
	
	
	public ITSDataBaseDataProvider(String query, Class[] columnDataType , Connection conection) {
		this.setQuery(query);
		this.conection = conection;
		setColumnDataType(columnDataType);		
	}
	
		
	public ITSDataBaseDataProvider(String query, Class[] columnDataType ) {
		this.setQuery(query);		
		setColumnDataType(columnDataType);		
	}
	
	public ITSDataBaseDataProvider(String query, Class[] columnDataType , String[] columnames) {
		this.setQuery(query);		
		setColumnDataType(columnDataType);
		setColumnNames(columnames);		
	}

	public void setConection(Connection conection) {
		this.conection = conection;
	}
	
	public String getFiltro() {
		return filtro;
	}
	
	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}
	
	
	/* Where se define de la forma 
	 *  campo1 = :p1 and capo2= :p2
	 * */
	public void setWhere(String where) {
		this.where = where;
	}
	
	public String getWhere() {
		return where;
	}
	
	/*
	 * (non-Javadoc)
	 * @see gob.sib.standard.componentes.SIBDataProvider#getData()
	 */
	@Override
	public Object[][] getData() {		
		return data;
	}
	

	public String[] getColumnNames() {
		return columnNames;
	}



	public void setColumnNames(String[] columnNames) {
		this.columnNames = columnNames;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see gob.sib.standard.componentes.SIBDataProvider#refresh()
	 */
	
	public void refresh() throws Exception {
		ArrayList<Object[]> registros = new ArrayList<Object[]>();
		Object []  registro;
			try {
							
				String consulta = getQuery().replaceAll(" 1=1", getWhere().isEmpty()?" 1=1": getWhere());
				System.out.println("Ejecutando SIN filtro "+consulta);
				ResultSet resultado = this.conection.prepareStatement(consulta).executeQuery();
				
				int row = 0;
				while (resultado.next()){
					registro= new Object[columnDataType.length];
					for (int x=0; x < columnDataType.length; x++){
						if ( columnDataType[x] == Integer.class ){
							registro[x]= resultado.getInt(x+1);
						}else if ( columnDataType[x] == Date.class ){
							registro[x]= resultado.getDate(x+1);
						}
						else
							registro[x]= resultado.getObject(x+1);					
					}					
					registros.add(registro);
				}
				data = new Object[registros.size()][columnDataType.length];
				for ( Object[] objeto :registros){
					data[row]= objeto;
					row++;
				}
			} catch (SQLException e) {
				
				throw new Exception(e.getMessage());
			}
		
		
	}
	
	public void refreshConFiltro() throws Exception {
		ArrayList<Object[]> registros = new ArrayList<Object[]>();
		Object []  registro;
			try {
							
				String consulta = getQuery().replaceAll(" 1=1", getWhere().isEmpty()?" 1=1": getWhere()+" and 1=1"  );
				consulta = consulta.replaceAll(" 1=1", getFiltro().isEmpty()?" 1=1": getFiltro()  );
				System.out.println("Ejecutando con filtro "+consulta);
				ResultSet resultado = this.conection.prepareStatement(consulta).executeQuery();
				
				int row = 0;
				while (resultado.next()){
					registro= new Object[columnDataType.length];
					for (int x=0; x < columnDataType.length; x++){
						if ( columnDataType[x] == Integer.class ){
							registro[x]= resultado.getInt(x+1);
						}else if ( columnDataType[x] == Date.class ){
							registro[x]= resultado.getDate(x+1);
						}
						else if ( columnDataType[x] == String.class ){
							registro[x]= resultado.getString(x+1);
						}
						else
							registro[x]= resultado.getObject(x+1);					
					}					
					registros.add(registro);
				}
				data = new Object[registros.size()][columnDataType.length];
				for ( Object[] objeto :registros){
					data[row]= objeto;
					row++;
				}
			} catch (SQLException e) {
				
				throw new Exception(e.getMessage());
			}
		
		
	}

	@Deprecated
	public void refresh2() throws Exception {
		//ArrayList<Object[]> registros = new ArrayList<Object[]>();
		Object []  registro;
			try {
				ResultSet resultado = this.conection.prepareStatement(getQuery()).executeQuery();
				//data = new Object[resultado.getFetchSize()][columnDataType.length];
				int row = 0;
				resultado.last();
				data = new Object[resultado.getRow()][columnDataType.length];
				resultado.first();				
				while (resultado.next()){
					registro= new Object[columnDataType.length];
					for (int x=0; x < columnDataType.length; x++){						
						data[row][x]= resultado.getObject(x+1);
						row++;
					}					
					//registros.add(registro);
				}
				//data  = registros.toArray();
			} catch (SQLException e) {
				
				throw new Exception(e.getMessage());
			}
		
		
	}
	

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}
	
	@SuppressWarnings("rawtypes")
	public Class[] getColumnDataType() {
		return columnDataType;
	}

	@SuppressWarnings("rawtypes")
	public void setColumnDataType(Class[] columnDataType) {
		this.columnDataType = columnDataType;
	}

}
