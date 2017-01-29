package gt.com.itSoftware.framework.core.utils.string;

public class StringUtils {

	public static String depurarMensajeError (String mensaje){
		if (mensaje != null){
			String mensajeError[] = mensaje.trim().split("ORA-\\d+:");
			for (String m :mensajeError){
				if (!m.isEmpty())
					return m;
			}
		}
		return "";
	}
	
	/**
	 * Permite validar un formato alfanumerico, que cumpla con el formato de paginas siguiendo el siguiente ejemplo:
	 * 	 Ej:  1-5,6,8-10 
	 * 		
	 * @param 	paginas alfanumerio que contemple el formato 1-5,6,8-10,11,21
	 * @return	true si cumple con el formato, y false si no cumple con el formato.
	 * 
	 */
	public static boolean validarFormatoPaginas(String paginas){
		return paginas.matches("((\\d+\\-\\d+)|(\\d+))+(,((\\d+\\-\\d+)|(\\d+)))*");
	}
	
	
	
}
