package ru.sfedu.arai.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "root")
public class WrapperXml<T> {
    @ElementList(inline = true, required = false)
    private List<T> list;

    public WrapperXml() {
    }

    public WrapperXml(List<T> list) {
        this.list = list;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
