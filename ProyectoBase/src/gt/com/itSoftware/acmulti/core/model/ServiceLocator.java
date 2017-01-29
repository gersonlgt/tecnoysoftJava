package gt.com.itSoftware.acmulti.core.model;

import gt.com.itSoftware.framework.core.services.BaseServiceLocator;

public class ServiceLocator extends BaseServiceLocator{
	
	private SamService samService = null;
	
	public ServiceLocator(){
		this.datasourceName = "java:/comp/env/jdbc/acmulti";
		samService = new SamService();
	}
	
	
}
