package Persistencia;

import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Vehiculo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class PersistenciaVehiculo implements IPersistenciaVehiculo {
    
    private static PersistenciaVehiculo instancia = null;
    private PersistenciaVehiculo(){}
    public static PersistenciaVehiculo getInstanciaPersVehiculo(){
        if(instancia != null){
            return instancia;
        }
        else{
            return new PersistenciaVehiculo();
        }
    }
    
    @Override
    public void Agregar(Vehiculo vehiculo)throws Exception{
        Connection conexion = null;
        CallableStatement cmdAgregarVehiculo = null;
        
        try{
            conexion = Conexion.getConexion();
            
            cmdAgregarVehiculo = conexion.prepareCall("{ CALL AgregarVehiculo(?, ?, ?, ?, ?, ?) }");
            cmdAgregarVehiculo.setString(1, vehiculo.getMatricula());
            cmdAgregarVehiculo.setString(2, vehiculo.getMarca());
            cmdAgregarVehiculo.setString(3,vehiculo.getModelo());
            cmdAgregarVehiculo.setInt(4, vehiculo.getPeso());
            cmdAgregarVehiculo.setLong(5, vehiculo.getPropietario().getCedula());
            cmdAgregarVehiculo.registerOutParameter(6, java.sql.Types.VARCHAR);
            
            cmdAgregarVehiculo.executeUpdate();
            String mensajeError = cmdAgregarVehiculo.getString(6);
                       
            if(mensajeError != null){
                throw new Exception(mensajeError);
            }
            
        } catch (Exception ex) {
            throw ex;
        }
        finally{
           try{
               if(cmdAgregarVehiculo!=null){
                   cmdAgregarVehiculo.close();
                }

               if(conexion!=null){
                   conexion.close();
               }
           }
           catch(Exception ex){
               throw new Exception("Error al cerrar los recursos");
           }
        }
    }
    
    @Override
    public void Eliminar(Vehiculo vehiculo) throws Exception{
        Connection conexion = null;
        CallableStatement cmdEliminarVehiculo = null;
        
        try{
            conexion = Conexion.getConexion();
            
            cmdEliminarVehiculo = conexion.prepareCall("{ CALL EliminarVehiculo(?, ?) }");
            cmdEliminarVehiculo.setString(1, vehiculo.getMatricula());
            cmdEliminarVehiculo.registerOutParameter(2, java.sql.Types.VARCHAR);
            
            cmdEliminarVehiculo.executeUpdate();
            String mensajeError = cmdEliminarVehiculo.getString(2);
            
            if(mensajeError != null){
                throw new Exception(mensajeError);
            }
            
           } catch(Exception ex){
             throw ex;
            }
        finally{
           try{
               if(cmdEliminarVehiculo!=null){
                   cmdEliminarVehiculo.close();
                }

               if(conexion!=null){
                   conexion.close();
               }
           }
           catch(Exception ex){
               throw new Exception("Error al cerrar los recursos");
           }
        }
    }
    
    @Override
    public void Modificar(Vehiculo vehiculo) throws Exception{
        Connection conexion = null;
        CallableStatement cmdModificarVehiculo = null;
        
        try{
            conexion = Conexion.getConexion();
            
            cmdModificarVehiculo = conexion.prepareCall("{ CALL ModificarVehiculo(?, ?, ?, ?, ?, ?) }");
            cmdModificarVehiculo.setString(1, vehiculo.getMatricula());
            cmdModificarVehiculo.setString(2, vehiculo.getMarca());
            cmdModificarVehiculo.setString(3,vehiculo.getModelo());
            cmdModificarVehiculo.setInt(4, vehiculo.getPeso());
            cmdModificarVehiculo.setLong(5, vehiculo.getPropietario().getCedula());
            cmdModificarVehiculo.registerOutParameter(6, java.sql.Types.VARCHAR);
            
            cmdModificarVehiculo.executeUpdate();
            String mensajeError = cmdModificarVehiculo.getString(6);
                       
            if(mensajeError != null){
                throw new Exception(mensajeError);
            }
            
        } catch (Exception ex) {
            throw ex;
        }
        finally{
           try{
               if(cmdModificarVehiculo!=null){
                   cmdModificarVehiculo.close();
                }

               if(conexion!=null){
                   conexion.close();
               }
           }
           catch(Exception ex){
               throw new Exception("Error al cerrar los recursos");
           }
        }
    }
    
    @Override
    public Vehiculo Buscar(String matricula) throws Exception{
        Connection conexion = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        Vehiculo vehiculo = null;
        try{             
             conexion = Conexion.getConexion();
             consulta= conexion.prepareStatement("SELECT * FROM Vehiculos WHERE Matricula = ?");
             consulta.setString(1, matricula);
             
             resultado = consulta.executeQuery();
             
             while(resultado.next()){
                 vehiculo = new Vehiculo(resultado.getString("Matricula"), resultado.getString("Marca"), resultado.getString("Modelo"), resultado.getInt("Peso"), FabricaPersistencia.GetPersistenciaCliente().Buscar(resultado.getLong("CedulaCliente")));
             }
         }
         catch(Exception ex){
             throw new Exception("Error al buscar el vehiculo de matricula: " + matricula + ex.getMessage());
         }
        finally{
          try{
            if(resultado != null) {
                resultado.close();
            }

            if(consulta!=null){
                consulta.close();
            }

            if(conexion !=null){
                conexion.close();
            }
          }
          catch(Exception ex){
               throw new Exception("Error al cerrar los recursos");
           }
        }
            
        return vehiculo;
    }
    
    @Override
    public ArrayList<Vehiculo> vehiculosPorCliente(Cliente cliente) throws Exception{
        
        Connection conexion = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        ArrayList<Vehiculo> vehiculos = null;
        try{             
             conexion = Conexion.getConexion();
             consulta= conexion.prepareStatement("SELECT * FROM Vehiculos INNER JOIN Clientes on CedulaCliente = Cedula WHERE CedulaCliente = ? and Vehiculos.Eliminado = 0 AND Clientes.Eliminado = 0");
             consulta.setLong(1, cliente.getCedula());
             
             resultado = consulta.executeQuery();
             
             vehiculos = new ArrayList<Vehiculo>();
             
             while(resultado.next()){
                 Vehiculo vehiculo = new Vehiculo(resultado.getString("Matricula"), resultado.getString("Marca"), resultado.getString("Modelo"), resultado.getInt("Peso"), cliente);
                 vehiculos.add(vehiculo);
             }
         }
         catch(Exception ex){
             throw new Exception("Error al los vehículos: " + ex.getMessage());
         }
        finally{
          try{
            if(resultado != null) {
                resultado.close();
            }

            if(consulta!=null){
                consulta.close();
            }

            if(conexion !=null){
                conexion.close();
            }
          }
          catch(Exception ex){
               throw new Exception("Error al cerrar los recursos");
           }
        }
            
        return vehiculos;
    
    }
}
