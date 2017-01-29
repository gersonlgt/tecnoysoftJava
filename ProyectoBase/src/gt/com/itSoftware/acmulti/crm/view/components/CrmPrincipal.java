package gt.com.itSoftware.acmulti.crm.view.components;

import gt.com.itSoftware.acmulti.core.view.components.ACMCustomComponent;

public class CrmPrincipal extends ACMCustomComponent{
	private CrmPrincipalVC pantalla = null;
	
	
	public CrmPrincipal(){
		try {
			pantalla = new CrmPrincipalVC();						
			setCompositionRoot(pantalla);
			addFunctionality();
			addButtonEvents();
			consulta();
			
		} catch (Exception e) {
			e.printStackTrace();
			errorMessage(e.getMessage());
		}
	}


	private void addButtonEvents() {
		
		
	}


	private void consulta() {
		
		
	}


	private void addFunctionality() {
		
		
	}
}
