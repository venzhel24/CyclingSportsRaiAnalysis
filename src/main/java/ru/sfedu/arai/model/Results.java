package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindAndSplitByName;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Results {
    @CsvBindByName
    private int place;

    @CsvBindByName
    private String participantId;

    @CsvBindByName
    private String raceId;

    @CsvBindByName
    private float totalTime;

    @CsvBindByName
    private String resultsId;


    public Results() {
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public String getParticipantId() {
        return participantId;
    }

    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(float totalTime) {
        this.totalTime = totalTime;
    }

    public String getResultsId() {
        return resultsId;
    }

    public void setResultsId(String resultsId) {
        this.resultsId = resultsId;
    }

    public void setResultsId() {
        resultsId = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Results results = (Results) o;
        return place == results.place && Float.compare(results.totalTime, totalTime) == 0 && Objects.equals(participantId, results.participantId) && Objects.equals(raceId, results.raceId) && Objects.equals(resultsId, results.resultsId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(place, participantId, raceId, totalTime, resultsId);
    }

    @Override
    public String toString() {

        return "Results{" +
                "place=" + place +
                ", participantId='" + participantId + '\'' +
                ", raceId='" + raceId + '\'' +
                ", totalTime=" + totalTime +
                ", resultsId=" + resultsId +
                '}';
    }
}
