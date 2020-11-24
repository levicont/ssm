package com.lvg.ssm;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor Levchenko LVG Corp. on 20.11.2020.
 */
public class DataExtractor {
    private static final String DESKTOP_SERVICE = "com.sun.star.frame.Desktop";
    private static final String BLANK_STR = "_blank";
    private static final String SHIPPING_TABLE_PATH = "file://"+DataExtractor.class
            .getClassLoader().getResource("templates/shipping-table.xlsx").getPath();




    private static XSpreadsheets getSpreadsheets(String url, PropertyValue[] loadProperties){
        try{
            XComponentContext xRemoteContext = Bootstrap.bootstrap();
            if (xRemoteContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
            }
            XMultiComponentFactory xRemoteContextServiceManager = xRemoteContext.getServiceManager();
            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,xRemoteContext);
            XComponentLoader xComponentLoader = (XComponentLoader)
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            XComponent xSpreadsheetComponent =
                    xComponentLoader.loadComponentFromURL(url,BLANK_STR,0,loadProperties);
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument)
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, xSpreadsheetComponent);
            return xSpreadsheetDocument.getSheets();

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static PropertyValue[] getHiddenAsTemplateProperties(){
        PropertyValue[] result = new PropertyValue[2];
        result[0] = new PropertyValue();
        result[0].Name = "Hidden";
        result[0].Value = Boolean.TRUE;

        result[1] = new PropertyValue();
        result[1].Name = "AsTemplate";
        result[1].Value = Boolean.TRUE;
        return result;
    }

    public static List<ShipmentEntity> getShipmentEntities() {
        List<ShipmentEntity> result = new ArrayList<>();
        XSpreadsheets xSpreadsheets = getSpreadsheets(SHIPPING_TABLE_PATH, getHiddenAsTemplateProperties());
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
        return result;

    }

    private static ShipmentEntity getShipmentEntityFromStartRow(XSpreadsheet xSpreadsheet, int startRow){
        try {
            ShipmentEntity entity = new ShipmentEntity();
            LocalDate date = OpenOfficeUtils.getLocalDateFromDoubleValue(xSpreadsheet.getCellByPosition(2, startRow).getValue());
            entity.setDate(date);
            entity.setTechnicalDrawings(xSpreadsheet.getCellByPosition(2,startRow+1).getFormula());
            entity.setObjectName(xSpreadsheet.getCellByPosition(2, startRow+2).getFormula());
            entity.setShippingShop(xSpreadsheet.getCellByPosition(2, startRow+3).getFormula());
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
            while ( !xSpreadsheet.getCellByPosition(0, currentRow).getFormula().isEmpty()){
                String mark = xSpreadsheet.getCellByPosition(0, currentRow).getFormula();
                String markName = xSpreadsheet.getCellByPosition(1, currentRow).getFormula();
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




}
