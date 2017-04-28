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
public class Vehiculo {
    private String _matricula;
    private String _marca;
    private String _modelo;
    private int _peso;
    private Cliente _propietario; 

    public Cliente getPropietario() {
        return _propietario;
    }

    public void setPropietario(Cliente _propietario) {
        this._propietario = _propietario;
    }

    public String getMatricula() {
        return _matricula;
    }

    public void setMatricula(String _matricula) {
        this._matricula = _matricula;
    }

    public String getMarca() {
        return _marca;
    }

    public void setMarca(String _marca) {
        this._marca = _marca;
    }

    public String getModelo() {
        return _modelo;
    }

    public void setModelo(String _modelo) {
        this._modelo = _modelo;
    }

    public int getPeso() {
        return _peso;
    }

    public void setPeso(int _peso) {
        this._peso = _peso;
    }

    public Vehiculo() {
        setMatricula("N/D");
        setMarca("N/D");
        setModelo("N/D");
        setPeso(1);
        setPropietario(new Cliente(1, "N/D", "N/D", 1));
    }
    
    
    public Vehiculo(String matricula, String marca, String modelo, int peso, Cliente cliente) {
        setMatricula(matricula);
        setMarca(marca);
        setModelo(modelo);
        setPeso(peso);
        setPropietario(cliente);
    }

    @Override
    public String toString() {
        return String.format("Matrícula: " + getMatricula() + ", Marca: " + getMarca()+ ",Modelo: " + getModelo() + ", Peso: " + getPeso() + ", Propietario: " + getPropietario().getCedula());
    }
    
    
}
