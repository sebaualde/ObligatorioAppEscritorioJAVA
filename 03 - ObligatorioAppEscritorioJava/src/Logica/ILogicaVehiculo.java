package Logica;

import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Vehiculo;
import java.util.ArrayList;

public interface ILogicaVehiculo {
    void Agregar(Vehiculo vehiculo) throws Exception;
    void Eliminar(Vehiculo vehiculo) throws Exception;
    void Modificar(Vehiculo vehiculo) throws Exception;
    Vehiculo Buscar(String matricula) throws Exception;
    ArrayList<Vehiculo> vehiculosPorCliente(Cliente cliente) throws Exception;
}
