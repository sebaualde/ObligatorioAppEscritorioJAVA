/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Presentacion;

import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Vehiculo;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Darío
 */
public class ModeloJTableVehiculos extends AbstractTableModel {
    
    private String[] columnas = { "Matricula", "Marca", "Modelo", "Peso", "Propietario"};
    private ArrayList<Vehiculo> datos;
    
    
    public ModeloJTableVehiculos(ArrayList<Vehiculo> datos) {
        this.datos = datos;
    }
    
    
    @Override
    public int getRowCount() {
        if (datos != null) {
            return datos.size();
        } else {
            return 0;
        }
    }
    
    @Override
    public int getColumnCount() {
        return columnas.length;
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return datos.get(rowIndex).getMatricula();
            case 1:
                return datos.get(rowIndex).getMarca();
            case 2:
                return datos.get(rowIndex).getModelo();
            case 3:
                return datos.get(rowIndex).getPeso();
            case 4:
                return datos.get(rowIndex).getPropietario().getCedula();
            default:
                return null;
        }
    }
    
    @Override
    public String getColumnName(int column) {
        return columnas[column];
    }
    
    @Override
    public Class getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return String.class;
            case 1:
                return String.class;
            case 2:
                return String.class;
            case 3:
                return Integer.class;
            case 4:
                return Long.class;
            default:
                return String.class;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }    
}
