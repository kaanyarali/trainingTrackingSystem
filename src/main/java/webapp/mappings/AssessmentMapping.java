package webapp.mappings;

import webapp.models.Trainee;

public class AssessmentMapping {
    private int weight;
    private int height;
    private int armSize;
    private int chestSize;
    private int waistSize;
    private int hipsSize;
    private int heartRate;
    private int minutesKm;
    private String traineeSelected;


    public AssessmentMapping(int weight, int height, int armSize, int chestSize, int waistSize, int hipsSize,
                             int heartRate, int minutesKm, String traineeSelected) {
        this.weight = weight;
        this.height = height;
        this.armSize = armSize;
        this.chestSize = chestSize;
        this.waistSize = waistSize;
        this.hipsSize = hipsSize;
        this.heartRate = heartRate;
        this.minutesKm = minutesKm;
        this.traineeSelected=traineeSelected;
    }


    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public int getArmSize() {
        return armSize;
    }

    public int getChestSize() {
        return chestSize;
    }

    public int getWaistSize() {
        return waistSize;
    }

    public int getHipsSize() {
        return hipsSize;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public int getMinutesKm() {
        return minutesKm;
    }

    public String getTraineeId() {
        return traineeSelected;
    }
}
