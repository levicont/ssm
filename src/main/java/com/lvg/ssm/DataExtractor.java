package com.lvg.ssm;

import com.sun.star.beans.PropertyValue;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

/**
 * Created by Victor Levchenko LVG Corp. on 20.11.2020.
 */
public class DataExtractor {
    private static final String DESKTOP_SERVICE = "com.sun.star.frame.Desktop";
    private static final String BLANK_STR = "_blank";


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

    private static PropertyValue[] getHiddenProperties(){
        PropertyValue[] result = new PropertyValue[1];
        result[0] = new PropertyValue();
        result[0].Name = "Hidden";
        result[0].Value = Boolean.TRUE;
        return result;
    }
}
