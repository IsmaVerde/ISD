package es.udc.ws.runfic.model.race;

import es.udc.ws.runfic.model.inscription.Inscription;

import java.sql.*;
import java.time.LocalDateTime;

public class Jbdc3CcSqlRaceDao extends AbstractSqlRaceDao {
    //Isma
    @Override
    public Race create(Connection connection, Race race) {

        String queryStr = "INSERT into Race" +
                " (city, description, startDateTime, price, participants, maxParticipants, addedDateTime)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryStr, Statement.RETURN_GENERATED_KEYS)){
            //Fill prepared Statement
            int i = 1;
            preparedStatement.setString(i++, race.getCity());
            preparedStatement.setString(i++, race.getDescription());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(race.getStartDateTime()));
            preparedStatement.setFloat(i++, race.getPrice());
            preparedStatement.setInt(i++, race.getParticipants());
            preparedStatement.setInt(i++, race.getMaxParticipants());
            preparedStatement.setTimestamp(i, Timestamp.valueOf(race.getAddedDateTime()));

            //Execute Query
            int modifiedRows = preparedStatement.executeUpdate();
            if (modifiedRows == 0) {
                throw new SQLException("No rows were added");
            }

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(!resultSet.next()){
                throw new SQLException("JDBC Driver returned no keys");
            }
            Long newID = resultSet.getLong(1);

            //Returns a Race object with the new ID
            return new Race(newID, race.getCity(), race.getDescription(), race.getStartDateTime(),
                    race.getPrice(), race.getParticipants(), race.getMaxParticipants(), race.getAddedDateTime());

        } catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}
