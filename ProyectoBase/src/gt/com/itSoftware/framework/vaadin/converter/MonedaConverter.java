package gt.com.itSoftware.framework.vaadin.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.data.util.converter.Converter;

public class MonedaConverter implements Converter<String, String>{

	@Override
	public String convertToModel(String value, Class targetType, Locale locale)
			throws ConversionException {		
		if (value == null || value.isEmpty()){
			return "";
		}
		NumberFormat formato = new DecimalFormat("###,###,###,##0.00");
		return "Q. " + formato.format(Double.valueOf(value).doubleValue());
	}

	@Override
	public String convertToPresentation(String value, Class targetType,
			Locale locale) throws ConversionException{		
		//NumberFormat formato = new DecimalFormat("########00.##");		
		try {
			return "";//formato.parse(value).doubleValue();
		} catch(Exception e){ //catch (ParseException e) {			
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Class<String> getModelType() {
		// TODO Auto-generated method stub
		return String.class;
	}

	@Override
	public Class<String> getPresentationType() {
		// TODO Auto-generated method stub
		return String.class;
	}

}
