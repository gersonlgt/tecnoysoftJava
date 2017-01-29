package gt.com.itSoftware.framework.core.services;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import gt.com.itSoftware.framework.core.model.Usuario;
import gt.com.itSoftware.framework.vaadin.components.ITSDataBaseDataProvider;
import gt.gob.sib.comunicacion.BaseDatos;





public class BaseServiceLocator implements IServiceLocator {

	public enum ConsultaQBE { SIN_TIPOS, CON_TIPOS, STRING};
	public enum Ambiente {DESARROLLO, PREPRODUCCION, PRODUCCION}
	private SimpleDateFormat formatter = new SimpleDateFormat ("yyyyMMddHHmmssSSS");
	private String usuarioBitacora = null;
	private String instruccionBitacora = null;
	
	protected String datasourceName = null;
//	private ReporteService reporteService = new ReporteService();
	
	public String getUsuarioBitacora() {
		return usuarioBitacora;
	}
	
	public void setUsuarioBitacora(String usuarioBitacora) {
		this.usuarioBitacora = usuarioBitacora;
	}
	
	public void setInstruccionBitacora(String instruccionBitacora) {
		this.instruccionBitacora = instruccionBitacora;
	}
	
	public String getInstruccionBitacora() {
		return instruccionBitacora;
	}	
	
	public String getDatasourceName() {
		return datasourceName;
	}

	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}
	
	public Ambiente obtenerAmbienteActual(){
		try{
			Object[][] r = getConsultaGenerica("SELECT NOMBRE_SERVIDOR FROM DUAL");
			if (r.length == 0){
				return Ambiente.DESARROLLO;
			}
			String nombreServidor = ((String)r[0][0]);
			if ( nombreServidor.contains("desa.sib.gob.gt") ){
				return Ambiente.DESARROLLO;
			}else if (nombreServidor.contains("aplicaciones.sib.gob.gt")){
				return Ambiente.PRODUCCION;
			}
			/** 10/11/2015: la instrucción en pre-producción contiene la frase desa.sib.gob.gt */
		}catch(Exception e){
			return Ambiente.DESARROLLO;
		}
		return Ambiente.DESARROLLO;
	}
	
	
	public String enviaCorreo(String de , String para, String asunto, String mensaje, String aNombreDe ) throws Exception{
		  String datos=null;
	      BaseDatos baseDeDatos = null;
	      try{
	    	  baseDeDatos = conectarBaseDeDatosFramework();
	          datos = (String)getConsultaGenerica(baseDeDatos.getConn(),"select sp_envia_mails2.envia(?,?,?,?,?) from dual", de,para,asunto,mensaje,aNombreDe)[0][0];
	          return datos;
	      }finally{
	    	  if (baseDeDatos!=null)
	    		  baseDeDatos.getConn().close();  
	      }
	}
	
	
	public Usuario obtenerInformacionUsuario (String username) throws Exception{
		Usuario usr = new Usuario();
		BaseDatos baseDeDatos = null;
		
		/*try{
			baseDeDatos = conectarBaseDeDatosFramework();
			//String sentencia = "begin PKG_SADE.PRSADE_DATOS_USUARIO(?,?,?,?,?,?); end;";
			String sentencia = "begin EXP_SIDADM.PKG_APLICACIONES.PRSADE_DATOS_USUARIO(?,?,?,?,?,?); end;";
	        CallableStatement creacionBitacoraProc = baseDeDatos.getConn().prepareCall(sentencia);
	        creacionBitacoraProc.setString(1,username);	        
	        creacionBitacoraProc.registerOutParameter(2, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(3, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(4, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(5, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(6, Types.INTEGER);
	        creacionBitacoraProc.execute();
	        
	        usr.setPersPerona(creacionBitacoraProc.getInt(2));
	        usr.setPeriodoActual(creacionBitacoraProc.getInt(3));
	        usr.setAnioPeriodoActual(creacionBitacoraProc.getInt(4));
	        usr.setUnidadQuePertence(creacionBitacoraProc.getInt(5));
	        usr.setUnidadPadre(creacionBitacoraProc.getInt(6));
			usr.setUsuarioSistema(username);
			creacionBitacoraProc.close();
			
			PreparedStatement ps = baseDeDatos.getConn().prepareStatement("select fu_nombre_colaborador(?), EXP_SIDADM.PKG_APLICACIONES.FNSADE_OBTENER_INICIALES_USR(?), trato_abr(fu_persona_antiguedad(?)) from dual");
			ps.setInt(1, usr.getPersPerona());
			ps.setInt(2, usr.getPersPerona());
			ps.setInt(3, usr.getPersPerona());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				usr.setNombre(rs.getString(1));
				usr.setIniciales(rs.getString(2));
				usr.setTrato(rs.getString(3));
			}			
			
			rs.close();
			ps.close();
			
		}finally{
			if (baseDeDatos != null)							
				if (!baseDeDatos.getConn().isClosed()){					
					baseDeDatos.getConn().close();
				}
			
		}	
		// se genera la bitacora por carga de pdf.*/
        
		return usr;
	}
	
	public Usuario obtenerInformacionUsuario (Integer persPersona) throws Exception{
		Usuario usr = new Usuario();
		BaseDatos baseDeDatos = null;
		
		try{
			baseDeDatos = conectarBaseDeDatosFramework();
			//String sentencia = "begin PKG_SADE.PRSADE_DATOS_USUARIO(?,?,?,?,?,?); end;";
			String sentencia = "begin EXP_SIDADM.PKG_APLICACIONES.PRSADE_DATOS_USUARIO(?,?,?,?,?,?); end;";
	        CallableStatement creacionBitacoraProc = baseDeDatos.getConn().prepareCall(sentencia);
	        creacionBitacoraProc.registerOutParameter(1, Types.VARCHAR);
	        creacionBitacoraProc.setInt(2,persPersona);
	        creacionBitacoraProc.registerOutParameter(3, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(4, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(5, Types.INTEGER);
	        creacionBitacoraProc.registerOutParameter(6, Types.INTEGER);
	        creacionBitacoraProc.execute();
	        
	        usr.setPersPerona(persPersona);
	        usr.setPeriodoActual(creacionBitacoraProc.getInt(3));
	        usr.setAnioPeriodoActual(creacionBitacoraProc.getInt(4));
	        usr.setUnidadQuePertence(creacionBitacoraProc.getInt(5));
	        usr.setUnidadPadre(creacionBitacoraProc.getInt(6));
			usr.setUsuarioSistema(creacionBitacoraProc.getString(1));
			creacionBitacoraProc.close();
			
			PreparedStatement ps = baseDeDatos.getConn().prepareStatement("select fu_nombre_colaborador(?), EXP_SIDADM.PKG_APLICACIONES.FNSADE_OBTENER_INICIALES_USR(?) from dual");
			ps.setInt(1, usr.getPersPerona());
			ps.setInt(2, usr.getPersPerona());
			
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()){
				usr.setNombre(rs.getString(1));
				usr.setIniciales(rs.getString(2));
			}
			rs.close();
			ps.close();
			
		}finally{
			if (baseDeDatos != null)							
				if (!baseDeDatos.getConn().isClosed()){					
					baseDeDatos.getConn().close();
				}
			
		}	
		// se genera la bitacora por carga de pdf.
        
		return usr;
	}
	
	
	
	
	/*
	 * (non-Javadoc)
	 * @see gt.com.itSoftware.framework.core.services.IServiceLocator#getConsultaGenerica(java.lang.String, java.lang.Object[])
	 */
	public Object[][] getConsultaGenerica(String consulta, Object ... params) throws Exception
    {
	      Object[][] datos=null;
	      BaseDatos baseDeDatos = null;
	      try{
	    	  
	    	  baseDeDatos = conectarBaseDeDatos();
	          datos = getConsultaGenerica(baseDeDatos.getConn(), consulta, params);
	          return datos;
	          
	      }finally{
	    	  if (baseDeDatos!=null)
	    		  baseDeDatos.getConn().close();  
	      }
              
    }
	
	public Object[][] getConsultaGenerica(Connection con , String consulta, Object ... params) throws Exception
    {
          Object[][] datos=null;
          //BaseDatos baseDeDatos = conectarBaseDeDatos();
          List lista = new ArrayList();
          PreparedStatement ps = null;
          ResultSet rs = null;
          ResultSetMetaData rsmd = null;
          try{
        	  ps =  con.prepareStatement(consulta);
              int contador = 0;
              for (Object parametro : params){
            	 contador++;
            	 if (parametro instanceof Boolean){
            		 ps.setBoolean(contador,(Boolean)parametro);
            	 }else
            	 if (parametro instanceof BigDecimal){
            		 ps.setBigDecimal(contador,(BigDecimal)parametro);
            	 }else
            	 if (parametro instanceof Double){
            		 ps.setDouble(contador,(Double)parametro);
            	 }else 
            	  if (parametro instanceof Long){
            		 ps.setDouble(contador,(Long)parametro);
            	 }else
            	 if (parametro instanceof Integer){
            		 ps.setInt(contador,(Integer)parametro);
            	 }else
        		 if (parametro instanceof Date){    			 
        			//ps.setDate(contador, new java.sql.Date(((Date)parametro).getTime()));
        			ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
        		 }else{
        			 ps.setString(contador, (String)parametro);
        		 }
            	  
              }          
              rs = ps.executeQuery();
              rsmd = rs.getMetaData();          
              int numColumnas = rsmd.getColumnCount();
              
              /// Crea el listado de grupo financieros con sus instituciones.
              while (rs.next())
              {
                    Object[] registro = new Object[numColumnas];
                    
                    for(int i=1;i<=numColumnas;i++)
                    {
                          registro[i-1]=rs.getObject(i);
                    }
                    lista.add(registro);
              }
              
              int numFilas = lista.size();
              
              datos = new Object[numFilas][numColumnas];
              
              Iterator<?> iterador = lista.iterator();
              int vFila=0;
              while(iterador.hasNext())
              {
                    Object[] fila = (Object[])iterador.next();
                    for(int col=0; col<fila.length; col++)
                    {
                          datos[vFila][col]=fila[col];
                    }
                    vFila++;
              }
              
          }finally{
        	  if (rs!= null)
        		  rs.close();
        	  if (ps != null)
        		  ps.close();
          }
         
        return datos;      
    }
	
	public Object[][] getConsultaGenericaString(String consulta, Object ... params) throws Exception
    {
          Object[][] datos=null;
          BaseDatos baseDeDatos = conectarBaseDeDatos();
          List lista = new ArrayList();
          PreparedStatement ps = null;
          ResultSet rs = null;
          ResultSetMetaData rsmd = null;
          try{
	          ps =  baseDeDatos.getConn().prepareStatement(consulta);
	          int contador = 0;
	          for (Object parametro : params){
	        	 contador++;
	        	 if (parametro instanceof Boolean){
	        		 ps.setBoolean(contador,(Boolean)parametro);
	        	 }else
	        	 if (parametro instanceof BigDecimal){
	        		 ps.setBigDecimal(contador,(BigDecimal)parametro);
	        	 }else
	        	 if (parametro instanceof Double){
	        		 ps.setDouble(contador,(Double)parametro);
	        	 }else
	        	 if (parametro instanceof Integer){
	        		 ps.setInt(contador,(Integer)parametro);
	        	 }else
	    		 if (parametro instanceof Date){    			 
	    			//ps.setDate(contador, new java.sql.Date(((Date)parametro).getTime()));
	    			ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
	    		 }else{
	    			 ps.setString(contador, (String)parametro);
	    		 }
	        	  
	          }          
	          rs = ps.executeQuery();
	          rsmd = rs.getMetaData();
	          
	          int numColumnas = rsmd.getColumnCount();
	          
	          while (rs.next())
	          {
	                Object[] registro = new Object[numColumnas];
	                
	                for(int i=1;i<=numColumnas;i++)
	                {
	                      registro[i-1]=rs.getString(i);
	                }
	                lista.add(registro);
	          }
	          
	          int numFilas = lista.size();
	          
	          datos = new Object[numFilas][numColumnas];
	          
	          Iterator<?> iterador = lista.iterator();
	          int vFila=0;
	          while(iterador.hasNext())
	          {
	                Object[] fila = (Object[])iterador.next();
	                for(int col=0; col<fila.length; col++)
	                {
	                      datos[vFila][col]=fila[col];
	                }
	                vFila++;
	          }
	          
          }finally{
        	  if (rs!= null)
        		  rs.close();
        	  if (ps != null)
        		  ps.close();
        	  if (baseDeDatos != null && baseDeDatos.getConn() != null && !baseDeDatos.getConn().isClosed())
        		  baseDeDatos.getConn().close();
          }
        return datos;      
    }

	
	public int ejecutaDML (String consulta, Object ... params) throws Exception
    {
		  BaseDatos baseDeDatos  = null;
		  int resultado = -1;
          try{
        	  
		          baseDeDatos = conectarBaseDeDatos();          
		          if ( usuarioBitacora != null && instruccionBitacora != null ){
		        	  
		        	  CallableStatement  ps =  null;
		        	  ps =baseDeDatos.getConn().prepareCall(instruccionBitacora );
		        	  ps.setString(1,usuarioBitacora);
		        	  ps.execute();
		        	  ps.close();
		        			  
		          }
		          PreparedStatement ps =  baseDeDatos.getConn().prepareStatement(consulta);
		          int contador = 0;
		          for (Object parametro : params){
		        	 contador++;
		        	 if (parametro instanceof Boolean){
		        		 ps.setBoolean(contador,(Boolean)parametro);
		        	 }else
		        	 if (parametro instanceof BigDecimal){
		        		 ps.setBigDecimal(contador,(BigDecimal)parametro);
		        	 }else
		        	 if (parametro instanceof Double){
		        		 ps.setDouble(contador,(Double)parametro);
		        	 }else
		        	 if (parametro instanceof Long){
		        		 ps.setDouble(contador,(Long)parametro);
		        	 }
		        	 else
		        	 if (parametro instanceof Integer){
		        		 ps.setInt(contador,(Integer)parametro);
		        	 }else
		    		 if (parametro instanceof Date){    			 
		    			ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
		    		 }else
		    	      if (parametro instanceof InputStream){    			 
			    			ps.setBlob(contador, (InputStream)parametro);
			    		 }
		    		 else{
		    			 ps.setString(contador, (String)parametro);
		    		 }
		        	  
		          }          
		          resultado = ps.executeUpdate();    
		          baseDeDatos.getConn().commit();
		          
		          ps.close();
          }finally{
        	  if (baseDeDatos != null){
        		  if (!baseDeDatos.getConn().isClosed()){
	        		  baseDeDatos.getConn().rollback();
	        		  baseDeDatos.getConn().close();
        		  }
        	  }
          }
        return resultado;      
    }
	
	
	public Map<String,Object> ejecutaInsertConRetornoLlaves (String consulta,Map<String,Object> llaves, Object ... params) throws Exception
    {
		  BaseDatos baseDeDatos  = null;
		  Map<String,Object> resultado = llaves;
          try{
        	  
		          baseDeDatos = conectarBaseDeDatos();
		          
		          if ( usuarioBitacora != null && instruccionBitacora != null ){
		        	  
		        	  CallableStatement  ps =  null;
		        	  ps =baseDeDatos.getConn().prepareCall(instruccionBitacora );
		        	  ps.setString(1,usuarioBitacora);
		        	  ps.execute();
		        	  ps.close();
		        			  
		          }
		        		
		          
		          
		          CallableStatement  ps =  null;
		          if (llaves.keySet() != null){
		        	 // String[] arrayLlaves = new String[llaves.keySet().size()];
		        	  String stringllaves = "";
		        	  String stringParams = "";
		        	  for (String key : llaves.keySet()){
		        		  stringllaves+=","+key;
		        		  stringParams+=",?";
		        	  }
		        	  //System.out.println("begin "+consulta +" returning "+stringllaves.replaceFirst(",", "")+" into "+stringParams.replaceFirst(",", "")+"; end;");
				      ps= baseDeDatos.getConn().prepareCall("begin "+consulta +" returning "+stringllaves.replaceFirst(",", "")+" into "+stringParams.replaceFirst(",", "")+"; end;");
		        	  
		          }
		          else
		        	  ps =baseDeDatos.getConn().prepareCall(consulta );
		          int contador = 0;
		          for (Object parametro : params){
		        	 contador++;
		        	 if (parametro instanceof Boolean){
		        		 ps.setBoolean(contador,(Boolean)parametro);
		        	 }else
		        	 if (parametro instanceof BigDecimal){
		        		 ps.setBigDecimal(contador,(BigDecimal)parametro);
		        	 }else
		        	 if (parametro instanceof Double){
		        		 ps.setDouble(contador,(Double)parametro);
		        	 }else
		        	 if (parametro instanceof Long){
		        		 ps.setDouble(contador,(Long)parametro);
		        	 }
		        	 else
		        	 if (parametro instanceof Integer){
		        		 ps.setInt(contador,(Integer)parametro);
		        	 }else
		    		 if (parametro instanceof Date){    			 
		    			ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
		    		 }else
		    	      if (parametro instanceof InputStream){    			 
			    			ps.setBlob(contador, (InputStream)parametro);
			    		 }
		    		 else{
		    			 ps.setString(contador, (String)parametro);
		    		 }
		        	 
		        	  
		          }
		          
		          for (String key : llaves.keySet()){
		        		 contador++;
		        		 if (llaves.get(key) == Integer.class)
		        			 ps.registerOutParameter(contador, Types.NUMERIC);
		        		 else
		        		 if (llaves.get(key) == String.class)
		        			 ps.registerOutParameter(contador, Types.VARCHAR);
		        		 else
		        			 ps.registerOutParameter(contador, Types.OTHER);
		        	 }
		          
		          ps.executeUpdate();
		          ResultSet rset = ps.getResultSet(); 
		         		          
		          if (rset.next())
			          for (String key : llaves.keySet()){
			        		
			        		 if (llaves.get(key) == Integer.class)
			        			 llaves.put(key, rset.getInt(key));
			        		 else
			        		 if (llaves.get(key) == String.class)
			        			 llaves.put(key, rset.getString(key));
			        		 else
			        			 llaves.put(key, rset.getObject(key));
			         }
		          
		          baseDeDatos.getConn().commit();
		          rset.close();
		          ps.close();
		          
          }finally{
        	  if (baseDeDatos != null){
        		  if (!baseDeDatos.getConn().isClosed()){
	        		  baseDeDatos.getConn().rollback();
	        		  baseDeDatos.getConn().close();
        		  }
        	  }
          }
        return resultado;      
    }
	
	public Object getDescripcion(String tabla, String campoCodigo, String campoDescripcion, Object parametro) throws Exception
    {
         Object resultado = null;
         BaseDatos baseDeDatos = conectarBaseDeDatos();
        
         
         PreparedStatement ps =  baseDeDatos.getConn().prepareStatement("select ".concat(campoDescripcion).concat(" from ").concat(tabla).concat(" where ").concat(campoCodigo).concat(" = ? ")) ;
         int contador = 1;          
        	
    	 if (parametro instanceof Boolean){
    		 ps.setBoolean(contador,(Boolean)parametro);
    	 }else
    	 if (parametro instanceof BigDecimal){
    		 ps.setBigDecimal(contador,(BigDecimal)parametro);
    	 }else
    	 if (parametro instanceof Double){
    		 ps.setDouble(contador,(Double)parametro);
    	 }else
    	 if (parametro instanceof Integer){
    		 ps.setInt(contador,(Integer)parametro);
    	 }else
		 if (parametro instanceof Date){    			 
			//ps.setDate(contador, new java.sql.Date(((Date)parametro).getTime()));
			 ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
		 }else{
			 ps.setString(contador, (String)parametro);
		 }
        	  
         ResultSet rs = ps.executeQuery();
        
         if (rs.next())
          {               
               resultado = rs.getObject(contador);
          }
           
          rs.close();
          ps.close();
          baseDeDatos.getConn().close();
        
        return resultado;      
    }
	

	/**
	 * Permite obtener el resultado de una consulta a la base de datos,
	 * definiendo el tipo de dato que se desea obtener en la consulta realizada.
	 * 
	 * @param consulta
	 *            Query que se desea ejecutar
	 * @param tiposDeDatos
	 *            Los tipos de datos que se desea obtener para cada una de las
	 *            columnas del resultado obtenido.
	 * @param params
	 *            Los parametros que se desean incluir en la consulta.
	 * @return Retorna una Matriz de objetos que representan el resultado de la
	 *         consulta.
	 * @throws Exception
	 *             Error lanzado por la base de datos al realizar la consulta.
	 */
		 
	  @SuppressWarnings("rawtypes")
	  public Object[][] getConsultaGenericaConTipos(String consulta, Class[] tiposDeDatos, Object ... params) throws Exception
	    {
	          Object[][] datos=null;
	          BaseDatos baseDeDatos = conectarBaseDeDatos();
	          List<Object> lista = new ArrayList<Object>();
	          PreparedStatement ps = null;
	          ResultSet rs = null;
	          ResultSetMetaData rsmd = null;
	          try{
			          ps =  baseDeDatos.getConn().prepareStatement(consulta);
			          int contador = 0;
			          for (Object parametro : params){
			        	 contador++;
			        	 if (parametro instanceof Boolean){
			        		 ps.setBoolean(contador,(Boolean)parametro);
			        	 }else
			        	 if (parametro instanceof BigDecimal){
			        		 ps.setBigDecimal(contador,(BigDecimal)parametro);
			        	 }else
			        	 if (parametro instanceof Double){
			        		 ps.setDouble(contador,(Double)parametro);
			        	 }else 
			        	  if (parametro instanceof Long){
			        		 ps.setDouble(contador,(Long)parametro);
			        	 }else
			        	 if (parametro instanceof Integer){
			        		 ps.setInt(contador,(Integer)parametro);
			        	 }else
			    		 if (parametro instanceof Date){    			 
			    			//ps.setDate(contador, new java.sql.Date(((Date)parametro).getTime()));
			    			 ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
			    		 }else{
			    			 ps.setString(contador, (String)parametro);
			    		 }
			        	  
			          }          
			          rs = ps.executeQuery();
			          rsmd = rs.getMetaData();
			          
			          int numColumnas = rsmd.getColumnCount();
			          
			          /// Crea el listado de grupo financieros con sus instituciones.
			          while (rs.next())
			          {
			                Object[] registro = new Object[numColumnas];
			                
			                for(int i=1;i<=numColumnas;i++)
			                {
		
				   	        	 if (tiposDeDatos[i-1] == String.class){
				   	        		registro[i-1]=rs.getString(i);
				   	        	 }else
				   	        	 if (tiposDeDatos[i-1] == BigDecimal.class){
				   	        		registro[i-1]=rs.getBigDecimal(i);
				   	        	 }else
				   	        	 if (tiposDeDatos[i-1] ==  Double.class){
				   	        		registro[i-1]=rs.getDouble(i);
				   	        	 }else 
				   	        	  if (tiposDeDatos[i-1] ==  Long.class){
				   	        		registro[i-1]=rs.getLong(i);
				   	        	 }else
				   	        	 if (tiposDeDatos[i-1] ==  Integer.class){
				   	        		registro[i-1]=rs.getInt(i);
				   	        	 }else
				   	    		 if (tiposDeDatos[i-1] ==  Date.class){    			 
				   	    			registro[i-1]=rs.getDate(i);
				   	    		 }else{
				   	    			registro[i-1]=rs.getObject(i);
				   	    		 }
			                	
			                      
			                }
			                lista.add(registro);
			          }
			          
			          int numFilas = lista.size();
			          
			          datos = new Object[numFilas][numColumnas];
			          
			          Iterator iterador = lista.iterator();
			          int vFila=0;
			          while(iterador.hasNext())
			          {
			                Object[] fila = (Object[])iterador.next();
			                for(int col=0; col<fila.length; col++)
			                {
			                      datos[vFila][col]=fila[col];
			                }
			                vFila++;
			          }
			    }finally{
		      	  if (rs!= null)
		      		  rs.close();
		      	  if (ps != null)
		      		  ps.close();
		      	  if (baseDeDatos != null && baseDeDatos.getConn() != null && !baseDeDatos.getConn().isClosed())
		      		  baseDeDatos.getConn().close();
		        }
	        return datos;      
	    }
	 
	  
	  
	 public int ejecutaDML (Connection conection, String consulta, Object ... params) throws Exception
	    {			
		 	  System.out.println(consulta);
			  int resultado = -1;
			  if ( usuarioBitacora != null && instruccionBitacora != null ){
	        	  
	        	  CallableStatement  ps =  null;
	        	  ps =conection.prepareCall(instruccionBitacora );
	        	  ps.setString(1,usuarioBitacora);
	        	  ps.execute();
	        	  ps.close();
	        			  
	          }
	          PreparedStatement ps =  conection.prepareStatement(consulta);
	          int contador = 0;
	          for (Object parametro : params){
	        	 contador++;
	        	 if (parametro instanceof Boolean){
	        		 ps.setBoolean(contador,(Boolean)parametro);
	        	 }else
	        	 if (parametro instanceof BigDecimal){
	        		 ps.setBigDecimal(contador,(BigDecimal)parametro);
	        	 }else
	        	 if (parametro instanceof Double){
	        		 ps.setDouble(contador,(Double)parametro);
	        	 }else
	        	 if (parametro instanceof Long){
	        		 ps.setDouble(contador,(Long)parametro);
	        	 }
	        	 else
	        	 if (parametro instanceof Integer){
	        		 ps.setInt(contador,(Integer)parametro);
	        	 }else
	    		 if (parametro instanceof Date){    			 
	    			//ps.setDate(contador, new java.sql.Date(((Date)parametro).getTime()));
	    			 ps.setTimestamp(contador, new java.sql.Timestamp(((Date)parametro).getTime()));
	    		 }else
	    	     if (parametro instanceof InputStream){    			 
		    			ps.setBlob(contador, (InputStream)parametro);
		    	 }
	    		 else{
	    			 ps.setString(contador, (String)parametro);
	    		 }	        	  
	          }   
	          
	          resultado = ps.executeUpdate();
	          ps.close();
             return resultado;      
	    }	
	
	/**
	 * Permite conectarse a la base de datos, utilizando el datasource  
	 * 
	 * 	
	 * @return Conección activa de la base de datos. 
	 * @throws Excepción lanzada al intentar conectar.
	 * 
	 */
	 public BaseDatos conectarBaseDeDatos() throws Exception{
		BaseDatos baseDeDatos = new BaseDatos();
		baseDeDatos.conectarBDDWeb(this.datasourceName, false);		
		return baseDeDatos;
		
	 }
	 
	 public BaseDatos conectarBaseDeDatosFramework() throws Exception{
		BaseDatos baseDeDatos = new BaseDatos();
		//baseDeDatos.conectarBDDWeb("java:/comp/env/jdbc/genericoLib", false);	
		baseDeDatos.conectarBDDWeb("java:/comp/env/jdbc/acmulti", false);
		return baseDeDatos;
		
	 }
	 
	 /** Fin métodos necesarios para el componente de Reportes **/	
	 
	 public ITSDataBaseDataProvider createDatabaseProvider (String query, Class[] columnDataType){
			return new ITSDataBaseDataProvider(query, columnDataType);
	 }
	 
		public Object[][] getConsultaDataBaseProvider (ITSDataBaseDataProvider provider) throws Exception{
			
			BaseDatos baseDeDatos = null;
			Object[][] data = null;
			try{
				baseDeDatos = conectarBaseDeDatos();
				provider.setConection(baseDeDatos.getConn());
				provider.refresh();
				data = provider.getData();
			} catch (Exception e) {			
				e.printStackTrace();
				throw e;
			}finally{
				if (baseDeDatos != null){
					baseDeDatos.desconectarBDD();
				}
			}
			
			return data;
		}
		
		
		
		public Object[][] getConsultaDataBaseProviderConFiltro (ITSDataBaseDataProvider provider) throws Exception{
			
			BaseDatos baseDeDatos = null;
			Object[][] data = null;
			try{
				baseDeDatos = conectarBaseDeDatos();
				provider.setConection(baseDeDatos.getConn());
				provider.refreshConFiltro();
				data = provider.getData();
			} catch (Exception e) {			
				e.printStackTrace();
				throw e;
			}finally{
				if (baseDeDatos != null){
					baseDeDatos.desconectarBDD();
				}
			}
			
			return data;
		}
}
