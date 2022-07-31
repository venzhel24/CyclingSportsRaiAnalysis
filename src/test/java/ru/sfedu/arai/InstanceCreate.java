package ru.sfedu.arai;

import ru.sfedu.arai.api.DataProvider;
import ru.sfedu.arai.enums.DisciplineType;
import ru.sfedu.arai.enums.RaceType;
import ru.sfedu.arai.model.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

public class InstanceCreate {

    public static Participant createParticipant(String name){
        Participant participant = new Participant();
        participant.setId();
        participant.setName(name);
        return participant;
    }

    public static Participant createParticipant(String name, String id){
        Participant participant = new Participant();
        participant.setId(id);
        participant.setName(name);
        return participant;
    }
    public static Race createRace(String name) throws ParseException {
        Race race = new Race();
        race.setRaceId();
        race.setDate();
        race.setRaceName(name);
        race.setRaceType(RaceType.TRIATHLON);
        return race;
    }

    public static Race createRace(String name, String id) throws ParseException {
        Race race = new Race();
        race.setRaceId(id);
        race.setDate();
        race.setRaceName(name);
        race.setRaceType(RaceType.TRIATHLON);
        return race;
    }

    public static Discipline createSwimmingDisciplineRecord(float time, int distance, int disciplinePlace, int waterTemperature, boolean hydroSuit, String resultsId){
        SwimmingDiscipline swimmingDiscipline = new SwimmingDiscipline();
        swimmingDiscipline.setDisciplineType(DisciplineType.SWIMMING);
        swimmingDiscipline.setWaterTemperature(waterTemperature);
        swimmingDiscipline.setHydroSuit(hydroSuit);
        swimmingDiscipline.setResultsId(resultsId);
        swimmingDiscipline.setTime(time);
        swimmingDiscipline.setDistance(distance);
        swimmingDiscipline.setDisciplinePlace(disciplinePlace);
        return swimmingDiscipline;
    }

    public static Discipline createRunningDisciplineRecord(float time, int distance, int disciplinePlace, String runningShoes, String resultsId){
        RunningDiscipline runningDiscipline = new RunningDiscipline();
        runningDiscipline.setResultsId(resultsId);
        runningDiscipline.setDisciplineType(DisciplineType.RUNNING);
        runningDiscipline.setTime(time);
        runningDiscipline.setDistance(distance);
        runningDiscipline.setDisciplinePlace(disciplinePlace);
        runningDiscipline.setRunningShoes(runningShoes);
        return runningDiscipline;
    }

    public static Discipline createCyclingDisciplineRecord(float time, int distance, int disciplinePlace, long bikeId, String bikeModel, String cyclingShoes, String resultsId){
        CyclingDiscipline cyclingDiscipline = new CyclingDiscipline();
        cyclingDiscipline.setResultsId(resultsId);
        cyclingDiscipline.setDisciplineType(DisciplineType.CYCLING);
        cyclingDiscipline.setTime(time);
        cyclingDiscipline.setDistance(distance);
        cyclingDiscipline.setDisciplinePlace(disciplinePlace);
        cyclingDiscipline.setBikeId(bikeId);
        cyclingDiscipline.setBikeModel(bikeModel);
        cyclingDiscipline.setCyclingShoes(cyclingShoes);
        return cyclingDiscipline;
    }


    public static Results createResultsRecord( int place, String participantId, String raceId, float totalTime){
        Results results = new Results();
        results.setParticipantId(participantId);
        results.setRaceId(raceId);
        results.setPlace(place);
        results.setTotalTime(totalTime);
        results.setResultsId();
        return results;
    }

    public static Results createResultsRecord( int place, String participantId, String raceId, float totalTime, String resultsId){
        Results results = new Results();
        results.setParticipantId(participantId);
        results.setRaceId(raceId);
        results.setPlace(place);
        results.setTotalTime(totalTime);
        results.setResultsId(resultsId);
        return results;
    }

    public static void createTestData(DataProvider dataProvider) throws ParseException {
        Participant participantBean;
        Race raceBean;
        Results resultsBean;
        Discipline swimmingDisciplineBean;
        Discipline runningDisciplineBean;
        Discipline cyclingDisciplineBean;
        for(int i = 0; i < 5; i++){
            participantBean = InstanceCreate.createParticipant("name".concat(String.valueOf(i+1)), "participantId".concat(String.valueOf(i+1)));
            raceBean = InstanceCreate.createRace("raceName".concat(String.valueOf(i+1)), "raceId".concat(String.valueOf(i+1)));
            dataProvider.addRaceRecord(raceBean);
            dataProvider.addParticipantRecord(participantBean);
        }

        int k = 0;
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++){
                k++;
                resultsBean = InstanceCreate.createResultsRecord(j+1, "participantId".concat(String.valueOf(j+1)), "raceId".concat(String.valueOf(i+1)), (i+j+10)*200, "resultsId".concat(String.valueOf(k)));
                dataProvider.addResultsRecord(resultsBean);
            }

        k=0;
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++){
                k++;
                swimmingDisciplineBean = InstanceCreate.createSwimmingDisciplineRecord((j+10)*100, (i+1)*100, j+1, i+5, true, "resultsId".concat(String.valueOf(k)));
                runningDisciplineBean = InstanceCreate.createRunningDisciplineRecord((j+8)*100, (i+1)*500, j+1, "runningShoes".concat(String.valueOf(k)), "resultsId".concat(String.valueOf(k)));
                cyclingDisciplineBean = InstanceCreate.createCyclingDisciplineRecord((j+20)*50, (i+1)*1000, j+1, k, "bikeModel".concat(String.valueOf(k)), "cyclingShoes".concat(String.valueOf(k)), "resultsId".concat(String.valueOf(k)));
                dataProvider.addDisciplineRecord(swimmingDisciplineBean);
                dataProvider.addDisciplineRecord(runningDisciplineBean);
                dataProvider.addDisciplineRecord(cyclingDisciplineBean);
            }
    }

    public static void deleteTestData(DataProvider dataProvider) throws ParseException {
        for(int i = 0; i < 5; i++){
            dataProvider.deleteParticipantRecord("participantId".concat(String.valueOf(i+1)));
            dataProvider.deleteRaceRecord("raceId".concat(String.valueOf(i+1)));
        }

        int k=0;
        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++){
                k++;
                dataProvider.deleteDisciplineRecord("resultsId".concat(String.valueOf(k)), DisciplineType.SWIMMING);
                dataProvider.deleteDisciplineRecord("resultsId".concat(String.valueOf(k)), DisciplineType.CYCLING);
                dataProvider.deleteDisciplineRecord("resultsId".concat(String.valueOf(k)), DisciplineType.RUNNING);
            }

        for(int i = 0; i < 5; i++)
            for(int j = 0; j < 5; j++){
                dataProvider.deleteResultsRecord("participantId".concat(String.valueOf(j+1)), "raceId".concat(String.valueOf(i+1)));
            }
    }
}
