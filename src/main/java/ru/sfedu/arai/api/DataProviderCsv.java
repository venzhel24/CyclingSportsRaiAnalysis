package ru.sfedu.arai.api;

import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.simpleframework.xml.Default;
import ru.sfedu.arai.Constants;
import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.*;
import ru.sfedu.arai.utils.ConfigurationUtil;
import ru.sfedu.arai.utils.DisciplineUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.sfedu.arai.api.MongoProvider.COMMAND_TYPE.*;
import static ru.sfedu.arai.api.MongoProvider.RepositoryType.CSV;

public class DataProviderCsv implements DataProvider{
    private static String PATH;
    private static final String EXTENSION = Constants.CSV_EXTENSION;
    private static final Logger log = LogManager.getLogger(DataProviderCsv.class);

    public DataProviderCsv() {
        PATH = Constants.CSV_PATH;
    }

    public DataProviderCsv(String filePath){
        PATH = filePath;
    }

    @Override
    public Optional<List<Participant>> getAllParticipants(){
        try {
            log.debug("getAllParticipants[1]: ");
            List<Participant> participantsList = readFile(Participant.class);
            log.debug("Participants received[2]: ");
            return Optional.of(participantsList);
        }
        catch(Exception e){
            log.error("showAllParticipants Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Race>> getAllRaces(){
        try {
            log.debug("getAllRaces[1]:");
            List<Race> racesList = readFile(Race.class);
            log.debug("Races received[2]: ");
            return Optional.of(racesList);
        }
        catch(Exception e){
            log.error("showAllRaces Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Results>> getAllResults(){
        try {
            log.debug("getAllResults[1]: ");
            List<Results> resultsList = readFile(Results.class);
            log.debug("Results received[2]: ");
            return Optional.of(resultsList);
        }
        catch (Exception e){
            log.error("showAllResultsByRace Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Discipline>> getAllDisciplineRecords(DisciplineType disciplineType){
        try {
            log.debug("getAllDisciplineRecords[1]:");
            Discipline disciplineBean = DisciplineUtil.getDisciplineBeanByType(disciplineType);
            List<Discipline> disciplinesList = readFile(disciplineBean.getClass());
            log.debug("DisciplineRecords received[2]: ");
            return Optional.of(disciplinesList);
        }
        catch(Exception e){
            log.error("getAllDisciplineRecords Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }


    public Optional<List<Results>> getAllResultsByRace(String raceId){
        try {
            log.debug("getAllResultsByRace: {}[1]: ", raceId);
            List<Results> resultsList = readFile(Results.class);
            resultsList = resultsList.stream().filter(x -> x.getRaceId().equals(raceId)).toList();
            log.debug("Results by race received[2]: ");
            return Optional.of(resultsList);
        }
        catch (Exception e){
            log.error("showAllResultsByRace Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    public Optional<List<Results>> getAllResultsByParticipant(String participantId){
        try {
            log.debug("getAllResultsByParticipant: {}[1]: ", participantId);
            List<Results> resultsList = readFile(Results.class);
            resultsList = resultsList.stream().filter(x -> x.getParticipantId().equals(participantId)).toList();
            log.debug("Results by participant received[2]: ");
            return Optional.of(resultsList);
        }
        catch (Exception e){
            log.error("showAllResultsByRace Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }


    @Override
    public boolean addParticipantRecord(Participant participant){
        try{
            log.debug("addParticipantRecord[1]:");
            if (participant == null)
                throw new Exception("Adding record error, record equals null");
            List<Participant> participantsList = readFile(Participant.class);
            participantsList.add(participant);

            //MongoProvider.save(ADDED, CSV, participant);
            writeFile(participantsList, Participant.class);
            log.debug("Participant added[2]: ".concat(participant.toString()));
            return true;
        }
        catch (Exception e){
            log.error("addParticipantRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addRaceRecord(Race race){
        try{
            log.debug("addRaceRecord[1]:");
            if (race == null)
                throw new Exception("Adding record error, record equals null");
            List<Race> racesList = readFile(Race.class);
            racesList.add(race);

            writeFile(racesList, Race.class);
            log.debug("Race added[2]: ".concat(race.toString()));
            return true;
        }
        catch (Exception e){
            log.error("addRaceRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addDisciplineRecord(Discipline discipline){
        try{
            log.debug("addDisciplineRecord[1]:");

            if (discipline == null)
                throw new Exception("Adding record error, record equals null");

            List<Discipline> disciplinesList = readFile(discipline.getClass());
            disciplinesList.add(discipline);
            writeFile(disciplinesList, discipline.getClass());
            log.debug("Discipline record added[2]: ".concat(discipline.toString()));
            return true;
        }
        catch (Exception e){
            log.error("addDisciplineRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean addResultsRecord(Results results){
        try{
            log.debug("addResultsRecord[1]: {}", results);

            if (results == null)
                throw new Exception("Adding record error, record equals null");

            if(getParticipantById(results.getParticipantId()).isEmpty())
                throw new Exception("Adding record error, participant with id:".concat(results.getParticipantId()).concat(" doesn't exist"));

            if(getRaceById(results.getRaceId()).isEmpty())
                throw new Exception("Adding record error, race with id:".concat(results.getRaceId()).concat(" doesn't exist"));

            List<Results> resultsList = readFile(Results.class);
            resultsList.add(results);

            //MongoProvider.save(UPDATED, CSV, participant);
            writeFile(resultsList, Results.class);
            log.debug("ResultsRecord added[2]: ".concat(results.toString()));
            return true;
        }
        catch (Exception e){
            log.error("addResultsRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean deleteParticipantRecord(String id){
        try{
            log.debug("deleteParticipantRecord[1]:");
            List<Participant> participantsList = readFile(Participant.class);
            List<Participant> participantListClone = List.copyOf(participantsList);

            participantsList.removeIf(participant -> participant.getId().equals(id));
            if(participantsList.equals(participantListClone)){
                throw new Exception("Participant with id " + id + " not found");
            }

            //MongoProvider.save(DELETED, CSV, participantListClone.stream().filter(participant -> participant.getId().equals(id)).findAny().get());

            writeFile(participantsList, Participant.class);
            log.debug("Participant deleted[2]: ".concat(participantListClone
                    .stream()
                    .filter(bean -> (bean).getId().equals(id))
                    .findAny().get().toString()));
            return true;
        }
        catch(Exception e){
            log.error("deleteParticipantRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteRaceRecord(String id){
        try{
            log.debug("deleteRaceRecord[1]:");
            List<Race> racesList = readFile(Race.class);
            List<Race> racesListClone = List.copyOf(racesList);
            racesList.removeIf(race -> race.getRaceId().equals(id));

            if(racesList.equals(racesListClone)){
                throw new Exception("Race with raceId " + id + " not found");
            }

            writeFile(racesList, Race.class);
            log.debug("Race deleted[2]: ".concat(racesListClone
                    .stream()
                    .filter(bean -> (bean).getRaceId().equals(id))
                    .findAny().get().toString()));
            return true;
        }
        catch(Exception e){
            log.error("deleteRaceRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteDisciplineRecord(String id, DisciplineType disciplineType){
        try{
            log.debug("deleteDisciplineRecord[1]:");
            Discipline disciplineBean = DisciplineUtil.getDisciplineBeanByType(disciplineType);
            List<Discipline> disciplinesList = readFile(disciplineBean.getClass());
            List<Discipline> disciplinesListClone = List.copyOf(disciplinesList);;
            disciplinesList.removeIf(x -> x.getResultsId().equals(id) && x.getDisciplineType().equals(disciplineType));

            if(disciplinesList.equals(disciplinesListClone)){
                throw new Exception("DisciplineRecord with id " + id + " not found");
            }

            writeFile(disciplinesList, disciplineBean.getClass());
            log.debug("DisciplineRecord deleted[2]: ".concat(disciplinesListClone
                    .stream()
                    .filter(bean -> (bean).getResultsId().equals(id) && (bean).getDisciplineType().equals(disciplineType))
                    .findAny().get().toString()));
            return true;
        }
        catch(Exception e){
            log.error("deleteParticipantRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteResultsRecord(String participantId, String raceId){
        try{
            log.debug("deleteResultsRecord[1]:");
            List<Results> resultsList = readFile(Results.class);
            List<Results> resultsListClone = List.copyOf(resultsList);
            resultsList.removeIf(results -> (results.getParticipantId().equals(participantId) && results.getRaceId().equals(raceId)));

            if(resultsList.equals(resultsListClone)){
                throw new Exception("results with participantId " + participantId + " or with raceId" + raceId + " not found");
            }

            writeFile(resultsList, Results.class);
            log.debug("resultsRecord deleted[2]: ".concat(resultsListClone
                    .stream()
                    .filter(bean -> (bean.getParticipantId().equals(participantId) && bean.getRaceId().equals(raceId)))
                    .findAny().get().toString()));
            return true;
        }
        catch(Exception e){
            log.error("deleteResultsRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean updateParticipantRecord(Participant participant, String id){
        try{
            log.debug("updateParticipant[1]:");
            List<Participant> participantsList = readFile(Participant.class);
            int index = participantsList.indexOf(getParticipantById(id).get());
            participantsList.set(index, participant);
            writeFile(participantsList, Participant.class);
            log.debug("Updating complete[2]: ");
            //MongoProvider.save(UPDATED, CSV, participant);
            return true;
        }
        catch(Exception e){
            log.error("updateParticipant Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateRaceRecord(Race race, String id){
        try{
            log.debug("updateRace[1]: ");
            List<Race> racesList = readFile(Race.class);
            int index = racesList.indexOf(getRaceById(id).get());
            racesList.set(index, race);
            writeFile(racesList, Race.class);
            log.debug("Updating complete[2]: ");
            return true;
        }
        catch(Exception e){
            log.error("updateRace Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateDisciplineRecord(Discipline discipline, String id){
        try{
            log.debug("updateDisciplineRecord[1]: ");
            Discipline disciplineBean = DisciplineUtil.getDisciplineBeanByType(discipline.getDisciplineType());
            List<Discipline> disciplinesList = readFile(disciplineBean.getClass());
            int index = disciplinesList.indexOf(getDisciplineRecordById(id, discipline.getDisciplineType()).get());
            disciplinesList.set(index, discipline);
            writeFile(disciplinesList, disciplineBean.getClass());
            log.debug("Updating complete[2]: ");
            return true;
        }
        catch(Exception e){
            log.error("updateDisciplineRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateResultsRecord(Results results, String participantId, String raceId){
        try{
            log.debug("updateDisciplineRecord[1]: ");
            List<Results> resultsList = readFile(Results.class);
            int index = resultsList.indexOf(getResultsRecordById(participantId, raceId).get());
            resultsList.set(index, results);
            writeFile(resultsList, Results.class);
            log.debug("Updating complete[2]: ");
            return true;
        }
        catch(Exception e){
            log.error("updateResultsRecord Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }


    @Override
    public Optional<Participant> getParticipantById(String id) {
        try {
            log.debug("getParticipantId[1]: ");
            List<Participant> participantsList = readFile(Participant.class);
            Participant participantBean = participantsList.stream().filter(beans -> (beans).getId().equals(id)).findAny().get();
            log.debug("Participant received[2]: ".concat(participantBean.toString()));
            return Optional.of(participantBean);
        }
        catch(Exception e){
            log.error("getParticipantById Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Race> getRaceById(String id) {
        try {
            log.debug("getRaceById[1]:");
            List<Race> racesList = readFile(Race.class);
            Race raceBean = racesList.stream().filter(beans -> (beans).getRaceId().equals(id)).findAny().get();
            log.debug("Race received[2]: ".concat(raceBean.toString()));
            return Optional.of(raceBean);
        }
        catch(Exception e){
            log.error("getRaceById Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Discipline> getDisciplineRecordById(String id, DisciplineType disciplineType) {
        try {
            log.debug("getDisciplineRecordById[1]:");
            Discipline disciplineBean = DisciplineUtil.getDisciplineBeanByType(disciplineType);
            List<Discipline> disciplinesList = readFile(disciplineBean.getClass());
            disciplineBean = disciplinesList.stream().filter(beans -> (beans).getResultsId().equals(id) && (beans).getDisciplineType().equals(disciplineType)).findAny().get();
            log.debug("DisciplineRecord received[2]: ".concat(disciplineBean.toString()));
            return Optional.of(disciplineBean);
        }
        catch(Exception e){
            log.error("getDisciplineRecordById Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Results> getResultsRecordById(String ParticipantId, String RaceId) {
        try {
            log.debug("getResultsById[1]:");
            List<Results> resultsList = readFile(Results.class);
            Results resultsBean = resultsList.stream().filter(beans -> ((beans).getParticipantId().equals(ParticipantId) && (beans).getRaceId().equals(RaceId))).findAny().get();
            log.debug("Results record received[2]: ".concat(resultsBean.toString()));
            return Optional.of(resultsBean);
        }
        catch(Exception e){
            log.error("getResultsRecordById Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Results> getResultsRecordById(String resultsId) {
        try {
            log.debug("getResultsById[1]: ");
            List<Results> resultsList = readFile(Results.class);
            Results resultsBean = resultsList.stream().filter(beans -> ((beans).getResultsId().equals(resultsId))).findAny().get();
            log.debug("Results record received[2]: ".concat(resultsBean.toString()));
            return Optional.of(resultsBean);
        }
        catch(Exception e){
            log.error("getResultsRecordById Error ");
            log.error(e.getClass().getName() + ": " + e.getMessage());
            return Optional.empty();
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
            if(disciplineList.isEmpty()) throw new Exception("disciplineList is null");
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
                        .filter(x -> (x.getParticipantId().equals(participantId) && getRaceById(x.getRaceId()).get().getRaceType().equals(raceType)))
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


    private <T> void writeFile(List<T> beans, Class<?> clazz)  {
        try {
            log.debug("Start saveFile[1]: ");
            FileWriter sw = new FileWriter(ConfigurationUtil.getConfigurationEntry(PATH)
                    .concat(clazz.getSimpleName().toLowerCase())
                    .concat(ConfigurationUtil.getConfigurationEntry(EXTENSION)));
            CSVWriter writer = new CSVWriter(sw);
            StatefulBeanToCsv<T> beanToCsv = new StatefulBeanToCsvBuilder<T>(writer).build();
            beanToCsv.write(beans);
            writer.close();
            sw.close();
            log.debug("File Saved[2]: ");
        }
        catch(Exception e) {
            log.error("saveFile Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    private <T> List<T> readFile(Class<?> clazz) {
        List<T> loadedBeans = null;
        try {
            log.debug("Start readFile[1]:");
            loadedBeans = new CsvToBeanBuilder(new FileReader(checkFile(clazz)))
                    .withType(clazz)
                    .build()
                    .parse();
            log.debug("Beans loaded[2]:");
        }
        catch(Exception e){
            log.error("loadBeanList Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
        }
        return loadedBeans;
    }

    private File checkFile(Class<?> clazz) throws Exception {
        log.debug("Checking file[1]: ");
        File folderPath = new File(ConfigurationUtil.getConfigurationEntry(PATH));
        if (!folderPath.exists())
            if(!folderPath.mkdirs())
                throw new Exception("create folder error");
        File file = new File(ConfigurationUtil.getConfigurationEntry(PATH)
                .concat(clazz.getSimpleName().toLowerCase())
                .concat(ConfigurationUtil.getConfigurationEntry(EXTENSION)));
        if (!file.exists()) {
            if (!file.createNewFile())
                throw new IllegalArgumentException("create " + file.getAbsolutePath() + " error");
            log.debug("File created: ".concat(clazz.getSimpleName().toLowerCase())
                    .concat(ConfigurationUtil.getConfigurationEntry(EXTENSION)));
        }
        log.debug("File checked[2]:".concat(file.toString()));
        return file;
    }

    public void deleteFile(Class<?> clazz) throws Exception {
        try {
            log.debug("Deleting file[1]: ");
            File file = new File(ConfigurationUtil.getConfigurationEntry(PATH)
                    .concat(clazz.getSimpleName().toLowerCase())
                    .concat(ConfigurationUtil.getConfigurationEntry(EXTENSION)));
            //file.delete();
            Files.deleteIfExists(file.toPath());
            log.debug("File was deleted[2]: {}", file.getAbsolutePath());
        }
        catch(Exception e){
            log.error("deleteFile Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    public void deleteFolder(String folderPath){
        try{
            log.debug("Deleting folder path[1]: ");
            File file = new File(folderPath);
            file.delete();
            log.debug("Folder was deleted[2]: {}", file.getAbsolutePath());
        }
        catch(Exception e){
            log.error("deleteFolder Error");
            log.error(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
