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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class DataProviderJdbcTest {
    private static final Logger log = LogManager.getLogger(DataProviderJdbcTest.class);

    static DataProviderJdbc dataProviderJdbc = new DataProviderJdbc(Constants.TEST_JDBC_PATH);
    String testUUID = UUID.randomUUID().toString();

    @BeforeAll
    static void setUp() throws ParseException {
        dataProviderJdbc.dropTables();
        dataProviderJdbc.createTables();
        InstanceCreate.createTestData(dataProviderJdbc);
        log.info("tables was deleted and created");
    }
    @AfterAll
    static void clearDB() throws ParseException {
        dataProviderJdbc.dropTables();
        dataProviderJdbc.createTables();
        InstanceCreate.createTestData(dataProviderJdbc);
        log.info("tables was deleted and created");
    }

    @Test
    public void testAddParticipantRecordSuccess() {
        log.info("Test AddParticipantRecord Success");
        Participant participant = InstanceCreate.createParticipant("name1", "anyId");
        assertTrue(dataProviderJdbc.addParticipantRecord(participant));
        dataProviderJdbc.deleteParticipantRecord(participant.getId());
    }

    @Test
    public void testAddParticipantRecordFail() {
        log.info("Test AddParticipantRecord Fail");
        Participant participant = null;
        assertFalse(dataProviderJdbc.addParticipantRecord(participant));
        //dataProviderJdbc.deleteParticipantRecord(participant.getId());
    }

    @Test
    public void testShowAllParticipants(){
        log.info("test showAllParticipants");
        Participant participant1 = InstanceCreate.createParticipant("daniil");
        Participant participant2 = InstanceCreate.createParticipant("dani", "12345");
        dataProviderJdbc.addParticipantRecord(participant1);
        dataProviderJdbc.addParticipantRecord(participant2);
        dataProviderJdbc.getAllParticipants().get().forEach(log::info);
        dataProviderJdbc.deleteParticipantRecord(participant1.getId());
        dataProviderJdbc.deleteParticipantRecord(participant2.getId());
    }

    @Test
    public void testDeleteParticipantSuccess(){
        log.info("test deleteParticipant success");
        Participant participant = InstanceCreate.createParticipant("name1");
        dataProviderJdbc.addParticipantRecord(participant);
        assertTrue(dataProviderJdbc.deleteParticipantRecord(participant.getId()));
    }

    @Test
    public void testDeleteParticipantFail(){
        log.info("test deleteParticipant fail");
        assertFalse(dataProviderJdbc.deleteParticipantRecord("1234"));
    }

    @Test
    public void testGetParticipantByIdSuccess(){
        log.info("test getParticipantById success");
        Participant participant = InstanceCreate.createParticipant("name1");
        log.info(participant);
        dataProviderJdbc.addParticipantRecord(participant);
        assertEquals(participant, dataProviderJdbc.getParticipantById(participant.getId()).get());
        dataProviderJdbc.deleteParticipantRecord(participant.getId());
    }

    @Test
    public void testGetParticipantByIdFail(){
        log.info("test getParticipantById fail");
        assertFalse(dataProviderJdbc.getParticipantById("1234").isPresent());
    }

    @Test
    public void testUpdateParticipantRecordSuccess(){
        log.info("test updateParticipantRecord success");
        Participant participant = InstanceCreate.createParticipant("name1");
        dataProviderJdbc.addParticipantRecord(participant);
        Participant participant2 = InstanceCreate.createParticipant("dan", "12345");
        assertTrue(dataProviderJdbc.updateParticipantRecord(participant2, participant.getId()));
        dataProviderJdbc.getAllParticipants();
        dataProviderJdbc.deleteParticipantRecord(participant2.getId());
    }

    @Test
    public void testUpdateParticipantRecordFail(){
        log.info("test updateParticipantRecord fail");
        Participant participant = InstanceCreate.createParticipant("name1");
        dataProviderJdbc.addParticipantRecord(participant);
        Participant participant2 = InstanceCreate.createParticipant("dan", "12345");
        assertFalse(dataProviderJdbc.updateParticipantRecord(participant2, "2222"));
        dataProviderJdbc.getAllParticipants();
        dataProviderJdbc.deleteParticipantRecord(participant.getId());
    }


    @Test
    public void testAddRaceRecordSuccess() throws ParseException {
        log.info("Test AddRaceRecord Success");
        Race race = InstanceCreate.createRace("race1");
        assertTrue(dataProviderJdbc.addRaceRecord(race));
        dataProviderJdbc.deleteRaceRecord(race.getRaceId());
    }

    @Test
    public void testAddRaceRecordFail() {
        log.info("Test AddRaceRecord Fail");
        Race race = null;
        assertFalse(dataProviderJdbc.addRaceRecord(race));
        //dataProviderJdbc.deleteParticipantRecord(participant.getId());
    }

    @Test
    public void testDeleteRaceSuccess() throws ParseException {
        log.info("test deleteRace success");
        Race race = InstanceCreate.createRace("race1");
        dataProviderJdbc.addRaceRecord(race);
        assertTrue(dataProviderJdbc.deleteRaceRecord(race.getRaceId()));
    }

    @Test
    public void testDeleteRaceFail(){
        log.info("test deleteRace fail");
        assertFalse(dataProviderJdbc.deleteRaceRecord("wrongId"));
    }

    @Test
    public void testGetRaceByIdSuccess() throws ParseException {
        log.info("test getRaceById success");
        Race race = InstanceCreate.createRace("race1");
        log.info(race);
        dataProviderJdbc.addRaceRecord(race);
        assertEquals(race, dataProviderJdbc.getRaceById(race.getRaceId()).get());
        dataProviderJdbc.deleteRaceRecord(race.getRaceId());
    }

    @Test
    public void testGetRaceByIdFail(){
        log.info("test getRaceById fail");
        assertFalse(dataProviderJdbc.getRaceById("wrongId").isPresent());
    }

    @Test
    public void testUpdateRaceRecordSuccess() throws ParseException {
        log.info("test updateRaceRecord success");
        Race race = InstanceCreate.createRace("race1");
        dataProviderJdbc.addRaceRecord(race);
        Race race2 = InstanceCreate.createRace("race2");
        assertTrue(dataProviderJdbc.updateRaceRecord(race2, race.getRaceId()));
        dataProviderJdbc.deleteRaceRecord(race2.getRaceId());
    }

    @Test
    public void testUpdateRaceRecordFail() throws ParseException {
        log.info("test updateRaceRecord fail");
        Race race = InstanceCreate.createRace("race1");
        dataProviderJdbc.addRaceRecord(race);
        Race race2 = InstanceCreate.createRace("race2");
        assertFalse(dataProviderJdbc.updateRaceRecord(race2, "wrongId"));
        dataProviderJdbc.deleteRaceRecord(race.getRaceId());
    }


    @Test
    public void addDisciplineRecordPositive() {
        log.info("addDisciplineRecord Test success");
        Discipline swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord((float)987.03, 500, 1, 14, true, testUUID);

        assertTrue(dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean));
        dataProviderJdbc.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void addDisciplineRecordNegative() {
        log.info("addDisciplineRecord Test fail");
        Discipline swimmingDisciplineBean = null;

        assertFalse(dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean));
    }

    @Test
    public void deleteDisciplineRecordPositive() throws IOException, ParseException {
        log.info("deleteDisciplineRecord success");

        Discipline swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, testUUID);

        dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean);

        assertTrue(dataProviderJdbc.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType()));
    }

    @Test
    public void deleteDisciplineRecordNegative() throws IOException, ParseException {
        log.info("deleteDisciplineRecord fail");

        assertFalse(dataProviderJdbc.deleteDisciplineRecord("wrongId", DisciplineType.SWIMMING));
    }

    @Test
    public void getDisciplineRecordByIdPositive() {
        log.info("getDisciplineRecordById Test success");
        Discipline swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, testUUID);

        dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean);

        assertEquals(swimmingDisciplineBean, dataProviderJdbc.getDisciplineRecordById(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType()).get());

        dataProviderJdbc.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void getDisciplineRecordByIdNegative() {
        log.info("getDisciplineRecordById Test fail");
        Discipline swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, testUUID);

        dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean);

        assertFalse(dataProviderJdbc.getDisciplineRecordById(swimmingDisciplineBean.getResultsId(), DisciplineType.RUNNING).isPresent());

        dataProviderJdbc.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void updateDisciplineRecordPositive() throws ParseException {
        log.info("updateDisciplineRecord success");

        Discipline swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, testUUID);
        dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean);
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 4, 10, true, testUUID);

        assertTrue(dataProviderJdbc.updateDisciplineRecord(swimmingDisciplineBean2, swimmingDisciplineBean.getResultsId()));
        dataProviderJdbc.deleteDisciplineRecord(swimmingDisciplineBean2.getResultsId(), swimmingDisciplineBean2.getDisciplineType());
    }

    @Test
    public void updateDisciplineRecordNegative() throws ParseException {
        log.info("updateDisciplineRecord fail");

        Discipline swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 3, 10, true, testUUID);
        dataProviderJdbc.addDisciplineRecord(swimmingDisciplineBean);
        Discipline swimmingDisciplineBean2 = InstanceCreate.createSwimmingDisciplineRecord(900, 1500, 4, 10, true, testUUID);

        assertFalse(dataProviderJdbc.updateDisciplineRecord(swimmingDisciplineBean2, "wrongId"));
        dataProviderJdbc.deleteDisciplineRecord(swimmingDisciplineBean.getResultsId(), swimmingDisciplineBean.getDisciplineType());
    }

    @Test
    public void addResultsRecordPositive() throws ParseException {
        log.info("addResultsRecord Test success");
        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);

        assertTrue(dataProviderJdbc.addResultsRecord(resultsBean));

        dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void addResultsRecordNegative() throws ParseException {
        log.info("addResultsRecord Test fail");
        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), "wrongRaceId", 2985);

        dataProviderJdbc.addParticipantRecord(participantBean);

        assertFalse(dataProviderJdbc.addResultsRecord(resultsBean));

        //dataProviderXml.deleteResultsRecord(participantBean.getId(), "wrongRaceId");
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
    }

    @Test
    public void deleteResultsRecordPositive() throws ParseException {
        log.info("deleteResultsRecord success");

        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);
        dataProviderJdbc.addResultsRecord(resultsBean);

        assertTrue(dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId()));

        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void deleteResultsRecordNegative() throws ParseException {
        log.info("deleteResultsRecord fail");

        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);
        dataProviderJdbc.addResultsRecord(resultsBean);

        assertFalse(dataProviderJdbc.deleteResultsRecord("wrongParticipantId", raceBean.getRaceId()));

        dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }


    @Test
    public void getResultsRecordPositive() throws ParseException {
        log.info("getResultsRecord Test success");
        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);
        dataProviderJdbc.addResultsRecord(resultsBean);

        assertEquals(resultsBean, dataProviderJdbc.getResultsRecordById(participantBean.getId(), raceBean.getRaceId()).get());

        dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void getResultsRecordNegative() throws ParseException {
        log.info("getResultsRecord Test fail");
        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);
        dataProviderJdbc.addResultsRecord(resultsBean);

        assertFalse(dataProviderJdbc.getResultsRecordById("wrongParticipantId", raceBean.getRaceId()).isPresent());

        dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void updateResultsRecordPositive() throws ParseException {
        log.info("updateResultsRecord Test success");
        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);
        Results resultsBean2 = InstanceCreate.createResultsRecord(2, participantBean.getId(), raceBean.getRaceId(), 3000);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);
        dataProviderJdbc.addResultsRecord(resultsBean);

        assertTrue(dataProviderJdbc.updateResultsRecord(resultsBean2, resultsBean.getParticipantId(), resultsBean.getRaceId()));

        dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void updateResultsRecordNegative() throws ParseException {
        log.info("updateResultsRecord Test fail");
        Participant participantBean = InstanceCreate.createParticipant("daniL");
        Race raceBean = InstanceCreate.createRace("race1");
        Results resultsBean = InstanceCreate.createResultsRecord(1, participantBean.getId(), raceBean.getRaceId(), 2985);
        Results resultsBean2 = InstanceCreate.createResultsRecord(2, participantBean.getId(), raceBean.getRaceId(), 3000);

        dataProviderJdbc.addRaceRecord(raceBean);
        dataProviderJdbc.addParticipantRecord(participantBean);
        dataProviderJdbc.addResultsRecord(resultsBean);

        assertFalse(dataProviderJdbc.updateResultsRecord(resultsBean2, "wrongParticipant", resultsBean.getRaceId()));

        dataProviderJdbc.deleteResultsRecord(participantBean.getId(), raceBean.getRaceId());
        dataProviderJdbc.deleteParticipantRecord(participantBean.getId());
        dataProviderJdbc.deleteRaceRecord(raceBean.getRaceId());
    }

    @Test
    public void analysisStageWinsPositive() throws ParseException {
        log.info("analysisStageWins success");
        List<Float> expected = List.of(1F, 0F);
        List<Float> resultList = dataProviderJdbc.analysisStageWins(RaceType.TRIATHLON, DisciplineType.SWIMMING, "y").get();
        assertEquals(expected, resultList);
        log.info("1 - actual: {}; expected: {}", resultList.get(0), expected.get(0));
        log.info("2 - actual: {}; expected: {}", resultList.get(1), expected.get(1));
    }

    @Test
    public void analysisStageWinsNegative() throws ParseException {
        log.info("analysisStageWins fail");
        assertFalse(dataProviderJdbc.analysisStageWins(RaceType.TRIATHLON, DisciplineType.CLIMBING, "y").isPresent());
    }

    @Test public void countingStageWithoutWinPositive() {
        log.info("countingStageWithoutWin success");
        List<Discipline> disciplineList = dataProviderJdbc.getAllDisciplineRecords(DisciplineType.SWIMMING).get();
        int expected = 0;
        int actual = dataProviderJdbc.countingStageWithoutWin(disciplineList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test public void countingStageWithoutWinNegative() {
        log.info("countingStageWithoutWin fail");
        List<Discipline> disciplineList = null;
        Assertions.assertNull(dataProviderJdbc.countingStageWithoutWin(disciplineList));
    }

    @Test public void countingStageWithWinPositive() {
        log.info("countingStageWithoutWin success");
        List<Discipline> disciplineList = dataProviderJdbc.getAllDisciplineRecords(DisciplineType.CYCLING).get();
        int expected = 5;
        int actual = dataProviderJdbc.countingStageWithWin(disciplineList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test public void countingStageWithWinNegative() {
        log.info("countingStageWithoutWin fail");
        List<Discipline> disciplineList = null;
        Assertions.assertNull(dataProviderJdbc.countingStageWithWin(disciplineList));
    }

    @Test
    public void gapAnalysisPositive() throws ParseException {
        log.info("gapAnalysis success");
        List<Float> expected = List.of(400F, 200F);
        List<Float> gapList = dataProviderJdbc.gapAnalysis(RaceType.TRIATHLON,"participantId3", DisciplineType.SWIMMING).get();
        assertEquals(expected, gapList);
        log.info("1 - actual: {}; expected: {}", gapList.get(0), expected.get(0));
        log.info("2 - actual: {}; expected: {}", gapList.get(1), expected.get(1));
    }

    @Test
    public void gapAnalysisFail() throws ParseException {
        log.info("gapAnalysis fail");
        List<Float> expected = List.of(400F, 200F);
        assertTrue(dataProviderJdbc.gapAnalysis(RaceType.TRIATHLON,"nonExistentId", DisciplineType.SWIMMING).isPresent());
    }

    @Test
    public void countingTotalGapPositive(){
        log.info("countingTotalGap success");
        List<Results> resultsList = dataProviderJdbc.getAllResults().get()
                .stream()
                .filter(x -> (x.getParticipantId().equals("participantId4") && dataProviderJdbc.getRaceById(x.getRaceId()).get().getRaceType().equals(RaceType.TRIATHLON)))
                .toList();
        Float expected = 3000F;
        Float actual = dataProviderJdbc.countingTotalGap(resultsList);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test
    public void countingTotalGapNegative(){
        log.info("countingTotalGap fail");
        List<Results> resultsList = null;
        Float actual = dataProviderJdbc.countingTotalGap(resultsList);
        Assertions.assertNull(actual);
    }

    @Test
    public void countingTotalDisciplineGapPositive(){
        log.info("countingTotalDisciplineGap success");
        List<Results> resultsList = dataProviderJdbc.getAllResults().get()
                .stream()
                .filter(x -> (x.getParticipantId().equals("participantId4") && dataProviderJdbc.getRaceById(x.getRaceId()).get().getRaceType().equals(RaceType.TRIATHLON)))
                .toList();
        Float expected = 1500F;
        Float actual = dataProviderJdbc.countingTotalDisciplineGap(resultsList, DisciplineType.SWIMMING);
        Assertions.assertEquals(expected, actual);
        log.info("1 - actual: {}; expected: {}", actual, expected);
    }

    @Test
    public void countingTotalDisciplineGapNegative(){
        log.info("countingTotalDisciplineGap fail");
        List<Results> resultsList = null;
        Float actual = dataProviderJdbc.countingTotalDisciplineGap(resultsList, DisciplineType.RUNNING);
        Assertions.assertNull(actual);
    }

}