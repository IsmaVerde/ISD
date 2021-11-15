package es.udc.ws.runfic.rest.dto;

import es.udc.ws.runfic.model.inscription.Inscription;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToRestInscriptionDtoConversor {
    public static RestInscriptionDto toRestInscriptionDto(Inscription modelIns){
        return new RestInscriptionDto(modelIns.getInscriptionID(), modelIns.getUser(),
                modelIns.getCreditCardNumber(), modelIns.getRaceID(),
                modelIns.getRunnerNumber(), modelIns.isNumberTaken());
    }

    public static Inscription toInscription(RestInscriptionDto restIns){
        return new Inscription(restIns.getInscriptionID(), restIns.getUser(), restIns.getCreditCardNumber(), restIns.getRaceID(),
                null, restIns.getRunnerNumber(), restIns.isNumberTaken());
    }
}
