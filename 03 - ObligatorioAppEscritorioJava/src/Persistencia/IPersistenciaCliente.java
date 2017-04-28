/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Persistencia;
import EntidadesCompartidas.Cliente;
import java.util.ArrayList;
/**
 *
 * @author dario
 */
public interface IPersistenciaCliente {    
    void Agregar(Cliente cliente) throws Exception;
    void Eliminar(Cliente cliente) throws Exception;
    void Modificar(Cliente cliente) throws Exception;
    Cliente Buscar(long cedula) throws Exception;
  }
