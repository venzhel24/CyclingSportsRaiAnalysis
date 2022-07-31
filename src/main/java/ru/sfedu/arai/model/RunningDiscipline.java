package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class RunningDiscipline extends Discipline{
    @CsvBindByName
    private String runningShoes;

    public String getRunningShoes() {
        return runningShoes;
    }

    public void setRunningShoes(String runningShoes) {
        this.runningShoes = runningShoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        RunningDiscipline that = (RunningDiscipline) o;
        return Objects.equals(runningShoes, that.runningShoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), runningShoes);
    }

    @Override
    public String toString() {
        return "RunningDiscipline{" +
                super.toString() +
                "runningShoes='" + runningShoes +
                "}";
    }
}
