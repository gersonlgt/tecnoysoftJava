package gt.com.itSoftware.framework.core.utils.string;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtils {
	
	private static SimpleDateFormat formatoDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	private static SimpleDateFormat formatoDDMMYYYYHHmmss = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	//private static SimpleDateFormat formatoDDMMYYYY = new SimpleDateFormat("dd/MM/yyyy");
	
	/**
	 * Permite formatear un objeto tipo date a un formato DD/MM/YYYY
	 * 
	 * @param fecha Fecha a que se desa formatear
	 * @return	Fecha con el formato aplicadoa.
	 * @throws Exception
	 */
	public static Date parseStringToDate(String fecha) throws Exception{
		return formatoDDMMYYYY.parse(fecha);
	}
	
	public static String parseDateToString(Date fecha) throws Exception{
		return formatoDDMMYYYY.format(fecha);
	}
	public static Date parseStringToDateTiempo(String fecha) throws Exception{
		return formatoDDMMYYYYHHmmss.parse(fecha);
	}
	
	public static String parseDateToStringTiempo(Date fecha) throws Exception{
		return formatoDDMMYYYYHHmmss.format(fecha);
	}
	
	/**
	 * Permite agregar dias a una fecha.
	 * Si el valor en dias es negativo, estos se restan
	 * 
	 * @param fecha
	 * @param dias
	 * @return
	 * @throws ParseException 
	 */
	public static Date agregarDias(String fecha, Integer dias) throws ParseException{
		return agregarDias(formatoDDMMYYYY.parse(fecha), dias);
	}
	
	public static Date agregarDias(Date fecha, Integer dias){
		Calendar calendario = GregorianCalendar.getInstance();
		calendario.setTime(fecha);
		calendario.add(Calendar.DATE, dias);
		return calendario.getTime();
	}
	
	public static Integer getAnioActual() throws Exception{
		return Integer.parseInt(formatoDDMMYYYY.format(new Date()).substring(6));
	}
	
	public static Integer getMesActual() throws Exception{
		return Integer.parseInt(formatoDDMMYYYY.format(new Date()).substring(3,5));
	}
	
	public static Integer getDiaActual() throws Exception{
		return Integer.parseInt(formatoDDMMYYYY.format(new Date()).substring(0,2));
	}

	
	public static void main(String[] args) {
		System.out.println(formatoDDMMYYYY.format(new Date()).substring(0,2));
		System.out.println(formatoDDMMYYYY.format(new Date()).substring(3,5));
		System.out.println(formatoDDMMYYYY.format(new Date()).substring(6));
	}
}
