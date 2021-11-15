package es.udc.ws.runfic.service.dto;

import java.time.LocalDateTime;
import java.util.Objects;

import static es.udc.ws.runfic.service.utils.RunServiceDateTimeUtils.roundToMinute;

public class ClientRaceDto {
    private Long raceID;
    private String city;
    private String description;
    private LocalDateTime startDateTime;
    private float price;
    private int participants;
    private int maxParticipants;

    public ClientRaceDto(Long raceID, String city, String description,
                         LocalDateTime startDateTime, float price,
                         int participants, int maxParticipants) {
        this.raceID = raceID;
        this.city = city;
        this.description = description;
        this.startDateTime = startDateTime;
        this.price = price;
        this.participants = participants;
        this.maxParticipants = maxParticipants;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientRaceDto that = (ClientRaceDto) o;
        return Float.compare(that.price, price) == 0 &&
                participants == that.participants &&
                maxParticipants == that.maxParticipants &&
                Objects.equals(raceID, that.raceID) &&
                city.equals(that.city) &&
                description.equals(that.description) &&
                roundToMinute(startDateTime).equals(roundToMinute(that.startDateTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceID, city, description, roundToMinute(startDateTime), price, participants, maxParticipants);
    }
}
