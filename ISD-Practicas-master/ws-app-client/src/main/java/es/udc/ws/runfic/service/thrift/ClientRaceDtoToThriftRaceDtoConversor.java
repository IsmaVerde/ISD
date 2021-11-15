package es.udc.ws.runfic.service.thrift;

import es.udc.ws.runfic.service.dto.ClientRaceDto;
import es.udc.ws.runfic.thrift.ThriftRaceDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ClientRaceDtoToThriftRaceDtoConversor {
    public static ClientRaceDto toClientRaceDto(ThriftRaceDto race) {
        return new ClientRaceDto(
                race.getRaceID(),
                race.getCity(),
                race.getDescription(),
                LocalDateTime.parse(race.getStartDateTime()),
                Double.valueOf(race.getPrice()).floatValue(),
                race.getMaxParticipants(),
                race.getParticipants()
        );
    }

    public static ThriftRaceDto toThriftRaceDto(ClientRaceDto clientDto){
        return new ThriftRaceDto(
                clientDto.getRaceID(),
                clientDto.getCity(),
                clientDto.getDescription(),
                clientDto.getStartDateTime().toString(),
                clientDto.getPrice(),
                clientDto.getMaxParticipants(),
                clientDto.getParticipants()
        );
    }

    public static List<ClientRaceDto> toClientRaceDto(List<ThriftRaceDto> thriftList) {
        List<ClientRaceDto> raceList = new ArrayList<>();
        for (ThriftRaceDto race : thriftList){
            raceList.add(toClientRaceDto(race));
        }
        return raceList;
    }
}
