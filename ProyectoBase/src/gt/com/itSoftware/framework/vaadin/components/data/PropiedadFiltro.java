package gt.com.itSoftware.framework.vaadin.components.data;

public class PropiedadFiltro {

	private Integer id = null;
	private String width = null;
	private String caption = null;
	private String tooltip = null;
	
	
	
	public PropiedadFiltro(Integer id, String width, String caption,
			String tooltip) {
		super();
		this.id = id;
		this.width = width;
		this.caption = caption;
		this.tooltip = tooltip;
	}

	public PropiedadFiltro() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String size) {
		this.width = size;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getTooltip() {
		return tooltip;
	}

	public void setTooltip(String tooltip) {
		this.tooltip = tooltip;
	}
	
	
}
