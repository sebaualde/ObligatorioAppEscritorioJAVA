package Logica;

import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Vehiculo;
import java.util.ArrayList;

class LogicaVehiculo implements ILogicaVehiculo{
    
    private static LogicaVehiculo _instancia = null;
    private LogicaVehiculo(){}
    public static LogicaVehiculo GetInstancia()
    {
        if(_instancia == null)
        {
            _instancia = new LogicaVehiculo();
        }
        return _instancia;
    }
    
    public void validarVehiculo(Vehiculo vehiculo) throws Exception{
        if(String.valueOf(vehiculo.getMatricula().trim()).length()==0){
            throw new Exception("La matrícula es un dato obligatorio");
        }
        if(String.valueOf(vehiculo.getMatricula().trim()).length()>7){
            throw new Exception("La matrícula excede los 7 caracteres");
        }
        
        if(String.valueOf(vehiculo.getMarca()).length()==0){
            throw new Exception("Debe ingresar la Marca");
        }
        if(String.valueOf(vehiculo.getMarca().trim()).length()>20){
            throw new Exception("La marca excede los 20 caracteres");
        }
        
        if(String.valueOf(vehiculo.getModelo()).length()==0){
            throw new Exception("Debe ingresar el modelo");
        }
        if(String.valueOf(vehiculo.getModelo().trim()).length()>20){
            throw new Exception("El modelo excede los 20 caracteres");
        }
        
        if(vehiculo.getPeso()<0){
            throw new Exception("El peso del vehículo no puede ser negativo");
        }
        if(String.valueOf(vehiculo.getPeso()).length()==0){
            throw new Exception("El peso es un dato obligatorio");
        }
        
        if(vehiculo.getPropietario() == null){
            throw new Exception("El propietario no puede ser nulo");
        }                
    }
    
    @Override
    public void Agregar(Vehiculo vehiculo) throws Exception{
            validarVehiculo(vehiculo);
            Persistencia.FabricaPersistencia.GetIPersistenciaVehiculo().Agregar(vehiculo);        
    }
    
    @Override
    public void Eliminar(Vehiculo vehiculo) throws Exception{
            Persistencia.FabricaPersistencia.GetIPersistenciaVehiculo().Eliminar(vehiculo);        
    }
    
    @Override
    public void Modificar(Vehiculo vehiculo) throws Exception{
            validarVehiculo(vehiculo);
            Persistencia.FabricaPersistencia.GetIPersistenciaVehiculo().Modificar(vehiculo);        
    }
    
    @Override
    public Vehiculo Buscar(String matricula) throws Exception{
        return Persistencia.FabricaPersistencia.GetIPersistenciaVehiculo().Buscar(matricula);
    }
    
    @Override
    public ArrayList<Vehiculo> vehiculosPorCliente(Cliente cliente) throws Exception{
        return Persistencia.FabricaPersistencia.GetIPersistenciaVehiculo().vehiculosPorCliente(cliente);
    }
}
