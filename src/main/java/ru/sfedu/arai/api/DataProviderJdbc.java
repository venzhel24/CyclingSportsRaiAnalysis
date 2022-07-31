package ru.sfedu.arai.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.arai.Constants;
import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.*;
import ru.sfedu.arai.utils.ConfigurationUtil;
import ru.sfedu.arai.utils.DisciplineUtil;
import ru.sfedu.arai.utils.RaceTypeUtil;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DataProviderJdbc implements DataProvider{
    private static final Logger log = LogManager.getLogger(DataProviderJdbc.class);
    private static String PATH;

    public DataProviderJdbc() {
        PATH = Constants.JDBC_PATH;
        createTables();
    }

    public DataProviderJdbc(String filePath) {
        PATH = Constants.TEST_JDBC_PATH;
        createTables();
    }

    public Connection getMyConnection() throws SQLException, IOException {
//        try {
//            log.debug("get connection");
//            //Class.forName("org.h2.Driver");
//            return DriverManager.getConnection(ConfigurationUtil.getConfigurationEntry(Constants.JDBC_URL).concat(ConfigurationUtil.getConfigurationEntry(PATH)).concat(Constants.JDBC_NAME),
//                    ConfigurationUtil.getConfigurationEntry(Constants.JDBC_USER),
//                    ConfigurationUtil.getConfigurationEntry(Constants.JDBC_PASSWORD));
//        } catch (Exception e){
//            log.error("getMyConnection Error");
//            log.error(e.getClass().getName() + ": " + e.getMessage());
//            return null;
//        }

        return DriverManager.getConnection(ConfigurationUtil.getConfigurationEntry(Constants.JDBC_URL).concat(ConfigurationUtil.getConfigurationEntry(PATH)).concat(Constants.JDBC_NAME),
                ConfigurationUtil.getConfigurationEntry(Constants.JDBC_USER),
                ConfigurationUtil.getConfigurationEntry(Constants.JDBC_PASSWORD));
    }

    public void createTables(){
        try {
            log.debug("Create tables[1]: ");
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(Constants.CREATE_TABLE_PARTICIPANTS);
            statement.executeUpdate(Constants.CREATE_TABLE_RACE);
            statement.executeUpdate(Constants.CREATE_TABLE_SWIMMING_DISCIPLINE);
            statement.executeUpdate(Constants.CREATE_TABLE_RUNNING_DISCIPLINE);
            statement.executeUpdate(Constants.CREATE_TABLE_CYCLING_DISCIPLINE);
            statement.executeUpdate(Constants.CREATE_TABLE_RESULTS);
            statement.close();
            connection.close();
            log.debug("Tables was created[2]: ");
        } catch (Exception e){
            log.error("createTables Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    public void dropTables(){
        try {
            log.debug("Delete tables[1]");
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(Constants.DROP_PARTICIPANTS_TABLE);
            statement.executeUpdate(Constants.DROP_RACE_TABLE);
            statement.executeUpdate(Constants.DROP_SWIMMING_DISCIPLINE_TABLE);
            statement.executeUpdate(Constants.DROP_RUNNING_DISCIPLINE_TABLE);
            statement.executeUpdate(Constants.DROP_CYCLING_DISCIPLINE_TABLE);
            statement.executeUpdate(Constants.DROP_RESULTS_TABLE);
            statement.close();
            connection.close();
            log.debug("Tables was deleted[2]: ");
        } catch (Exception e){
            log.error("dropTables Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
        }

    }

    @Override
    public Optional<List<Participant>> getAllParticipants() {
        try{
            log.debug("getAllParticipants[1]:");
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            List<Participant> participantsList = new ArrayList<>();
            String query = Constants.SELECT_ALL_FROM_PARTICIPANTS;
            log.debug("Trying to execute[2]: ".concat(query));
            ResultSet rs = statement.executeQuery(query);

            while (rs.next()) {
                Participant participant = new Participant();
                participant.setId(rs.getString("id"));
                participant.setName(rs.getString("name"));
                participantsList.add(participant);
            }

            if (participantsList.isEmpty()){
                statement.close();
                connection.close();
                log.debug("RecordSet is empty[3]: ");
                return Optional.empty();
            }

            log.debug("Participants received[3]: ");
            statement.close();
            connection.close();
            return Optional.of(participantsList);
        } catch (Exception e){
            log.error("showAllParticipants Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Race>> getAllRaces(){
        try{
            log.debug("Start getAllRaces[1]: ");
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            String query = String.format(Constants.SELECT_ALL_FROM_RACE);
            log.debug("Trying to execute[2]: "+ query);
            ResultSet rs = statement.executeQuery(query);
            List<Race> raceList = new ArrayList<>();

            while(rs.next()) {
                Race race = new Race();
                race.setRaceId(rs.getString("raceId"));
                race.setRaceName(rs.getString("raceName"));
                race.setDate(rs.getDate("date"));
                race.setRaceType(RaceTypeUtil.raceTypeParser(rs.getString("raceType")));
                raceList.add(race);
            }

            if (raceList.isEmpty()){
                statement.close();
                connection.close();
                log.debug("RecordSet is empty[3]: ");
                return Optional.empty();
            }

            statement.close();
            connection.close();
            log.debug("Races received [3]: ");
            return Optional.of(raceList);
        } catch (Exception e){
            log.error("getAllRaces Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Discipline>> getAllDisciplineRecords(DisciplineType disciplineType){
        try{
            log.debug("getDisciplineRecordById[1]: ");
            String query;
            switch(disciplineType) {
                case SWIMMING:
                    query = Constants.SELECT_ALL_FROM_SWIMMING_DISCIPLINE;
                    break;
                case RUNNING:
                    query = Constants.SELECT_ALL_FROM_RUNNING_DISCIPLINE;
                    break;
                case CYCLING:
                    query = Constants.SELECT_ALL_FROM_CYCLING_DISCIPLINE;
                    break;
                default:
                    throw new Exception("generateQuery error, incorrect disciplineType");
            }

            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<Discipline> disciplineList = new ArrayList<>();

            while(rs.next()) {
                Discipline discipline = DisciplineUtil.getDisciplineBeanByType(disciplineType);
                discipline.setResultsId(rs.getString("resultsId"));
                discipline.setTime(rs.getFloat("time_"));
                discipline.setDistance(rs.getInt("distance"));
                discipline.setDisciplinePlace(rs.getInt("disciplinePlace"));
                discipline.setDisciplineType(DisciplineUtil.disciplineTypeParser(rs.getString("disciplineType")));
                switch (disciplineType) {
                    case SWIMMING:
                        ((SwimmingDiscipline) discipline).setWaterTemperature((rs.getInt("waterTemperature")));
                        ((SwimmingDiscipline) discipline).setHydroSuit((rs.getBoolean("hydroSuit")));
                        break;
                    case RUNNING:
                        ((RunningDiscipline) discipline).setRunningShoes((rs.getString("runningShoes")));
                        break;
                    case CYCLING:
                        ((CyclingDiscipline) discipline).setBikeId((rs.getLong("bikeId")));
                        ((CyclingDiscipline) discipline).setBikeModel((rs.getString("bikeModel")));
                        ((CyclingDiscipline) discipline).setCyclingShoes((rs.getString("cyclingShoes")));
                        break;
                    default:
                        throw new Exception("generateQuery error, incorrect disciplineType");
                }
                disciplineList.add(discipline);
            }

            if (disciplineList.isEmpty()){
                statement.close();
                connection.close();
                log.debug("RecordSet is empty[3]: ");
                return Optional.empty();
            }

            statement.close();
            connection.close();
            log.debug("Disciplines received[3]: ");
            return Optional.of(disciplineList);
        } catch (Exception e){
            log.error("getDisciplineById Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Results>> getAllResults(){
        try{
            log.debug("getAllResults [1]: ");
            String query = Constants.SELECT_ALL_FROM_RESULTS;
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<Results> resultsList = new ArrayList<>();

            while(rs.next()) {
                Results results = new Results();
                results.setPlace(rs.getInt("place"));
                results.setParticipantId(rs.getString("participantId"));
                results.setRaceId(rs.getString("raceId"));
                results.setTotalTime(rs.getFloat("totalTime"));
                results.setResultsId(rs.getString("resultsId"));
                resultsList.add(results);
            }

            if (resultsList.isEmpty()){
                statement.close();
                connection.close();
                log.debug("RecordSet is empty[3]: ");
                return Optional.empty();
            }

            statement.close();
            connection.close();
            log.debug("Results received[3]: ");
            return Optional.of(resultsList);
        } catch (Exception e){
            log.error("getAllResults Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Results>> getAllResultsByRace(String raceId){
        try{
            log.debug("getAllResultsByRace [1]: ");
            String query = String.format(Constants.SELECT_ALL_FROM_RESULTS_BY_RACE, raceId);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<Results> resultsList = new ArrayList<>();

            while(rs.next()) {
                Results results = new Results();
                results.setPlace(rs.getInt("place"));
                results.setParticipantId(rs.getString("participantId"));
                results.setRaceId(rs.getString("raceId"));
                results.setTotalTime(rs.getFloat("totalTime"));
                results.setResultsId(rs.getString("resultsId"));
                resultsList.add(results);
            }

            if (resultsList.isEmpty()){
                statement.close();
                connection.close();
                log.debug("RecordSet is empty[3]: ");
                return Optional.empty();
            }

            statement.close();
            connection.close();
            log.debug("Results received[3]: ");
            return Optional.of(resultsList);
        } catch (Exception e){
            log.error("getAllResults Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }



    @Override
    public boolean addParticipantRecord(Participant participant) {
        try{
            log.debug("addParticipantRecord[1]: ");
            String query = String.format(Constants.INSERT_INTO_PARTICIPANTS, participant.getId(), participant.getName());
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            log.debug("ParticipantRecord added[3]: {}", participant.toString());
            statement.close();
            connection.close();
            return true;
        } catch (Exception e){
            log.error("addParticipantRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addRaceRecord(Race race) {
        try{
            log.debug("addRaceRecord[1]: ");
            String query = String.format(Constants.INSERT_INTO_RACE, race.getRaceId(),
                            race.getRaceName(),
                            race.getDate(),
                            race.getRaceType());
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            log.debug("RaceRecord added[3]: {}", race.toString());
            statement.close();
            connection.close();
            return true;
        } catch (Exception e){
            log.error("addRaceRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addDisciplineRecord(Discipline discipline) {
        try{
            log.debug("addDisciplineRecord[1]: ");
            String query;
            switch(discipline.getDisciplineType()){
                case SWIMMING:
                    query = String.format(Constants.INSERT_INTO_SWIMMING_DISCIPLINE, discipline.getResultsId(),
                                    discipline.getTime(),
                                    discipline.getDistance(),
                                    discipline.getDisciplinePlace(),
                                    discipline.getDisciplineType(),
                                    ((SwimmingDiscipline) discipline).getWaterTemperature(),
                                    ((SwimmingDiscipline) discipline).isHydroSuit());
                    break;

                case RUNNING:
                    assert discipline instanceof RunningDiscipline;
                    query = String.format(Constants.INSERT_INTO_RUNNING_DISCIPLINE, discipline.getResultsId(),
                                    discipline.getTime(),
                                    discipline.getDistance(),
                                    discipline.getDisciplinePlace(),
                                    discipline.getDisciplineType(),
                                    ((RunningDiscipline) discipline).getRunningShoes());
                    break;

                case CYCLING:
                    assert discipline instanceof CyclingDiscipline;
                    query = String.format(Constants.INSERT_INTO_CYCLING_DISCIPLINE, discipline.getResultsId(),
                                    discipline.getTime(),
                                    discipline.getDistance(),
                                    discipline.getDisciplinePlace(),
                                    discipline.getDisciplineType(),
                                    ((CyclingDiscipline) discipline).getBikeId(),
                                    ((CyclingDiscipline) discipline).getBikeModel(),
                                    ((CyclingDiscipline) discipline).getCyclingShoes());
                    break;
                default: throw new Exception("generateQuery error, incorrect discipline");
            }

            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            log.debug("DisciplineRecord added[3]: {}", discipline.toString());
            statement.close();
            connection.close();
            return true;
        } catch (Exception e){
            log.error("addDisciplineRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addResultsRecord(Results results) {
        try{
            log.debug("addResultsRecord[1]: ");

            if (results == null)
                throw new Exception("Adding record error, record equals null");

            if(getParticipantById(results.getParticipantId()).isEmpty())
                throw new Exception("Adding record error, participant with id:".concat(results.getParticipantId()).concat(" doesn't exist"));

            if(getRaceById(results.getRaceId()).isEmpty())
                throw new Exception("Adding record error, race with id:".concat(results.getRaceId()).concat(" doesn't exist"));

            String query = String.format(Constants.INSERT_INTO_RESULTS, results.getPlace(),
                            results.getParticipantId(),
                            results.getRaceId(),
                            results.getTotalTime(),
                            results.getResultsId());
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            log.debug("ResultsRecord added[3]: {}", results.toString());
            statement.close();
            connection.close();
            return true;
        } catch (Exception e){
            log.error("addResultsRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean deleteParticipantRecord(String id) {
        try{
            log.debug("deleteParticipantRecord[1]: ");
            String query = String.format(Constants.DELETE_FROM_PARTICIPANTS, id);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            if (statement.executeUpdate(query) == 0) {
                log.error("Cant find such id");
                statement.close();
                connection.close();
                return false;
            }
            statement.close();
            connection.close();
            log.debug("Deleted [3]: participant with id {}", id);
            return true;
        } catch (Exception e){
            log.error("deleteParticipantRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRaceRecord(String id) {
        try{
            log.debug("deleteRaceRecord[1]: ");
            String query = String.format(Constants.DELETE_FROM_RACE, id);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            if (statement.executeUpdate(query) == 0) {
                log.error("Cant find such id");
                statement.close();
                connection.close();
                return false;
            }
            statement.close();
            connection.close();
            log.debug("Deleted[3]: race with id {}", id);
            return true;
        } catch (Exception e){
            log.error("deleteRaceRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteDisciplineRecord(String id, DisciplineType disciplineType) {
        try{
            log.debug("deleteDisciplineRecord[1]: ");
            String query;
            switch(disciplineType) {
                case SWIMMING:
                    query = String.format(Constants.DELETE_FROM_SWIMMING_DISCIPLINE, id, disciplineType);
                    break;
                case RUNNING:
                    query = String.format(Constants.DELETE_FROM_RUNNING_DISCIPLINE, id, disciplineType);
                    break;
                case CYCLING:
                    query = String.format(Constants.DELETE_FROM_CYCLING_DISCIPLINE, id, disciplineType);
                    break;
                default:
                    throw new Exception("generateQuery error, incorrect disciplineType");
            }

            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            if (statement.executeUpdate(query) == 0) {
                log.error("Cant find such id");
                statement.close();
                connection.close();
                return false;
            }
            statement.close();
            connection.close();
            log.debug("Deleted [3]: discipline with id {}", id);
            return true;
        } catch (Exception e){
            log.error("deleteDisciplineRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteResultsRecord(String participantId, String raceId) {
        try{
            log.debug("deleteResultsRecord[1]: ");
            String query = String.format(Constants.DELETE_FROM_RESULTS, raceId, participantId);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            if (statement.executeUpdate(query) == 0) {
                log.error("Cant find such id");
                statement.close();
                connection.close();
                return false;
            }
            statement.close();
            connection.close();
            log.debug("Deleted [3]: Results with raceId {} and with participantId {}", raceId, participantId);
            return true;
        } catch (Exception e){
            log.error("deleteResultsRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public Optional<Participant> getParticipantById(String id) {
        try{
            log.debug("getParticipantRecordById[1]: ");
            String query = String.format(Constants.SELECT_FROM_PARTICIPANTS, id);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            rs.first();
            Participant participant = new Participant();
            participant.setId(rs.getString("id"));
            participant.setName(rs.getString("name"));

            statement.close();
            connection.close();
            log.debug("Received[3]: {}", participant.toString());
            return Optional.of(participant);
        } catch (Exception e){
            log.error("getParticipantById Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Race> getRaceById(String id) {
        try{
            log.debug("getRaceRecordById[1]: ");
            String query = String.format(Constants.SELECT_FROM_RACE, id);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            rs.first();
            Race race = new Race();
            race.setRaceId(rs.getString("raceId"));
            race.setRaceName(rs.getString("raceName"));
            race.setDate(rs.getDate("date"));
            race.setRaceType(RaceTypeUtil.raceTypeParser(rs.getString("raceType")));

            statement.close();
            connection.close();
            log.debug("Received [3]: {}", race.toString());
            return Optional.of(race);
        } catch (Exception e){
            log.error("getRaceById Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Discipline> getDisciplineRecordById(String id, DisciplineType disciplineType) {
        try{
            log.debug("getDisciplineRecordById[1]: ");
            String query;
            switch(disciplineType) {
                case SWIMMING:
                    query = String.format(Constants.SELECT_FROM_SWIMMING_DISCIPLINE, id, disciplineType);
                    break;
                case RUNNING:
                    query = String.format(Constants.SELECT_FROM_RUNNING_DISCIPLINE, id, disciplineType);
                    break;
                case CYCLING:
                    query = String.format(Constants.SELECT_FROM_CYCLING_DISCIPLINE, id, disciplineType);
                    break;
                default:
                    throw new Exception("generateQuery error, incorrect disciplineType");
            }

            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            rs.first();
            Discipline discipline = DisciplineUtil.getDisciplineBeanByType(disciplineType);
            discipline.setResultsId(rs.getString("resultsId"));
            discipline.setTime(rs.getFloat("time_"));
            discipline.setDistance(rs.getInt("distance"));
            discipline.setDisciplinePlace(rs.getInt("disciplinePlace"));
            discipline.setDisciplineType(DisciplineUtil.disciplineTypeParser(rs.getString("disciplineType")));
            switch(disciplineType) {
                case SWIMMING:
                    ((SwimmingDiscipline) discipline).setWaterTemperature((rs.getInt("waterTemperature")));
                    ((SwimmingDiscipline) discipline).setHydroSuit((rs.getBoolean("hydroSuit")));
                    break;
                case RUNNING:
                    ((RunningDiscipline) discipline).setRunningShoes((rs.getString("runningShoes")));
                    break;
                case CYCLING:
                    ((CyclingDiscipline) discipline).setBikeId((rs.getLong("bikeId")));
                    ((CyclingDiscipline) discipline).setBikeModel((rs.getString("bikeModel")));
                    ((CyclingDiscipline) discipline).setCyclingShoes((rs.getString("cyclingShoes")));
                    break;
                default:
                    throw new Exception("generateQuery error, incorrect disciplineType");
            }

            statement.close();
            connection.close();
            log.debug("Received [3]: {}", discipline.toString());
            return Optional.of(discipline);
        } catch (Exception e){
            log.error("getDisciplineById Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Results> getResultsRecordById(String participantId, String raceId) {
        try{
            log.info("Start getResultsRecord [1]: ");
            String query = String.format(Constants.SELECT_FROM_RESULTS, raceId, participantId);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            rs.first();
            Results results = new Results();
            results.setPlace(rs.getInt("place"));
            results.setParticipantId(rs.getString("participantId"));
            results.setRaceId(rs.getString("raceId"));
            results.setTotalTime(rs.getFloat("totalTime"));
            results.setResultsId(rs.getString("resultsId"));


            statement.close();
            connection.close();
            log.debug("Received [3]: {}", results.toString());
            return Optional.of(results);
        } catch (Exception e){
            log.error("getResultsRecordByParticipant Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Results> getResultsRecordById(String resultsId) {
        try{
            log.debug("getResultsRecord[1]: ");
            String query = String.format(Constants.SELECT_FROM_RESULTS1, resultsId);
            log.debug("Trying to execute[2]: "+ query);
            Connection connection = getMyConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);

            rs.first();
            Results results = new Results();
            results.setPlace(rs.getInt("place"));
            results.setParticipantId(rs.getString("participantId"));
            results.setRaceId(rs.getString("raceId"));
            results.setTotalTime(rs.getFloat("totalTime"));
            results.setResultsId(rs.getString("resultsId"));

            statement.close();
            connection.close();
            log.debug("Received [3]: {}", results.toString());
            return Optional.of(results);
        } catch (Exception e){
            log.error("getResultsRecordByParticipant Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public boolean updateParticipantRecord(Participant participant, String id) {
        try {
            log.debug("updateParticipant[1]: ");
            if (deleteParticipantRecord(id)) {
                addParticipantRecord(participant);
                log.debug("updated [2]: {}", participant);
                return true;
            } else {
                log.debug("No value to update[2]: ");
                return false;
            }
        }
        catch (Exception e){
            log.error("updateParticipantRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateRaceRecord(Race race, String id) {
        try {
            log.debug("Start updateRace [1]: ");
            if (deleteRaceRecord(id)) {
                addRaceRecord(race);
                log.debug("updated [2]: {}", race);
                return true;
            } else {
                log.debug("No value to update[2]: ");
                return false;
            }
        }
        catch (Exception e){
            log.error("updateRaceRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateDisciplineRecord(Discipline discipline, String id) {
        try {
            log.debug("Start updateDisciplineRecord [1]: ");
            if (deleteDisciplineRecord(id, discipline.getDisciplineType())) {
                addDisciplineRecord(discipline);
                log.debug("updated [2]: {}", discipline);
                return true;
            } else {
                log.debug("No value to update[2]: ");
                return false;
            }
        }
        catch (Exception e){
            log.error("updateDisciplineRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateResultsRecord(Results results, String participantId, String raceId) {
        try {
            log.debug("updateResultsRecord[1]: ");
            if (deleteResultsRecord(participantId, raceId)) {
                addResultsRecord(results);
                log.debug("updated [2]: {}", results);
                return true;
            } else {
                log.debug("No value to update[2]: ");
                return false;
            }
        }
        catch (Exception e){
            log.error("updateResultsRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public Optional<List<Float>> analysisStageWins(RaceType raceType, DisciplineType disciplineType, String analysisWithoutWin){
        try{
            log.debug("analysisStageWins[1]: ");
            int totalWins = getAllRaces().get().stream().filter(x -> x.getRaceType().equals(raceType)).toList().size();
            List<Discipline> disciplineList = getAllDisciplineRecords(disciplineType).get();
            int disciplineWins = countingStageWithWin(disciplineList); //<include>
            List<Float> resultsList = new ArrayList<>();
            log.debug("Win rate with win in stage[2]: ");
            resultsList.add(calcWinRate(totalWins, disciplineWins));

            if(analysisWithoutWin.equals("y")){
                log.debug("Win Rate without win in stage[3]: ");
                resultsList.add(calcWinRate(totalWins, countingStageWithoutWin(disciplineList))); //<extend>
            }

            log.debug("results received[4]: {}", resultsList);
            return Optional.of(resultsList);
        }
        catch(Exception e){
            log.error("analysisStageWins Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }

    }

    @Override
    public Integer countingStageWithWin(List<Discipline> disciplineList){
        try{
            log.debug("countingStageWithWin[1]: ");
            int disciplineWins = disciplineList
                    .stream()
                    .filter(x -> x.getDisciplinePlace() == 1 && getResultsRecordById(x.getResultsId()).get().getPlace() == 1)
                    .toList().size();
            log.debug("Number of wins with win in discipline received[2]: {}", disciplineWins);
            return disciplineWins;
        }
        catch(Exception e){
            log.error("countingStageWithWin Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public Integer countingStageWithoutWin(List<Discipline> disciplineList){
        try{
            log.debug("countingStageWithoutWin[1]: ");
            int disciplineNotWins = disciplineList
                    .stream()
                    .filter(x -> x.getDisciplinePlace() != 1 && getResultsRecordById(x.getResultsId()).get().getPlace() == 1)
                    .toList().size();
            log.debug("Number of wins without win in discipline received[2]: {}", disciplineNotWins);
            return disciplineNotWins;
        }
        catch(Exception e){
            log.error("countingStageWithoutWin Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    private float calcWinRate(int totalWins, int disciplineWins){
        float res = (float)disciplineWins/totalWins;
        log.debug(res);
        return res;
    }

    @Override
    public Optional<List<Float>> gapAnalysis(RaceType raceType, String participantId, DisciplineType disciplineType){
        try {
            log.debug("gapAnalysis[1]: ");
            List<Float> gapResultsList = new ArrayList<>();
            float participantTime, winnerTime, gap = 0;
            List<Results> resultsList = getAllResults().get()
                    .stream()
                    .filter(x -> x.getParticipantId().equals(participantId))
                    .toList();

            //<include>
            gap = countingTotalGap(resultsList);

            log.debug("Total gap[2]: ");
            gapResultsList.add(calcAvgGap(gap, resultsList.size()));

            //<extend>
            if(disciplineType != null){
                log.debug("Gap in {} [3]: ", disciplineType);
                gap = countingTotalDisciplineGap(resultsList, disciplineType);
                gapResultsList.add(calcAvgGap(gap, resultsList.size()));
            }
            return Optional.of(gapResultsList);
        }
        catch(Exception e) {
            log.error("gapAnalysis Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Float countingTotalGap(List<Results> resultsList){
        float participantTime, winnerTime, gap = 0;
        try {
            log.debug("countingTotalGap[1]: ");
            for (Results x : resultsList) {
                participantTime = x.getTotalTime();
                winnerTime = getAllResultsByRace(x.getRaceId()).get()
                        .stream()
                        .filter(result -> result.getPlace() == 1)
                        .findFirst().get()
                        .getTotalTime();
                log.debug("Winner: {}; Participant: {}", winnerTime, participantTime);
                gap += (participantTime - winnerTime);
            }
            return gap;
        }
        catch(Exception e) {
            log.error("gapAnalysis Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    @Override
    public Float countingTotalDisciplineGap(List<Results> resultsList, DisciplineType disciplineType) {
        float participantTime, winnerTime, gap = 0;
        try {
            log.debug("gapDisciplineAnalysis[1]: ");
            List<Discipline> disciplineList = getAllDisciplineRecords(disciplineType).get()
                    .stream()
                    .filter(x -> x.getDisciplinePlace() == 1)
                    .toList();

            for (Results x : resultsList) {
                participantTime = getDisciplineRecordById(x.getResultsId(), disciplineType).get().getTime();

                for(Discipline y : disciplineList) {
                    if(getResultsRecordById(y.getResultsId()).get().getRaceId().equals(x.getRaceId())) {
                        winnerTime = y.getTime();
                        log.debug("Discipline Win: {}; Part: {}", winnerTime, participantTime);
                        gap += (participantTime - winnerTime);
                    }
                }
            }
            return gap;
        } catch (Exception e) {
            log.error("gapDisciplineAnalysis Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    private float calcAvgGap(float gap, int count){
        gap /= count;
        log.debug(gap);
        return gap;
    }
}
