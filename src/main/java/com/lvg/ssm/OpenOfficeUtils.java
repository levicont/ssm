package com.lvg.ssm;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;
import sun.misc.ExtensionInstallationException;

import java.time.LocalDate;

/**
 * Created by Victor Levchenko LVG Corp. on 22.11.2020.
 */
public class OpenOfficeUtils {
    private static final LocalDate DEFAULT_START_DATE = LocalDate.of(1899,12,30);
    private static final String DESKTOP_SERVICE = "com.sun.star.frame.Desktop";
    private static final String BLANK_STR = "_blank";


    public static LocalDate getLocalDateFromDoubleValue(double value){
        return DEFAULT_START_DATE.plusDays((long)value);
    }

    public static XSpreadsheets getSpreadsheets(String url, PropertyValue[] loadProperties){
        try{
            XComponentContext xRemoteContext = Bootstrap.bootstrap();
            if (xRemoteContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
            }
            XMultiComponentFactory xRemoteContextServiceManager = xRemoteContext.getServiceManager();
            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,xRemoteContext);
            XComponentLoader xComponentLoader =
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            XComponent xSpreadsheetComponent =
                    xComponentLoader.loadComponentFromURL(url,BLANK_STR,0,loadProperties);
            XSpreadsheetDocument xSpreadsheetDocument =
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, xSpreadsheetComponent);
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


}
