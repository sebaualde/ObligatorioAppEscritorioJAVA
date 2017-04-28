package Persistencia;

import EntidadesCompartidas.Cliente;
import EntidadesCompartidas.Recibo;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

class PersistenciaRecibo implements IPersistenciaRecibo {
    
    private static PersistenciaRecibo instancia = null;
    private PersistenciaRecibo(){}
    public static PersistenciaRecibo getInstanciaPersRecibo(){
        if(instancia != null){
            return instancia;
        }
        else{
            return new PersistenciaRecibo();
        }
    }
    
    @Override
    public void GenerearRecibos(double mensualidad) throws Exception{
        Connection conexion = null;
        PreparedStatement consulta= null;
        
        try{
            conexion = Conexion.getConexion();
            conexion.setAutoCommit(false);
            
            consulta = conexion.prepareStatement("INSERT INTO Recibos(Importe, CedulaCliente) VALUES(?, ?)");
        
            ArrayList<Recibo> subtotales = TotalServiciosPorCliente(mensualidad);
            if(subtotales.size()==0){
                throw new Exception("No hay recibos para generar por servicios del mes anterior");
            }
            
            for(Recibo st : subtotales){
                                       
                consulta.setDouble(1, st.getImporte());
                consulta.setLong(2, st.getCliente().getCedula());
                
                consulta.executeUpdate();
            }
            conexion.commit();
            
        }catch(Exception ex){
            try {
                if (conexion != null) {
                    conexion.rollback();
                }
            } catch (Exception exR) {
                throw new Exception("Ocurrió un error al deshacer los cambios");
            }
            
            throw new Exception(ex.getMessage());
        }finally {
            try {
                if (consulta != null) {
                    consulta.close();
                }
                
                if (conexion != null) {
                    conexion.close();
                }
            } catch (Exception ex) {
                throw new Exception("Ocurrió un error al cerrar los recursos");
            }
        }
    }
    
    @Override
    public ArrayList<Recibo> ListarRecibosDelMes(int mes, int anio)throws Exception{
        Connection conexion = null;
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        ArrayList<Recibo> recibos = null;
        try{             
             conexion = Conexion.getConexion();
             consulta= conexion.prepareStatement("SELECT * FROM Recibos WHERE MONTH(Fecha) = ? AND YEAR(Fecha) = ?");
             consulta.setInt(1, mes);
             consulta.setInt(2, anio);
             
             resultado = consulta.executeQuery();
             
             recibos = new ArrayList<Recibo>();
             
             while(resultado.next()){
                 Cliente cliente = FabricaPersistencia.GetPersistenciaCliente().Buscar(resultado.getLong("CedulaCliente"));
                 Recibo recibo = new Recibo(resultado.getLong("NumSerie"), resultado.getDate("Fecha"), resultado.getDouble("Importe"), cliente);
                 recibos.add(recibo);
             }
         }
         catch(Exception ex){
             throw new Exception("Error al listar los recibos: " + ex.getMessage());
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
            
        return recibos;
    }
    
    private ArrayList<Recibo> TotalServiciosPorCliente(double tarifaMensual) throws Exception{
        Connection conexion = null;
        CallableStatement consulta = null;
        ResultSet resultado = null;
        ArrayList<Recibo> recibos = new ArrayList<Recibo>();
        try{
            conexion = Conexion.getConexion();
            consulta = conexion.prepareCall("{CALL CalcularCostosServiciosPorCliente()}");

            resultado = consulta.executeQuery();
            
            while(resultado.next()){
                Recibo rec = new Recibo(1, resultado.getDate("fechaRecibo"),resultado.getDouble("TotalPorServicios")+tarifaMensual, FabricaPersistencia.GetPersistenciaCliente().Buscar(resultado.getLong("CedulaCliente"))) ;           
                recibos.add(rec);
            }
            consulta=null;
            consulta = conexion.prepareCall("SELECT *, now() AS fechaRecibo FROM Clientes WHERE Cedula NOT IN(SELECT V.CedulaCliente FROM Servicios S INNER JOIN SolicitudesDeServicios SS ON S.NumSolicitud = SS.NumSerie INNER JOIN Vehiculos V ON SS.Matricula = V.Matricula WHERE MONTH(SS.Fecha) = (MONTH(now())-1) AND V.CedulaCliente NOT IN(SELECT CedulaCliente FROM Recibos WHERE MONTH(Fecha) = month(now())) GROUP BY V.CedulaCliente) AND Cedula NOT IN (SELECT CedulaCliente FROM Recibos WHERE MONTH(Fecha) = month(now()))");
            
            resultado = consulta.executeQuery();
            
            while (resultado.next()) {
                Recibo rec = new Recibo(1, resultado.getDate("fechaRecibo"), tarifaMensual, FabricaPersistencia.GetPersistenciaCliente().Buscar(resultado.getLong("Cedula"))) ;           
                recibos.add(rec);
            }
        }
         catch(Exception ex){
             throw new Exception("Error al calcular el total de los servicios a cobrar:");
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
        return recibos;
    }
}
