package es.udc.ws.runfic.thriftservice;

import es.udc.ws.runfic.model.inscription.Inscription;
import es.udc.ws.runfic.thrift.ThriftInscriptionDto;

import java.util.ArrayList;
import java.util.List;

public class InscriptionToThriftInscriptionDtoConversor {
    public static ThriftInscriptionDto toThriftInscriptionDto(Inscription modelIns){
        return new ThriftInscriptionDto(modelIns.getInscriptionID(), modelIns.getUser(),
                modelIns.getCreditCardNumber(), modelIns.getRaceID(),
                modelIns.getRunnerNumber(), modelIns.isNumberTaken());
    }

    public static Inscription toInscription(ThriftInscriptionDto ThriftIns){
        return new Inscription(ThriftIns.getInscriptionID(), ThriftIns.getUser(), ThriftIns.getCreditCardNumber(), ThriftIns.getRaceID(),
                null, ThriftIns.getRunnerNumber(), ThriftIns.isIsNumberTaken());
    }

    public static List<ThriftInscriptionDto> toThriftInscriptionDto(List<Inscription> found) {
        List<ThriftInscriptionDto> newlist = new ArrayList<>();
        for (Inscription insc : found) {
            newlist.add(toThriftInscriptionDto(insc));
        }
        return newlist;
    }
}
