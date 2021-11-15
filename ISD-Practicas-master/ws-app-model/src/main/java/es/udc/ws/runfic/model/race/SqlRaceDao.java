package es.udc.ws.runfic.model.race;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlRaceDao {
    //Persiste una carrera a la Base de Datos. El método devuelve un objeto Race con el atributo raceID inicializado al valor correcto.
    Race create(Connection connection, Race race);

    //Encuentra una carrera según el ID
    Race find(Connection connection, Long raceID) throws InstanceNotFoundException;

    //Encuentra una carrera según fecha y opcionalmente ciudad
    List<Race> findByDateCity(Connection connection, LocalDate date, String city);

    //Actualiza los datos de una carrera
    int update(Connection connection, Race newrace);

    //Borra una carrera por su ID
    int remove(Connection connection, Long raceID);
}
