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
public class Remolque extends Servicio{
    private int _cantidadKm;

    public int getCantidadKm() {
        return _cantidadKm;
    }

    public void setCantidadKm(int pCantidadKm) {
        this._cantidadKm = pCantidadKm;
    }
    
    public Remolque() {
		this(0, 0, new SolicitudDeServicio(), 0);
	}
    
    public Remolque(int pNumSerie, double pImporteTotal, SolicitudDeServicio pUnaSolicitud, int pCantidadKm) {
        super(pNumSerie, pImporteTotal, pUnaSolicitud);
        setCantidadKm(pCantidadKm);
    }
    
    @Override
    public String toString() {
        return super.toString()+("Cantidad de Km: " + getCantidadKm());
    }
    
}
