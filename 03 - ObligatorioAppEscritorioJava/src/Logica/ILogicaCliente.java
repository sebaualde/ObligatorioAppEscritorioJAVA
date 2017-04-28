package Logica;

import EntidadesCompartidas.Cliente;
import java.util.ArrayList;

public interface ILogicaCliente {
    void Agregar(Cliente cliente) throws Exception;
    void Eliminar(Cliente cliente) throws Exception;
    void Modificar(Cliente cliente) throws Exception;
    Cliente Buscar(long cedula) throws Exception;
}
