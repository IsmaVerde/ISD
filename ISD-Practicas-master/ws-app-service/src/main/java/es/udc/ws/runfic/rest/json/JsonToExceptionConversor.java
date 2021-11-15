package es.udc.ws.runfic.rest.json;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import es.udc.ws.runfic.model.runservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class JsonToExceptionConversor {
    private JsonToExceptionConversor(){}

    public static ObjectNode toInputValidationException(InputValidationException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InputValidation");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInstanceNotFoundException(InstanceNotFoundException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InstanceNotFound");
        exceptionObject.put("instanceId", (ex.getInstanceId() != null) ?
                ex.getInstanceId().toString() : null);
        exceptionObject.put("instanceType",
                ex.getInstanceType().substring(ex.getInstanceType().lastIndexOf('.') + 1));

        return exceptionObject;
    }

    public static ObjectNode toAlreadyInscribedException(AlreadyInscribedException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "AlreadyInscribed");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInscriptionClosedException(InscriptionClosedException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InscriptionClosed");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toInvalidUserException(InvalidUserException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "InvalidUser");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toNumberTakenException(NumberTakenException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NumberTaken");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }

    public static ObjectNode toRaceFullException(RaceFullException ex){

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "RaceFull");
        exceptionObject.put("message", ex.getMessage());

        return exceptionObject;
    }
}
