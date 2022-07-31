package ru.sfedu.arai;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.arai.api.DataProvider;
import ru.sfedu.arai.api.DataProviderCsv;
import ru.sfedu.arai.api.DataProviderJdbc;
import ru.sfedu.arai.api.DataProviderXml;
import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.Discipline;
import ru.sfedu.arai.model.Participant;
import ru.sfedu.arai.model.Race;
import ru.sfedu.arai.model.Results;
import ru.sfedu.arai.utils.ConfigurationUtil;
import ru.sfedu.arai.utils.CreateUtil;
import ru.sfedu.arai.utils.DisciplineUtil;
import ru.sfedu.arai.utils.RaceTypeUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public class RaiAnalysis {
    private static final Logger log = LogManager.getLogger(RaiAnalysis.class);

    public static void main(String[] args) throws Exception {
        DataProvider dataProvider = setDataProvider(args[0]);
        setOperation(args, dataProvider);
    }

    private static DataProvider setDataProvider(String arg){
        DataProvider dataProvider = new DataProviderCsv();
        switch (arg) {
            case "XML" -> dataProvider = new DataProviderXml();
            case "CSV" -> dataProvider = new DataProviderCsv();
            case "H2" -> dataProvider = new DataProviderJdbc();
            default -> log.info("invalid data type, dataProvider will be installed by default");
        }
        log.info("Set {} data provider.", dataProvider.getClass().getSimpleName());
        return dataProvider;
    }

    private static void setOperation(String[] args, DataProvider dataProvider) throws Exception {

        if(args[1].equals(Constants.ADD_PARTICIPANT)){
            log.info("Adding participant : ".concat(args[2]));
            Participant participant = CreateUtil.createParticipant(args[2]);
            dataProvider.addParticipantRecord(participant);
        }

        if(args[1].equals(Constants.DELETE_PARTICIPANT)){
            log.info("Deleting participant by id: ".concat(args[2]));
            dataProvider.deleteParticipantRecord(args[2]);
        }

        if(args[1].equals(Constants.GET_PARTICIPANT)){
            log.info("Getting participant by id: ".concat(args[2]));
            Optional<Participant> participant = dataProvider.getParticipantById(args[2]);
            if(participant.isEmpty()) log.info("Participant doesn't exist");
            else log.info(participant.get());
        }

        if(args[1].equals(Constants.GET_ALL_PARTICIPANTS)){
            log.info("Getting all participants: ");
            Optional<List<Participant>> participantList = dataProvider.getAllParticipants();
            if (participantList.isEmpty()) log.info("No participants");
            else participantList.get().forEach(log::info);
        }

        if(args[1].equals(Constants.ADD_RACE)){
            log.info("Adding race: ");
            Race race = CreateUtil.createRace(args[2]);
            dataProvider.addRaceRecord(race);
        }

        if(args[1].equals(Constants.DELETE_RACE)){
            log.info("Deleting race id: ".concat(args[2]));
            dataProvider.deleteRaceRecord(args[2]);
        }

        if(args[1].equals(Constants.GET_RACE)){
            log.info("Getting race by id: ".concat(args[2]));
            Optional<Race> race = dataProvider.getRaceById(args[2]);
            if(race.isEmpty()) log.info("Race doesn't exist");
            else log.info(race.get());
        }


        if(args[1].equals(Constants.GET_ALL_RACES)){
            log.info("Getting all races: ");
            Optional<List<Race>> raceList = dataProvider.getAllRaces();
            if (raceList.isEmpty()) log.info("No races");
            else raceList.get().forEach(log::info);
        }

        if(args[1].equals(Constants.ADD_DISCIPLINE)){
            log.info("adding result by discipline ".concat(args[2]));
            Discipline discipline;
            switch(DisciplineUtil.disciplineTypeParser(args[2])){
                case RUNNING:
                    discipline = CreateUtil.createRunningDisciplineRecord(Integer.parseInt(args[3]), args[4], Float.parseFloat(args[5]), Integer.parseInt(args[6]), args[7]);
                    dataProvider.addDisciplineRecord(discipline);
                    break;
                case SWIMMING:
                    discipline = CreateUtil.createSwimmingDisciplineRecord(Integer.parseInt(args[3]), args[4], Float.parseFloat(args[5]), Integer.parseInt(args[6]), Integer.parseInt(args[7]), Boolean.parseBoolean(args[8]));
                    dataProvider.addDisciplineRecord(discipline);
                    break;
                case CYCLING:
                    discipline = CreateUtil.createCyclingDisciplineRecord(Integer.parseInt(args[3]), args[4], Float.parseFloat(args[5]), Integer.parseInt(args[6]), Long.parseLong(args[7]), args[8], args[9]);
                    dataProvider.addDisciplineRecord(discipline);
                    break;
                default:
                    throw new Exception("This discipline doesn't exist");
            }
            log.debug("disciplineBean added");
        }

        if(args[1].equals(Constants.DELETE_DISCIPLINE)){
            log.info("Deleting result by ".concat(args[2]));
            dataProvider.deleteDisciplineRecord(args[3], DisciplineUtil.disciplineTypeParser(args[2]));
        }

        if(args[1].equals(Constants.GET_DISCIPLINE)){
            log.info("Getting result by discipline: ". concat(args[3]));
            Optional<Discipline> discipline = dataProvider.getDisciplineRecordById(args[2], DisciplineUtil.disciplineTypeParser(args[3]));
            if(discipline.isEmpty()) log.info("Discipline record doesn't exist");
            else log.info(discipline.get());
        }

        if(args[1].equals(Constants.GET_ALL_DISCIPLINES)){
            log.info("Getting all results by discipline: ". concat(args[2]));
            Optional<List<Discipline>> disciplineList = dataProvider.getAllDisciplineRecords(DisciplineUtil.disciplineTypeParser(args[2]));
            if (disciplineList.isEmpty()) log.info("No discipline results");
            else disciplineList.get().forEach(log::info);
        }

        if(args[1].equals(Constants.ADD_RESULTS)){
            Results results = CreateUtil.createResultsRecord(Integer.parseInt(args[2]), args[3], args[4], Float.parseFloat(args[5]));
            log.info("Adding result ".concat(results.toString()));
            dataProvider.addResultsRecord(results);
            log.info("Record added with resultsId: ".concat(results.getResultsId()));
        }

        if(args[1].equals(Constants.DELETE_RESULTS)){
            log.info("Deleting result record");
            dataProvider.deleteResultsRecord(args[2], args[3]);
        }

        if(args[1].equals(Constants.GET_RESULTS)){
            log.info("Getting result record: ");
            Optional<Results> results = dataProvider.getResultsRecordById(args[2]);
            if(results.isEmpty()) log.info("Result record doesn't exist");
            else log.info(results);
        }

        if(args[1].equals(Constants.GET_ALL_RESULTS)){
            log.info("Getting all results");
            Optional<List<Results>> resultsList = dataProvider.getAllResults();
            if (resultsList.isEmpty()) log.info("No results");
            else resultsList.get().forEach(log::info);
        }


        if(args[1].equals(Constants.ANALYSIS_STAGE_WINS)){
            log.info("analysis stage wins by {}", args[3]);
            List<Float> floatList = dataProvider.analysisStageWins(RaceTypeUtil.raceTypeParser(args[2]), DisciplineUtil.disciplineTypeParser(args[3]), args[4]).get();
            log.info("winRate: {}", floatList);
        }

        if(args[1].equals(Constants.GAP_ANALYSIS)){
            log.info("Gap analysis by participant Id: {}", args[3]);
            List<Float> floatList = dataProvider.gapAnalysis(RaceTypeUtil.raceTypeParser(args[2]), args[3], DisciplineUtil.disciplineTypeParser(args[4])).get();
            log.info("Gap: {}", floatList);
        }

        if(args[1].equals("ctd")){
            CreateUtil.createTestData(dataProvider);
        }

        if(args[1].equals("dtd")){
            CreateUtil.deleteTestData(dataProvider);
        }
    }

    private static void logBasicSystemInfo() {
        log.info("Launching the application...");
        log.info(
                "Operating System: " + System.getProperty("os.name") + " "
                        + System.getProperty("os.version")
        );
        log.info("JRE: " + System.getProperty("java.version"));
        log.info("Java Launched From: " + System.getProperty("java.home"));
        log.info("Class Path: " + System.getProperty("java.class.path"));
        log.info("Library Path: " + System.getProperty("java.library.path"));
        log.info("User Home Directory: " + System.getProperty("user.home"));
        log.info("User Working Directory: " + System.getProperty("user.dir"));
        log.info("Test INFO logging.");
        log.debug("debug message!");

    }
}
