package com.lvg.ssm.entities;

import com.lvg.ssm.utils.ApplicationProperties;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TestReportVT extends TestReport {

    private static Long lastIndex = 1L;
    static {
        lastIndex = Long.valueOf(ApplicationProperties.getProperty("StartVTProtocolsNumber"));
    }

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
    private String laboratoryHeadCert = "Сертификат № 614 VT.02.2016";
    private String ndtSpecialist = "Левченко В.Г.";
    private String ndtSpecialistCert = "Сертификат № 614 VT.03.2016";
    private List<AppendixEntity> appendixEntities = new ArrayList<>();


    public TestReportVT(){
        this.index = lastIndex++;
    }

    @Override
    public Long getIndex() {
        return this.index;
    }

    @Override
    public TestReportType getType() {
        return TestReportType.VT;
    }

    public String getNdtSpecialistCert(){
        return this.ndtSpecialistCert;
    }

    public void setNdtSpecialistCert(String ndtSpecialistCert){
        this.ndtSpecialistCert = ndtSpecialistCert;
    }

    @Override
    public String getNumber() {
        return super.getNumber();
    }
}
