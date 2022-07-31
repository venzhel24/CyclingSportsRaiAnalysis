package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class SwimmingDiscipline extends Discipline{
    @CsvBindByName
    private int waterTemperature;

    @CsvBindByName
    private boolean hydroSuit;

//    public SwimmingDiscipline() {
//        super();
//    }

    public int getWaterTemperature() {
        return waterTemperature;
    }

    public void setWaterTemperature(int waterTemperature) {
        this.waterTemperature = waterTemperature;
    }

    public boolean isHydroSuit() {
        return hydroSuit;
    }

    public void setHydroSuit(boolean hydroSuit) {
        this.hydroSuit = hydroSuit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SwimmingDiscipline that = (SwimmingDiscipline) o;
        return waterTemperature == that.waterTemperature && hydroSuit == that.hydroSuit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), waterTemperature, hydroSuit);
    }


    @Override
    public String toString() {
        return "SwimmingDiscipline{" +
                super.toString() +
                ", waterTemperature=" + waterTemperature +
                ", hydroSuit=" + hydroSuit +
                '}';
    }
}
