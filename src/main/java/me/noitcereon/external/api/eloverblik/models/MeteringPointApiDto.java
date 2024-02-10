package me.noitcereon.external.api.eloverblik.models;

import java.util.List;

@EloverblikApiModel
public class MeteringPointApiDto {
    private String meteringPointId;
    private String typeOfMP;
    private String balanceSupplierName;
    private String postcode;
    private String cityName;
    private boolean hasRelation;
    private String consumerCVR;
    private String dataAccessCVR;
    private List<ChildMeteringPointDto> childMeteringPoints;

    private String streetCode;
    private String streetName;
    private String buildingNumber;
    private String floorId;
    private String roomId;
    private String citySubDivisionName;
    private String municipalityCode;
    private String locationDescription;
    private String settlementMethod;
    private String meterReadingOccurrence;
    private String firstConsumerPartyName;
    private String secondConsumerPartyName;
    private String meterNumber;
    private String consumerStartDate;

    public MeteringPointApiDto() {
        // Empty constructor for serialization
    }

    // Standard Getters and Setters for all private variables.
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

    public String getBalanceSupplierName() {
        return balanceSupplierName;
    }

    public void setBalanceSupplierName(String balanceSupplierName) {
        this.balanceSupplierName = balanceSupplierName;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public boolean isHasRelation() {
        return hasRelation;
    }

    public void setHasRelation(boolean hasRelation) {
        this.hasRelation = hasRelation;
    }

    public String getConsumerCVR() {
        return consumerCVR;
    }

    public void setConsumerCVR(String consumerCVR) {
        this.consumerCVR = consumerCVR;
    }

    public String getDataAccessCVR() {
        return dataAccessCVR;
    }

    public void setDataAccessCVR(String dataAccessCVR) {
        this.dataAccessCVR = dataAccessCVR;
    }

    public List<ChildMeteringPointDto> getChildMeteringPoints() {
        return childMeteringPoints;
    }

    public void setChildMeteringPoints(List<ChildMeteringPointDto> childMeteringPoints) {
        this.childMeteringPoints = childMeteringPoints;
    }

    public String getStreetCode() {
        return streetCode;
    }

    public void setStreetCode(String streetCode) {
        this.streetCode = streetCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getCitySubDivisionName() {
        return citySubDivisionName;
    }

    public void setCitySubDivisionName(String citySubDivisionName) {
        this.citySubDivisionName = citySubDivisionName;
    }

    public String getMunicipalityCode() {
        return municipalityCode;
    }

    public void setMunicipalityCode(String municipalityCode) {
        this.municipalityCode = municipalityCode;
    }

    public String getLocationDescription() {
        return locationDescription;
    }

    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    public String getSettlementMethod() {
        return settlementMethod;
    }

    public void setSettlementMethod(String settlementMethod) {
        this.settlementMethod = settlementMethod;
    }

    public String getMeterReadingOccurrence() {
        return meterReadingOccurrence;
    }

    public void setMeterReadingOccurrence(String meterReadingOccurrence) {
        this.meterReadingOccurrence = meterReadingOccurrence;
    }

    public String getFirstConsumerPartyName() {
        return firstConsumerPartyName;
    }

    public void setFirstConsumerPartyName(String firstConsumerPartyName) {
        this.firstConsumerPartyName = firstConsumerPartyName;
    }

    public String getSecondConsumerPartyName() {
        return secondConsumerPartyName;
    }

    public void setSecondConsumerPartyName(String secondConsumerPartyName) {
        this.secondConsumerPartyName = secondConsumerPartyName;
    }

    public String getMeterNumber() {
        return meterNumber;
    }

    public void setMeterNumber(String meterNumber) {
        this.meterNumber = meterNumber;
    }

    public String getConsumerStartDate() {
        return consumerStartDate;
    }

    public void setConsumerStartDate(String consumerStartDate) {
        this.consumerStartDate = consumerStartDate;
    }

    @Override
    public String toString() {
        return "MeteringPointApiDto{" +
                "meteringPointId='" + meteringPointId + '\'' +
                ", typeOfMP='" + typeOfMP + '\'' +
                ", balanceSupplierName='" + balanceSupplierName + '\'' +
                ", postcode='" + postcode + '\'' +
                ", cityName='" + cityName + '\'' +
                ", hasRelation=" + hasRelation +
                ", consumerCVR='" + consumerCVR + '\'' +
                ", childMeteringPoints=" + childMeteringPoints +
                ", streetCode='" + streetCode + '\'' +
                ", streetName='" + streetName + '\'' +
                ", buildingNumber='" + buildingNumber + '\'' +
                ", floorId='" + floorId + '\'' +
                ", roomId='" + roomId + '\'' +
                ", citySubDivisionName='" + citySubDivisionName + '\'' +
                ", municipalityCode='" + municipalityCode + '\'' +
                ", locationDescription='" + locationDescription + '\'' +
                ", settlementMethod='" + settlementMethod + '\'' +
                ", meterReadingOccurrence='" + meterReadingOccurrence + '\'' +
                ", firstConsumerPartyName='" + firstConsumerPartyName + '\'' +
                ", secondConsumerPartyName='" + secondConsumerPartyName + '\'' +
                ", meterNumber='" + meterNumber + '\'' +
                ", consumerStartDate='" + consumerStartDate + '\'' +
                '}';
    }
}
