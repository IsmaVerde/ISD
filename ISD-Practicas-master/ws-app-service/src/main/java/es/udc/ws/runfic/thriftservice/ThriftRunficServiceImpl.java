package es.udc.ws.runfic.thriftservice;

import es.udc.ws.runfic.model.inscription.Inscription;
import es.udc.ws.runfic.model.race.Race;
import es.udc.ws.runfic.model.runservice.RunService;
import es.udc.ws.runfic.model.runservice.RunServiceFactory;
import es.udc.ws.runfic.model.runservice.exceptions.*;
import es.udc.ws.runfic.rest.json.JsonToExceptionConversor;
import es.udc.ws.runfic.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.ServletUtils;
import org.apache.thrift.TException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ThriftRunficServiceImpl implements ThriftRunficService.Iface{

    @Override
    public long addRace(String city, String description, String startDateTime, double price, int maxParticipants ) throws ThriftInputValidationException {
        try {
            return RunServiceFactory.getService().addRace(city, description, LocalDateTime.parse(startDateTime), (float) price, maxParticipants).getRaceID();
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftRaceDto findRace(long raceID) throws ThriftInstanceNotFoundException, TException {
        try {
            Race race = RunServiceFactory.getService().findRace(raceID);
            return RaceToThriftRaceDtoConversor.toThriftRaceDto(race);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public List<ThriftRaceDto> findByDate(String date, String city){
        LocalDate finalDate = LocalDate.parse(date);
        List<Race> races;
        races = RunServiceFactory.getService().findByDate(finalDate, city);
        return RaceToThriftRaceDtoConversor.toThriftRaceDtos(races);
    }

    @Override
    public ThriftInscriptionDto inscribe(long raceID, String email, String ccn) throws ThriftInputValidationException, ThriftInscriptionClosedException, ThriftInstanceNotFoundException, ThriftRaceFullException, ThriftAlreadyInscribedException, TException{
        try{
            Inscription returned = RunServiceFactory.getService().inscribe(raceID, email, ccn);
            return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDto(returned);
        } catch (InscriptionClosedException | AlreadyInscribedException | RaceFullException | InputValidationException e) {
            throw new ThriftInscriptionClosedException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType());
        }
    }

    @Override
    public List<ThriftInscriptionDto> findAllFromUser(String email) throws ThriftInputValidationException, TException{
        try {
            List<Inscription> found = RunServiceFactory.getService().findAllFromUser(email);
            return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDto(found);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public ThriftInscriptionDto getRunnerNumber(long inscriptionID, String creditCardNumber) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftNumberTakenException, ThriftInvalidUserException, TException{
        try{
            Inscription inscription = RunServiceFactory.getService().getRunnerNumber(inscriptionID, creditCardNumber);
            return InscriptionToThriftInscriptionDtoConversor.toThriftInscriptionDto(inscription);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (NumberTakenException e) {
            throw new ThriftNumberTakenException(e.getMessage());
        } catch (InvalidUserException e) {
            throw new ThriftInvalidUserException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(), e.getInstanceType());
        }
    }
}
