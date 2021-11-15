package es.udc.ws.runfic.model.runservice;

import es.udc.ws.runfic.model.inscription.Inscription;
import es.udc.ws.runfic.model.race.Race;
import es.udc.ws.runfic.model.runservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface RunService {
    //Para dar de alta una carrera, se indicará la ciudad donde se celebra, una descripción, la fecha y hora, el  precio de la inscripción
    //y el número máximo de participantes. Además, se guardará la fecha y hora en la que se ha dado de alta la carrera.
    Race addRace(String city, String description, LocalDateTime startDateTime, float price, int maxParticipants)
            throws InputValidationException;

    //Será posible buscar carreras por su identificador. La información devuelta de la carrera incluirá, además de la información proporcionada
    //al darla de alta, el número de inscritos en ella
    Race findRace(Long raceID) throws InstanceNotFoundException;

    // Será posible buscar carreras que se celebren antes de una fecha (debe ser una fecha futura y el resultado contendrá únicamente las carreras
    //que aún no se han celebrado). Opcionalmente, se podrá indicar el nombre de una ciudad, en cuyo caso se devolverán solamente las carreras que se
    //celebren en esa ciudad. Al igual que en el punto anterior, la información devuelta de cada carrera incluirá, además de la información
    //proporcionada al darla de alta, el número de inscritos en ella
    List<Race> findByDate(LocalDate date, String city);

    //Será posible que un usuario se inscriba en una carrera hasta 24 horas antes de su celebración. Además de otros parámetros que puedan ser
    //necesarios, recibe como entrada un e-mail para identificar al usuario, y un número de tarjeta de crédito. En caso de ejecutarse con éxito,
    //devuelve un código que será necesario para recoger el dorsal, y se almacena la inscripción, quedando registrado el número de dorsal
    //asignado al participante y la fecha y hora a la que se hizo la inscripción
    Inscription inscribe(Long raceID, String email, String creditCardNumber) throws InputValidationException, InscriptionClosedException, InstanceNotFoundException, RaceFullException, AlreadyInscribedException;

    //Será posible obtener todas las inscripciones que un usuario ha realizado a lo largo del tiempo. Deben devolverse todos los datos
    //almacenados para cada inscripción
    List<Inscription> findAllFromUser(String email) throws InputValidationException;

    //Será posible indicar que un usuario ha recogido el dorsal correspondiente a una inscripción. Un usuario recoge el dorsal correspondiente
    //a una inscripción en el local de RunFic, presentando el código o identificador obtenido al realizar la inscripción y el número de tarjeta
    //de crédito utilizada para pagarla. A partir de esos datos, un empleado de RunFic podrá indicar que el dorsal correspondiente a esa inscripción
    //se ha entregado. Es necesario contemplar todos los posibles casos de error, como que el código de inscripción y el número de tarjeta no se
    //correspondan con ninguna inscripción, o que el dorsal correspondiente a esa inscripción ya ha sido entregado previamente
    Inscription getRunnerNumber(Long inscriptionID, String creditCardNumber) throws InputValidationException,
            InstanceNotFoundException, NumberTakenException, InvalidUserException;
}
