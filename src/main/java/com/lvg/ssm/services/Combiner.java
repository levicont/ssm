package com.lvg.ssm.services;

import com.lvg.ssm.entities.*;

import java.util.*;

public class Combiner  {
    private static final Double MAX_WEIGHT_FOR_UT_KG = 1000D;

      public static Set<TestReport> combineJournalWelding(List<ShipmentEntity> shipmentEntities,
                                                          List<JournalWeldingEntity> journalWeldingEntities,
                                                          TestReportType reportType){
        System.out.println("Starting combine "+reportType+" protocol...");
        Set<TestReport> result = new TreeSet<>(Comparator.comparing(TestReport::getNumber));
        shipmentEntities.forEach(shipmentEntity -> {
            TestReport report = TestReport.getTestReport(reportType);
            report.setDate(shipmentEntity.getDate());
            report.setWorkingDrawings(shipmentEntity.getTechnicalDrawings());
            String journalWeldingSuffixNumber = getJournalNumber(shipmentEntity,journalWeldingEntities);
            report.setWelder(TestReport.WELDING_JOURNAL_PREFIX_RU+journalWeldingSuffixNumber);
            report.setWelderEng(TestReport.WELDING_JOURNAL_PREFIX_ENG+journalWeldingSuffixNumber);
            report.setWelderMark(TestReport.WELDING_JOURNAL_PREFIX_RU+journalWeldingSuffixNumber);
            report.setWelderMarkEng(TestReport.WELDING_JOURNAL_PREFIX_ENG+journalWeldingSuffixNumber);
            report.getAppendixEntities().addAll(getAppendixEntities(shipmentEntity,report));
            result.add(report);
        });
        return result;
    }

    private static String getJournalNumber(ShipmentEntity shipmentEntity, List<JournalWeldingEntity> journalWeldingEntities){
        StringBuilder number = new StringBuilder();
        journalWeldingEntities.forEach(entity->{
            if (isEqualsKMD(entity.getWorkingDrawings(),shipmentEntity.getTechnicalDrawings()))
                number.append(entity.getNumber()).append(", ");
        });
        if(number.toString().isEmpty())
            System.out.println("Cannot find KMD for shipment entity: "+shipmentEntity.getShortStringData());
        if (number.length()>2)
            number.deleteCharAt(number.length()-2);
        return number.toString();
    }

    private static boolean isEqualsKMD(String shipmentKMD, String journalKMD){

        return normalizeKMDString(shipmentKMD).equals(normalizeKMDString(journalKMD));
    }

    private static String normalizeKMDString(String KMDString){
        String normalizeShipmentKMD = KMDString.trim().toLowerCase().replace('-',' ');
        StringTokenizer st  = new StringTokenizer(normalizeShipmentKMD," ");
        StringBuilder normalizeKMDBuild = new StringBuilder();
        while (st.hasMoreTokens()) {
            String nextToken = st.nextToken();
            if (nextToken.equals(" "))
                continue;
            normalizeKMDBuild.append(nextToken.trim()).append(" ");
        }
        return normalizeKMDBuild.toString();
    }

    private static List<TestReport.AppendixEntity> getAppendixEntities(ShipmentEntity shipmentEntity, TestReport testReport){
        List<TestReport.AppendixEntity> result = new ArrayList<>();
        shipmentEntity.getDetailEntities().forEach(detailEntity -> {
            String workingDrawings = shipmentEntity.getTechnicalDrawings();
            String position = detailEntity.getMark();
            String positionName = detailEntity.getMarkName();
            Integer amount = detailEntity.getCount();
            TestReport.Condition condition = TestReport.Condition.FIT;
            String comments = "";
            TestReport.AppendixEntity appendixEntity =
                    testReport.new AppendixEntity(workingDrawings,
                            position,
                            positionName,
                            amount,
                            condition,
                            comments);
            result.add(appendixEntity);
        });
        return result;
    }
}
