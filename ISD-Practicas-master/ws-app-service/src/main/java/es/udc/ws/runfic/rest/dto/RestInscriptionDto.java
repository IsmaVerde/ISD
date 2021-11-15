package es.udc.ws.runfic.rest.dto;

import java.util.Objects;

public class RestInscriptionDto {
    public RestInscriptionDto(){}
    private Long inscriptionID;
    private String user;
    private String creditCardNumber;
    private Long raceID;
    private int runnerNumber;
    private boolean isNumberTaken;

    public RestInscriptionDto(Long inscriptionID, String user, String creditCardNumber,
                              Long raceID,
                              int runnerNumber, boolean isNumberTaken) {
        this.inscriptionID = inscriptionID;
        this.user = user;
        this.creditCardNumber = creditCardNumber;
        this.raceID = raceID;
        this.runnerNumber = runnerNumber;
        this.isNumberTaken = isNumberTaken;
    }

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
        RestInscriptionDto that = (RestInscriptionDto) o;
        return runnerNumber == that.runnerNumber &&
                isNumberTaken == that.isNumberTaken &&
                Objects.equals(inscriptionID, that.inscriptionID) &&
                user.equals(that.user) &&
                creditCardNumber.equals(that.creditCardNumber) &&
                raceID.equals(that.raceID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inscriptionID, user, creditCardNumber, raceID, runnerNumber, isNumberTaken);
    }
}
