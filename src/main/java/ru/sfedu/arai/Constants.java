package ru.sfedu.arai;

public class Constants {

    public static final String CSV_PATH = "csv_path";
    public static final String TEST_CSV_PATH = "test_csv_path";
    public static final String CSV_EXTENSION = "csv_extension";

    public static final String XML_PATH = "xml_path";
    public static final String TEST_XML_PATH = "test_xml_path";
    public static final String XML_EXTENSION = "xml_extension";

    public static final String MONGO_HOST= "mongo_host";
    public static final String MONGO_PORT = "mongo_port";
    public static final String MONGO_DB = "mongo_db";
    public static final String MONGO_USER = "mongo_user";
    public static final String MONGO_PASSWORD = "mongo_password";
    public static final String MONGO_TIMEOUT = "mongo_timeout";
    public static final String MONGO_FIELD_TIME = "time";
    public static final String MONGO_FIELD_COMMAND = "command";
    public static final String MONGO_FIELD_REPOSITORY = "repository";
    public static final String MONGO_FIELD_OBJECT = "item";

    public static final String JDBC_DRIVER = "jdbc_driver";
    public static final String JDBC_NAME = "h2database";
    public static final String JDBC_PATH = "jdbc_path";
    public static final String TEST_JDBC_PATH = "test_jdbc_path";
    public static final String JDBC_URL = "jdbc_url";
    public static final String JDBC_USER = "jdbc_user";
    public static final String JDBC_PASSWORD = "jdbc_password";

    public static final String CREATE_TABLE_PARTICIPANTS =
            "CREATE TABLE IF NOT EXISTS PARTICIPANTS (" +
                    "id VARCHAR, " +
                    "name VARCHAR)";
    public static final String CREATE_TABLE_RACE =
            "CREATE TABLE IF NOT EXISTS RACE (" +
                    "raceId VARCHAR, " +
                    "raceName VARCHAR, " +
                    "date DATE, " +
                    "raceType VARCHAR)";
    public static final String CREATE_TABLE_SWIMMING_DISCIPLINE =
            "CREATE TABLE IF NOT EXISTS SWIMMING_DISCIPLINE (" +
                    "resultsId VARCHAR, " +
                    "time_ REAL, " +
                    "distance INTEGER, " +
                    "disciplinePlace INTEGER, " +
                    "disciplineType VARCHAR, " +
                    "waterTemperature INTEGER, " +
                    "hydroSuit BIT)";
    public static final String CREATE_TABLE_RUNNING_DISCIPLINE =
            "CREATE TABLE IF NOT EXISTS RUNNING_DISCIPLINE (" +
                    "resultsId VARCHAR, " +
                    "time_ REAL, " +
                    "distance INTEGER, " +
                    "disciplinePlace INTEGER, " +
                    "disciplineType VARCHAR, " +
                    "runningShoes VARCHAR)";
    public static final String CREATE_TABLE_CYCLING_DISCIPLINE =
            "CREATE TABLE IF NOT EXISTS CYCLING_DISCIPLINE (" +
                    "resultsId VARCHAR, " +
                    "time_ REAL, " +
                    "distance INTEGER, " +
                    "disciplinePlace INTEGER, " +
                    "disciplineType VARCHAR, " +
                    "bikeId BIGINT, " +
                    "bikeModel VARCHAR, " +
                    "cyclingShoes VARCHAR)";
    public static final String CREATE_TABLE_RESULTS =
            "CREATE TABLE IF NOT EXISTS RESULTS (" +
                    "place INTEGER, " +
                    "participantId VARCHAR, " +
                    "raceId VARCHAR, " +
                    "totalTime REAL, " +
                    "resultsId VARCHAR)";


    public static final String SELECT_ALL_FROM_PARTICIPANTS = "SELECT * FROM PARTICIPANTS";
    public static final String INSERT_INTO_PARTICIPANTS = "INSERT INTO PARTICIPANTS VALUES ('%s','%s')";
    public static final String SELECT_FROM_PARTICIPANTS = "SELECT * FROM PARTICIPANTS WHERE id = '%s'";
    public static final String DELETE_FROM_PARTICIPANTS = "DELETE FROM PARTICIPANTS WHERE id = '%s'";

    public static final String SELECT_ALL_FROM_RACE = "SELECT * FROM RACE";
    public static final String INSERT_INTO_RACE = "INSERT INTO RACE VALUES ('%s','%s','%s','%s')";
    public static final String SELECT_FROM_RACE = "SELECT * FROM RACE WHERE raceId = '%s'";
    public static final String DELETE_FROM_RACE = "DELETE FROM RACE WHERE raceId = '%s'";

    public static final String SELECT_ALL_FROM_SWIMMING_DISCIPLINE = "SELECT * FROM SWIMMING_DISCIPLINE";
    public static final String INSERT_INTO_SWIMMING_DISCIPLINE = "INSERT INTO SWIMMING_DISCIPLINE VALUES ('%s','%s','%d','%d','%s','%d','%b')";
    public static final String SELECT_FROM_SWIMMING_DISCIPLINE = "SELECT * FROM SWIMMING_DISCIPLINE WHERE resultsId = '%s' AND disciplineType = '%s'";
    public static final String DELETE_FROM_SWIMMING_DISCIPLINE = "DELETE FROM SWIMMING_DISCIPLINE WHERE resultsId = '%s' AND disciplineType = '%s'";

    public static final String SELECT_ALL_FROM_RUNNING_DISCIPLINE = "SELECT * FROM RUNNING_DISCIPLINE";
    public static final String INSERT_INTO_RUNNING_DISCIPLINE = "INSERT INTO RUNNING_DISCIPLINE VALUES ('%s','%s','%d','%d','%s','%s')";
    public static final String SELECT_FROM_RUNNING_DISCIPLINE = "SELECT * FROM RUNNING_DISCIPLINE WHERE resultsId = '%s' AND disciplineType = '%s'";
    public static final String DELETE_FROM_RUNNING_DISCIPLINE = "DELETE FROM RUNNING_DISCIPLINE WHERE resultsId = '%s' AND disciplineType = '%s'";

    public static final String SELECT_ALL_FROM_CYCLING_DISCIPLINE = "SELECT * FROM CYCLING_DISCIPLINE";
    public static final String INSERT_INTO_CYCLING_DISCIPLINE = "INSERT INTO CYCLING_DISCIPLINE VALUES ('%s','%s','%d','%d','%s','%d','%s','%s')";
    public static final String SELECT_FROM_CYCLING_DISCIPLINE = "SELECT * FROM CYCLING_DISCIPLINE WHERE resultsId = '%s' AND disciplineType = '%s'";
    public static final String DELETE_FROM_CYCLING_DISCIPLINE = "DELETE FROM CYCLING_DISCIPLINE WHERE resultsId = '%s' AND disciplineType = '%s'";

    public static final String SELECT_ALL_FROM_RESULTS_BY_PARTICIPANT = "SELECT * FROM RESULTS WHERE participantId = '%s'";
    public static final String SELECT_ALL_FROM_RESULTS = "SELECT * FROM RESULTS";
    public static final String SELECT_ALL_FROM_RESULTS_BY_RACE = "SELECT * FROM RESULTS WHERE raceId = '%s'";
    public static final String INSERT_INTO_RESULTS = "INSERT INTO RESULTS VALUES ('%s','%s','%s','%s','%s')";
    public static final String SELECT_FROM_RESULTS = "SELECT * FROM RESULTS WHERE raceId = '%s' AND participantId = '%s'";
    public static final String SELECT_FROM_RESULTS1 = "SELECT * FROM RESULTS WHERE resultsId = '%s'";
    public static final String DELETE_FROM_RESULTS = "DELETE FROM RESULTS WHERE raceId = '%s' AND participantId = '%s'";

    public final static String DROP_PARTICIPANTS_TABLE = "DROP TABLE IF EXISTS PARTICIPANTS";
    public final static String DROP_RACE_TABLE = "DROP TABLE IF EXISTS RACE";
    public final static String DROP_SWIMMING_DISCIPLINE_TABLE = "DROP TABLE IF EXISTS SWIMMING_DISCIPLINE";
    public final static String DROP_RUNNING_DISCIPLINE_TABLE = "DROP TABLE IF EXISTS RUNNING_DISCIPLINE";
    public final static String DROP_CYCLING_DISCIPLINE_TABLE = "DROP TABLE IF EXISTS CYCLING_DISCIPLINE";
    public final static String DROP_RESULTS_TABLE = "DROP TABLE IF EXISTS RESULTS";

    public static final String ADD_PARTICIPANT = "ap";
    public static final String DELETE_PARTICIPANT = "dp";
    public static final String GET_PARTICIPANT = "gp";
    public static final String GET_ALL_PARTICIPANTS = "lp";

    public static final String ADD_RACE = "ar";
    public static final String DELETE_RACE = "dr";
    public static final String GET_RACE = "gr";
    public static final String GET_ALL_RACES = "lr";

    public static final String ADD_DISCIPLINE = "ad";
    public static final String DELETE_DISCIPLINE = "dd";
    public static final String GET_DISCIPLINE = "gd";
    public static final String GET_ALL_DISCIPLINES = "ld";

    public static final String ADD_RESULTS = "art";
    public static final String DELETE_RESULTS = "drt";
    public static final String GET_RESULTS = "grt";
    public static final String GET_ALL_RESULTS = "lrt";

    public static final String ANALYSIS_STAGE_WINS = "asw";
    public static final String GAP_ANALYSIS = "gap";
}
