package ru.sfedu.arai.model;

import com.opencsv.bean.CsvBindByName;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class Participant implements Serializable{

    @Element
    @CsvBindByName
    private String id;

    @Element
    @CsvBindByName
    private String name;

    public Participant(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Participant{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
