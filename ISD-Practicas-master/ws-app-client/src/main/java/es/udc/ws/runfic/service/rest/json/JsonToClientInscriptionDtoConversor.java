package es.udc.ws.runfic.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.runfic.service.dto.ClientInscriptionDto;
import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientInscriptionDtoConversor {
    public static ObjectNode toObjectNode(ClientInscriptionDto inscription){
        throw new UnsupportedOperationException();
    }

    public static ClientInscriptionDto toClientInscriptionDto(InputStream jsonInscription) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonInscription);
            return toClientInscriptionDto(rootNode);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientInscriptionDto toClientInscriptionDto(JsonNode jsonNode) throws ParsingException{
        try{
            if (jsonNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (expected Object), got " + jsonNode.getNodeType().toString());
            }

            ObjectNode inscriptionObject = (ObjectNode) jsonNode;

            Long inscriptionID = inscriptionObject.get("inscriptionID").longValue(); //Si no está, es null
            String user = inscriptionObject.get("user").textValue().trim();
            String creditCardNumber = inscriptionObject.get("creditCardNumber").textValue().trim();
            Long raceID = inscriptionObject.get("raceID").longValue();
            int runnerNumber = inscriptionObject.get("runnerNumber").intValue();
            boolean isNumberTaken = inscriptionObject.get("isNumberTaken").booleanValue();

            return new ClientInscriptionDto(inscriptionID, user, creditCardNumber, raceID, runnerNumber, isNumberTaken);

        } catch (ParsingException ex){
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientInscriptionDto> toClientInscriptionDtos(InputStream content) throws ParsingException{
        try{
            JsonNode rootNode = ObjectMapperFactory.instance().readTree(content);

            if (rootNode.getNodeType() != JsonNodeType.ARRAY){
                throw new ParsingException("Unrecognised JSON (expected ARRAY), got "+rootNode.getNodeType().toString());
            }
            ArrayNode inscriptionArray = (ArrayNode) rootNode;
            List<ClientInscriptionDto> inscriptionDtoList = new ArrayList<>();

            for (JsonNode jsonNode : inscriptionArray) {
                inscriptionDtoList.add(toClientInscriptionDto(jsonNode));
            }

            return inscriptionDtoList;

        }catch (ParsingException ex){
            throw ex;
        }catch (Exception e){
            throw new ParsingException(e);
        }
    }
}
