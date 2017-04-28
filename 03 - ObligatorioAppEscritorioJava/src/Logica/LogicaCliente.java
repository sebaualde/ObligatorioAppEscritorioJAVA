package Logica;

import EntidadesCompartidas.Cliente;
import java.util.ArrayList;

class LogicaCliente implements ILogicaCliente{
    
    private static LogicaCliente _instancia = null;
    private LogicaCliente(){}
    public static LogicaCliente GetInstancia()
    {
        if(_instancia == null)
        {
            _instancia = new LogicaCliente();
        }
        return _instancia;
    }
    
    public void validarCliente(Cliente cliente) throws Exception{
        if(String.valueOf(cliente.getCedula()).length()==0){
            throw new Exception("La cédula es un dato obligatorio");
        }
        
        if(String.valueOf(cliente.getCedula()).length()!=8){
            throw new Exception("La cédula debe tener 8 dígitos");
        }
        
        if(cliente.getCedula()<0){
            throw new Exception("El nro. de cédula no puede ser negativo");
        }
        
        if(cliente.getNombre().length()>30){
            throw new Exception("El nombre supera los 30 caracteres");
        }
        
        if(cliente.getNombre().length()==0){
            throw new Exception("El nombre no puede estar vacío");         
        }
        
        if(cliente.getDireccion().length()==0){
            throw new Exception("El campo Dirección está vacío");
         }
        
        if(cliente.getDireccion().length()>50){
            throw new Exception("La dirección supera los 50 caracteres");
         }
        
        if(cliente.getTelefono()<0){
            throw new Exception("El teléfono no puede ser un nro negativo");
         }
        
        if(String.valueOf(cliente.getTelefono()).length()==0){
            throw new Exception("El teléfono es un dato obligatorio");
        }        
    }
    @Override
    public void Agregar(Cliente cliente) throws Exception{
            validarCliente(cliente);
            Persistencia.FabricaPersistencia.GetPersistenciaCliente().Agregar(cliente);        
    }
    
    @Override
    public void Eliminar(Cliente cliente) throws Exception{
            Persistencia.FabricaPersistencia.GetPersistenciaCliente().Eliminar(cliente);        
    }

    @Override
    public void Modificar(Cliente cliente) throws Exception{
            validarCliente(cliente);
            Persistencia.FabricaPersistencia.GetPersistenciaCliente().Modificar(cliente);        
    }
    
    @Override
    public Cliente Buscar(long cedula) throws Exception{
        return Persistencia.FabricaPersistencia.GetPersistenciaCliente().Buscar(cedula);
    }
}
