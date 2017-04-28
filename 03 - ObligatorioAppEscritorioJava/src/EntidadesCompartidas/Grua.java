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
public class Grua 
{
    private int _numero;
    private int _pesoMaximoCarga;

    public int getNumero() {
        return _numero;
    }

    public void setNumero(int _numero) {
        this._numero = _numero;
    }

    public int getPesoMaximoCarga() {
        return _pesoMaximoCarga;
    }

    public void setPesoMaximoCarga(int _pesoMaximoCarga) {
        this._pesoMaximoCarga = _pesoMaximoCarga;
    }

    public Grua() 
    {
        this(0,0);
    }

    public Grua(int pNumero, int pPesoMaximoCarga) 
    {
        setNumero(pNumero);
        setPesoMaximoCarga(pPesoMaximoCarga);
    }
    
    @Override
    public String toString()
    {
        return String.format("Número: " + getNumero() + ", Peso máximo de carga: " + getPesoMaximoCarga());
    }
    
}
