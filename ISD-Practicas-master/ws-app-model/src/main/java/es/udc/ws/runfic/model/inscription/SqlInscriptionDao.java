package es.udc.ws.runfic.model.inscription;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlInscriptionDao {
    //Persiste una inscripción a la BBDD. El método devuelve un objeto Inscription con sus atributos correctamente inicializados
    Inscription create(Connection connection, Inscription inscription);

    //Recuperar una inscripción de la BBDD
    Inscription find(Connection connection, Long inscriptionID) throws InstanceNotFoundException;

    //Recuperar todas las inscripciones de un usuario
    List<Inscription> findByUser(Connection connection, String email);

    //Actualizar una inscripción
    public int update(Connection connection, Inscription newInscription);

    //Carlos
    /*
    Checks if a user is already inscribed in the given race
     */
    boolean isUserInscribed(Connection connection, Long raceID, String user);

    //Borrar una inscripción según su ID
    int remove(Connection connection, Long inscriptionID);
}
