package gt.com.itSoftware.acmulti.core.model;

import gt.com.itSoftware.framework.core.services.BaseServiceLocator;

public class SamService extends BaseServiceLocator{

	public SamService(){
		this.datasourceName = "java:/comp/env/jdbc/acmulti";
	}
	
	
}
