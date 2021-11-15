package es.udc.ws.runfic.service.thrift;

import es.udc.ws.runfic.service.ClientRunFicService;
import es.udc.ws.runfic.service.dto.ClientInscriptionDto;
import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.runfic.service.dto.ServerException;
import es.udc.ws.runfic.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientRunFicService implements ClientRunFicService {
    
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientRunFicService.endpointAddress";
    
    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
    
    @Override
    public Long addRace(ClientRaceDto race) throws InputValidationException {
        ThriftRunficService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return client.addRace(
                    race.getCity(),
                    race.getDescription(),
                    race.getStartDateTime().toString(),
                    (double) race.getPrice(),
                    race.getMaxParticipants()
            );
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientRaceDto findRace(Long raceID) throws InstanceNotFoundException {
        ThriftRunficService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            ThriftRaceDto race = client.findRace(raceID);
            return ClientRaceDtoToThriftRaceDtoConversor.toClientRaceDto(race);
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientRaceDto> findByDate(LocalDate date, String city) throws InputValidationException {
        if ((city == null) || (city.strip().equals(""))){
            throw new InputValidationException("City is a mandatory parameter");
        }

        ThriftRunficService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return ClientRaceDtoToThriftRaceDtoConversor.toClientRaceDto(client.findByDate(date.toString(), city));
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ClientInscriptionDto inscribe(Long raceID, String email, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ServerException {
        ThriftRunficService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            ThriftInscriptionDto tinsc = client.inscribe(raceID, email, creditCardNumber);
            return ClientInscriptionDtoToThriftInscriptionDtoConversor.toClientInscriptionDto(tinsc);
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (ThriftRaceFullException | ThriftAlreadyInscribedException | ThriftInscriptionClosedException e) {
            throw new ServerException(e.getLocalizedMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientInscriptionDto> findAllFromUser(String email) throws InputValidationException {
        ThriftRunficService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            List<ThriftInscriptionDto> found = client.findAllFromUser(email);
            return ClientInscriptionDtoToThriftInscriptionDtoConversor.toClientInscriptionDto(found);
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getRunnerNumber(Long inscriptionID, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ServerException {
        ThriftRunficService.Client client = getClient();

        try (TTransport transport = client.getInputProtocol().getTransport()) {
            transport.open();
            return client.getRunnerNumber(inscriptionID, creditCardNumber).getRunnerNumber();
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInvalidUserException | ThriftNumberTakenException e) {
            throw new ServerException(e.getLocalizedMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    private ThriftRunficService.Client getClient(){
        try{
            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftRunficService.Client(protocol);
        } catch (TTransportException e){
            throw new RuntimeException(e);
        }
    }
}
