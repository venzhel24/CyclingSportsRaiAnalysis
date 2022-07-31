package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindByName;
import ru.sfedu.arai.enums.DisciplineType;

import java.util.Objects;
import java.util.UUID;

public class Discipline {
    @CsvBindByName
    private String resultsId;

    @CsvBindByName
    private float time;

    @CsvBindByName
    private int distance;

    @CsvBindByName
    private int disciplinePlace;

    @CsvBindByName
    private DisciplineType disciplineType;

    public Discipline() {
    }

    public String getResultsId() {
        return resultsId;
    }

    public void setResultsId(String disciplineId) {
        this.resultsId = disciplineId;
    }

//    public void setResultsId() {
//        this.resultsId = UUID.randomUUID().toString();
//    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getDisciplinePlace() {
        return disciplinePlace;
    }

    public void setDisciplinePlace(int disciplinePlace) {
        this.disciplinePlace = disciplinePlace;
    }

    public DisciplineType getDisciplineType() {
        return disciplineType;
    }

    public void setDisciplineType(DisciplineType disciplineType) {
        this.disciplineType = disciplineType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discipline that = (Discipline) o;
        return Float.compare(that.time, time) == 0 && distance == that.distance && disciplinePlace == that.disciplinePlace && Objects.equals(resultsId, that.resultsId) && disciplineType == that.disciplineType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resultsId, time, distance, disciplinePlace, disciplineType);
    }

    @Override
    public String toString() {
        return  "resultsId='" + resultsId + '\'' +
                ", time=" + time +
                ", distance=" + distance +
                ", disciplinePlace=" + disciplinePlace +
                ", disciplineType=" + disciplineType;
    }
}
