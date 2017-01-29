package gt.com.itSoftware.framework.vaadin.formatter;

import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.ui.TextField;

public class TextfieldNumberFormatter implements TextChangeListener{
	String textoAnterior = "";
	@Override
	public void textChange(TextChangeEvent event) {
		if (event.getText().matches("\\d*")){
			textoAnterior = event.getText();
			((TextField)event.getComponent()).setValue(textoAnterior);
		}else{
			((TextField)event.getComponent()).setValue(textoAnterior);			
		}
		
	}

}
