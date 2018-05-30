package webapp.models;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.text.DateFormat;
import java.util.Date;

public class Assesment {
    private int weight;
    private int height;
    private int armSize;
    private int chestSize;
    private int waistSize;
    private int hipsSize;
    private int heartRate;
    private int minutesKm;
    private AssesmentDifference assesmentDifference;
    @DBRef
    private Trainer createdBy;
    @DBRef
    private Trainee trainee;
    private Date createdDate;

    public Assesment(int weight, int height, int armSize, int chestSize, int waistSize, int hipsSize, int heartRate,
                     int minutesKm, Trainer createdBy,Trainee trainee) {
        this.weight = weight;
        this.height = height;
        this.armSize = armSize;
        this.chestSize = chestSize;
        this.waistSize = waistSize;
        this.hipsSize = hipsSize;
        this.heartRate = heartRate;
        this.minutesKm = minutesKm;
        this.createdBy = createdBy;
        this.trainee=trainee;
        this.createdDate=new Date();
        this.assesmentDifference=null;

    }

    public void setTrainee(Trainee trainee) {
        this.trainee = trainee;
    }

    public Trainee getTrainee() {

        return trainee;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {

        return createdDate;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setArmSize(int armSize) {
        this.armSize = armSize;
    }

    public void setChestSize(int chestSize) {
        this.chestSize = chestSize;
    }

    public void setWaistSize(int waistSize) {
        this.waistSize = waistSize;
    }

    public void setHipsSize(int hipsSize) {
        this.hipsSize = hipsSize;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public void setMinutesKm(int minutesKm) {
        this.minutesKm = minutesKm;
    }

    public void setCreatedBy(Trainer createdBy) {
        this.createdBy = createdBy;
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

    public Trainer getCreatedBy() {
        return createdBy;
    }

    public AssesmentDifference getAssesmentDifference() {
        return assesmentDifference;
    }

    public void setAssesmentDifference(AssesmentDifference assesmentDifference) {
        this.assesmentDifference = assesmentDifference;
    }
    public String getFormattedDate()
    {
        return DateFormat.getDateInstance(DateFormat.LONG).format(getCreatedDate());
    }

    public String getAssessmentDifValues() {
        return new AssessmentRatio().getAssessmentDifRatios();
    }

    public String getAssessmentDifLabels() {
        return new AssessmentRatio().getAssessmentDifRatioNames();
    }

    private class AssessmentRatio {
        public String getAssessmentDifRatios() {
            float total = assesmentDifference.totalDiffRation();

            return String.join(",",
                    String.valueOf(Math.abs(assesmentDifference.getWeightDif()) * 100/total),
                    String.valueOf(Math.abs(assesmentDifference.getWaistSizeDif()) * 100/total),
                    String.valueOf(Math.abs(assesmentDifference.getArmSizeDif()) * 100/total),
                    String.valueOf(Math.abs(assesmentDifference.getHeartRateDif()) * 100/total),
                    String.valueOf(Math.abs(assesmentDifference.getChestSizeDif()) * 100/total),
                    String.valueOf(Math.abs(assesmentDifference.getMinutesKmDif()) * 100/total),
                    String.valueOf(Math.abs(assesmentDifference.getHipsSizeDif()) * 100/total));
        }

        public String getAssessmentDifRatioNames() {
            return String.join(",",
                    "'Weight'", "'Waist'", "'Arm'", "'Heart'", "'Chest'", "'Minutes'", "'Hips'");
        }

    }
}
