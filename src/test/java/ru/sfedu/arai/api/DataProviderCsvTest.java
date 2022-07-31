package ru.sfedu.arai.api;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.junit.jupiter.api.*;
import ru.sfedu.arai.Constants;
import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.*;

import ru.sfedu.arai.InstanceCreate;
import ru.sfedu.arai.utils.ConfigurationUtil;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataProviderCsvTest{
    private static final Logger log = LogManager.getLogger(DataProviderCsvTest.class);

    static DataProviderCsv dataProviderCsv = new DataProviderCsv(Constants.TEST_CSV_PATH);
    static Discipline swimmingDisciplineBean = new SwimmingDiscipline();
    static Discipline runningDisciplineBean = new RunningDiscipline();
    static Discipline cyclingDisciplineBean = new CyclingDiscipline();
    static Results resultsBean = new Results();
    static Participant participantBean = new Participant();
    static Race raceBean = new Race();


    @BeforeAll
    static void setUp() throws Exception {
        dataProviderCsv.deleteFile(swimmingDisciplineBean.getClass());
        dataProviderCsv.deleteFile(runningDisciplineBean.getClass());
        dataProviderCsv.deleteFile(cyclingDisciplineBean.getClass());
        dataProviderCsv.deleteFile(resultsBean.getClass());
        dataProviderCsv.deleteFile(participantBean.getClass());
        dataProviderCsv.deleteFile(raceBean.getClass());
        InstanceCreate.createTestData(dataProviderCsv);
    }

    @AfterAll
    static void clearFiles() throws Exception {
        dataProviderCsv.deleteFile(swimmingDisciplineBean.getClass());
        dataProviderCsv.deleteFile(runningDisciplineBean.getClass());
        dataProviderCsv.deleteFile(cyclingDisciplineBean.getClass());
        dataProviderCsv.deleteFile(resultsBean.getClass());
        dataProviderCsv.deleteFile(raceBean.getClass());
        dataProviderCsv.deleteFile(participantBean.getClass());
    }

    @Test
    public void testShowAllParticipants() {
        log.info("ShowAllParticipants test");
        Optional<List<Participant>> participantList = dataProviderCsv.getAllParticipants();
        Assertions.assertTrue(participantList.isPresent());
        participantList.get().forEach(log::info);
    }

    @Test
    public void testShowAllRaces() throws ParseException {
        log.info("show all races test");

        Optional<List<Race>> raceList = dataProviderCsv.getAllRaces();
        Assertions.assertTrue(raceList.isPresent());
        raceList.get().forEach(log::info);
    }

    @Test
    public void addParticipantRecordPositive() {
        log.info("addParticipant Test success");
        participantBean = InstanceCreate.createParticipant("danii");

        Assertions.assertTrue(dataProviderCsv.addParticipantRecord(participantBean));
        log.info("Participant added");
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void addParticipantRecordNegative() {
        log.info("addParticipant Test fail");
        Assertions.assertFalse(dataProviderCsv.addParticipantRecord(null));
    }

    @Test
    public void addRaceRecordPositive() throws ParseException {
        log.info("addRace Test success");
        raceBean = InstanceCreate.createRace("Monio");

        Assertions.assertTrue(dataProviderCsv.addRaceRecord(raceBean));
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void addRaceRecordNegative() {
        log.info("addRace Test fail");
        Assertions.assertFalse(dataProviderCsv.addRaceRecord(null));
    }

    @Test
    public void addDisciplineRecordPositive() {
        log.info("addDisciplineRecord Test success");
        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(987, 500, 1, 14, true, "waadw");

        Assertions.assertTrue(dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean));
        dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void addDisciplineRecordNegative() {
        log.info("addDisciplineRecord Test fail");

        Assertions.assertFalse(dataProviderCsv.addDisciplineRecord(null));
    }

    @Test
    public void addResultsRecordPositive() throws ParseException {
        log.info("addResultsRecord Test success");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);

        Assertions.assertTrue(dataProviderCsv.addResultsRecord(resultsBean));

        dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void addResultsRecordNegative() throws ParseException {
        log.info("addResultsRecord Test fail");
        participantBean = InstanceCreate.createParticipant("daniL");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderCsv.addParticipantRecord(participantBean);

        Assertions.assertFalse(dataProviderCsv.addResultsRecord(resultsBean));

        //dataProviderCsv.deleteResultsRecord(participantBean.getId(), "wrongRaceId");
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getParticipantByIdPositive(){
        log.info("getParticipantById success");

        participantBean = InstanceCreate.createParticipant("danii");

        dataProviderCsv.addParticipantRecord(participantBean);
        Assertions.assertEquals(participantBean, dataProviderCsv.getParticipantById(participantBean.getId()).get());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void getParticipantByIdNegative() throws IOException {
        log.info("getParticipantById fail");

        participantBean = InstanceCreate.createParticipant("danii");
        dataProviderCsv.addParticipantRecord(participantBean);
        String xtr = participantBean.getId();

        participantBean = InstanceCreate.createParticipant("daniiil");
        dataProviderCsv.addParticipantRecord(participantBean);
        Assertions.assertNotEquals(participantBean, dataProviderCsv.getParticipantById(xtr).get());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteParticipantRecord(xtr);
    }

    @Test
    public void getRaceByIdPositive() throws ParseException {
        log.info("getRaceById success");

        raceBean = InstanceCreate.createRace("Monio");

        dataProviderCsv.addRaceRecord(raceBean);
        Assertions.assertEquals(raceBean, dataProviderCsv.getRaceById(raceBean.getRaceId()).get());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getRaceByIdNegative() throws IOException, ParseException {
        log.info("getRaceById fail");

        raceBean = InstanceCreate.createRace("Rucca");
        dataProviderCsv.addRaceRecord(raceBean);
        String xtr = raceBean.getRaceId();

        raceBean = InstanceCreate.createRace("Martell");
        dataProviderCsv.addRaceRecord(raceBean);
        Assertions.assertNotEquals(raceBean, dataProviderCsv.getRaceById(xtr).get());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
        dataProviderCsv.deleteRaceRecord(xtr);
    }

    @Test
    public void getDisciplineRecordByIdPositive() {
        log.info("getDisciplineRecordById Test success");
        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "segseg");

        dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean);

        Assertions.assertEquals(swimmingDisciplineBean, dataProviderCsv.getDisciplineRecordById(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType()).get());

        dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void getDisciplineRecordByIdNegative() {
        log.info("getDisciplineRecordById Test fail");
        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(901, 1500, 4, 10, true, "someId2");

        dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean);
        dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean2);

        Assertions.assertNotEquals(swimmingDisciplineBean, dataProviderCsv.getDisciplineRecordById(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean.getDisciplineType()).get());

        dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
        dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean2.getDisciplineType());
    }

    @Test
    public void getResultsRecordPositive() throws ParseException {
        log.info("getResultsRecord Test success");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);
        dataProviderCsv.addResultsRecord(resultsBean);

        Assertions.assertEquals(resultsBean, dataProviderCsv.getResultsRecordById(participantBean.getId(), raceBean.getRaceId()).get());

        dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getResultsRecordNegative() throws ParseException {
        log.info("getResultsRecord Test fail");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);
        dataProviderCsv.addResultsRecord(resultsBean);

        Assertions.assertFalse(dataProviderCsv.getResultsRecordById("wrongParticipantId", raceBean.getRaceId()).isPresent());

        dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void deleteParticipantRecordPositive() throws IOException{
        log.info("deleteParticipantRecord success");

        participantBean = InstanceCreate.createParticipant("danii");
        dataProviderCsv.addParticipantRecord(participantBean);
        Assertions.assertTrue(dataProviderCsv.deleteParticipantRecord(participantBean.getId()));
    }

    @Test
    public void deleteParticipantRecordNegative() throws IOException{
        log.info("deleteParticipantRecord fail");

        participantBean = InstanceCreate.createParticipant("danii");

        Assertions.assertFalse(dataProviderCsv.deleteParticipantRecord(participantBean.getId()));
    }

    @Test
    public void deleteRaceRecordPositive() throws IOException, ParseException {
        log.info("deleteRaceRecord success");

        raceBean = InstanceCreate.createRace("Kontio");
        dataProviderCsv.addRaceRecord(raceBean);
        Assertions.assertTrue(dataProviderCsv.deleteRaceRecord(raceBean.getRaceId()));
    }

    @Test
    public void deleteRaceRecordNegative() throws IOException, ParseException {
        log.info("deleteRaceRecord fail");

        raceBean = InstanceCreate.createRace("Konyio");

        Assertions.assertFalse(dataProviderCsv.deleteParticipantRecord(participantBean.getId()));
    }

    @Test
    public void deleteDisciplineRecordPositive() throws IOException, ParseException {
        log.info("deleteDisciplineRecord success");

        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");

        dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean);

        Assertions.assertTrue(dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType()));
    }

    @Test
    public void deleteDisciplineRecordNegative() throws IOException, ParseException {
        log.info("deleteDisciplineRecord fail");

        Assertions.assertFalse(dataProviderCsv.deleteDisciplineRecord("wrongId", DisciplineType.SWIMMING));
    }


    @Test
    public void deleteResultsRecordPositive() throws ParseException {
        log.info("deleteResultsRecord success");

        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);
        dataProviderCsv.addResultsRecord(resultsBean);

        Assertions.assertTrue(dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId()));

        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void deleteResultsRecordNegative() throws ParseException {
        log.info("deleteResultsRecord fail");

        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);
        dataProviderCsv.addResultsRecord(resultsBean);

        Assertions.assertFalse(dataProviderCsv.deleteResultsRecord("wrongParticipantId", raceBean.getRaceId()));

        dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }


    @Test
    public void updateParticipantRecordPositive(){
        log.info("updateParticipantRecord success");

        participantBean = InstanceCreate.createParticipant("name1");
        Participant participantBean2 = InstanceCreate.createParticipant("name2");
        dataProviderCsv.addParticipantRecord(participantBean);

        Assertions.assertTrue(dataProviderCsv.updateParticipantRecord(participantBean2, participantBean.getId()));

        dataProviderCsv.deleteParticipantRecord(participantBean2.getId());
    }

    @Test
    public void updateParticipantRecordNegative(){
        log.info("updateParticipantRecord fail");

        participantBean = InstanceCreate.createParticipant("name1");
        Participant participantBean2 = InstanceCreate.createParticipant("name2");
        dataProviderCsv.addParticipantRecord(participantBean);

        Assertions.assertFalse(dataProviderCsv.updateParticipantRecord(participantBean2, "wrongId"));

        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void updateRaceRecordPositive() throws ParseException {
        log.info("updateRaceRecord success");

        raceBean = InstanceCreate.createRace("race1");
        Race raceBean2 = InstanceCreate.createRace("race2");
        dataProviderCsv.addRaceRecord(raceBean);

        Assertions.assertTrue(dataProviderCsv.updateRaceRecord(raceBean2, raceBean.getRaceId()));

        dataProviderCsv.deleteRaceRecord(raceBean2.getRaceId());
    }

    @Test
    public void updateRaceRecordNegative() throws ParseException {
        log.info("updateRaceRecord fail");

        raceBean = InstanceCreate.createRace("race1");
        Race raceBean2 = InstanceCreate.createRace("race2");
        dataProviderCsv.addRaceRecord(raceBean);

        Assertions.assertFalse(dataProviderCsv.updateRaceRecord(raceBean2, "wrongId"));

        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void updateDisciplineRecordPositive() throws ParseException {
        log.info("updateDisciplineRecord success");

        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");
        dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean);
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 4, 10, true, "someId");

        Assertions.assertTrue(dataProviderCsv.updateDisciplineRecord(swimmingDisciplineBean2, swimmingDisciplineBean.getResultsId()));
        dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean2.getDisciplineType());
    }

    @Test
    public void updateDisciplineRecordNegative() throws ParseException {
        log.info("updateDisciplineRecord fail");

        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");
        dataProviderCsv.addDisciplineRecord(swimmingDisciplineBean);
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 4, 10, true, "someId");

        Assertions.assertFalse(dataProviderCsv.updateDisciplineRecord(swimmingDisciplineBean2, "wrongId"));
        dataProviderCsv.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void updateResultsRecordPositive() throws ParseException {
        log.info("updateResultsRecord Test success");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);
        Results resultsBean2 = InstanceCreate.createResultsRecord(2, participantBean.getId(), raceBean.getRaceId(), 3000);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);
        dataProviderCsv.addResultsRecord(resultsBean);

        Assertions.assertTrue(dataProviderCsv.updateResultsRecord(resultsBean2, resultsBean.getParticipantId(), resultsBean.getRaceId()));

        dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void updateResultsRecordNegative() throws ParseException {
        log.info("updateResultsRecord Test fail");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);
        Results resultsBean2 = InstanceCreate.createResultsRecord(2, participantBean.getId(), raceBean.getRaceId(), 3000);

        dataProviderCsv.addRaceRecord(raceBean);
        dataProviderCsv.addParticipantRecord(participantBean);
        dataProviderCsv.addResultsRecord(resultsBean);

        Assertions.assertFalse(dataProviderCsv.updateResultsRecord(resultsBean2, "wrongParticipant", resultsBean.getRaceId()));

        dataProviderCsv.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderCsv.deleteParticipantRecord(participantBean.getId());
        dataProviderCsv.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void analysisStageWinsPositive() throws ParseException {
        log.info("analysisStageWins success");
        List<Float> expected = List.of(1F, 0F);
        List<Float> resultList = dataProviderCsv.analysisStageWins(RaceType.TRIATHLON, DisciplineType.SWIMMING, "y").get();
        Assertions.assertEquals(expected, resultList);
        log.info("1 - actual: {}; expected: {}", resultList.get(0), expected.get(0));
        log.info("2 - actual: {}; expected: {}", resultList.get(1), expected.get(1));
    }

    @Test
    public void analysisStageWinsNegative() throws ParseException {
        log.info("analysisStageWins fail");
        Assertions.assertFalse(dataProviderCsv.analysisStageWins(RaceType.TRIATHLON, DisciplineType.CLIMBING, "y").isPresent());
    }

    @Test public void countingStageWithoutWinPositive() {
        log.info("countingStageWithoutWin success");
        List<Discipline> disciplineList = dataProviderCsv.getAllDisciplineRecords(DisciplineType.RUNNING).get();
        int expected = 0;
        int actual = dataProviderCsv.countingStageWithoutWin(disciplineList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test public void countingStageWithoutWinNegative() {
        log.info("countingStageWithoutWin fail");
        List<Discipline> disciplineList = null;
        Assertions.assertNull(dataProviderCsv.countingStageWithoutWin(disciplineList));
    }

    @Test public void countingStageWithWinPositive() {
        log.info("countingStageWithoutWin success");
        List<Discipline> disciplineList = dataProviderCsv.getAllDisciplineRecords(DisciplineType.CYCLING).get();
        int expected = 5;
        int actual = dataProviderCsv.countingStageWithWin(disciplineList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test public void countingStageWithWinNegative() {
        log.info("countingStageWithoutWin fail");
        List<Discipline> disciplineList = null;
        Assertions.assertNull(dataProviderCsv.countingStageWithWin(disciplineList));
    }

    @Test
    public void gapAnalysisPositive() throws ParseException {
        log.info("gapAnalysis success");
        List<Float> expected = List.of(400F, 200F);
        List<Float> gapList = dataProviderCsv.gapAnalysis(RaceType.TRIATHLON,"participantId3", DisciplineType.SWIMMING).get();
        Assertions.assertEquals(expected, gapList);
        log.info("1 - actual: {}; expected: {}", gapList.get(0), expected.get(0));
        log.info("2 - actual: {}; expected: {}", gapList.get(1), expected.get(1));
    }

    @Test
    public void gapAnalysisFail() throws ParseException {
        log.info("gapAnalysis fail");
        List<Float> expected = List.of(400F, 200F);
        Assertions.assertTrue(dataProviderCsv.gapAnalysis(RaceType.TRIATHLON, "nonExistentId", DisciplineType.SWIMMING).isPresent());
    }

    @Test
    public void countingTotalGapPositive(){
        log.info("countingTotalGap success");
        List<Results> resultsList = dataProviderCsv.getAllResults().get()
                .stream()
                .filter(x -> (x.getParticipantId().equals("participantId4") && dataProviderCsv.getRaceById(x.getRaceId()).get().getRaceType().equals(RaceType.TRIATHLON)))
                .toList();
        Float expected = 3000F;
        Float actual = dataProviderCsv.countingTotalGap(resultsList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test
    public void countingTotalGapNegative(){
        log.info("countingTotalGap fail");
        List<Results> resultsList = null;
        Float actual = dataProviderCsv.countingTotalGap(resultsList);
        Assertions.assertNull(actual);
    }

    @Test
    public void countingTotalDisciplineGapPositive(){
        log.info("countingTotalDisciplineGap success");
        List<Results> resultsList = dataProviderCsv.getAllResults().get()
                .stream()
                .filter(x -> (x.getParticipantId().equals("participantId4") && dataProviderCsv.getRaceById(x.getRaceId()).get().getRaceType().equals(RaceType.TRIATHLON)))
                .toList();
        Float expected = 1500F;
        Float actual = dataProviderCsv.countingTotalDisciplineGap(resultsList, DisciplineType.SWIMMING);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test
    public void countingTotalDisciplineGapNegative(){
        log.info("countingTotalDisciplineGap fail");
        List<Results> resultsList = null;
        Float actual = dataProviderCsv.countingTotalDisciplineGap(resultsList, DisciplineType.RUNNING);
        Assertions.assertNull(actual);
    }

}