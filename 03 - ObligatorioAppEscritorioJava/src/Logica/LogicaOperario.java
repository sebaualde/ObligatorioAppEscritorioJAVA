
package Logica;

import EntidadesCompartidas.Operario;
import Persistencia.FabricaPersistencia;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

class LogicaOperario implements ILogicaOperario
{
    private static LogicaOperario instancia = null;
    private LogicaOperario(){}
    public static LogicaOperario GetInstancia()
    {
        if (instancia == null)
        {
            instancia = new LogicaOperario();
        }
        return instancia;
    }
    
    @Override
    public void AltaOperario(Operario pOperario) throws Exception
    {
        VaidarOperario(pOperario);
        
        FabricaPersistencia.GetPersistenciaOperario().AltaOperario(pOperario);
    }
    
    @Override
    public void BajaOperario(Operario pOperario) throws Exception
    {
        FabricaPersistencia.GetPersistenciaOperario().BajaOperario(pOperario);
    }
    
    @Override
    public Operario BuscarOperario(String pNumOperario)throws Exception
    {
        return FabricaPersistencia.GetPersistenciaOperario().BuscarOperario(pNumOperario);
    }
    
    @Override
    public ArrayList<Operario> ListarOperarios() throws Exception
    {
        return FabricaPersistencia.GetPersistenciaOperario().ListarOperarios();
    }
    
    public static void VaidarOperario(Operario pOperario) throws Exception
    {
        if (pOperario == null)  
        {
            throw new Exception("El operario es nulo.");
        }
        
        if (pOperario.getIdEmpleado().isEmpty() || pOperario.getIdEmpleado() == null ) 
        {
            throw new Exception("El ID del operario no puede quedar vacío.");
        }
        
        if (!(pOperario.getIdEmpleado().length() != 6)) 
        {
            //comprobacion de formato del ID con 3 Letras y 3 Números
            for (int i = 0; i < 6; i++) 
            {
                if (i == 0 || i == 1|| i == 2) 
                {
                    //pregunto si el caracter del id en la posicion i es un numero
                    if (Character.isDigit(pOperario.getIdEmpleado().charAt(i)))
                    {
                        i=6;
                        throw new Exception("Los primeros 3 caracteres del ID deben ser letras.");   
                    }
                }
                else
                {
                    //pregunto si los ultimos 3 carateres no son numeros
                    if (!Character.isDigit(pOperario.getIdEmpleado().charAt(i)))
                    {
                        i=6;
                        throw new Exception("Los últimos 3 caracteres del ID deben ser números.");   
                    }
                }
            }
        }
        else
        {
            throw new Exception("El ID deben tener 6 caracteres.");   
        }
        
        if (pOperario.getNombre().isEmpty() || pOperario.getNombre().length() > 50) 
        {
            throw new Exception("El nombre del operario no puede quedar vacío ni tener mas de 50 caracteres.");   
        }
        
        if (pOperario.getFechaIngreso() == null) 
        {
            throw new Exception("La fecha de ingreso es nula.");   
        }
        
        //creo un formato simple para poder comparar las fechas actual y la seleccionada 
        DateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
        Date fechaIngreso = pOperario.getFechaIngreso();
        formato.format(fechaIngreso);
        //a la fecha actual le resto un dia para permitir que se agreguen operarios el dia de hoy inclusive
        Calendar cal = new GregorianCalendar();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.DATE, -1);        
        //si la fecha de ingreso es anterior a la actual no se permite su ingreso a la empresa
        if (fechaIngreso.before(new Date(cal.getTimeInMillis())))
        {
            throw new Exception("La fecha de ingreso no puede ser anterior al día de hoy.");   
        }

    }
}
