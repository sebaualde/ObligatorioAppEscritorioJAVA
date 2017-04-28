/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;
import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Vehiculo;
import java.util.ArrayList;

/**
 *
 * @author TetoX
 */
public interface IPersistenciaVehiculo {
    Vehiculo Buscar(String matricula) throws Exception;
    void Agregar(Vehiculo vehiculo) throws Exception;
    void Eliminar(Vehiculo vehiculo) throws Exception;
    void Modificar(Vehiculo vehiculo) throws Exception;
    ArrayList<Vehiculo> vehiculosPorCliente(Cliente cliente) throws Exception;
}
