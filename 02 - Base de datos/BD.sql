DROP DATABASE IF EXISTS AppEscritorioJava;

CREATE DATABASE AppEscritorioJava;

USE AppEscritorioJava;

CREATE TABLE Clientes (
	Cedula BIGINT PRIMARY KEY CHECK(LENGTH = 8),
	Nombre VARCHAR(30) NOT NULL,
	Direccion VARCHAR(50) NOT NULL,
	Telefono BIGINT NOT NULL,
    Eliminado BIT DEFAULT 0
);

CREATE TABLE Vehiculos(
	Matricula VARCHAR(7) PRIMARY KEY,
	Marca VARCHAR(20) NOT NULL,
    Modelo VARCHAR(20),
    Peso INT NOT NULL,
    CedulaCliente BIGINT,
	Eliminado BIT DEFAULT 0,
    FOREIGN KEY (CedulaCliente) REFERENCES Clientes(Cedula)
    
);

CREATE TABLE Recibos(
	NumSerie INT PRIMARY KEY AUTO_INCREMENT,
    Fecha TIMESTAMP default current_timestamp,
    Importe DOUBLE NOT NULL,
    CedulaCliente BIGINT,
    FOREIGN KEY (CedulaCliente) REFERENCES Clientes(Cedula)
);

CREATE TABLE Operarios(
	IdEmpleado VARCHAR (6) PRIMARY KEY,
    Nombre VARCHAR(50) NOT NULL,
    FechaIngreso DATETIME NOT NULL,
    Eliminado BIT NOT NULL DEFAULT 0
);

CREATE TABLE Gruas(
	Numero INT PRIMARY KEY,
    PesoMaxCarga INT NOT NULL,
    Eliminado BIT NOT NULL DEFAULT 0
);

CREATE TABLE SolicitudesDeServicios(
	NumSerie INT PRIMARY KEY AUTO_INCREMENT,
    Fecha timestamp default current_timestamp,
    Direccion VARCHAR (150) NOT NULL,
    Cancelado BIT NOT NULL DEFAULT 0,
    Matricula VARCHAR (7) NOT NULL,
    NroGrua INT NOT NULL,
    FOREIGN KEY (NroGrua)REFERENCES Gruas (Numero),
    FOREIGN KEY (Matricula) REFERENCES Vehiculos (Matricula)
);

CREATE TABLE OperariosAsignadosASolicitudes(
	IdEmpleado VARCHAR (6),
	NumSolicitud INT,
	FOREIGN KEY (IdEmpleado) REFERENCES Operarios (IdEmpleado),
	FOREIGN KEY (NumSolicitud) REFERENCES SolicitudesDeServicios (NumSerie),
    PRIMARY KEY (IdEmpleado, NumSolicitud)
);

CREATE TABLE Servicios(
	NumServicio INT PRIMARY KEY AUTO_INCREMENT,
    ImporteTotal DOUBLE NOT NULL,
    NumSolicitud INT,
    FOREIGN KEY (NumSolicitud) REFERENCES SolicitudesDeServicios(NumSerie)
);

CREATE TABLE AuxiliosMecanicos (
	NumServicio INT PRIMARY KEY REFERENCES Servicios (NumServicio),
    DescripcionProblema VARCHAR (300) NOT NULL,
    DescripcionReparacion VARCHAR (300) NOT NULL,
    CostoRepuesto DOUBLE NOT NULL
);

CREATE TABLE Remolques(
	NumServicio INT PRIMARY KEY REFERENCES Servicios (NumServicio),
    CantidadKm INT NOT NULL
);


/*--------------SP GRUAS-------------*/

DELIMITER //

CREATE PROCEDURE AltaGrua(pNumero INT, pPesoMaximo INT, OUT pError VARCHAR(100))
altaGrua:BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
		SET pError = 'No se pudo agregar la Grúa.';
        
	IF EXISTS (SELECT * FROM Gruas WHERE Numero = pNumero) THEN
		SET pError = 'Número de Grúa no disponible.';
        
        LEAVE altaGrua;
    END IF;
        
	IF (pPesoMaximo < 0) THEN
		SET pError = 'La grúa debe cargar más de 0 kg.';
        
        LEAVE altaGrua;
    END IF;
    
	INSERT INTO Gruas (Numero, PesoMaxCarga) VALUES(pNumero, pPesoMaximo);
END //

DELIMITER ; 

/* PRUEBAS DE ALTAS GRUAS
------------Error: YA EXISTE ESE NUMERO DE GRUA-------------
SET @resultado = '';
CALL AltaGrua(1, 1400, @resultado);
SELECT @resultado;
------------Error: CARGA INFERIOR A 50 KG-------------
SET @resultado = '';
CALL AltaGrua(7, -39, @resultado);
SELECT @resultado;
------------GRÚA AGREGADA CORRECTAMENTE-------------
SET @resultado = '';
CALL AltaGrua(6, 5600, @resultado);
SELECT @resultado;
*/

DELIMITER //

CREATE PROCEDURE BajaGrua(pId INT, OUT pError VARCHAR(100))
bajaGrua:BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
		SET pError = 'No se pudo eliminar la Grúa.';
        
	IF NOT EXISTS(SELECT * FROM Gruas WHERE pId = Numero) THEN
		SET pError = 'No existe la grúa que quiere eliminar.';
        
        LEAVE bajaGrua;
    END IF;
    
    IF NOT EXISTS(SELECT * FROM SolicitudesDeServicios WHERE pId = NroGrua) THEN
		DELETE 
		FROM Gruas
		WHERE  pId = Numero;
        
        LEAVE bajaGrua;        
	ELSEIF EXISTS (SELECT * FROM GruasDisponibles WHERE pId = Numero) THEN
		UPDATE Gruas
		SET Eliminado = 1 
		WHERE pId = Numero;
        
        LEAVE bajaGrua;
	ELSE
		SET pError = 'La grúa tiene una solicitud pendiente.';
        
        LEAVE bajaGrua;
	END IF;
		
END //

DELIMITER ;

/* PRUEBAS DE BAJAS GRUAS
------------Error: NO EXISTE LA GRÚA-------------
SET @resultado = '';
CALL BajaGrua(123, @resultado);
SELECT @resultado;
------------Error: LA GRÚA TIENE SOLICITUDES PENDIENTES-------------
SET @resultado = '';
CALL BajaGrua(1, @resultado);
SELECT @resultado;
------------GRÚA ELIMINADA LOGICAMENTE-------------
SET @resultado = '';
CALL BajaGrua(2, @resultado);
SELECT @resultado;
------------GRÚA ELIMINADA FIDICAMENTE-------------
SET @resultado = '';
CALL BajaGrua(4, @resultado);
SELECT @resultado;
*/

CREATE PROCEDURE ListarGruasDisponibles()
	SELECT * FROM GruasDisponibles;

-- la grua 1 tiene una solicitud pendiente por lo que no esta disponible
-- la grua 2 ya realizo el servicio por lo que ya esta disponible 
-- la grua 3 tiene una solicitud cancelada por lo que tambien esta disponible
-- CALL ListarGruasDisponibles();

-- Vista usada para verificaciones de disponibilidad de gruas
CREATE VIEW GruasDisponibles AS
	SELECT * 
	FROM Gruas
	WHERE Eliminado = 0 AND NOT EXISTS(
										SELECT * 
										FROM SolicitudesDeServicios
										WHERE Gruas.Numero = SolicitudesDeServicios.NroGrua  AND Cancelado = 0
										AND NOT EXISTS( SELECT *
														FROM Servicios
														WHERE SolicitudesDeServicios.NumSerie = Servicios.NumSolicitud));
 

/*------------SP OPERARIOS-----------*/
CREATE PROCEDURE BuscarOperario(pId VARCHAR(6))
	SELECT * FROM Operarios WHERE IdEmpleado = pId AND Eliminado = 0;
    
/* PRUEBAS DE BUSQUEDA
------------OPERARIO ENCONTRADO-------------
CALL BuscarOperario('AAA123');
*/

DELIMITER //

CREATE PROCEDURE AltaOperario(pId VARCHAR(6), pNombre VARCHAR(50), pFechaIngreso DATETIME, OUT pError VARCHAR(100))
altaOp:BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
		SET pError = 'No se pudo agregar el empleado.';
                
    IF EXISTS(SELECT * FROM Operarios WHERE IdEmpleado = pId) THEN 
		SET pError = 'ID de empleado no disponible.';
        
        LEAVE altaOp;
	END IF;
    
    IF (pFechaIngreso < CURDATE()) THEN
		SET pError = 'La fecha de ingreso no puede ser anterior a la de hoy.';
        
        LEAVE altaOp;
	END IF;
     
    INSERT INTO Operarios (IdEmpleado, Nombre, FechaIngreso) VALUES (pId, pNombre, pFechaIngreso);

END //

DELIMITER ;

/* PRUEBAS DE ALTAS OPERARIOS
------------Error: YA EXISTE EL OPERARIO-------------
SET @resultado = '';
CALL AltaOperario('AAA123', 'Chuck Norris', '2016/5/19', @resultado);
SELECT @resultado;
------------Error: LA FECHA DE INGRESO ES ANTERIOR A LA DE HOY-------------
SET @resultado = '';
CALL AltaOperario('AS5123', 'Pablo Picasso', '2016/6/16', @resultado);
SELECT @resultado;
------------OPERARIO AGREGADO CORRECTAMENTE-------------
SET @resultado = '';
CALL AltaOperario('AS5123', 'Pablo Picasso', '2017/6/18', @resultado);
SELECT @resultado;
*/

DELIMITER //

CREATE PROCEDURE BajaOperario(pId VARCHAR(6), OUT pError VARCHAR(100))
bajaOp:BEGIN
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
		SET pError = 'No se pudo eliminar el empleado.';

    IF NOT EXISTS(SELECT * FROM Operarios WHERE IdEmpleado = pId AND Eliminado = 0) THEN
		SET pError = 'No existe el operario a eliminar.';
        
        LEAVE bajaOp;
    END IF;
    
    IF NOT EXISTS(
			SELECT * 
			FROM OperariosAsignadosASolicitudes INNER JOIN SolicitudesDeServicios
					ON OperariosAsignadosASolicitudes.NumSolicitud = SolicitudesDeServicios.NumSerie
			WHERE OperariosAsignadosASolicitudes.IdEmpleado = pId) THEN
					DELETE 
					FROM Operarios
					WHERE IdEmpleado = pId;
					
					LEAVE bajaOp;
	ELSEIF NOT EXISTS(
		SELECT *
		FROM OperariosDisponibles
		WHERE pId = IdEmpleado) then
				SET pError = 'El operario tiene solicitudes de servicio pendientes.';
				
				LEAVE bajaOp;
	ELSE 
		UPDATE Operarios
		SET Eliminado = 1
		WHERE pId = IdEmpleado;
		
		LEAVE bajaOp;
    END IF;
    
END //

DELIMITER ;

/* PRUEBAS DE BAJAS OPERARIOS
------------Error: NO EXISTE EL OPERARIO-------------
SET @resultado = '';
CALL BajaOperario('AFA123', @resultado);
SELECT @resultado;
------------Error: EL OPERARIO TIENE SOLICITUDES PENDIENTES-------------
SET @resultado = '';
CALL BajaOperario('CCC789', @resultado);
SELECT @resultado;
------------BAJA LOGICA CON SOLICITUD CANCELADA-------------
SET @resultado = '';
CALL BajaOperario('BBB456', @resultado);
SELECT @resultado;
------------BAJA LOGICA CORRECTA-------------
SET @resultado = '';
CALL BajaOperario('EEE456', @resultado);
SELECT @resultado;
*/

CREATE PROCEDURE ListarOperariosDisponibles()
	SELECT * FROM OperariosDisponibles;


-- todos los operarios estan disponibles salvo CCC789 que tiene una solicitud pendiente 
-- FFF789  ya cumplio con el servicio que fue solicitado por tanto vuelve a estar disponible
-- BBB456 esta disponible ya que la solictud que le asignaron fue cancelada
-- CALL ListarOperariosDisponibles();

CREATE VIEW OperariosDisponibles AS
	SELECT * 
	FROM Operarios
	WHERE Eliminado = 0 AND NOT EXISTS(
						SELECT * 
						FROM OperariosAsignadosASolicitudes INNER JOIN SolicitudesDeServicios
								ON OperariosAsignadosASolicitudes.NumSolicitud = SolicitudesDeServicios.NumSerie
						WHERE Operarios.IdEmpleado = OperariosAsignadosASolicitudes.IdEmpleado AND Cancelado = 0 AND
								NOT EXISTS(
										SELECT *
										FROM Servicios 
										WHERE SolicitudesDeServicios.NumSerie = Servicios.NumSolicitud )); 
											

/*--------------SP SOLICITUDES DE SERVICIOS-------------*/

DELIMITER //

CREATE PROCEDURE AgregarSolicitudServicio(pDireccion VARCHAR(150), pMatricula VARCHAR(7), pOperario1 VARCHAR(6), pOperario2 VARCHAR(6),  pNroGrua INT, OUT pError VARCHAR(100)) 
alta : BEGIN
	DECLARE mensajeError VARCHAR(100);
    DECLARE transaccionActiva BIT;
	DECLARE Operario1Vacio BIT;
	DECLARE Operario2Vacio BIT;
	DECLARE indice INT;

	
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		IF transaccionActiva THEN
			ROLLBACK;
        END IF;
        
        SET pError = mensajeError;
    END;

	SET Operario1Vacio = 0;
	SET Operario2Vacio = 0;

    #----------------COMPROBACION DE EXISTENCIA DE OPERARIOS---------------------------------------------------------------
    IF (NOT EXISTS(
		SELECT * 
        FROM OperariosDisponibles
        WHERE IdEmpleado LIKE pOperario1) || pOperario1 LIKE '') THEN
			SET Operario1Vacio = 1; #SI EL OPERARIO 1 NO EXISTE O ESTA VACIO SE MARCA COMO TRUE
	END IF;

	IF (NOT EXISTS(
		SELECT * 
		FROM OperariosDisponibles
		WHERE IdEmpleado LIKE pOperario2) || pOperario2 LIKE '') THEN
			SET Operario2Vacio = 1; #SI EL OPERARIO 2 NO EXISTE O ESTA VACIO SE MARCA COMO TRUE
	END IF;

	#SI NINGUN OPERARIO FUE ENCONTRADO O ESTAN VACIOS NO SE PUEDE GENERAR LA SOLICITUD
	IF (Operario1Vacio = 1 AND Operario2Vacio = 1) THEN
		SET pError = 'Los operarios seleccionados no existen o no estan disponibles.';
   
		LEAVE alta;
	END IF;

    #-------------------COMPROBACION DE DISPONIBILIDAD DE GRUA------------------------------------------------------------
    IF NOT EXISTS(
		SELECT *
        FROM GruasDisponibles
        WHERE Numero = pNroGrua) THEN
			SET pError = 'La grúa seleccionada no esta disponible o no existe.';
            
            LEAVE alta;
	END IF;

    #--------------------COMPROBACION DE EXISTENCIA DE VEHICULO-----------------------------------------------------------
    IF NOT EXISTS(
		SELECT * 
        FROM Vehiculos
        WHERE Matricula = pMatricula) THEN
			SET pError = 'No existe un vehiculo con esa matricula.';
            
			LEAVE alta;
	END IF;

    #-------------------INICIO DE TRANSACCION DE ALTA------------------------------------------------------------
    SET transaccionActiva = 1;
    
    START TRANSACTION;

	SET mensajeError = 'No se pudo ingresar la solicitud de servicio';

	# SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Error de prueba.';

	INSERT INTO SolicitudesDeServicios (Direccion, Matricula, NroGrua) VALUES(pDireccion, pMatricula, pNroGrua);
	
	SET indice =(SELECT LAST_INSERT_ID()); #OBTENGO EL ULTIMO ID INGRESADO PARA ENVIAR A LA TABLA ASOCIADA

	SET mensajeError = 'No se pudo agregar él o los operarios para la solicitud.';

	# SIGNAL SQLSTATE '10000' SET MESSAGE_TEXT = 'Error de prueba.';

	IF (pOperario1 LIKE pOperario2) THEN 
		INSERT INTO OperariosAsignadosASolicitudes VALUES(pOperario1, indice);
		SET Operario1Vacio = 1;
		SET Operario2Vacio = 1; # SI EL OPERARIO 1 TIENE EL MISMO ID QUE EL 2 MARCO EN TRUE PARA NO HACER EL INSERT DOS VECES DEL MISMO OPERARIO

	ELSEIF(Operario1Vacio = 0) THEN
		INSERT INTO OperariosAsignadosASolicitudes VALUES(pOperario1, indice);
	END IF;

	IF(Operario2Vacio = 0) THEN
		INSERT INTO OperariosAsignadosASolicitudes VALUES(pOperario2, indice);
	END IF;

	COMMIT;

	SET transaccionActiva = 0;
END//

DELIMITER ;

/* PRUEBAS DE ALTAS SOLICITUDES DE SERVICIO

------------Error: OPERARIOS 1 Y 2 VACIOS-----------------------------------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  '', '', 11, @resultado);
SELECT @resultado;

------------Error: OPERARIOS 1 Y 2 NO EXISTEN O NO ESTAN DISPONIBLES--------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  'QQQ789', '', 2, @resultado);
SELECT @resultado;

------------Error: LA GRÚA NO ESTA DISPONIBLE O NO EXISTE-------------------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  'EEE456', 'EEE456', 88, @resultado);
SELECT @resultado;

------------Error: NO EXISTE UN VEHICULO CON ESA MATRICULA------------------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'ERRO104',  'EEE456', 'DDD123', 11, @resultado);
SELECT @resultado;

------------Error: NO SE PUDO INGRESAR LA SOLICITUD- (DESCOMENTAR SIGNAL STATE EN EL SP)------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  'AAA123', 'BBB456', 2, @resultado);
SELECT @resultado;

------------Error: NO SE PUDO INGRESAR LOS OPERARIOS VINCULADOS A LA SOLICITUD-(DESCOMENTAR SIGNAL STATE EN EL SP)-----------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  'AAA123', 'BBB456', 2, @resultado);
SELECT @resultado;

------------ALTA CORRECTA CON OPERARIO 1 Y EL 2 VACIO O INEXISTENTE----------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  'DDD123', '', 11, @resultado);
SELECT @resultado;

------------ALTA CORRECTA CON OPERARIO 2 Y EL 1 VACIO O INEXISTENTE----------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardeboxxxxx 1145', 'SAS1415',  '', 'FFF789', 11, @resultado);
SELECT @resultado;

------------ALTA CORRECTA CON OPERARIO 1 IGUAL A EL 2 -----------------------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('Vilardebo 1145', 'SAS1415',  'EEE456', 'EEE456', 16, @resultado);
SELECT @resultado;

------------ALTA CORRECTA CON LOS 2 OPERARIOS--------------------------------------------------------------
SET @resultado = '';
CALL AgregarSolicitudServicio('xxxxVilardebo 1145', 'SAS1415',  'EEE456', 'DDD123', 16, @resultado);
SELECT @resultado;
*/

DELIMITER // 

CREATE PROCEDURE CancelacionSolicitudServicio(pNumSerie INT, OUT pError VARCHAR(50))
cancelacion:BEGIN
	IF NOT EXISTS(
		SELECT * 
		FROM SolicitudesDeServicios
		WHERE NumSerie = pNumSerie AND Cancelado = 0) THEN
			SET pError = 'No existe la solicitud o ya fue cancelada';

			LEAVE cancelacion;
	END IF;

	IF EXISTS(
		SELECT *
		FROM Servicios
		WHERE pNumSerie = NumSolicitud) THEN
			SET pError = 'El servicio ya fue realizado';

			LEAVE cancelacion;
	END IF;

	UPDATE SolicitudesDeServicios
	SET Cancelado = 1
	WHERE NumSerie = pNumSerie;
	
END//

DELIMITER ;

/* PRUEBAS DE CANCELACION DE SOLICITUDES DE SERVICIO
------------Error: NO EXISTE UNA SOLICITUD CON ESE NÚMERO DE SERIE----------------------------------------
SET @resultado = '';
CALL CancelacionSolicitudServicio(10555, @resultado);
SELECT @resultado;
------------Error: LA SOLICITUD YA FUE CANCELADA ----------------------------------------
SET @resultado = '';
CALL CancelacionSolicitudServicio(456, @resultado);
SELECT @resultado;
------------Error: EL SERVICIO YA FUE REALIZADO-----------------------------------------------------------------
SET @resultado = '';
CALL CancelacionSolicitudServicio(222, @resultado);
SELECT @resultado;
------------Error: CANCELACION DE SOLICITUD CORRECTA--------------------------------------
SET @resultado = '';
CALL CancelacionSolicitudServicio(123, @resultado);
SELECT @resultado;
*/

CREATE PROCEDURE BuscarSolicitudDeServicio(pNumSerie INT)
	SELECT * 
	FROM SolicitudesDeServicios INNER JOIN OperariosAsignadosASolicitudes
		ON NumSerie =  NumSolicitud 
	WHERE NumSerie = pNumSerie AND NOT EXISTS(SELECT * FROM Servicios WHERE pNumSerie = NumSolicitud) AND Cancelado = 0 ;

/*************************************
************SP DE CLIENTES************
**************************************/

DELIMITER //

CREATE PROCEDURE AgregarCliente (pCedula BIGINT, pNombre VARCHAR(30), pDireccion VARCHAR(50), pTelefono BIGINT, OUT pMensajeError varchar(100))
altacliente:BEGIN
	IF EXISTS(SELECT * FROM Clientes WHERE Cedula = pCedula AND Eliminado = 0) THEN
		SET pMensajeError = 'Ya existe un cliente con ese nro. de cédula';
		LEAVE altacliente;
    END IF;
    
    IF EXISTS(SELECT * FROM Clientes WHERE Cedula = pCedula AND Eliminado = 1) THEN
		UPDATE Clientes 
        SET Nombre = pNombre, Direccion = pDireccion, Telefono = pTelefono, Eliminado = 0
        WHERE Cedula = pCedula;
    ELSE
		INSERT INTO Clientes(Cedula, Nombre, Direccion, Telefono)
		VALUES(pCedula, pNombre, pDireccion, pTelefono);
    END IF;
END//


CREATE PROCEDURE ModificarCliente (pCedula BIGINT, pNombre VARCHAR(30), pDireccion VARCHAR(50), pTelefono BIGINT, OUT pMensajeError varchar(100))
modificarcliente:BEGIN
	IF NOT EXISTS(SELECT * FROM Clientes WHERE Cedula = pCedula AND Eliminado = 0) THEN
		SET pMensajeError = 'No existe el cliente que quiere modificar';
		LEAVE modificarcliente;
    END IF;
    
    UPDATE Clientes
    SET Nombre = pNombre, Direccion = pDireccion, Telefono = pTelefono
    WHERE Cedula = pCedula;
    
END//


CREATE PROCEDURE EliminarCliente(pCedula LONG, OUT pMensajeError VARCHAR(100))
bajacliente:BEGIN

    DECLARE mensajeError VARCHAR(100);
    DECLARE transaccionActiva BIT;
 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		IF transaccionActiva THEN
			ROLLBACK;
        END IF;
		
		SET pMensajeError = mensajeError;
	END;


#SI EL CLIENTE NO EXISTE, MUESTRO ERRROR
IF NOT EXISTS(SELECT * FROM Clientes WHERE Cedula = pCedula AND Eliminado = 0) THEN
	SET pMensajeError = 'No existe un cliente con el número de cédula indicado';
    LEAVE bajacliente;
END IF;

#EN LA TRANSACCIÓN SE VE SI SE BORRA LÓGICA O FÍSICAMENTE EL CLIENTE
SET transaccionActiva = 1;

START TRANSACTION;

#SI EL CLIENTE TIENE SOLICITUDES DE SERVICIO, SE REALIZA BAJA LÓGICA
IF EXISTS (SELECT * FROM SolicitudesDeServicios SS INNER JOIN Vehiculos Veh  
 	ON SS.Matricula = Veh.Matricula WHERE Veh.CedulaCliente = pCedula)THEN  
            
		#baja lógica de los vehículos  
		UPDATE Vehiculos  
		SET Eliminado = 1  
        WHERE CedulaCliente = pCedula;  

        
		#baja lógica del cliente  
        UPDATE Clientes  
        SET Eliminado = 1  
        WHERE Cedula = pCedula;  
        COMMIT;  
ELSE
        #baja física de los vehículos  
        DELETE FROM Vehiculos  
		WHERE CedulaCliente = pCedula;  

		#baja fisica del cliente  
        DELETE FROM Clientes  
        WHERE Cedula = pCedula;  

		COMMIT;
    END IF;
    
    COMMIT;

	SET transaccionActiva = 0;

END//

/*************************************
************FIN SP DE CLIENTES********
**************************************/
 

/*--------------SP REMOLQUES-------------*/

DELIMITER //  
  
CREATE PROCEDURE AltaServicioRemolque(pImporteTotal INT, pNumSolicitud  INT,  pCantidadKm  INT, OUT pError VARCHAR(100))   
altaRemolque : BEGIN  
	DECLARE mensajeError VARCHAR(100);  
    DECLARE transaccionActiva BIT;  
	DECLARE indice INT;
  
    DECLARE EXIT HANDLER FOR SQLEXCEPTION  
    BEGIN  
		IF transaccionActiva THEN  
			ROLLBACK;  
         END IF;  
          
        SET pError = mensajeError;  
    END;                
  
    IF NOT EXISTS (  
		SELECT *   
        FROM SolicitudesDeServicios  
		WHERE NumSerie = pNumSolicitud) THEN  
			SET pError = 'No existe una solicitud de servicio con ese número de serie';  
              
            LEAVE altaRemolque;  
	END IF;  
  
    #ALTA SERVICIO DE REMOLQUE  
    SET transaccionActiva = 1;  
      
    START TRANSACTION;  
  
	SET mensajeError = 'Error! No se pudo ingresar el servicio';  
  
	INSERT INTO Servicios (ImporteTotal, NumSolicitud) VALUES(pImporteTotal, pNumSolicitud);  

	SET indice =(SELECT LAST_INSERT_ID());
  
	SET mensajeError = 'Error! La cantidad de Kms debe de ser mayor que 0';  
  
	IF (pCantidadKm > 0) THEN   
		INSERT INTO Remolques VALUES(indice, pCantidadKm);  
  
	END IF;  
  
	COMMIT;  
  
	SET transaccionActiva = 0;  
END//  
 
DELIMITER ;  

DELIMITER //

CREATE PROCEDURE AltaServicioAuxilioMecanico(pImporteTotal DOUBLE, pNumSolicitud INT, pDescripcionProblema VARCHAR (300), pDescripcionReparacion VARCHAR (300), pCostoRepuesto DOUBLE, OUT pError VARCHAR(100))
altaAuxilio : BEGIN
	DECLARE mensajeError VARCHAR(100);
    DECLARE transaccionActiva BIT;
	DECLARE indice INT;
    
	DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
		IF transaccionActiva THEN
			ROLLBACK;
        END IF;
        
        SET pError = mensajeError;
    END;

    IF NOT EXISTS (
		SELECT * 
        FROM SolicitudesDeServicios
		WHERE NumSerie = pNumSolicitud) THEN
			SET pError = 'No existe una solicitud de servicio con ese número de serie';
            
            LEAVE altaAuxilio;
	END IF;

    #ALTA DE AUXILIO MECANICO
    
    SET transaccionActiva = 1;
    
    START TRANSACTION;  
  
	SET mensajeError = 'Error! No se pudo ingresar el servicio';  
  
	INSERT INTO Servicios (ImporteTotal, NumSolicitud) VALUES(pImporteTotal, pNumSolicitud);  

	SET indice =(SELECT LAST_INSERT_ID());
  
	SET mensajeError = 'Error! El costo de la reparacion debe ser mayor que 0';  
  
	IF (pCostoRepuesto >= 0) THEN   
		INSERT INTO AuxiliosMecanicos VALUES(indice, pDescripcionProblema, pDescripcionReparacion, pCostoRepuesto);  
  
	END IF;  
  
	COMMIT;  
  
	SET transaccionActiva = 0;  
END//

DELIMITER ;  

#SET @resultado = '';
#CALL AgregarSolicitudServicio('Carlos Ott 1962', 'SAS1415', 'DDD123', 0,  8, @resultado);
#SELECT @resultado;


#SET @resultado = '';
#CALL AltaServicioRemolque(1535, 5, 340, @resultado);
#SELECT @resultado;

#SET @resultado = '';
#CALL AltaServicioAuxilioMecanico(1535, 5, 'NO FUNCIONA', 'SE ARREGLO', 0, @resultado);
#auxiliosmecanicosSELECT @resultado;

/*************************************
************SP DE VEHÍCULOS************
**************************************/

DELIMITER //

CREATE PROCEDURE AgregarVehiculo(pMatricula varchar(7), pMarca VARCHAR(20), pModelo VARCHAR(20), pPeso INT, pCliente BIGINT, OUT pMensajeError varchar(100))
altavehiculo:BEGIN
	IF EXISTS(SELECT * FROM Vehiculos  WHERE Matricula = pMatricula AND Eliminado = 0) THEN
		SET pMensajeError = 'Ya existe un vehículo con esa matrícula';
		LEAVE altavehiculo;
    END IF;
    
	IF NOT EXISTS(SELECT * FROM Clientes  WHERE Cedula = pCliente AND Eliminado = 0) THEN
		SET pMensajeError = 'El cliente ingresado no existe';
		LEAVE altavehiculo;
    END IF;
    
    IF EXISTS(SELECT * FROM Vehiculos WHERE Matricula = pMatricula AND Eliminado = 1) THEN
		UPDATE Vehiculos 
        SET Marca = pMarca, Modelo = pModelo, Peso = pPeso, Eliminado = 0, CedulaCliente = pCliente
        WHERE Matricula = matricula;
    ELSE
		INSERT INTO Vehiculos(Matricula, Marca, Modelo, Peso, CedulaCliente)
		VALUES(pMatricula, pMarca, pModelo, pPeso, pCliente);
    END IF;
END//

CREATE PROCEDURE ModificarVehiculo(pMatricula varchar(7), pMarca VARCHAR(20), pModelo VARCHAR(20), pPeso INT, pCliente BIGINT, OUT pMensajeError varchar(100))
modificarvehiculo:BEGIN
	IF NOT EXISTS(SELECT * FROM Vehiculos WHERE Matricula = pMatricula AND Eliminado = 0) THEN
		SET pMensajeError = 'No existe el vehículo que quiere modificar';
		LEAVE modificarvehiculo;
    END IF;
    
    IF NOT EXISTS(SELECT * FROM Clientes  WHERE Cedula = pCliente AND Eliminado = 0) THEN
		SET pMensajeError = 'El cliente ingresado no existe';
		LEAVE modificarvehiculo;
    END IF;
    
    UPDATE Vehiculos
    SET Marca = pMarca, Modelo = pModelo, Peso = pPeso, CedulaCliente = pCliente
    WHERE Matricula = pMatricula;
    
END//


CREATE PROCEDURE EliminarVehiculo(pMatricula VARCHAR(7), OUT pMensajeError VARCHAR(100))
bajavehiculo:BEGIN

    DECLARE mensajeError VARCHAR(100);
    DECLARE transaccionActiva BIT;
 
	DECLARE EXIT HANDLER FOR SQLEXCEPTION 
	BEGIN
		IF transaccionActiva THEN
			ROLLBACK;
        END IF;
		
		SET pMensajeError = mensajeError;
	END;


#SI EL VEHICULO NO EXISTE, MUESTRO ERRROR
IF NOT EXISTS(SELECT * FROM Vehiculos WHERE Matricula = pMatricula AND Eliminado = 0) THEN
		SET pMensajeError = 'No existe el vehículo que quiere modificar';
		LEAVE bajavehiculo;
    END IF;

#EN LA TRANSACCIÓN SE VE SI SE BORRA LÓGICA O FÍSICAMENTE EL VEHÍCULO
SET transaccionActiva = 1;

START TRANSACTION;

#SI EL VEHICULO TIENE SOLICITUDES DE SERVICIO, SE REALIZA BAJA LÓGICA
IF EXISTS (SELECT * FROM SolicitudesDeServicios WHERE Matricula = pMatricula)THEN  
            
		#baja lógica del vehículo
		UPDATE Vehiculos  
		SET Eliminado = 1  
        WHERE Matricula = pMatricula;  
ELSE
        #baja física del vehículo
        DELETE FROM Vehiculos  
		WHERE Matricula = pMatricula; 
    END IF;
    
    COMMIT;

	SET transaccionActiva = 0;

END//

/*************************************
************FIN SP DE VEHÍCULOS*******
**************************************/


/*************************************
************SP DE RECIBOS*************
**************************************/
CREATE PROCEDURE CalcularCostosServiciosPorCliente() 
#Devuelve el importe por cliente para los servicios del mes previo al actual. 
#A esto se le sumará luego la mensualidad para calcular el recibo
BEGIN
	SELECT V.CedulaCliente, SUM(ImporteTotal) AS TotalPorServicios, NOW() AS fechaRecibo
	FROM Servicios S INNER JOIN SolicitudesDeServicios SS
		ON S.NumSolicitud = SS.NumSerie INNER JOIN Vehiculos V
			ON SS.Matricula = V.Matricula
	WHERE MONTH(SS.Fecha) = (MONTH(NOW())-1) AND V.CedulaCliente NOT IN(SELECT CedulaCliente FROM Recibos WHERE MONTH(Fecha) = MONTH(NOW()))
	GROUP BY V.CedulaCliente;
END//

DELIMITER ;


/*****************************************
************ DATOS DE PRUEBA *************
*****************************************/

INSERT INTO Gruas VALUES(1, 3000, 0);
INSERT INTO Gruas VALUES(2, 2500, 0);
INSERT INTO Gruas VALUES(3, 4500, 0);
INSERT INTO Gruas VALUES(4, 3500, 1);
INSERT INTO Gruas VALUES(5, 1500, 1);
INSERT INTO Gruas VALUES(11, 3500, 0);
INSERT INTO Gruas VALUES(16, 1500, 0);
INSERT INTO Gruas VALUES(6, 3500, 0);
INSERT INTO Gruas VALUES(7, 1500, 0);
INSERT INTO Gruas VALUES(8, 3500, 0);
INSERT INTO Gruas VALUES(9, 1500, 0);

INSERT INTO Operarios VALUES('AAA123', 'Chuck Norris', '2016/5/19', 0);
INSERT INTO Operarios VALUES('BBB456', 'Ramón Valdés', '2015/7/22', 0);
INSERT INTO Operarios VALUES('CCC789', 'Carl Sagan', '2015/8/01', 0);
INSERT INTO Operarios VALUES('DDD123', 'Clint Eastwood', '2014/4/08', 0);
INSERT INTO Operarios VALUES('EEE456', 'Sr. Burns', '2013/11/18', 0);
INSERT INTO Operarios VALUES('FFF789', 'Philip J. Fry', '2035/5/22', 0);

INSERT INTO Clientes VALUES(11111111, 'juan', 'Un lugar', 123456, 0); 
INSERT INTO Clientes VALUES(22222222, 'Alguien', 'Un lugar', 555555, 0);
INSERT INTO Clientes VALUES(33333333, 'Otro', 'Un lugar', 6543321, 0);
INSERT INTO Clientes VALUES(44444444, 'Pedro', 'Un lugar', 162435, 0);

INSERT INTO Vehiculos VALUES('SAS1415', 'Audi', 'V6', 2000, 11111111, 0); 
INSERT INTO Vehiculos VALUES('SAS2030', 'Volkswagen', 'Gol', 2200, 22222222, 0); 
INSERT INTO Vehiculos VALUES('SAS2005', 'Citroen', 'DS3', 1400, 22222222, 0); 
INSERT INTO Vehiculos VALUES('SAS6666', 'Fiat', 'Uno', 1700, 33333333, 0); 
INSERT INTO Vehiculos VALUES('SAR0001', 'Chevrolet', 'Corsa', 1700, 44444444, 0);
 
INSERT INTO SolicitudesDeServicios (Direccion, Matricula, NroGrua) VALUES('Avda. Artigas 5541', 'SAS1415', 1);
INSERT INTO SolicitudesDeServicios (Direccion, Matricula, NroGrua) VALUES('18 de Julio 5541', 'SAS1415', 3); 
INSERT INTO SolicitudesDeServicios (Fecha, Direccion, Matricula, NroGrua) VALUES('2016/06/30', 'Vilardebo 1145', 'SAS1415', 2);
INSERT INTO SolicitudesDeServicios (Fecha, Direccion, Cancelado, Matricula, NroGrua)  VALUES('2016/08/30', 'Avda. Simepre viva 1145', 1, 'SAS1415', 2);

INSERT INTO OperariosAsignadosASolicitudes VALUES('CCC789', 1);
INSERT INTO OperariosAsignadosASolicitudes VALUES('BBB456', 2);
INSERT INTO OperariosAsignadosASolicitudes VALUES('AAA123', 3);
INSERT INTO OperariosAsignadosASolicitudes VALUES('FFF789', 4);
INSERT INTO OperariosAsignadosASolicitudes VALUES('CCC789', 4);

INSERT INTO Servicios VALUES(1, 5000, 4);
INSERT INTO Servicios VALUES(2, 2500, 3);

