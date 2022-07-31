package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import org.simpleframework.xml.Element;
import ru.sfedu.arai.enums.RaceType;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class Race implements Serializable {
    @Element
    @CsvBindByName
    private String raceId;

    @Element
    @CsvBindByName
    private String raceName;

    @Element
    @CsvBindByName
    @CsvDate("dd.MM.yyyy")
    private Date date;

    @Element
    @CsvBindByName
    private RaceType raceType;

    public Race() {
    }

    public String getRaceId() {
        return raceId;
    }

    public void setRaceId(){
        this.raceId = UUID.randomUUID().toString();
    }

    public void setRaceId(String raceId) {
        this.raceId = raceId;
    }

    public String getRaceName() {
        return raceName;
    }

    public void setRaceName(String raceName) {
        this.raceName = raceName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    public void setDate() throws ParseException {
//        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
//        Date today = new Date();
//        this.date = formatter.parse(formatter.format(today));
        this.date = java.sql.Date.valueOf(LocalDate.now());
    }

    public RaceType getRaceType() {
        return raceType;
    }

    public void setRaceType(RaceType raceType) {
        this.raceType = raceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Race race = (Race) o;
        return Objects.equals(raceId, race.raceId) && Objects.equals(raceName, race.raceName) && Objects.equals(date, race.date) && raceType == race.raceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(raceId, raceName, date, raceType);
    }

    @Override
    public String toString() {
        return "Race{" +
                "raceId='" + raceId + '\'' +
                ", raceName='" + raceName + '\'' +
                ", date=" + date +
                ", raceType=" + raceType +
                '}';
    }
}
