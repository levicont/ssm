package com.lvg.ssm;

import com.sun.star.beans.PropertyValue;
import com.sun.star.sheet.XCellRangesQuery;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCellRange;
import com.sun.star.uno.UnoRuntime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import static com.lvg.ssm.OpenOfficeUtils.*;

public class Filler {

    public static void main(String[] args) {
        List<ShipmentEntity> shipmentEntities = DataExtractor.getShipmentEntities();
        List<JournalWeldingEntity> journalWeldingEntities = DataExtractor.getJournalWeldingEntities();
        Set<TestReport> testReports = Combiner.combineJournalWelding(shipmentEntities,journalWeldingEntities);
        fullfillVTReport(testReports.iterator().next());
    }



    private static final String PROTOCOL_TAMPLATE_PATH = "file://"+Filler.class
            .getClassLoader().getResource("templates/prot-VT-UT-template.ods").getPath();




    public static void fullfillVTReport(TestReport testReport){
        System.out.println("Starting filling report");
        PropertyValue[] props = getHiddenAsTemplateProperties();
        props[0].Value = Boolean.FALSE;
        try {
            XSpreadsheets xSpreadsheets = getSpreadsheets(PROTOCOL_TAMPLATE_PATH, props);
            Object sheet = xSpreadsheets.getByName(xSpreadsheets.getElementNames()[0]);
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            System.out.println("Number: "+ testReport.getNumber());
            System.out.println("Date: "+ formatDate(testReport.getDate()));
            setCellTextByPosition(xSpreadsheet,5,9,testReport.getNumber());
            setCellTextByPosition(xSpreadsheet,7,9,formatDate(testReport.getDate()));
            xSpreadsheet.getCellByPosition(3, 13).setFormula(testReport.getWorkingDrawings());
            xSpreadsheet.getCellByPosition(8, 17).setFormula(testReport.getWelder());
            xSpreadsheet.getCellByPosition(8, 18).setFormula(testReport.getWelderEng());
            xSpreadsheet.getCellByPosition(8, 20).setFormula(testReport.getWelderMark());
            xSpreadsheet.getCellByPosition(8, 21).setFormula(testReport.getWelderMarkEng());

            int appendixStartRow = 76;
            for(int i = 0; i < testReport.getAppendixEntities().size(); i++ ){
                TestReport.AppendixEntity appendixEntity = testReport.getAppendixEntities().get(i);
                try {
                    xSpreadsheet.getCellByPosition(0,appendixStartRow).setFormula((i+1)+"");
                    xSpreadsheet.getCellByPosition(1,appendixStartRow).setFormula(appendixEntity.getWorkingDrawings());
                    xSpreadsheet.getCellByPosition(3,appendixStartRow).setFormula(appendixEntity.getPosition());
                    xSpreadsheet.getCellByPosition(4,appendixStartRow).setFormula(appendixEntity.getPositionName());
                    xSpreadsheet.getCellByPosition(6,appendixStartRow).setFormula(appendixEntity.getAmount()+"");
                    xSpreadsheet.getCellByPosition(7,appendixStartRow).setFormula(appendixEntity.getCondition().toString());
                    xSpreadsheet.getCellByPosition(8,appendixStartRow).setFormula(appendixEntity.getComments());
                    appendixStartRow++;
                }catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static String formatDate(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }


}
