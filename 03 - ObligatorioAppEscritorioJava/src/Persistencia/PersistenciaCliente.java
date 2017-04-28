package Persistencia;

import EntidadesCompartidas.Cliente;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class PersistenciaCliente implements IPersistenciaCliente{
    //Singleton
    private static PersistenciaCliente instancia=null;
    private PersistenciaCliente(){}
    public static PersistenciaCliente getInstanciaPersCli(){
        if(instancia !=null){
            return instancia;
        }
        else{
            return new PersistenciaCliente();
        }
    }
    //Fin Singleton
    
    @Override
    public void Agregar(Cliente cliente)throws Exception{
        Connection conexion = null;
        CallableStatement cmdAgregarCliente = null;
        
        try{
            conexion = Conexion.getConexion();
            
            cmdAgregarCliente = conexion.prepareCall("{ CALL AgregarCliente(?, ?, ?, ?, ?) }");
            cmdAgregarCliente.setLong(1, cliente.getCedula());
            cmdAgregarCliente.setString(2, cliente.getNombre());
            cmdAgregarCliente.setString(3,cliente.getDireccion());
            cmdAgregarCliente.setLong(4, cliente.getTelefono());
            cmdAgregarCliente.registerOutParameter(5, java.sql.Types.VARCHAR);
            
            cmdAgregarCliente.executeUpdate();
            String mensajeError = cmdAgregarCliente.getString(5);
                       
            if(mensajeError != null){
                throw new Exception(mensajeError);
            }
            
        } catch (Exception ex) {
            throw ex;
        }
        finally{
           try{
               if(cmdAgregarCliente!=null){
                   cmdAgregarCliente.close();
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
    public void Eliminar(Cliente cliente) throws Exception{
        Connection conexion = null;
        CallableStatement cmdEliminarCliente = null;
        
        try{
            conexion = Conexion.getConexion();
            
            cmdEliminarCliente = conexion.prepareCall("{ CALL EliminarCliente(?, ?) }");
            cmdEliminarCliente.setLong(1, cliente.getCedula());
            cmdEliminarCliente.registerOutParameter(2, java.sql.Types.VARCHAR);
            
            cmdEliminarCliente.executeUpdate();
            String mensajeError = cmdEliminarCliente.getString(2);
            
            if(mensajeError != null){
                throw new Exception(mensajeError);
            }
            
           } catch(Exception ex){
             throw ex;
            }
        finally{
           try{
               if(cmdEliminarCliente!=null){
                   cmdEliminarCliente.close();
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
    public void Modificar(Cliente cliente) throws Exception{
        Connection conexion = null;
        CallableStatement cmdModificarCliente = null;
        
        try{
            conexion = Conexion.getConexion();
            
            cmdModificarCliente = conexion.prepareCall("{CALL ModificarCliente(?, ?, ?, ?, ?)}");
            cmdModificarCliente.setLong(1, cliente.getCedula());
            cmdModificarCliente.setString(2, cliente.getNombre());
            cmdModificarCliente.setString(3,cliente.getDireccion());
            cmdModificarCliente.setLong(4, cliente.getTelefono());
            cmdModificarCliente.registerOutParameter(5, java.sql.Types.VARCHAR);
            
            cmdModificarCliente.executeUpdate();
            String mensajeError = cmdModificarCliente.getString(5);
                       
            if(mensajeError != null){
                throw new Exception(mensajeError);
            }
            
        } catch (Exception ex) {
            throw ex;
        }
        finally{
           try{
               if(cmdModificarCliente!=null){
                   cmdModificarCliente.close();
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
    public Cliente Buscar(long cedula) throws Exception{
        Connection conexion = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        Cliente cliente = null;
        try{             
             conexion = Conexion.getConexion();
             consulta= conexion.prepareStatement("SELECT * FROM Clientes WHERE Cedula = ? and Eliminado = 0");
             consulta.setLong(1, cedula);
             
             resultado = consulta.executeQuery();
             
             while(resultado.next()){
                 cliente = new Cliente(resultado.getLong("Cedula"), resultado.getString("Nombre"), resultado.getString("Direccion"), resultado.getLong("Telefono"));
             }
         }
         catch(Exception ex){
             throw new Exception("Error al buscar el cliente: " + ex.getMessage());
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
            
        return cliente;
    }
 }
