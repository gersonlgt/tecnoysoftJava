package gt.com.itSoftware.framework.vaadin.formatter;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

public class RegularExpressionFormatter implements TextChangeListener{
	
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String textoAnterior = "";
	private String formato = null;
	
	public RegularExpressionFormatter(String formato) {
		this.formato = formato;
	}
	
	@Override
	public void textChange(TextChangeEvent event) {
		if (event.getText().matches(formato)){
			textoAnterior = event.getText();
			((TextField)event.getComponent()).setValue(textoAnterior);
		}else{
			((TextField)event.getComponent()).setValue(textoAnterior);			
		}
		
	}

}
