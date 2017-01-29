package gt.com.itSoftware.framework.core.model;

public class Usuario {
	
	private String nombre = null;
	private Integer persPerona = null;
	private String usuarioSistema = null;	
	private String iniciales = null;
	private Integer unidadPadre = null;
	private Integer unidadQuePertence = null;
	private Integer periodoActual = null;
	private Integer anioPeriodoActual = null;
	private String trato = null;

	

	public Usuario() {	
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getPersPerona() {
		return persPerona;
	}

	public void setPersPerona(Integer persPerona) {
		this.persPerona = persPerona;
	}

	public String getUsuarioSistema() {
		return usuarioSistema;
	}

	public void setUsuarioSistema(String usuarioSistema) {
		this.usuarioSistema = usuarioSistema;
	}

	public Integer getUnidadPadre() {
		return unidadPadre;
	}

	public void setUnidadPadre(Integer unidadPadre) {
		this.unidadPadre = unidadPadre;
	}

	public Integer getUnidadQuePertence() {
		return unidadQuePertence;
	}

	public void setUnidadQuePertence(Integer unidadQuePertence) {
		this.unidadQuePertence = unidadQuePertence;
	}
	
	public Integer getPeriodoActual() {
		return periodoActual;
	}

	public void setPeriodoActual(Integer periodoActual) {
		this.periodoActual = periodoActual;
	}

	public Integer getAnioPeriodoActual() {
		return anioPeriodoActual;
	}

	public void setAnioPeriodoActual(Integer anioPeriodoActual) {
		this.anioPeriodoActual = anioPeriodoActual;
	}
	
	public String getIniciales() {
		return iniciales;
	}
	
	public void setIniciales(String iniciales) {
		this.iniciales = iniciales;
	}

	public String getTrato() {
		return trato;
	}

	public void setTrato(String trato) {
		this.trato = trato;
	}
	
	

}
