package es.udc.ws.runfic.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.InvalidStackFrameException;
import es.udc.ws.runfic.service.ClientRunFicService;
import es.udc.ws.runfic.service.dto.ClientInscriptionDto;
import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.runfic.service.dto.ServerException;
import es.udc.ws.runfic.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.runfic.service.rest.json.JsonToClientInscriptionDtoConversor;
import es.udc.ws.runfic.service.rest.json.JsonToClientRaceDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RestClientRunFicService implements ClientRunFicService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientRunFicService.endpointAddress";
    private String endpointAddress = null;

    @Override
    //Caso de Uso 1 - Brais
    public Long addRace(ClientRaceDto race) throws InputValidationException {
        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "/race").
                    bodyStream(toInputStream(race), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientRaceDtoConversor.toClientRaceDto(response.getEntity().getContent()).getRaceID();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    //Caso de Uso 2 - Isma
    public ClientRaceDto findRace(Long raceID) throws InstanceNotFoundException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "/race/" + raceID)
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRaceDtoConversor.toClientRaceDto(
                    response.getEntity().getContent());

        } catch (InstanceNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    //Caso de Uso 3 - Brais
    public List<ClientRaceDto> findByDate(LocalDate date, String city) throws InputValidationException {
        try {
            if (date == null) {
                throw new InputValidationException("Date can not be null");
            }
            if ((city == null) || (city.strip() == "")) {
                throw new InputValidationException("City can not be null nor empty");
            }

            HttpResponse response = null;
            String dateStr = date.toString();

            String requestStr = getEndpointAddress() + "/race?date="
                    + URLEncoder.encode(dateStr, "UTF-8")
                    + "&city=" + URLEncoder.encode(city, "UTF-8");

            response = Request.Get(requestStr).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientRaceDtoConversor.toClientRaceDtos(
                    response.getEntity().getContent());
        } catch (InputValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    //Caso de Uso 4 - Carlos
    public ClientInscriptionDto inscribe(Long raceID, String email, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ServerException {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "/inscription")
                    .bodyForm(
                            Form.form()
                                    .add("raceID", raceID.toString())
                                    .add("user", email)
                                    .add("creditCard", creditCardNumber)
                                    .build()
                    )
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(
                    response.getEntity().getContent());

        }catch(InputValidationException | InstanceNotFoundException | ServerException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    //Caso de Uso 5 - Carlos
    public List<ClientInscriptionDto> findAllFromUser(String email) throws InputValidationException {
        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "/inscription/?user="
                    + URLEncoder.encode(email, "UTF-8"))
                    .execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDtos(
                    response.getEntity().getContent()
            );
        } catch (InputValidationException ex){
            throw ex;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    //Caso de Uso 6 - Isma
    public int getRunnerNumber(Long inscriptionID, String creditCardNumber) throws InputValidationException, InstanceNotFoundException, ServerException {
        try {
            HttpResponse response = Request.Post(
                    getEndpointAddress() + "/inscription/" + inscriptionID.toString())
                    .bodyForm(Form.form()
                            .add("creditCardNumber", creditCardNumber)
                            .build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientInscriptionDtoConversor.toClientInscriptionDto(response.getEntity().getContent()).getRunnerNumber();

        }catch(InputValidationException | InstanceNotFoundException | ServerException e){
            throw e;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private InputStream toInputStream(ClientRaceDto race) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientRaceDtoConversor.toObjectNode(race));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager
                    .getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_GONE:
                    throw JsonToClientExceptionConversor.fromGoneErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
