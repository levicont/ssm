package com.lvg.ssm;

import java.time.LocalDate;
import java.util.*;

public class Combiner  {

    private static int[] lastNumbers = new int[12];
    {
        Arrays.stream(lastNumbers).forEach(i -> i=1);
    }


    public static Set<TestReport> combineJournalWelding(Set<ShipmentEntity> shipmentEntities,
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
        return result;
    }

    private static String getSufixNumber(LocalDate date){

        String month = date.getMonthValue()<10?"0"+date.getMonthValue():""+date.getMonthValue();
        return "-" + month + "/" + date.getYear();
    }

    private static String getPrefixNumber(int index){
        return index<10?"0"+index:""+index;
    }

    private static String getNextNumber(Set<TestReport> testReports, ShipmentEntity shipmentEntity){
        int number = lastNumbers[shipmentEntity.getDate().getMonthValue()-1]++;
        int startNumber = 1;

//        for (TestReport testReport : testReports) {
//
//            if (testReport.
//                    equals(new TestReport(getPrefixNumber(startNumber)+getSufixNumber(shipmentEntity.getDate()))))
//                startNumber++;
//        }

        return getPrefixNumber(number)+getSufixNumber(shipmentEntity.getDate());
    }

    private static String getJournalNumber(ShipmentEntity shipmentEntity, List<JournalWeldingEntity> journalWeldingEntities){
        StringBuilder number = new StringBuilder();
        journalWeldingEntities.forEach(entity->{
            if (isEqualsKMD(entity.getWorkingDrawings(),shipmentEntity.getTechnicalDrawings()))
                number.append(entity.getNumber()).append(", ");
        });
        if (number.length()>2)
            number.deleteCharAt(number.length()-2);
        return number.toString();
    }

    private static boolean isEqualsKMD(String sheepmentKMD, String journalKMD){
        String normalizeSheepmentKMD = sheepmentKMD.toLowerCase().replace('-',' ');
        String normalizeJournalKMD = journalKMD.toLowerCase().replace('-',' ');
        return normalizeJournalKMD.equals(normalizeSheepmentKMD);
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
