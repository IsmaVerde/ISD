package es.udc.ws.runfic.model.race;

import java.time.LocalDateTime;
import java.util.Objects;

import static es.udc.ws.runfic.model.utils.RunFicDateTimeUtils.roundToMinute;

public class Race {
    private Long raceID;
    private String city;
    private String description;
    private LocalDateTime startDateTime;
    private float price;
    private int participants;
    private int maxParticipants;
    private LocalDateTime addedDateTime;

    public Race(Long raceID, String city, String description, LocalDateTime startDateTime, float price,
                int participants, int maxParticipants, LocalDateTime addedDateTime) {
        this.raceID = raceID;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
        this.addedDateTime = addedDateTime;
    }

    public Race(String description, LocalDateTime startDateTime, float price, int maxParticipants, int participants) {
    }

    public Long getRaceID() {
        return raceID;
    }

    public void setRaceID(Long raceID) {
        this.raceID = raceID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }

    public int getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public LocalDateTime getAddedDateTime() {
        return addedDateTime;
    }

    public void setAddedDateTime(LocalDateTime addedDateTime) {
        this.addedDateTime = addedDateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return Float.compare(race.price, price) == 0 &&
                participants == race.participants &&
                maxParticipants == race.maxParticipants &&
                Objects.equals(raceID, race.raceID) &&
                city.equals(race.city) &&
                description.equals(race.description) &&
                roundToMinute(startDateTime).equals(roundToMinute(race.startDateTime)) &&
                Objects.equals(roundToMinute(addedDateTime), roundToMinute(race.addedDateTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceID, city, description, roundToMinute(startDateTime), price, participants, maxParticipants, addedDateTime);
    }
}
