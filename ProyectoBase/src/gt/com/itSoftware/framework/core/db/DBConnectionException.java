package gt.com.itSoftware.framework.core.db;

public class DBConnectionException  extends RuntimeException{

	/**
	 * Excepcion para identificar que puede existir una excepcion
	 * al inicializar algun tipo de coneccion a base de datos.
	 */
	private static final long serialVersionUID = 1L;
	
	public DBConnectionException(String mensaje) {
		super(mensaje);
	}
	

}