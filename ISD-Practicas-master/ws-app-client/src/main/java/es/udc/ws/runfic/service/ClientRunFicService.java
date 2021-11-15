package es.udc.ws.runfic.service;

import es.udc.ws.runfic.service.dto.ClientInscriptionDto;
import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.runfic.service.dto.ServerException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ClientRunFicService {
    //Dar de alta una carrera
    Long addRace(ClientRaceDto race) throws InputValidationException;

    //Buscar una carrera por su identificador.
    ClientRaceDto findRace(Long raceID) throws InstanceNotFoundException;

    // Será posible buscar carreras que se celebren antes de una fecha (debe ser una fecha futura y el resultado contendrá únicamente las carreras
    //que aún no se han celebrado) y una ciudad.
    List<ClientRaceDto> findByDate(LocalDate date, String city) throws InputValidationException;

    //Será posible que un usuario se inscriba en una carrera hasta 24 horas antes de su celebración. Además de otros parámetros que puedan ser
    //necesarios, recibe como entrada un e-mail para identificar al usuario, y un número de tarjeta de crédito. En caso de ejecutarse con éxito,
    //devuelve un código que será necesario para recoger el dorsal, y se almacena la inscripción, quedando registrado el número de dorsal
    //asignado al participante y la fecha y hora a la que se hizo la inscripción
    ClientInscriptionDto inscribe(Long raceID, String email, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ServerException;

    //Será posible obtener todas las inscripciones que un usuario ha realizado a lo largo del tiempo.
    List<ClientInscriptionDto> findAllFromUser(String email) throws InputValidationException;

    //Será posible indicar que un usuario ha recogido el dorsal correspondiente a una inscripción. Un usuario recoge el dorsal correspondiente
    //a una inscripción en el local de RunFic, presentando el código o identificador obtenido al realizar la inscripción y el número de tarjeta
    //de crédito utilizada para pagarla. A partir de esos datos, un empleado de RunFic podrá indicar que el dorsal correspondiente a esa inscripción
    //se ha entregado
    int getRunnerNumber(Long inscriptionID, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ServerException;
}
