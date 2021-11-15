package es.udc.ws.runfic.thriftservice;

import es.udc.ws.runfic.model.race.Race;
import es.udc.ws.runfic.thrift.ThriftRaceDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RaceToThriftRaceDtoConversor {

    public static ThriftRaceDto toThriftRaceDto(Race modelRace){
        return new ThriftRaceDto(modelRace.getRaceID(), modelRace.getCity(), modelRace.getDescription(),
                modelRace.getStartDateTime().toString(), modelRace.getPrice(),
                modelRace.getParticipants(), modelRace.getMaxParticipants());
    }

    public static List<ThriftRaceDto> toThriftRaceDtos(List<Race> races) {
        List<ThriftRaceDto> RaceDtos = new ArrayList<ThriftRaceDto>(races.size());
        for (Race race : races) {
            RaceDtos.add(toThriftRaceDto(race));
        }
        return RaceDtos;
    }

    public static Race toRace(ThriftRaceDto ThriftRace){
        return new Race(ThriftRace.getRaceID(), ThriftRace.getCity(), ThriftRace.getDescription(), LocalDateTime.parse(ThriftRace.getStartDateTime()),
                ((float) ThriftRace.getPrice()), ThriftRace.getParticipants(), ThriftRace.getMaxParticipants(), null);
    }
}
