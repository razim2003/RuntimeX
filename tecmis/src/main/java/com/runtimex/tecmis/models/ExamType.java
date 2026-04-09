package com.runtimex.tecmis.models;

public class ExamType {
    private String typeId;
    private String name;
    private double weight;

    public ExamType() {
    }

    public ExamType(String typeId, String name, double weight) {
        this.typeId = typeId;
        this.name = name;
        this.weight = weight;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
