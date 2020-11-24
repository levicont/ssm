package com.lvg.ssm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Levchenko LVG Corp. on 20.11.2020.
 */
public class TestReport {
    private String number;
    private LocalDate date;
    private String testObject = "Сварные соединения деталей Приложение 1   /    Weld joints of details in Appendix 1";
    private String workingDrawings;
    private String material = "Сталь  S355J2+N / Steel S355J2+N (1.2 ISO/TR15608)";
    private String geometricSize = "-";
    private String weldJoinId = "-";
    private String welder;
    private String welderEng;
    private String welderMark;
    private String welderMarkEng;
    private String weldTechnology = "135";
    private String manufacturer = "ООО «Стальспецмонтаж Завод» / Privat Limeted Company «SSM Factory»";
    private String controlValue = "Сварной шов (100%)  и ЗТВ (10 мм) / Weld (100%) and HAZ (10 mm)";
    private String testObjectOrigin = "Маркер / Marker";
    private String defectFounds = "Дефектов не обнаружено / No defects found";
    private String conclusions = "Удовлетворяет требованиям по качеству / Satisfies quality requirements";
    private String laboratoryHead = "Крапива Д.А.";
    private String laboratoryHeadCert = "Сертификат № 614 VT.02.2016";
    private String ndtSpecialist = "Левченко В.Г.";
    private String getNdtSpecialistCert = "Сертификат № 614 VT.03.2016";
    private List<AppendixEntity> appendixEntities = new ArrayList<>();

    private class AppendixEntity{
        private String workingDrawings;
        private String position;
        private String positionName;
        private Integer amount;
        private Condition condition = Condition.FIT;
        private String comments;

        public AppendixEntity(String workingDrawings, String position, String positionName, Integer amount, Condition condition, String comments) {
            this.workingDrawings = workingDrawings;
            this.position = position;
            this.positionName = positionName;
            this.amount = amount;
            this.condition = condition;
            this.comments = comments;
        }

        public String getWorkingDrawings() {
            return workingDrawings;
        }

        public String getPosition() {
            return position;
        }

        public String getPositionName() {
            return positionName;
        }

        public Integer getAmount() {
            return amount;
        }

        public Condition getCondition() {
            return condition;
        }

        public String getComments() {
            return comments;
        }
    }

    private enum Condition{
        FIT(" годен / fit"), UNFIT("не годен / unfit");

        private String value;

        Condition(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return this.value;
        }
    }



}
