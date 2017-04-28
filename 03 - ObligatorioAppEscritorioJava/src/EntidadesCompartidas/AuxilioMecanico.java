package EntidadesCompartidas;

public class AuxilioMecanico extends Servicio {
    
    private String _descripcionProblema; 
    private String _descripcionReparacion; 
    private double _costoRepuesto;

    public String getDescripcionProblema() {
        return _descripcionProblema;
    }

    public void setDescripcionProblema(String pDescripcionProblema) {
        this._descripcionProblema = pDescripcionProblema;
    }

    public String getDescripcionReparacion() {
        return _descripcionReparacion;
    }

    public void setDescripcionReparacion(String pDescripcionReparacion) {
        this._descripcionReparacion = pDescripcionReparacion;
    }

    public double getCostoRepuesto() {
        return _costoRepuesto;
    }

    public void setCostoRepuesto(double pCostoRepuesto) {
        this._costoRepuesto = pCostoRepuesto;
    }
    
    public AuxilioMecanico() {
		this(0, 0, new SolicitudDeServicio(), "N/D", "N/D", 0);
	}
    
    public AuxilioMecanico(int pNumSerie, double pImporteTotal, SolicitudDeServicio pUnaSolicitud, String pDescripcionProblema, String pDescripcionReparacion, double pCostoRepuesto) {
        super(pNumSerie, pImporteTotal, pUnaSolicitud);
        setDescripcionProblema(pDescripcionProblema);
        setDescripcionReparacion(pDescripcionReparacion);
        setCostoRepuesto(pCostoRepuesto);
    }
    
    @Override
    public String toString() {
        return super.toString() + ( " Descripcion del problema: " + getDescripcionProblema() + " Descripcion del repuesto: " + getDescripcionReparacion() + " Costo de la reparacion: " + getCostoRepuesto());
    }
    
}
