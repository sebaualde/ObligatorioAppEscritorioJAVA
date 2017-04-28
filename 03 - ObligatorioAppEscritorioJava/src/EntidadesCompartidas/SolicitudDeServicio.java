/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package EntidadesCompartidas;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author S.U.R
 */
public class SolicitudDeServicio {
    private int _numeroSerie;
    private Date _fecha;
    private String _direccion;
    private boolean _cancelacion;
    private Vehiculo _vehiculo;
    private Grua _grua;
    private ArrayList<Operario> _operarios;
    
    
    public int getNumeroSerie() {
        return _numeroSerie;
    }

    public void setNumeroSerie(int pNumeroSerie) {
        this._numeroSerie = pNumeroSerie;
    }

    public Date getFecha() {
        return _fecha;
    }

    public void setFecha(Date pFecha) {
        this._fecha = pFecha;
    }

    public String getDireccion() {
        return _direccion;
    }

    public void setDireccion(String pDireccion) {
        this._direccion = pDireccion;
    }

    public boolean isCancelacion() {
        return _cancelacion;
    }

    public void setCancelacion(boolean pCancelacion) {
        this._cancelacion = pCancelacion;
    }

    public Vehiculo getVehiculo() {
        return _vehiculo;
    }

    public void setVehiculo(Vehiculo pVehiculo) {
        this._vehiculo = pVehiculo;
    }

    public Grua getGrua() {
        return _grua;
    }

    public void setGrua(Grua pGrua) {
        this._grua = pGrua;
    }

    public ArrayList<Operario> getOperarios() {
        return _operarios;
    }

    public void setOperarios(ArrayList<Operario> pOperarios) {
        this._operarios = pOperarios;
    }

    public SolicitudDeServicio() 
    {
        this(0, new Date(), "N/D", false, new Vehiculo(), new Grua(), new ArrayList<>());
    }
    
    
     
    public SolicitudDeServicio(int pNumeroSerie, Date pFecha, String pDireccion, boolean pCancelacion, Vehiculo pVehiculo, Grua pGrua, ArrayList<Operario> pOperarios) 
    {
        setNumeroSerie(pNumeroSerie);
        setFecha(pFecha);
        setDireccion(pDireccion);
        setCancelacion(pCancelacion);
        setVehiculo(pVehiculo);
        setGrua(pGrua);
        setOperarios(pOperarios);
    }

    @Override
    public String toString()
    {
        DateFormat formateador = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault());  
        boolean dosOperarios = false;
        if (getOperarios().size() >1) 
        {
            dosOperarios = true;
        }
        return "|| Número de Serie: " + getNumeroSerie() + " || Fecha: " + formateador.format(getFecha()) + " || Dirección: " + getDireccion() + " || Cancelado: " + (isCancelacion() ? "si":"no") + " || Matricula del Vehiculo: " + getVehiculo().getMatricula() + " || Operarior 1: " + getOperarios().get(0).getNombre() + " || Operaior 2: " + (dosOperarios ? getOperarios().get(1).getNombre() : "N/D") + " || Grúa: " + getGrua().getNumero() + " || ";
    }
  
}
