package es.udc.ws.runfic.rest.servlets;

import es.udc.ws.runfic.model.inscription.Inscription;
import es.udc.ws.runfic.model.runservice.RunServiceFactory;
import es.udc.ws.runfic.model.runservice.exceptions.*;
import es.udc.ws.runfic.rest.dto.InscriptionToRestInscriptionDtoConversor;
import es.udc.ws.runfic.rest.dto.RestInscriptionDto;
import es.udc.ws.runfic.rest.json.JsonToExceptionConversor;
import es.udc.ws.runfic.rest.json.JsonToRestInscriptionDtoConversor;
import es.udc.ws.runfic.model.utils.RunficPropertyValidator;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.util.servlet.ServletUtils;
import es.udc.ws.util.validation.PropertyValidator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static es.udc.ws.runfic.model.utils.ModelConstants.MAX_PRICE;

public class InscriptionServlet extends HttpServlet {
    //Carlos
    //Corresponde al Caso de Uso 5 - findAllFromUser
    //GET /inscription?user=email
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if(path != null && path.length() > 0){
            //Si hay algo tras /inscription -> Bad Request
            ServletUtils.writeServiceResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(new InputValidationException("Invalid arguments " + path)),
                    null);
            return;
        }
        String email = req.getParameter("user");
        List<Inscription> modelInss = null;
        try {
            modelInss = RunServiceFactory.getService().findAllFromUser(email);
            List<RestInscriptionDto> restInss = new ArrayList<>();
            for(Inscription ins: modelInss){
                restInss.add(InscriptionToRestInscriptionDtoConversor.toRestInscriptionDto(ins));
            }

            ServletUtils.writeServiceResponse(resp,
                    HttpServletResponse.SC_OK,
                    JsonToRestInscriptionDtoConversor.toArrayNode(restInss),
                    null);
        } catch (InputValidationException e) {
            ServletUtils.writeServiceResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(e),
                    null);
            return;
        }
    }

    //Para CU 4 - POST a /inscription
    //Para CU 6 - POST a /inscription/id&creditCardNumber=ccn
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = ServletUtils.normalizePath(req.getPathInfo());
        if ((path != null) && (path.length() > 0)) {
            //Un POST contra una inscripción concreta podría ser para obtener el dorsal
            doGetRunnerRunnerNumber(req, resp);
        } else {
            doCreateInscription(req, resp);
        }
    }

    //Carlos
    //Corresponde al Caso de Uso 4 - inscribe
    private void doCreateInscription(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //Si estamos en este punto, es porque la petición fue exactamente "POST /inscription", sin nada más
        try {
            String inputRaceIDStr = req.getParameter("raceID");
            if (inputRaceIDStr == null) {
                throw new InputValidationException("RaceID cannot be null nor empty");
            }
            String inputEmail = req.getParameter("user");
            if (inputEmail == null) {
                throw new InputValidationException("Email cannot be null nor empty");
            }
            String inputCreditCard = req.getParameter("creditCard");
            if (inputCreditCard == null) {
                throw new InputValidationException("Credit Card Number cannot be null nor empty");
            }
            Long inputRaceID = Long.parseLong(inputRaceIDStr);

            Inscription modelIns = RunServiceFactory.getService().inscribe(inputRaceID, inputEmail, inputCreditCard);
            RestInscriptionDto inscriptionDto = InscriptionToRestInscriptionDtoConversor.toRestInscriptionDto(modelIns);

            String inscriptionURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + inscriptionDto.getInscriptionID();
            Map<String, String> headers = new HashMap<>(1);
            headers.put("Location", inscriptionURL);

            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                    JsonToRestInscriptionDtoConversor.toObjectNode(inscriptionDto), headers);
        } catch (NumberFormatException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(
                            new InputValidationException(ex.getLocalizedMessage())), null);
        } catch (InscriptionClosedException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                JsonToExceptionConversor.toInscriptionClosedException(e), null);
        } catch (InstanceNotFoundException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                JsonToExceptionConversor.toInstanceNotFoundException(e), null);
        } catch (AlreadyInscribedException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                JsonToExceptionConversor.toAlreadyInscribedException(e), null);
        } catch (InputValidationException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                JsonToExceptionConversor.toInputValidationException(e), null);
        } catch (RaceFullException e) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_GONE,
                JsonToExceptionConversor.toRaceFullException(e), null);
        }
    }

    //Isma
    //Corresponde al Caso de Uso 6 - getRunnerNumber
    private void doGetRunnerRunnerNumber(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //Inscription ID Checking
        String path = ServletUtils.normalizePath(req.getPathInfo());
        String inscIdAsString = path.substring(1);
        Long inscriptionID = null;
        try {
            inscriptionID = Long.parseLong(inscIdAsString);
        } catch (NumberFormatException ex) {
            ServletUtils
                    .writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                            JsonToExceptionConversor.toInputValidationException(new InputValidationException(
                                    "Invalid Request: " + "parameter 'InscriptionID' is invalid '" + inscIdAsString + "'")),
                            null);
            return;
        }

        //CreditCardNumber checking
        String ccn = req.getParameter("creditCardNumber");
        try{
            if (ccn == null){
                throw new InputValidationException("Parameter Credit Card Number is invalid: " + ccn);
            }
            PropertyValidator.validateCreditCard(ccn);
        }
        catch (InputValidationException ex){
            ServletUtils.writeServiceResponse(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(ex),
                    null);
            return;
        }

        Inscription inscription;
        RestInscriptionDto inscriptionDto;
        try {
            inscription = RunServiceFactory.getService().getRunnerNumber(inscriptionID, ccn);
            inscriptionDto = InscriptionToRestInscriptionDtoConversor.toRestInscriptionDto(inscription);

            //If everything went just fine, send an OK
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                    JsonToRestInscriptionDtoConversor.toObjectNode(inscriptionDto), null);

        } catch (InstanceNotFoundException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND,
                    JsonToExceptionConversor.toInstanceNotFoundException(ex), null);
        } catch (InputValidationException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInputValidationException(ex), null);
        } catch (InvalidUserException ex){
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toInvalidUserException(ex), null);
        } catch(NumberTakenException ex) {
            ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_BAD_REQUEST,
                    JsonToExceptionConversor.toNumberTakenException(ex), null);
        }
    }
}
