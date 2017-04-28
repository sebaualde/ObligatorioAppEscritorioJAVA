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
public class Cliente {
    private long _cedula;
    private String _nombre;
    private String _direccion; 
    private long _telefono;

    public long getCedula() {
        return _cedula;
    }

    public void setCedula(long _cedula) {
        this._cedula = _cedula;
    }

    public String getNombre() {
        return _nombre;
    }

    public void setNombre(String _nombre) {
        this._nombre = _nombre;
    }

    public String getDireccion() {
        return _direccion;
    }

    public void setDireccion(String _direccion) {
        this._direccion = _direccion;
    }

    public long getTelefono() {
        return _telefono;
    }

    public void setTelefono(long _telefono) {
        this._telefono = _telefono;
    }
    
    public Cliente(long cedula, String nombre, String direccion, long telefono){
        setCedula(cedula);
        setNombre(nombre);
        setDireccion(direccion);
        setTelefono(telefono);
    }
    
    @Override
    public String toString(){
        return String.format("Cédula: "+ getCedula() +", Nombre: "+getNombre()+", Dirección: "+getDireccion()+", Teléfono: "+getTelefono());
    }
        
}
