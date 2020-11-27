package com.lvg.ssm;

import java.time.LocalDate;
import java.util.*;

public class Combiner  {


    public static Set<TestReport> combineJournalWelding(List<ShipmentEntity> shipmentEntities,
                                                           List<JournalWeldingEntity> journalWeldingEntities){
        System.out.println("Starting combine VT protocol...");
        Set<TestReport> result = new HashSet<>();
        shipmentEntities.forEach(shipmentEntity -> {
            TestReport report = new TestReport();
            report.setNumber(getNextNumber(result,shipmentEntity));
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

        result.forEach(testReport -> {
            System.out.println(testReport);
        });
        return result;
    }

    private static String getSufixNumber(LocalDate date){
        return "-" + date.getMonthValue() + "/" + date.getYear();
    }

    private static String getNextNumber(Set<TestReport> testReports, ShipmentEntity shipmentEntity){
        int startNumber = 1;

        Iterator<TestReport> iterator = testReports.iterator();
        while(iterator.hasNext()){
            if (iterator.next().
                    equals(new TestReport(""+startNumber+getSufixNumber(shipmentEntity.getDate()))))
            startNumber++;
        }
        return ""+startNumber+""+getSufixNumber(shipmentEntity.getDate());
    }

    private static String getJournalNumber(ShipmentEntity shipmentEntity, List<JournalWeldingEntity> journalWeldingEntities){
        StringBuilder number = new StringBuilder();
        journalWeldingEntities.forEach(entity->{
            if (entity.getWorkingDrawings().equalsIgnoreCase(shipmentEntity.getTechnicalDrawings()))
                number.append(entity.getNumber()).append(", ");
        });
       // number.deleteCharAt(number.length()-2);
        return number.toString();
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
