/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesCompartidas;

/**
 *
 * @author S.U.R
 */
public abstract class Servicio {
    private int _numSerie;
    private double _importeTotal;
    private SolicitudDeServicio _unaSolicitud;

    public int getNumSerie() {
        return _numSerie;
    }

    public void setNumSerie(int pNumSerie) {
        this._numSerie = pNumSerie;
    }

    public double getImporteTotal() {
        return _importeTotal;
    }

    public void setImporteTotal(double pImporteTotal) {
        this._importeTotal = pImporteTotal;
    }

    public SolicitudDeServicio getUnaSolicitud() {
        return _unaSolicitud;
    }

    public void setUnaSolicitud(SolicitudDeServicio pUnaSolicitud) {
        this._unaSolicitud = pUnaSolicitud;
    }
    
    public Servicio() {
        this(0, 0, new SolicitudDeServicio());
	}

    public Servicio(int pNumSerie, double pImporteTotal, SolicitudDeServicio pUnaSolicitud) {
        setNumSerie(pNumSerie);
        setImporteTotal(pImporteTotal);
        setUnaSolicitud(pUnaSolicitud);
    }

    @Override
    public String toString() {
        return String.format("Numero de Serie: " + getNumSerie() + "Numero de solicitud: " +getUnaSolicitud().getNumeroSerie()+ "Importe total: " + getImporteTotal());
    }
    
}
