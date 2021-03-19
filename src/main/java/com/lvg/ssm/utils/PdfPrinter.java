package com.lvg.ssm.utils;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.entities.TestReportType;
import com.sun.star.beans.PropertyValue;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.view.XPrintable;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.lvg.ssm.utils.ApplicationProperties.*;
import static com.lvg.ssm.utils.OpenOfficeUtils.getFileNameAccordingOS;

public class PdfPrinter {

    private static PropertyValue[] getPdfPrinterOptions(){
        PropertyValue[] properties;
        properties = new PropertyValue[1];
        properties[0] = new PropertyValue();
        properties[0].Name = "Name";
        properties[0].Value = "5D PDF Creator";
        properties[0].Value = "PDF24";
        return properties;
    }

    public static void print(TestReport testReport, XSpreadsheetDocument xSpreadsheetDocument){
        try{
            XPrintable xPrintable = UnoRuntime.queryInterface(XPrintable.class,
                    xSpreadsheetDocument);

            xPrintable.setPrinter(getPdfPrinterOptions());

            PropertyValue[] propertyOptions = new PropertyValue[2];

            propertyOptions[0] = new PropertyValue();
            propertyOptions[0].Name = "FileName";
            propertyOptions[0].Value = OPEN_OFFICE_FILE_PATH_PREFIX+getPdfFileName(testReport);

            xPrintable.print(propertyOptions);
            System.out.println("Printed at: "+propertyOptions[0].Value);
        }catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static String getPdfFileName(TestReport testReport){
        String pathToPdfDir;
        if (testReport.getType() == TestReportType.VT) {
            pathToPdfDir = getProperty(USER_HOME_PATH_PROPERTY_NAME)+getProperty("ProtocolVTPdfPath");
            checkStoreDirectory(pathToPdfDir);
            return  getFileNameAccordingOS(pathToPdfDir+
                    getProperty("ProtocolVTPdfFilePrefix") +
                    testReport.getFormatNumber(4).replace('/', '-') + "_" +
                    Formatter.formatDate(testReport.getDate()).replace('.', '-') +
                    getProperty("PdfFileSuffix"));
        }
        if (testReport.getType() == TestReportType.UT){
            pathToPdfDir = getProperty(USER_HOME_PATH_PROPERTY_NAME)+getProperty("ProtocolUTPdfPath");
            checkStoreDirectory(pathToPdfDir);
            return  getFileNameAccordingOS(pathToPdfDir+
                    getProperty("ProtocolUTPdfFilePrefix")+
                    testReport.getFormatNumber(4).replace('/','-')+"_"+
                    Formatter.formatDate(testReport.getDate()).replace('.','-')+
                    getProperty("PdfFileSuffix"));
        }
        throw new IllegalArgumentException("Test report type not supported");
    }

    private static void checkStoreDirectory(String path){

        Path pathDir = Paths.get(path);
        if (Files.exists(pathDir))
            return;
        try {
            Files.createDirectories(pathDir);
        }catch (FileAlreadyExistsException ex){
            System.out.println("Directory: "+path+"already exists");
        }catch (IOException ex){
            ex.printStackTrace();
            System.exit(1);
        }
    }


}
