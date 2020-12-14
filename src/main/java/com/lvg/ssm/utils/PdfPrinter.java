package com.lvg.ssm.utils;

import com.lvg.ssm.entities.TestReport;
import com.sun.star.beans.PropertyValue;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XPrintable;

public class PdfPrinter {

    private static final String PROTOCOL_VT_PDF_PATH = "file:///home/lvg/tmp/VT-protocols/pdf/";
    private static final String PDF_FILE_SUFFIX = ".pdf";



    private static PropertyValue[] getPdfPrinterOptions(){
        PropertyValue[] properties = new PropertyValue[1];
        properties[0] = new PropertyValue();
        properties[0].Name = "Name";
        properties[0].Value = "5D PDF Creator";
        return properties;
    }

    public static void print(TestReport testReport, XSpreadsheetDocument xSpreadsheetDocument){
        try{
            XPrintable xPrintable = UnoRuntime.queryInterface(XPrintable.class,
                    xSpreadsheetDocument);

            xPrintable.setPrinter(getPdfPrinterOptions());

            PropertyValue[] propertyOptions = new PropertyValue[1];

            propertyOptions[0] = new PropertyValue();
            propertyOptions[0].Name = "FileName";
            propertyOptions[0].Value = getPdfFileName(testReport);


            xPrintable.print(propertyOptions);

        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static String getPdfFileName(TestReport testReport){
        return PROTOCOL_VT_PDF_PATH+testReport.getNumber().replace('/','-')+"_"+
                Formatter.formatDate(testReport.getDate()).replace('.','-')+
                PDF_FILE_SUFFIX;
    }


}
