package es.udc.ws.runfic.service.thrift;

import es.udc.ws.runfic.service.dto.ClientInscriptionDto;
import es.udc.ws.runfic.thrift.ThriftInscriptionDto;
import es.udc.ws.runfic.thrift.ThriftRunficService;

import java.util.ArrayList;
import java.util.List;

public class ClientInscriptionDtoToThriftInscriptionDtoConversor {
    public static ThriftInscriptionDto toThriftInscriptionDto(ClientInscriptionDto clientDto){
        return new ThriftInscriptionDto(
                clientDto.getInscriptionID(),
                clientDto.getUser(),
                clientDto.getCreditCardNumber(),
                clientDto.getRaceID(),
                clientDto.getRunnerNumber(),
                clientDto.isNumberTaken()
        );
    }

    public static ClientInscriptionDto toClientInscriptionDto(ThriftInscriptionDto thriftDto){
        return new ClientInscriptionDto(
                thriftDto.getInscriptionID(),
                thriftDto.getUser(),
                thriftDto.getCreditCardNumber(),
                thriftDto.getRaceID(),
                thriftDto.getRunnerNumber(),
                thriftDto.isIsNumberTaken()
        );
    }

    public static List<ClientInscriptionDto> toClientInscriptionDto(List<ThriftInscriptionDto> thriftList) {
        List<ClientInscriptionDto> newList = new ArrayList<>();
        for (ThriftInscriptionDto inscription : thriftList){
            newList.add(toClientInscriptionDto(inscription));
        }
        return newList;
    }
}
