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
public class Operario 
{
    private String _idEmpleado;
    private String _nombre;
    private Date _fechaIngreso;

    public String getIdEmpleado() {
        return _idEmpleado;
    }

    public void setIdEmpleado(String _idEmpleado) {
        this._idEmpleado = _idEmpleado;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public Date getFechaIngreso() {
        return _fechaIngreso;
    }

    public void setFechaIngreso(Date _fechaIngreso) {
        this._fechaIngreso = _fechaIngreso;
    }
    
    

    public Operario() 
    {
        this("N/D", "N/D", new Date());
    }

    public Operario(String pIdOperario, String pNombre, Date pFecha) 
    {
        setIdEmpleado(pIdOperario);
        setNombre(pNombre);
        setFechaIngreso(pFecha);
    }
    
    @Override 
    public String toString()
    {
        return "ID Operario: " + getIdEmpleado() + ", Nombre: " + getNombre() + ", Fecha: " + getFechaIngreso();
    }
    
}
