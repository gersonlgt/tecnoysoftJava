package gt.com.itSoftware.framework.vaadin.components.data;

import java.io.Serializable;

public class ITSItem<T> implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private T	id;
	private String	descripcion;
	
	public ITSItem()
	{
		
	}
	/**
	 * Permite crear un item para un SIBCombobox
	 * @param pId		Representa el conjunto de valores que identifican de forma unica la descripcion, puede ser un objeto, un numero, una alfanumerico, etc. 
	 * @param pDescripcion	 Representa la descripción que sera mostrada en el combobox
	 */
	public ITSItem(T pId, String pDescripcion)
	{
		id = pId;
		descripcion = pDescripcion;
	}
	
	public T getId() {
		return id;
	}
	
	public void setId(T id) {
		this.id = id;
	}
	
	
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	@Override
	public String toString()
	{	
		return descripcion;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof String){
			return obj.equals(id);
		}else if (obj instanceof Integer){
			return obj.equals(id);
		}else
		if (obj instanceof ITSItem){
			boolean igual = id.equals(((ITSItem<T>)obj).getId());
			if (igual){
				descripcion = ((ITSItem<T>)obj).getDescripcion();
			}		
			return igual; 
		}else{
			return super.equals(obj);
			
		}
	}
	
	@Override
	public int hashCode() {
		return id.hashCode();
	}
}
