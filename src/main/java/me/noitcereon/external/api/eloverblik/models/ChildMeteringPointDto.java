package me.noitcereon.external.api.eloverblik.models;

@EloverblikApiModel
public class ChildMeteringPointDto {
    private String parentMeteringPointId;
    private String meteringPointId;
    private String typeOfMP;
    private String meterReadingOccurrence;
    private String meterNumber;


    public ChildMeteringPointDto() {
        // Empty contructor to allow serialization
    }

    public String getParentMeteringPointId() {
        return parentMeteringPointId;
    }

    public void setParentMeteringPointId(String parentMeteringPointId) {
        this.parentMeteringPointId = parentMeteringPointId;
    }

    public String getMeteringPointId() {
        return meteringPointId;
    }

    public void setMeteringPointId(String meteringPointId) {
        this.meteringPointId = meteringPointId;
    }

    public String getTypeOfMP() {
        return typeOfMP;
    }

    public void setTypeOfMP(String typeOfMP) {
        this.typeOfMP = typeOfMP;
    }

    public String getMeterReadingOccurrence() {
        return meterReadingOccurrence;
    }

    public void setMeterReadingOccurrence(String meterReadingOccurrence) {
        this.meterReadingOccurrence = meterReadingOccurrence;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    @Override
    public String toString() {
        return "ChildMeteringPointDto{" +
                "parentMeteringPointId='" + parentMeteringPointId + '\'' +
                ", meteringPointId='" + meteringPointId + '\'' +
                ", typeOfMP='" + typeOfMP + '\'' +
                ", meterReadingOccurrence='" + meterReadingOccurrence + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                '}';
    }

}
