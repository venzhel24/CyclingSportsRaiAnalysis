package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindByName;

import java.util.Objects;

public class CyclingDiscipline extends Discipline{
    @CsvBindByName
    private long bikeId;

    @CsvBindByName
    private String bikeModel;

    @CsvBindByName
    private String cyclingShoes;

    public long getBikeId() {
        return bikeId;
    }

    public void setBikeId(long bikeId) {
        this.bikeId = bikeId;
    }

    public String getBikeModel() {
        return bikeModel;
    }

    public void setBikeModel(String bikeModel) {
        this.bikeModel = bikeModel;
    }

    public String getCyclingShoes() {
        return cyclingShoes;
    }

    public void setCyclingShoes(String cyclingShoes) {
        this.cyclingShoes = cyclingShoes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        CyclingDiscipline that = (CyclingDiscipline) o;
        return bikeId == that.bikeId && Objects.equals(bikeModel, that.bikeModel) && Objects.equals(cyclingShoes, that.cyclingShoes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), bikeId, bikeModel, cyclingShoes);
    }

    @Override
    public String toString() {
        return "CyclingDiscipline{" +
                super.toString() +
                "bikeId=" + bikeId +
                ", bikeModel='" + bikeModel +
                ", cyclingShoes='" + cyclingShoes +
                '}';
    }
}
