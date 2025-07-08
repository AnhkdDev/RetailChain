package model;

public class Color {

    private int colorID;
    private String colorValue;
    private boolean isActive;

    // Default constructor
    public Color() {
    }

    // Parameterized constructor
    public Color(int colorID, String colorValue, boolean isActive) {
        this.colorID = colorID;
        this.colorValue = colorValue;
        this.isActive = isActive;
    }

    public int getColorID() {
        return colorID;
    }

    public void setColorID(int colorID) {
        this.colorID = colorID;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}