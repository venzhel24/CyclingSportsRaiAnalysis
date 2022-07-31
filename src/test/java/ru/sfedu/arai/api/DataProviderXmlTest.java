package ru.sfedu.arai.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.sfedu.arai.Constants;
import ru.sfedu.arai.InstanceCreate;
import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.*;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataProviderXmlTest {
    private static final Logger log = LogManager.getLogger(DataProviderXmlTest.class);

    static DataProviderXml dataProviderXml = new DataProviderXml(Constants.TEST_XML_PATH);
    static Participant participantBean = new Participant();
    static Race raceBean = new Race();
    static Discipline swimmingDisciplineBean = new SwimmingDiscipline();
    static Discipline runningDisciplineBean = new RunningDiscipline();
    static Discipline cyclingDisciplineBean = new CyclingDiscipline();
    static Results resultsBean = new Results();

    private List<Participant> participantList;

    @BeforeAll
    static void setUp() throws Exception {
        dataProviderXml.deleteFile(swimmingDisciplineBean.getClass());
        dataProviderXml.deleteFile(runningDisciplineBean.getClass());
        dataProviderXml.deleteFile(cyclingDisciplineBean.getClass());
        dataProviderXml.deleteFile(resultsBean.getClass());
        dataProviderXml.deleteFile(participantBean.getClass());
        dataProviderXml.deleteFile(raceBean.getClass());
        InstanceCreate.createTestData(dataProviderXml);
    }

    @AfterAll
    static void clearFiles() throws Exception {
        dataProviderXml.deleteFile(swimmingDisciplineBean.getClass());
        dataProviderXml.deleteFile(runningDisciplineBean.getClass());
        dataProviderXml.deleteFile(cyclingDisciplineBean.getClass());
        dataProviderXml.deleteFile(resultsBean.getClass());
        dataProviderXml.deleteFile(raceBean.getClass());
        dataProviderXml.deleteFile(participantBean.getClass());
    }

    @Test
    public void testShowAllParticipants() {
        log.info("show all participants test");
        participantBean = InstanceCreate.createParticipant("danii");
        String xtr = participantBean.getId();

        dataProviderXml.addParticipantRecord(participantBean);
        participantBean = InstanceCreate.createParticipant("Ivai");
        dataProviderXml.addParticipantRecord(participantBean);

        assertTrue(dataProviderXml.getAllParticipants().isPresent());
        participantList = dataProviderXml.getAllParticipants().get();
        participantList.forEach(log::info);
        dataProviderXml.deleteParticipantRecord(xtr);
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void addParticipantRecordPositive() {
        log.info("addParticipant Test success");
        participantBean = InstanceCreate.createParticipant("daniil");

        assertTrue(dataProviderXml.addParticipantRecord(participantBean));
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void addParticipantRecordNegative() {
        log.info("addParticipant Test fail");

        Participant emptyParticipant = null;
        assertFalse(dataProviderXml.addParticipantRecord(emptyParticipant));
    }

    @Test
    public void getParticipantByIdPositive(){
        log.info("getParticipantById success");

        participantBean = InstanceCreate.createParticipant("danii");

        dataProviderXml.addParticipantRecord(participantBean);
        assertEquals(participantBean, dataProviderXml.getParticipantById(participantBean.getId()).get());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void getParticipantByIdNegative() throws IOException {
        log.info("getParticipantById fail");

        participantBean = InstanceCreate.createParticipant("danii");

        dataProviderXml.addParticipantRecord(participantBean);
        assertTrue(dataProviderXml.getParticipantById("324234").isEmpty());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void deleteParticipantRecordPositive() throws IOException{
        log.info("deleteParticipantRecord success");

        participantBean = InstanceCreate.createParticipant("danii");
        dataProviderXml.addParticipantRecord(participantBean);
        assertTrue(dataProviderXml.deleteParticipantRecord(participantBean.getId()));
    }

    @Test
    public void deleteParticipantRecordNegative() throws IOException{
        log.info("deleteParticipantRecord fail");

        participantBean = InstanceCreate.createParticipant("danii");

        assertFalse(dataProviderXml.deleteParticipantRecord(participantBean.getId()));
    }

    @Test
    public void updateParticipantRecordPositive(){
        log.info("updateParticipantRecord success");

        participantBean = InstanceCreate.createParticipant("name1");
        Participant participantBean2 = InstanceCreate.createParticipant("name2");

        dataProviderXml.addParticipantRecord(participantBean);
        assertTrue(dataProviderXml.updateParticipantRecord(participantBean2, participantBean.getId()));

        dataProviderXml.deleteParticipantRecord(participantBean2.getId());
    }

    @Test
    public void updateParticipantRecordNegative(){
        log.info("updateParticipantRecord fail");

        participantBean = InstanceCreate.createParticipant("name1");
        Participant participantBean2 = InstanceCreate.createParticipant("name2");

        dataProviderXml.addParticipantRecord(participantBean);
        assertFalse(dataProviderXml.updateParticipantRecord(participantBean2, "wrongId"));

        dataProviderXml.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void addRaceRecordPositive() throws ParseException {
        log.info("addRaceRecord success");
        raceBean = InstanceCreate.createRace("race1");

        assertTrue(dataProviderXml.addRaceRecord(raceBean));
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void addRaceRecordNegative(){
        log.info("addRaceRecord fail");
        raceBean = null;

        assertFalse(dataProviderXml.addRaceRecord(raceBean));
    }

    @Test
    public void deleteRaceRecordPositive() throws ParseException {
        log.info("deleteRaceRecord success");

        raceBean = InstanceCreate.createRace("race1");
        dataProviderXml.addRaceRecord(raceBean);
        assertTrue(dataProviderXml.deleteRaceRecord(raceBean.getRaceId()));
    }

    @Test
    public void deleteRaceRecordNegative() throws ParseException {
        log.info("deleteRaceRecord fail");

        assertFalse(dataProviderXml.deleteRaceRecord("wrongId"));
    }

    @Test
    public void getRaceByIdPositive() throws ParseException {
        log.info("getRaceById success");

        raceBean = InstanceCreate.createRace("race1");

        dataProviderXml.addRaceRecord(raceBean);
        assertEquals(raceBean, dataProviderXml.getRaceById(raceBean.getRaceId()).get());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getRaceByIdNegative() throws ParseException {
        log.info("getRaceById fail");
        assertFalse(dataProviderXml.getRaceById("wrongId").isPresent());
    }

    @Test
    public void updateRaceRecordPositive() throws ParseException {
        log.info("updateRaceRecord success");
        raceBean = InstanceCreate.createRace("race1");
        dataProviderXml.addRaceRecord(raceBean);
        Race raceBean2 = InstanceCreate.createRace("race2");
        assertTrue(dataProviderXml.updateRaceRecord(raceBean2, raceBean.getRaceId()));

        dataProviderXml.deleteRaceRecord(raceBean2.getRaceId());
    }

    @Test
    public void updateRaceRecordNegative() throws ParseException {
        log.info("updateRaceRecord fail");
        raceBean = InstanceCreate.createRace("race1");
        assertFalse(dataProviderXml.updateRaceRecord(raceBean, "wrongId"));
    }

    @Test
    public void addDisciplineRecordPositive() {
        log.info("addDisciplineRecord Test success");
        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(987, 500, 1, 14, true, "someId");

        assertTrue(dataProviderXml.addDisciplineRecord(swimmingDisciplineBean));
        dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void addDisciplineRecordNegative() {
        log.info("addDisciplineRecord Test fail");
        swimmingDisciplineBean = null;

        assertFalse(dataProviderXml.addDisciplineRecord(swimmingDisciplineBean));
    }

    @Test
    public void deleteDisciplineRecordPositive() throws IOException, ParseException {
        log.info("deleteDisciplineRecord success");

        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");

        dataProviderXml.addDisciplineRecord(swimmingDisciplineBean);

        assertTrue(dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType()));
    }

    @Test
    public void deleteDisciplineRecordNegative() throws IOException, ParseException {
        log.info("deleteDisciplineRecord fail");

        assertFalse(dataProviderXml.deleteDisciplineRecord("wrongId", DisciplineType.SWIMMING));
    }

    @Test
    public void getDisciplineRecordByIdPositive() {
        log.info("getDisciplineRecordById Test success");
        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");

        dataProviderXml.addDisciplineRecord(swimmingDisciplineBean);

        assertEquals(swimmingDisciplineBean, dataProviderXml.getDisciplineRecordById(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType()).get());

        dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void getDisciplineRecordByIdNegative() {
        log.info("getDisciplineRecordById Test fail");
        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(901, 1500, 4, 10, true, "someId2");

        dataProviderXml.addDisciplineRecord(swimmingDisciplineBean);
        dataProviderXml.addDisciplineRecord(swimmingDisciplineBean2);

        assertNotEquals(swimmingDisciplineBean, dataProviderXml.getDisciplineRecordById(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean.getDisciplineType()).get());

        dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
        dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean2.getDisciplineType());
    }

    @Test
    public void updateDisciplineRecordPositive() throws ParseException {
        log.info("updateDisciplineRecord success");

        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");
        dataProviderXml.addDisciplineRecord(swimmingDisciplineBean);
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 4, 10, true, "someId");

        assertTrue(dataProviderXml.updateDisciplineRecord(swimmingDisciplineBean2, swimmingDisciplineBean.getResultsId()));
        dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean2.getDisciplineType());
    }

    @Test
    public void updateDisciplineRecordNegative() throws ParseException {
        log.info("updateDisciplineRecord fail");

        swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, "someId");
        dataProviderXml.addDisciplineRecord(swimmingDisciplineBean);
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 4, 10, true, "someId");

        assertFalse(dataProviderXml.updateDisciplineRecord(swimmingDisciplineBean2, "wrongId"));
        dataProviderXml.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }


    @Test
    public void addResultsRecordPositive() throws ParseException {
        log.info("addResultsRecord Test success");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);

        assertTrue(dataProviderXml.addResultsRecord(resultsBean));

        dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void addResultsRecordNegative() throws ParseException {
        log.info("addResultsRecord Test fail");
        participantBean = InstanceCreate.createParticipant("daniL");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), "wrongRaceId", 2985);

        dataProviderXml.addParticipantRecord(participantBean);

        assertFalse(dataProviderXml.addResultsRecord(resultsBean));

        //dataProviderXml.deleteResultsRecord(participantBean.getId(), "wrongRaceId");
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void deleteResultsRecordPositive() throws ParseException {
        log.info("deleteResultsRecord success");

        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);
        dataProviderXml.addResultsRecord(resultsBean);

        assertTrue(dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId()));

        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void deleteResultsRecordNegative() throws ParseException {
        log.info("deleteResultsRecord fail");

        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);
        dataProviderXml.addResultsRecord(resultsBean);

        assertFalse(dataProviderXml.deleteResultsRecord("wrongParticipantId", raceBean.getRaceId()));

        dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getResultsRecordPositive() throws ParseException {
        log.info("getResultsRecord Test success");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);
        dataProviderXml.addResultsRecord(resultsBean);

        assertEquals(resultsBean, dataProviderXml.getResultsRecordById(participantBean.getId(), raceBean.getRaceId()).get());

        dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getResultsRecordNegative() throws ParseException {
        log.info("getResultsRecord Test fail");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);
        dataProviderXml.addResultsRecord(resultsBean);

        assertFalse(dataProviderXml.getResultsRecordById("wrongParticipantId", raceBean.getRaceId()).isPresent());

        dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void updateResultsRecordPositive() throws ParseException {
        log.info("updateResultsRecord Test success");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);
        Results resultsBean2 = InstanceCreate.createResultsRecord(2, participantBean.getId(), raceBean.getRaceId(), 3000);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);
        dataProviderXml.addResultsRecord(resultsBean);

        assertTrue(dataProviderXml.updateResultsRecord(resultsBean2, resultsBean.getParticipantId(), resultsBean.getRaceId()));

        dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void updateResultsRecordNegative() throws ParseException {
        log.info("updateResultsRecord Test fail");
        participantBean = InstanceCreate.createParticipant("daniL");
        raceBean = InstanceCreate.createRace("race1");
        resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);
        Results resultsBean2 = InstanceCreate.createResultsRecord(2, participantBean.getId(), raceBean.getRaceId(), 3000);

        dataProviderXml.addRaceRecord(raceBean);
        dataProviderXml.addParticipantRecord(participantBean);
        dataProviderXml.addResultsRecord(resultsBean);

        assertFalse(dataProviderXml.updateResultsRecord(resultsBean2, "wrongParticipant", resultsBean.getRaceId()));

        dataProviderXml.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderXml.deleteParticipantRecord(participantBean.getId());
        dataProviderXml.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void analysisStageWinsPositive() throws ParseException {
        log.info("analysisStageWins success");
        List<Float> expected = List.of(1F, 0F);
        List<Float> resultList = dataProviderXml.analysisStageWins(RaceType.TRIATHLON, DisciplineType.SWIMMING, "y").get();
        assertEquals(expected, resultList);
        log.info("1 - actual: {}; expected: {}", resultList.get(0), expected.get(0));
        log.info("2 - actual: {}; expected: {}", resultList.get(1), expected.get(1));
    }

    @Test
    public void analysisStageWinsNegative() throws ParseException {
        log.info("analysisStageWins fail");
        assertFalse(dataProviderXml.analysisStageWins(RaceType.TRIATHLON, DisciplineType.CLIMBING, "y").isPresent());
    }

    @Test public void countingStageWithoutWinPositive() {
        log.info("countingStageWithoutWin success");
        List<Discipline> disciplineList = dataProviderXml.getAllDisciplineRecords(DisciplineType.RUNNING).get();
        int expected = 0;
        int actual = dataProviderXml.countingStageWithoutWin(disciplineList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test public void countingStageWithoutWinNegative() {
        log.info("countingStageWithoutWin fail");
        List<Discipline> disciplineList = null;
        Assertions.assertNull(dataProviderXml.countingStageWithoutWin(disciplineList));
    }

    @Test public void countingStageWithWinPositive() {
        log.info("countingStageWithoutWin success");
        List<Discipline> disciplineList = dataProviderXml.getAllDisciplineRecords(DisciplineType.CYCLING).get();
        int expected = 5;
        int actual = dataProviderXml.countingStageWithWin(disciplineList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test public void countingStageWithWinNegative() {
        log.info("countingStageWithoutWin fail");
        List<Discipline> disciplineList = null;
        Assertions.assertNull(dataProviderXml.countingStageWithWin(disciplineList));
    }

    @Test
    public void gapAnalysisPositive() throws ParseException {
        log.info("gapAnalysis success");
        List<Float> expected = List.of(400F, 200F);
        List<Float> gapList = dataProviderXml.gapAnalysis(RaceType.TRIATHLON, "participantId3", DisciplineType.SWIMMING).get();
        assertEquals(expected, gapList);
        log.info("1 - actual: {}; expected: {}", gapList.get(0), expected.get(0));
        log.info("2 - actual: {}; expected: {}", gapList.get(1), expected.get(1));
    }

    @Test
    public void gapAnalysisFail() throws ParseException {
        log.info("gapAnalysis fail");
        List<Float> expected = List.of(100F, 200F);
        assertTrue(dataProviderXml.gapAnalysis(RaceType.TRIATHLON,"nonExistentId", DisciplineType.SWIMMING).isPresent());
    }

    @Test
    public void countingTotalGapPositive(){
        log.info("countingTotalGap success");
        List<Results> resultsList = dataProviderXml.getAllResults().get()
                .stream()
                .filter(x -> (x.getParticipantId().equals("participantId4") && dataProviderXml.getRaceById(x.getRaceId()).get().getRaceType().equals(RaceType.TRIATHLON)))
                .toList();
        Float expected = 3000F;
        Float actual = dataProviderXml.countingTotalGap(resultsList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test
    public void countingTotalGapNegative(){
        log.info("countingTotalGap fail");
        List<Results> resultsList = null;
        Float actual = dataProviderXml.countingTotalGap(resultsList);
        Assertions.assertNull(actual);
    }

    @Test
    public void countingTotalDisciplineGapPositive(){
        log.info("countingTotalDisciplineGap success");
        List<Results> resultsList = dataProviderXml.getAllResults().get()
                .stream()
                .filter(x -> (x.getParticipantId().equals("participantId4") && dataProviderXml.getRaceById(x.getRaceId()).get().getRaceType().equals(RaceType.TRIATHLON)))
                .toList();
        Float expected = 1500F;
        Float actual = dataProviderXml.countingTotalDisciplineGap(resultsList, DisciplineType.SWIMMING);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test
    public void countingTotalDisciplineGapNegative(){
        log.info("countingTotalDisciplineGap fail");
        List<Results> resultsList = null;
        Float actual = dataProviderXml.countingTotalDisciplineGap(resultsList, DisciplineType.RUNNING);
        Assertions.assertNull(actual);
    }
}
