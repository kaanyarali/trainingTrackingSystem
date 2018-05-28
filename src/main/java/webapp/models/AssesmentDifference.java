package webapp.models;

public class AssesmentDifference {
    private int weightDif;
    private int heightDif;
    private int armSizeDif;
    private int chestSizeDif;
    private int waistSizeDif;
    private int hipsSizeDif;
    private int heartRateDif;
    private int minutesKmDif;

    public AssesmentDifference(int weightDif, int heightDif, int armSizeDif, int chestSizeDif, int waistSizeDif,
                               int hipsSizeDif, int heartRateDif, int minutesKmDif) {
        this.weightDif = weightDif;
        this.heightDif = heightDif;
        this.armSizeDif = armSizeDif;
        this.chestSizeDif = chestSizeDif;
        this.waistSizeDif = waistSizeDif;
        this.hipsSizeDif = hipsSizeDif;
        this.heartRateDif = heartRateDif;
        this.minutesKmDif = minutesKmDif;
    }

    public void setWeightDif(int weightDif) {
        this.weightDif = weightDif;
    }

    public void setHeightDif(int heightDif) {
        this.heightDif = heightDif;
    }

    public void setArmSizeDif(int armSizeDif) {
        this.armSizeDif = armSizeDif;
    }

    public void setChestSizeDif(int chestSizeDif) {
        this.chestSizeDif = chestSizeDif;
    }

    public void setWaistSizeDif(int waistSizeDif) {
        this.waistSizeDif = waistSizeDif;
    }

    public void setHipsSizeDif(int hipsSizeDif) {
        this.hipsSizeDif = hipsSizeDif;
    }

    public void setHeartRateDif(int heartRateDif) {
        this.heartRateDif = heartRateDif;
    }

    public void setMinutesKmDif(int minutesKmDif) {
        this.minutesKmDif = minutesKmDif;
    }

    public int getWeightDif() {
        return weightDif;
    }

    public int getHeightDif() {
        return heightDif;
    }

    public int getArmSizeDif() {
        return armSizeDif;
    }

    public int getChestSizeDif() {
        return chestSizeDif;
    }

    public int getWaistSizeDif() {
        return waistSizeDif;
    }

    public int getHipsSizeDif() {
        return hipsSizeDif;
    }

    public int getHeartRateDif() {
        return heartRateDif;
    }

    public int getMinutesKmDif() {
        return minutesKmDif;
    }
}
