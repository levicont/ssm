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


    private class DetailEntity{
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
    }
}
