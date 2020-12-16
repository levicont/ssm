package com.lvg.ssm.services;

import com.lvg.ssm.entities.JournalWeldingEntity;
import com.lvg.ssm.entities.ShipmentEntity;
import com.sun.star.lang.XComponent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;

import java.time.LocalDate;
import java.util.*;

import static com.lvg.ssm.utils.OpenOfficeUtils.*;


/**
 * Created by Victor Levchenko LVG Corp. on 20.11.2020.
 */
public class DataExtractor {

    private static final String SHIPPING_TABLE_PATH = "file://"+ Objects.requireNonNull(DataExtractor.class
            .getClassLoader().getResource("templates/shipping-table.xlsx")).getPath();
    private static final String JOURNAL_TABLE_PATH = "file://"+ Objects.requireNonNull(DataExtractor.class
            .getClassLoader().getResource("templates/welding-journals.xlsx")).getPath();

    private static Long lastIndex = 1L;

    public static List<ShipmentEntity> getShipmentEntities() {
        List<ShipmentEntity> result = new ArrayList<>();
        XComponent xComponent = loadXComponent(SHIPPING_TABLE_PATH, getHiddenAsTemplateProperties());
        XSpreadsheets xSpreadsheets = getSpreadsheets(xComponent);
        String[] xSpreadsheetsNames = xSpreadsheets.getElementNames();

        Arrays.stream(xSpreadsheetsNames).forEach(name ->{
            try {
                Object sheet = xSpreadsheets.getByName(name);
                XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class,sheet);
                ShipmentEntity shipmentEntity;
                int row = 0;
                int emptyRowsCount = 0;

                while (emptyRowsCount < 2){
                    XCell xCell = xSpreadsheet.getCellByPosition(0, row);
                    XText xCellText = UnoRuntime.queryInterface(XText.class,xCell);
                    String cellString = xCellText.getString();
                    if (cellString.isEmpty()){
                        emptyRowsCount++;
                    }
                    if (cellString.equalsIgnoreCase("Дата отгрузки")){
                        shipmentEntity = getShipmentEntityFromStartRow(xSpreadsheet, row);

                        result.add(shipmentEntity);
                        row = row + 5 + shipmentEntity.getDetailEntities().size();
                        emptyRowsCount = 0;
                        continue;
                    }
                    row++;
                }
            }
            catch (Exception ex){
                throw new RuntimeException(ex);
            }

        });
        close(xComponent);
        System.out.println(result.size()+" shipment entities has found.");
        result.sort(Comparator.comparing(ShipmentEntity::getDate));
        return result;
    }

    public static Set<ShipmentEntity> getSortedShipmentEntities(List<ShipmentEntity> shipmentEntities){
        TreeSet<ShipmentEntity> result =
                new TreeSet<>(Comparator.comparing(ShipmentEntity::getIndex));
        shipmentEntities.forEach(shEntity ->{
            if(!result.add(shEntity))
                System.out.println("Shipment entity not added: "+shEntity.getShortStringData());
        });
        result.addAll(shipmentEntities);
        System.out.println(result.size()+" shipment entities added to sorted set. Source list has "+shipmentEntities.size());
        return result;

    }

    private static ShipmentEntity getShipmentEntityFromStartRow(XSpreadsheet xSpreadsheet, int startRow){
        try {
            ShipmentEntity entity = new ShipmentEntity();
            LocalDate date = getLocalDateFromDoubleValue(xSpreadsheet.getCellByPosition(2, startRow).getValue());
            entity.setDate(date);
            entity.setTechnicalDrawings(getCellTextByPosition(xSpreadsheet,2,startRow+1));
            entity.setObjectName(getCellTextByPosition(xSpreadsheet,2, startRow+2));
            entity.setShippingShop(getCellTextByPosition(xSpreadsheet,2, startRow+3));
            entity.getDetailEntities().addAll(
                    getDetailEntitiesFromStartRow(xSpreadsheet,entity,startRow+5));
            return entity;

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static List<ShipmentEntity.DetailEntity> getDetailEntitiesFromStartRow(XSpreadsheet xSpreadsheet, ShipmentEntity shipmentEntity ,int startRow){
        List<ShipmentEntity.DetailEntity> detailEntities = new ArrayList<>();
        try {
            int currentRow = startRow;
            while ( !getCellTextByPosition(xSpreadsheet,0, currentRow).isEmpty()){
                String mark = getCellTextByPosition(xSpreadsheet,0, currentRow);
                String markName = getCellTextByPosition(xSpreadsheet,1, currentRow);
                Integer count = (int)xSpreadsheet.getCellByPosition(2, currentRow).getValue();
                Double weightOfMarkKg = xSpreadsheet.getCellByPosition(3, currentRow).getValue();
                ShipmentEntity.DetailEntity detailEntity =
                        shipmentEntity.new DetailEntity(mark,markName,count,weightOfMarkKg);
                detailEntities.add(detailEntity);
                currentRow++;
            }
            return detailEntities;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static List<JournalWeldingEntity> getJournalWeldingEntities(){
        List<JournalWeldingEntity> result = new ArrayList<>();
        XComponent xComponent = loadXComponent(JOURNAL_TABLE_PATH,getHiddenAsTemplateProperties());
        XSpreadsheets xSpreadsheets = getSpreadsheets(xComponent);

        try {
            Object sheet = xSpreadsheets.getByName(xSpreadsheets.getElementNames()[0]);
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);

            int startRow = 5;
            int emptyRowsCount = 0;

            while(emptyRowsCount <2){
                String firstCellValue = getCellTextByPosition(xSpreadsheet,0,startRow);
                if(firstCellValue.isEmpty()){
                    emptyRowsCount++;
                }else{
                    result.add(getJournalWeldingEntityFromTableRow(startRow,xSpreadsheet));
                    startRow++;
                    emptyRowsCount=0;
                }
            }
            System.out.println(result.size()+" journal entities has found.");
            return result;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
       finally {
            close(xComponent);
        }

    }

    private static JournalWeldingEntity getJournalWeldingEntityFromTableRow(int row, XSpreadsheet xSpreadsheet){
        JournalWeldingEntity entity = new JournalWeldingEntity();
        try {
            entity.setNumber(getCellTextByPosition(xSpreadsheet,0,row));
            entity.setObjectName(getCellTextByPosition(xSpreadsheet,1, row));
            entity.setObjectAddress(getCellTextByPosition(xSpreadsheet, 2, row));
            entity.setCustomer(getCellTextByPosition(xSpreadsheet,3, row));
            entity.setContractNumberProduction(getCellTextByPosition(xSpreadsheet,4, row));
            entity.setProjectDeveloper(getCellTextByPosition(xSpreadsheet,5, row));
            entity.setWorkingDrawings(getCellTextByPosition(xSpreadsheet,6, row));
            entity.setContractNumberDeveloping(getCellTextByPosition(xSpreadsheet,7, row));
            entity.setArchiveNumberOfWorkingDrawings(getCellTextByPosition(xSpreadsheet,8, row));
            entity.setDateOfStartProduction(
                    getLocalDateFromDoubleValue(xSpreadsheet.getCellByPosition(9, row).getValue()));
            entity.setDateOfEndProduction(
                    getLocalDateFromDoubleValue(xSpreadsheet.getCellByPosition(10, row).getValue()));
            entity.setChangeNotesOfWorkingDrawings(getCellTextByPosition(xSpreadsheet,11, row));
            entity.setTimeOfKeepingJournal(getCellTextByPosition(xSpreadsheet,12, row));

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
        return entity;
    }




}