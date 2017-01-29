package gt.com.itSoftware.framework.core.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {

		  protected Connection conn = null;
		  
		 
		  public void conectar(String datasource,boolean autocommit) throws Exception {
		      javax.sql.DataSource dataSource;
		      javax.naming.InitialContext initCtx = new javax.naming.InitialContext();
		      dataSource = (javax.sql.DataSource) initCtx.lookup(datasource);
		      
		        conn = dataSource.getConnection();
		        conn.setAutoCommit(autocommit);
		    }
		  
		  public void desconectarBDD() throws Exception {
		      conn.close();
		  }
		  
		  
		  
		  public ResultSet consulta(String sql) throws SQLException {
		      PreparedStatement sentencia = conn.prepareStatement(sql);
		      ResultSet res = sentencia.executeQuery();   
		      return res;
		  }
		
		  
		  public void setConn(Connection conn) {
		        this.conn = conn;
		    }

		    public Connection getConn() {
		        return conn;
		    }
		}

