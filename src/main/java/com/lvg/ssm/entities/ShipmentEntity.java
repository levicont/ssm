package com.lvg.ssm.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Victor Levchenko LVG Corp. on 22.11.2020.
 */
public class ShipmentEntity {
    private static Long lastId = 1L;


    private Long index;
    private LocalDate date;
    private String technicalDrawings;
    private String objectName;
    private String shippingShop;
    private List<DetailEntity> detailEntities = new ArrayList<>();

    public ShipmentEntity(){
        index = lastId++;
    }

    public Long getIndex() {
        return index;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTechnicalDrawings() {
        return technicalDrawings;
    }

    public void setTechnicalDrawings(String technicalDrawings) {
        this.technicalDrawings = technicalDrawings;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getShippingShop() {
        return shippingShop;
    }

    public void setShippingShop(String shippingShop) {
        this.shippingShop = shippingShop;
    }

    public List<DetailEntity> getDetailEntities() {
        return detailEntities;
    }


    public String getShortStringData(){
        return getFormattedNumber()+"\t\t"+date+"\t\t"+technicalDrawings+"\t\t"+shippingShop+"\t\t";
    }

    public String getFormattedNumber(){
        return String.format("%04d",getIndex());
    }
    @Override
    public String toString() {
        return  index+"\n"+
                date+"\n"+
                technicalDrawings + "\n" +
                objectName + "\n" +
                shippingShop + "\n" +
                detailEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ShipmentEntity that = (ShipmentEntity) o;

        if (!index.equals(that.index)) return false;
        return Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        int result = index.hashCode();
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    public class DetailEntity{
        private String mark;
        private String markName;
        private Integer count;
        private Double weightOfMarkKg;

        public DetailEntity(String mark, String markName, Integer count, Double weightOfMarkKg){
            this.mark = mark;
            this.markName = markName;
            this.count = count;
            this.weightOfMarkKg = weightOfMarkKg;
        }

        public String getMark() {
            return mark;
        }

        public String getMarkName() {
            return markName;
        }

        public Integer getCount() {
            return count;
        }

        public Double getWeightOfMarkKg() {
            return weightOfMarkKg;
        }

        @Override
        public String toString() {
            return  mark + "\t\t\t" +
                    markName + "\t\t\t" +
                    count + "\t\t\t"+
                    weightOfMarkKg;
        }


    }


}
