package com.lvg.ssm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Levchenko LVG Corp. on 22.11.2020.
 */
public class ShipmentEntity {
    private LocalDate date;
    private String technicalDrawings;
    private String objectName;
    private String shippingShop;
    private List<DetailEntity> detailEntities = new ArrayList<>();

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

    public void setDetailEntities(List<DetailEntity> detailEntities) {
        this.detailEntities = detailEntities;

    }

    @Override
    public String toString() {
        return  date+"\n"+
                technicalDrawings + "\n" +
                objectName + "\n" +
                shippingShop + "\n" +
                detailEntities;
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
