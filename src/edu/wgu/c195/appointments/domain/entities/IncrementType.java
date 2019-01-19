package edu.wgu.c195.appointments.domain.entities;

public class IncrementTypes {
    private int incrementTypeId;
    private String incrementTypeDescription;

    public IncrementTypes() {

    }

    public IncrementTypes(int incrementTypeId, String incrementTypeDescription) {
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
