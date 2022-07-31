package ru.sfedu.arai.api;

import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.Discipline;
import ru.sfedu.arai.model.Participant;
import ru.sfedu.arai.model.Race;
import ru.sfedu.arai.model.Results;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface DataProvider {

    Optional<List<Participant>> getAllParticipants();
    Optional<List<Race>> getAllRaces();
    Optional<List<Discipline>> getAllDisciplineRecords(DisciplineType disciplineType);
    Optional<List<Results>> getAllResults();

    boolean addParticipantRecord(Participant participant);
    boolean addRaceRecord(Race race);
    boolean addDisciplineRecord(Discipline discipline);
    boolean addResultsRecord(Results results);

    boolean deleteParticipantRecord(String id);
    boolean deleteRaceRecord(String id);
    boolean deleteDisciplineRecord(String id, DisciplineType disciplineType);
    boolean deleteResultsRecord(String participantId, String raceId);

    boolean updateParticipantRecord(Participant participant, String id);
    boolean updateRaceRecord(Race race, String id);
    boolean updateDisciplineRecord(Discipline discipline, String id);
    boolean updateResultsRecord(Results results, String participantId, String raceId);

    Optional<Participant> getParticipantById(String id) throws IOException;
    Optional<Race> getRaceById(String id);
    Optional<Discipline> getDisciplineRecordById(String id, DisciplineType disciplineType);
    Optional<Results> getResultsRecordById(String ParticipantId, String RaceId);
    Optional<Results> getResultsRecordById(String resultsId);

    /**
     * Method for calculating the probability of wins in a race when winning a selected discipline
     * @param raceType - the type of race we are analyzing
     * @param disciplineType - type of discipline for which we count victories
     * @param analysisWithoutWin - defines the use of an additional function to find the percentage of wins, without wins in the selected discipline
     * @return a list with the probability of winning the race if winning the selected discipline, and with the probability of winning the race if not winning the selected discipline (if any)
     */
    Optional<List<Float>> analysisStageWins(RaceType raceType, DisciplineType disciplineType, String analysisWithoutWin);


    /**
     * An additional method for finding the number of wins at the end of the race, in any place except the first in the selected discipline.
     * @param disciplineList - list of discipline beans to iterate over
     * @return integer number, number of wins
     */
    Integer countingStageWithoutWin(List<Discipline> disciplineList);

    /**
     * Мethod for finding the number of wins in all races, in the first place in the selected discipline.
     * @param disciplineList - list of discipline beans to iterate over
     * @return integer number, number of wins
     */
    Integer countingStageWithWin(List<Discipline> disciplineList);


    /**
     * a method for finding the average gap between a participant and the leader in each race
     * @param raceType - the type of race we are analyzing
     * @param participantId - Selected participant ID
     * @param disciplineType - optional parameter to find the gap in a selected discipline
     * @return list with a gap for all disciplines at once, and with a gap for a selected discipline(if any)
     */
    Optional<List<Float>> gapAnalysis(RaceType raceType, String participantId, DisciplineType disciplineType);

    /**
     * Method for finding the sum of gap between a participant and the leader in all races.
     * @param resultsList - list of results to iterate over
     * @return float number, average gap
     */
    Float countingTotalGap(List<Results> resultsList);


    /**
     * an additional method for finding the sum of gap between a participant and the leader in chosen discipline
     * @param resultsList - list of results to iterate over
     * @param disciplineType - selected type of discipline
     * @return float number, average gap
     */
    Float countingTotalDisciplineGap(List<Results> resultsList, DisciplineType disciplineType);
}
