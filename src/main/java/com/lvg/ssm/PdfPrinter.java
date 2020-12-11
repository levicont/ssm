package com.lvg.ssm;

import com.sun.star.beans.PropertyValue;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XPrintable;

public class PdfPrinter {

    private static PropertyValue[] getPdfPrinterOptions(){
        PropertyValue[] properties = new PropertyValue[1];
        properties[0] = new PropertyValue();
        properties[0].Name = "Name";
        properties[0].Value = "5D PDF Creator";
        return properties;
    }

    public static void print(XSpreadsheetDocument xSpreadsheetDocument, String pathToFile){
        try{
            XPrintable xPrintable = UnoRuntime.queryInterface(XPrintable.class,
                    xSpreadsheetDocument);

            xPrintable.setPrinter(getPdfPrinterOptions());

            PropertyValue[] propertyOptions = new PropertyValue[1];

            propertyOptions[0] = new PropertyValue();
            propertyOptions[0].Name = "FileName";
            propertyOptions[0].Value = pathToFile;


            xPrintable.print(propertyOptions);

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }


}
