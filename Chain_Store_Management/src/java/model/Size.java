package model;

public class Size {

    private int sizeID;
    private String sizeValue;
    private boolean isActive;

    // Default constructor
    public Size() {
    }

    // Parameterized constructor
    public Size(int sizeID, String sizeValue, boolean isActive) {
        this.sizeID = sizeID;
        this.sizeValue = sizeValue;
        this.isActive = isActive;
    }

    public int getSizeID() {
        return sizeID;
    }

    public void setSizeID(int sizeID) {
        this.sizeID = sizeID;
    }

    public String getSizeValue() {
        return sizeValue;
    }

    public void setSizeValue(String sizeValue) {
        this.sizeValue = sizeValue;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}