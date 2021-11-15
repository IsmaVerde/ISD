package es.udc.ws.runfic.model.runservice;

import es.udc.ws.runfic.model.inscription.Inscription;
import es.udc.ws.runfic.model.inscription.SqlInscriptionDao;
import es.udc.ws.runfic.model.inscription.SqlInscriptionDaoFactory;
import es.udc.ws.runfic.model.race.Race;
import es.udc.ws.runfic.model.race.SqlRaceDao;
import es.udc.ws.runfic.model.race.SqlRaceDaoFactory;
import es.udc.ws.runfic.model.runservice.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static es.udc.ws.runfic.model.utils.ModelConstants.*;
import static es.udc.ws.runfic.model.utils.RunficPropertyValidator.*;
import static es.udc.ws.util.validation.PropertyValidator.validateCreditCard;

public class RunServiceImpl implements RunService{

    private DataSource datasource;
    private SqlRaceDao raceDao;
    private SqlInscriptionDao inscriptionDao;

    public RunServiceImpl(){
        datasource = DataSourceLocator.getDataSource(RUNFIC_DATA_SOURCE);
        raceDao = SqlRaceDaoFactory.getDao();
        inscriptionDao = SqlInscriptionDaoFactory.getDao();
    }

    //Caso de Uso 1 - Brais
    //Equivalente REST -> POST a /race
    @Override
    public Race addRace(String city, String description, LocalDateTime startDateTime, float price, int maxParticipants) throws InputValidationException {
        PropertyValidator.validateMandatoryString("city", city);
        PropertyValidator.validateMandatoryString("description", description);
        validateFloat("price", price, 0, MAX_PRICE);
        validateInt("maxParticipants", maxParticipants, 1, MAX_PARTICIPANTS);

        if (startDateTime.compareTo(LocalDateTime.now().withNano(0).plusHours(24)) <= 0){
            throw new InputValidationException("La fecha de inicio de la carrera no puede ser anterior a 24 desde ahora mismo");
        }

        try (Connection connection = datasource.getConnection()) {

            try {
                //Prepare connection.
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Race race = new Race(null, city, description, startDateTime, price, 0, maxParticipants, LocalDateTime.now());

                //Do work.
                Race createdRace = raceDao.create(connection, race);

                //Commit.
                connection.commit();

                return createdRace;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);

            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Caso de Uso 2 - Isma
    //Equivalente REST -> GET a /race/id
    @Override
    public Race findRace(Long raceID) throws InstanceNotFoundException {

        try (Connection connection = datasource.getConnection()) {
           return raceDao.find(connection, raceID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Caso de Uso 3 - Brais
    //Equivalente REST -> GET a /race?date=date&city=city
    @Override
    public List<Race> findByDate(LocalDate date, String city) {
        try (Connection connection = datasource.getConnection()) {
            return raceDao.findByDateCity(connection, date, city);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Caso de Uso 4 - Carlos
    //Equivalente REST -> POST a /inscription
    @Override
    public Inscription inscribe(Long raceID, String email, String creditCard)
            throws InputValidationException, InscriptionClosedException, InstanceNotFoundException, RaceFullException, AlreadyInscribedException {
        String creditCardNumber =  creditCard.replaceAll("\\s+", ""); //Removes all spaces inside
        validateEmail(email);
        validateCreditCard(creditCardNumber);
        try (Connection connection = datasource.getConnection()) {
            if (inscriptionDao.isUserInscribed(connection, raceID, email)){
                throw new AlreadyInscribedException("User "+email+" already inscribed in race " + raceID.toString());
            }

            Race thisRace = raceDao.find(connection, raceID);

            //Comprobamos que esté en plazo para inscribirse
            if ((LocalDateTime.now().plusDays(1).compareTo(thisRace.getStartDateTime())) > 0) {
                throw new InscriptionClosedException("Inscriptions close 24 hours before the race starts");
            }

            //Comprobamos que la carrera no esté llena
            if (thisRace.getParticipants() == thisRace.getMaxParticipants()){
                throw new RaceFullException("Race is full");
            }

            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Inscription newInscription = new Inscription(
                        null, email, creditCardNumber, raceID, LocalDateTime.now(), thisRace.getParticipants() + 1, false);
                //IDs are null because the database will create them

                Inscription createdInscription = inscriptionDao.create(connection, newInscription);

                //After the inscription was created succesfully, update the Race so it has one more participant
                int newparticipants = thisRace.getParticipants() +1;
                thisRace.setParticipants(newparticipants);
                raceDao.update(connection, thisRace);

                connection.commit();
                return createdInscription;

            }catch(SQLException | RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        }
        catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //Caso de Uso 5 - Carlos
    //Equivalente REST a -> GET a /inscription?user=email
    @Override
    public List<Inscription> findAllFromUser(String email) throws InputValidationException {
        validateEmail(email);
        try(Connection connection = this.datasource.getConnection()){
            return inscriptionDao.findByUser(connection, email);
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    //Caso de Uso 6 - Isma
    //Equivalente REST -> Overloaded POST a /inscription/id?creditCardNumber="ccn"
    @Override
    public Inscription getRunnerNumber(Long inscriptionID, String creditCard) throws InputValidationException, InstanceNotFoundException, NumberTakenException, InvalidUserException {
        String creditCardNumber =  creditCard.replaceAll("\\s+", ""); //Removes all spaces inside
        validateCreditCard(creditCardNumber);
        try (Connection connection = this.datasource.getConnection()) {
            Inscription thisInscription = this.inscriptionDao.find(connection, inscriptionID);

            //Empezamos transacción
            try {
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                //Comprueba que los datos del usuario se corresponden con la inscripción
                if (!((thisInscription.getCreditCardNumber().equals(creditCardNumber)))) {
                    throw new InvalidUserException("This user credit card doesn't match with the inscription");
                }
                //Comprueba que el número de inscripción no ha sido entregado previamente
                if (thisInscription.isNumberTaken()) {
                    throw new NumberTakenException("This runner number is already taken");
                }

                thisInscription.setNumberTaken(true);
                inscriptionDao.update(connection, thisInscription);

                connection.commit();
                return thisInscription;

            }catch (SQLException | RuntimeException | Error err){
                connection.rollback();
                throw new RuntimeException(err);
            }
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
