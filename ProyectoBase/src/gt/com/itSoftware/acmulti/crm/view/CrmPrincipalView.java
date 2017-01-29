package gt.com.itSoftware.acmulti.crm.view;

import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;

import gt.com.itSoftware.acmulti.core.view.ACMTemplateView;
import gt.com.itSoftware.acmulti.crm.view.components.CrmPrincipal;

public class CrmPrincipalView extends ACMTemplateView{
	private CrmPrincipal componente=null;

	@Override
	public void enter(ViewChangeEvent event) {
		if (componente==null){
			componente = new CrmPrincipal();
			getContenedorInferior().addComponent(componente);
		}		
	}

}
