package com.lvg.ssm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Victor Levchenko LVG Corp. on 20.11.2020.
 */
public class TestReport {

    public static final String WELDING_JOURNAL_PREFIX_RU = "Журнал* №";
    public static final String WELDING_JOURNAL_PREFIX_ENG = "Log* #";

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

    public TestReport(){}
    public TestReport(String number){
        this.number = number;
    }
    public class AppendixEntity{
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTestObject() {
        return testObject;
    }

    public void setTestObject(String testObject) {
        this.testObject = testObject;
    }

    public String getWorkingDrawings() {
        return workingDrawings;
    }

    public void setWorkingDrawings(String workingDrawings) {
        this.workingDrawings = workingDrawings;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getGeometricSize() {
        return geometricSize;
    }

    public void setGeometricSize(String geometricSize) {
        this.geometricSize = geometricSize;
    }

    public String getWeldJoinId() {
        return weldJoinId;
    }

    public void setWeldJoinId(String weldJoinId) {
        this.weldJoinId = weldJoinId;
    }

    public String getWelder() {
        return welder;
    }

    public void setWelder(String welder) {
        this.welder = welder;
    }

    public String getWelderEng() {
        return welderEng;
    }

    public void setWelderEng(String welderEng) {
        this.welderEng = welderEng;
    }

    public String getWelderMark() {
        return welderMark;
    }

    public void setWelderMark(String welderMark) {
        this.welderMark = welderMark;
    }

    public String getWelderMarkEng() {
        return welderMarkEng;
    }

    public void setWelderMarkEng(String welderMarkEng) {
        this.welderMarkEng = welderMarkEng;
    }

    public String getWeldTechnology() {
        return weldTechnology;
    }

    public void setWeldTechnology(String weldTechnology) {
        this.weldTechnology = weldTechnology;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getControlValue() {
        return controlValue;
    }

    public void setControlValue(String controlValue) {
        this.controlValue = controlValue;
    }

    public String getTestObjectOrigin() {
        return testObjectOrigin;
    }

    public void setTestObjectOrigin(String testObjectOrigin) {
        this.testObjectOrigin = testObjectOrigin;
    }

    public String getDefectFounds() {
        return defectFounds;
    }

    public void setDefectFounds(String defectFounds) {
        this.defectFounds = defectFounds;
    }

    public String getConclusions() {
        return conclusions;
    }

    public void setConclusions(String conclusions) {
        this.conclusions = conclusions;
    }

    public String getLaboratoryHead() {
        return laboratoryHead;
    }

    public void setLaboratoryHead(String laboratoryHead) {
        this.laboratoryHead = laboratoryHead;
    }

    public String getLaboratoryHeadCert() {
        return laboratoryHeadCert;
    }

    public void setLaboratoryHeadCert(String laboratoryHeadCert) {
        this.laboratoryHeadCert = laboratoryHeadCert;
    }

    public String getNdtSpecialist() {
        return ndtSpecialist;
    }

    public void setNdtSpecialist(String ndtSpecialist) {
        this.ndtSpecialist = ndtSpecialist;
    }

    public String getGetNdtSpecialistCert() {
        return getNdtSpecialistCert;
    }

    public void setGetNdtSpecialistCert(String getNdtSpecialistCert) {
        this.getNdtSpecialistCert = getNdtSpecialistCert;
    }

    public List<AppendixEntity> getAppendixEntities() {
        return appendixEntities;
    }

    public void setAppendixEntities(List<AppendixEntity> appendixEntities) {
        this.appendixEntities = appendixEntities;
    }

    public enum Condition{
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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestReport that = (TestReport) o;

        return number.equals(that.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
}
