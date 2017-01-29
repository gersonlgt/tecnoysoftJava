package gt.com.itSoftware.framework.vaadin.components.data;

public class MultiComboboxOption {
	private String descripcion = null;
	private Object value = null;
	
	public MultiComboboxOption() {	
	}
	
	public MultiComboboxOption(String descripcion, Object value) {
		super();
		this.descripcion = descripcion;
		this.value = value;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
	
	@Override
	public String toString() {	
		return getDescripcion().toString();
	}
	
}
