package es.udc.ws.runfic.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientRaceDtoConversor {
    //Isma
    public static ObjectNode toObjectNode(ClientRaceDto race) throws IOException {
        ObjectNode raceObject = JsonNodeFactory.instance.objectNode();

        if (race.getRaceID() != null) {
            raceObject.put("raceID", race.getRaceID());
        }
        raceObject.put("city", race.getCity()).
                put("description", race.getDescription()).
                put("startDateTime", race.getStartDateTime().toString()).
                put("price", race.getPrice()).
                put("participants", race.getParticipants()).
                put("maxParticipants", race.getMaxParticipants());

        return raceObject;
    }

    public static ClientRaceDto toClientRaceDto(InputStream jsonRace) throws ParsingException{
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientRaceDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientRaceDto> toClientRaceDtos(InputStream jsonRace) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonRace);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode racesArray = (ArrayNode) rootNode;
                List<ClientRaceDto> raceDtos = new ArrayList<>(racesArray.size());
                for (JsonNode raceNode : racesArray) {
                    raceDtos.add(toClientRaceDto(raceNode));
                }

                return raceDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientRaceDto toClientRaceDto(JsonNode raceNode) throws ParsingException {
        if (raceNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode raceObject = (ObjectNode) raceNode;

            JsonNode raceIdNode = raceObject.get("raceId");
            Long raceId = (raceIdNode != null) ? raceIdNode.longValue() : null;

            String city = raceObject.get("city").textValue().trim();
            String description = raceObject.get("description").textValue().trim();
            String strStartDateTime =  raceObject.get("startDateTime").textValue().trim();
            LocalDateTime startDateTime = LocalDateTime.parse(strStartDateTime);
            float price = raceObject.get("price").floatValue();
            int participants = raceObject.get("participants").intValue();
            int maxParticipants = raceObject.get("maxParticipants").intValue();

            return new ClientRaceDto(raceId, city, description, startDateTime, price,
                    participants, maxParticipants);
        }
    }

}
