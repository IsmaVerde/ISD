package es.udc.ws.runfic.model.inscription;

import java.time.LocalDateTime;
import java.util.Objects;

import static es.udc.ws.runfic.model.utils.RunFicDateTimeUtils.roundToMinute;

public class Inscription {
    private Long inscriptionID;
    private String user;
    private String creditCardNumber;
    private Long raceID;
    private LocalDateTime inscriptionDateTime;
    private int runnerNumber;
    private boolean isNumberTaken;

    public Inscription(String user, String creditCardNumber, Long raceID) {
        this.user = user;
        this.creditCardNumber = creditCardNumber;
        this.raceID = raceID;
    }

    public Inscription(Long inscriptionID, String user, String creditCardNumber, Long raceID, LocalDateTime inscriptionDateTime, int runnerNumber, boolean numberTaken) {
        this(user, creditCardNumber, raceID);
        this.inscriptionID = inscriptionID;
        this.inscriptionDateTime = inscriptionDateTime;
        this.runnerNumber = runnerNumber;
        this.isNumberTaken = numberTaken;
    }

    public boolean getIsNumberTaken() {return isNumberTaken;}

    public Long getInscriptionID() {
        return inscriptionID;
    }

    public void setInscriptionID(Long inscriptionID) {
        this.inscriptionID = inscriptionID;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public Long getRaceID() {
        return raceID;
    }

    public void setRaceID(Long raceID) {
        this.raceID = raceID;
    }

    public LocalDateTime getInscriptionDateTime() {
        return inscriptionDateTime;
    }

    public void setInscriptionDateTime(LocalDateTime inscriptionDateTime) {
        this.inscriptionDateTime = inscriptionDateTime;
    }

    public int getRunnerNumber() {
        return runnerNumber;
    }

    public void setRunnerNumber(int runnerNumber) {
        this.runnerNumber = runnerNumber;
    }

    public boolean isNumberTaken() {
        return isNumberTaken;
    }

    public void setNumberTaken(boolean numberTaken) {
        isNumberTaken = numberTaken;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Inscription that = (Inscription) o;
        return runnerNumber == that.runnerNumber &&
                isNumberTaken == that.isNumberTaken &&
                Objects.equals(inscriptionID, that.inscriptionID) &&
                user.equals(that.user) &&
                creditCardNumber.equals(that.creditCardNumber) &&
                raceID.equals(that.raceID) &&
                Objects.equals(roundToMinute(inscriptionDateTime), roundToMinute(that.inscriptionDateTime));
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscriptionID, user, creditCardNumber, raceID, roundToMinute(inscriptionDateTime), runnerNumber, isNumberTaken);
    }
}
