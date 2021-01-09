package com.lvg.ssm.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor Levchenko LVG Corp. on 20.11.2020.
 */
public abstract class TestReport {

    public static final String WELDING_JOURNAL_PREFIX_RU = "Журнал* №";
    public static final String WELDING_JOURNAL_PREFIX_ENG = "Log* #";

    private Long index;
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
    private String laboratoryHeadCert = "Сертификат № ";
    private String ndtSpecialist = "Левченко В.Г.";
    private String ndtSpecialistCert = "Сертификат № ";
    private List<AppendixEntity> appendixEntities = new ArrayList<>();

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

    public abstract Long getIndex();

    public String getNumber() {
        if(number!=null)
            return number;
        String month = getDate().getMonthValue()<10?"0"+getDate().getMonthValue():""+getDate().getMonthValue();
        String suffix = "-" + month + "/" + date.getYear();
        String prefix = getIndex()<10?"0"+getIndex():""+getIndex();
        return prefix+suffix;
    }

    public String getFormatNumber(int zeroLeadCount){
        String formatStr = "%0"+zeroLeadCount+"d";
        String suffix = getNumber().substring(getNumber().indexOf('-'));
        return String.format(formatStr,getIndex())+suffix;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getWorkingDrawings() {
        return workingDrawings;
    }

    public void setWorkingDrawings(String workingDrawings) {
        this.workingDrawings = workingDrawings;
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

    public List<AppendixEntity> getAppendixEntities() {
        return appendixEntities;
    }

    public enum Condition{
        FIT(" годен / fit"), UNFIT("не годен / unfit");

        private String value;

        Condition(String value){
            this.value = value;
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

        return getNumber().equals(that.getNumber());
    }

    @Override
    public int hashCode() {
        return getNumber().hashCode();
    }

    public static TestReport getTestReport(TestReportType type){
        if (type == TestReportType.VT)
            return new TestReportVT();
        if (type == TestReportType.UT)
            return new TestReportUT();
        throw new IllegalArgumentException();
    }

    public abstract TestReportType getType();
}
