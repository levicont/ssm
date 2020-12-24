package com.lvg.ssm.utils;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSheetCellRange;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import com.sun.star.util.XCloseable;
import ooo.connector.BootstrapSocketConnector;

import java.time.LocalDate;

/**
 * Created by Victor Levchenko LVG Corp. on 22.11.2020.
 */
public class OpenOfficeUtils {
    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1899,12,30);
    public static final String DESKTOP_SERVICE = "com.sun.star.frame.Desktop";
    public static final String BLANK_STR = "_blank";

    private static XComponentContext xComponentContext;

    public static XComponentContext getXComponentContext(){
        try {
            if (null == xComponentContext){
                xComponentContext = BootstrapSocketConnector.bootstrap(ApplicationProperties.getProperty(ApplicationProperties.OPEN_OFFICE_PATH_PROPERTY_NAME));
                if (xComponentContext == null) {
                    System.err.println("ERROR: Could not bootstrap default Office.");
                }
            }
            return xComponentContext;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }


    public static LocalDate getLocalDateFromDoubleValue(double value){
        return DEFAULT_START_DATE.plusDays((long)value);
    }

    public static XComponent loadXComponent(String url, PropertyValue[] loadProperties){
        try {
            XMultiComponentFactory xRemoteContextServiceManager = getXComponentContext().getServiceManager();
            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,getXComponentContext());
            XComponentLoader xComponentLoader =
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            return xComponentLoader.loadComponentFromURL(url,BLANK_STR,0,loadProperties);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
    public static void close(XComponent xComponent){
        try {
            XModel xModel = UnoRuntime.queryInterface(XModel.class, xComponent);
            XCloseable xCloseable = UnoRuntime.queryInterface(XCloseable.class, xModel);
            xCloseable.close(false);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void closeContext(){
        if (xComponentContext==null)
            return;
        try {
            XMultiComponentFactory xRemoteContextServiceManager = xComponentContext.getServiceManager();
            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,xComponentContext);
            XComponentLoader xComponentLoader =
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, xComponentLoader);
            xDesktop.terminate();
            xComponentContext=null;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static XSpreadsheets getSpreadsheets(XComponent xComponent){
        try{
            XSpreadsheetDocument xSpreadsheetDocument =
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, xComponent);
            return xSpreadsheetDocument.getSheets();

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static PropertyValue[] getHiddenAsTemplateProperties(){
        PropertyValue[] result = new PropertyValue[2];
        result[0] = new PropertyValue();
        result[0].Name = "Hidden";
        result[0].Value = Boolean.TRUE;

        result[1] = new PropertyValue();
        result[1].Name = "AsTemplate";
        result[1].Value = Boolean.TRUE;
        return result;
    }

    public static String getCellTextByPosition(XSpreadsheet xSpreadsheet, int column, int row){
        try {
            XText text = UnoRuntime.queryInterface(XText.class,xSpreadsheet.getCellByPosition(column,row));
            return text.getString();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void setCellTextByPosition(XSpreadsheet xSpreadsheet, int column, int row, String text){
        try{
            XText xText = UnoRuntime.queryInterface(XText.class, xSpreadsheet.getCellByPosition(column, row));
            xText.setString(text);


        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static XTableRows getAllSpreadsheetRows(XSpreadsheet xSpreadsheet){
        try {
            XSheetCellRange xSheetCellRange = UnoRuntime.queryInterface(XSheetCellRange.class,xSpreadsheet);
            XColumnRowRange xColumnRowRange = UnoRuntime.queryInterface(XColumnRowRange.class,xSheetCellRange);
            return xColumnRowRange.getRows();
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void hideTableRows(XSpreadsheet xSpreadsheet, int startIndex, int count){
        if (null == xSpreadsheet)
            throw new NullPointerException("xSpreadsheet is null");
        if (startIndex <=0 || count <= 0)
            return;
        try {
            XTableRows xTableRows = getAllSpreadsheetRows(xSpreadsheet);

            for (int i = startIndex; i <= count; i++){
                XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class,
                        xTableRows.getByIndex(i));
                xPropertySet.setPropertyValue("IsVisible", false);
            }

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public static void showTableRows(XSpreadsheet xSpreadsheet, int startIndex, int count){
        if (null == xSpreadsheet)
            throw new NullPointerException("xSpreadsheet is null");
        if (startIndex <=0 || count <= 0)
            return;
        try {
            for (int i = startIndex; i <= count; i++){
                XTableRows xTableRows = getAllSpreadsheetRows(xSpreadsheet);
                XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class,
                        xTableRows.getByIndex(i));
                xPropertySet.setPropertyValue("IsVisible", true);
            }

        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

}
