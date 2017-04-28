/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesCompartidas;

import java.util.Date;

/**
 *
 * @author S.U.R
 */
public class Recibo {
    private long _numeroSerie;
    private Date _fecha;
    private double _importe;
    private Cliente _cliente;

    public long getNumeroSerie() {
        return _numeroSerie;
    }

    public void setNumeroSerie(long _numeroSerie) {
        this._numeroSerie = _numeroSerie;
    }

    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date _fecha) {
        this._fecha = _fecha;
    }

    public double getImporte() {
        return _importe;
    }

    public void setImporte(double _importe) {
        this._importe = _importe;
    }

    public Cliente getCliente() {
        return _cliente;
    }

    public void setCliente(Cliente _cliente) {
        this._cliente = _cliente;
    }
    
    public Recibo(long numeroSerie, Date fecha, double importe, Cliente cliente){
        setNumeroSerie(numeroSerie);
        setFecha(fecha);
        setImporte(importe);
        setCliente(cliente);
    }

    @Override
    public String toString() {
        return String.format("Nro. de Serie: {0}, Fecha: {1}, Importe: {2}, Cliente: {3}", getNumeroSerie(), getFecha(), getImporte(), getCliente().getCedula());
    }
}
