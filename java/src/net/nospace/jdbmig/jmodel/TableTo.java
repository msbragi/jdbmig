package net.nospace.jdbmig.jmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TableTo {

    private String name;
    private List<FieldTo> fields;
    private List<Map<String, Object>> data;

    public TableTo() {
        fields = new ArrayList<>();
        data = new ArrayList<>();
    }

    public TableTo(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FieldTo> getFields() {
        return fields;
    }

    public void setFields(List<FieldTo> fields) {
        this.fields = fields;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }

    public void setData(List<Map<String, Object>> data) {
        this.data = data;
    }
    /*
    public void addField(String name, int type) {
        fields.add(new FieldTo(name, type));
    }
     */
}
