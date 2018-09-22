package com.smartmovetheapp.smartmove.data.remote.model;

import java.util.Date;

public class Order {

    private int orderId;

    private int customerId;

    private int truckTypeId;

    private Date orderDateTime;

    private long date;

    private long time;

    private String pickupPlace;

    private double pickupLat;

    private double pickupLong;

    private String pickupUnitNumber;

    private String pickupFloor;

    private boolean pickupHasElevator;

    private String pickupDistanceFromParking;

    private String pickupAdditionalInfo;

    private String dropPlace;

    private double dropLat;

    private double dropLong;

    private String dropUnitNumber;

    private String dropFloor;

    private boolean dropHasElevator;

    private String dropDistanceFromParking;

    private String dropAdditionalInfo;

    private int estimatedNumOfTrips;

    private String estimatedWeight;

    private String estimatedArea;

    private String orderStatus;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTruckTypeId() {
        return truckTypeId;
    }

    public void setTruckTypeId(int truckTypeId) {
        this.truckTypeId = truckTypeId;
    }

    public Date getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Date orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getPickupPlace() {
        return pickupPlace;
    }

    public void setPickupPlace(String pickupPlace) {
        this.pickupPlace = pickupPlace;
    }

    public double getPickupLat() {
        return pickupLat;
    }

    public void setPickupLat(double pickupLat) {
        this.pickupLat = pickupLat;
    }

    public double getPickupLong() {
        return pickupLong;
    }

    public void setPickupLong(double pickupLong) {
        this.pickupLong = pickupLong;
    }

    public String getPickupUnitNumber() {
        return pickupUnitNumber;
    }

    public void setPickupUnitNumber(String pickupUnitNumber) {
        this.pickupUnitNumber = pickupUnitNumber;
    }

    public String getPickupFloor() {
        return pickupFloor;
    }

    public void setPickupFloor(String pickupFloor) {
        this.pickupFloor = pickupFloor;
    }

    public boolean isPickupHasElevator() {
        return pickupHasElevator;
    }

    public void setPickupHasElevator(boolean pickupHasElevator) {
        this.pickupHasElevator = pickupHasElevator;
    }

    public String getPickupDistanceFromParking() {
        return pickupDistanceFromParking;
    }

    public void setPickupDistanceFromParking(String pickupDistanceFromParking) {
        this.pickupDistanceFromParking = pickupDistanceFromParking;
    }

    public String getPickupAdditionalInfo() {
        return pickupAdditionalInfo;
    }

    public void setPickupAdditionalInfo(String pickupAdditionalInfo) {
        this.pickupAdditionalInfo = pickupAdditionalInfo;
    }

    public String getDropPlace() {
        return dropPlace;
    }

    public void setDropPlace(String dropPlace) {
        this.dropPlace = dropPlace;
    }

    public double getDropLat() {
        return dropLat;
    }

    public void setDropLat(double dropLat) {
        this.dropLat = dropLat;
    }

    public double getDropLong() {
        return dropLong;
    }

    public void setDropLong(double dropLong) {
        this.dropLong = dropLong;
    }

    public String getDropUnitNumber() {
        return dropUnitNumber;
    }

    public void setDropUnitNumber(String dropUnitNumber) {
        this.dropUnitNumber = dropUnitNumber;
    }

    public String getDropFloor() {
        return dropFloor;
    }

    public void setDropFloor(String dropFloor) {
        this.dropFloor = dropFloor;
    }

    public boolean isDropHasElevator() {
        return dropHasElevator;
    }

    public void setDropHasElevator(boolean dropHasElevator) {
        this.dropHasElevator = dropHasElevator;
    }

    public String getDropDistanceFromParking() {
        return dropDistanceFromParking;
    }

    public void setDropDistanceFromParking(String dropDistanceFromParking) {
        this.dropDistanceFromParking = dropDistanceFromParking;
    }

    public String getDropAdditionalInfo() {
        return dropAdditionalInfo;
    }

    public void setDropAdditionalInfo(String dropAdditionalInfo) {
        this.dropAdditionalInfo = dropAdditionalInfo;
    }

    public int getEstimatedNumOfTrips() {
        return estimatedNumOfTrips;
    }

    public void setEstimatedNumOfTrips(int estimatedNumOfTrips) {
        this.estimatedNumOfTrips = estimatedNumOfTrips;
    }

    public String getEstimatedWeight() {
        return estimatedWeight;
    }

    public void setEstimatedWeight(String estimatedWeight) {
        this.estimatedWeight = estimatedWeight;
    }

    public String getEstimatedArea() {
        return estimatedArea;
    }

    public void setEstimatedArea(String estimatedArea) {
        this.estimatedArea = estimatedArea;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", truckTypeId=" + truckTypeId +
                ", orderDateTime=" + orderDateTime +
                ", date=" + date +
                ", time=" + time +
                ", pickupPlace='" + pickupPlace + '\'' +
                ", pickupLat=" + pickupLat +
                ", pickupLong=" + pickupLong +
                ", pickupUnitNumber='" + pickupUnitNumber + '\'' +
                ", pickupFloor='" + pickupFloor + '\'' +
                ", pickupHasElevator=" + pickupHasElevator +
                ", pickupDistanceFromParking='" + pickupDistanceFromParking + '\'' +
                ", pickupAdditionalInfo='" + pickupAdditionalInfo + '\'' +
                ", dropPlace='" + dropPlace + '\'' +
                ", dropLat=" + dropLat +
                ", dropLong=" + dropLong +
                ", dropUnitNumber='" + dropUnitNumber + '\'' +
                ", dropFloor='" + dropFloor + '\'' +
                ", dropHasElevator=" + dropHasElevator +
                ", dropDistanceFromParking='" + dropDistanceFromParking + '\'' +
                ", dropAdditionalInfo='" + dropAdditionalInfo + '\'' +
                ", estimatedNumOfTrips=" + estimatedNumOfTrips +
                ", estimatedWeight='" + estimatedWeight + '\'' +
                ", estimatedArea='" + estimatedArea + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                '}';
    }
}
