package edu.wgu.c195.appointments.domain.entities;

public class IncrementType {
    private int incrementTypeId;
    private String incrementTypeDescription;

    public IncrementType() {

    }

    public IncrementType(int incrementTypeId, String incrementTypeDescription) {
        this.incrementTypeId = incrementTypeId;
        this.incrementTypeDescription = incrementTypeDescription;
    }

    public int getIncrementTypeId() {
        return incrementTypeId;
    }

    public void setIncrementTypeId(int incrementTypeId) {
        this.incrementTypeId = incrementTypeId;
    }

    public String getIncrementTypeDescription() {
        return incrementTypeDescription;
    }

    public void setIncrementTypeDescription(String incrementTypeDescription) {
        this.incrementTypeDescription = incrementTypeDescription;
    }
}
