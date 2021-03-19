package com.lvg.ssm.services;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.entities.TestReportType;
import com.lvg.ssm.utils.ApplicationProperties;
import com.lvg.ssm.utils.Formatter;
import com.lvg.ssm.utils.OpenOfficeUtils;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.lvg.ssm.utils.ApplicationProperties.OPEN_OFFICE_FILE_PATH_PREFIX;
import static com.lvg.ssm.utils.ApplicationProperties.USER_HOME_PATH_PROPERTY_NAME;
import static com.lvg.ssm.utils.ApplicationProperties.getProperty;
import static com.lvg.ssm.utils.OpenOfficeUtils.getFileNameAccordingOS;

/**
 * Created by Victor Levchenko LVG Corp. on 13.12.2020.
 */
public class Saver {

   private TestReport testReport;

   public Saver(TestReport testReport){
      this.testReport = testReport;
   }


   protected String getRootPath(){
      return ApplicationProperties.getProperty(ApplicationProperties.USER_HOME_PATH_PROPERTY_NAME);
   }

   public void save(XSpreadsheetDocument xSpreadsheetDocument){

      try{
         XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, xSpreadsheetDocument);
         String path = getPath();
         xStorable.storeAsURL(path,new PropertyValue[0]);
         System.out.println("Stored at url: "+path);
      }catch (Exception ex){
         throw new RuntimeException(ex);
      }
   }

   public void exportPDF(XSpreadsheetDocument xSpreadsheetDocument){
      try{
         XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, xSpreadsheetDocument);
         String path = getPdfPath();

         PropertyValue[] propertyValues = new PropertyValue[1];
         propertyValues[0] = new PropertyValue();
         propertyValues[0].Name = "FilterName";
         propertyValues[0].Value = "writer_pdf_Export";
         System.out.println("PDF creating at url: "+path);
         xStorable.storeToURL(path,propertyValues);

      }catch (Exception ex){
         throw new RuntimeException(ex);
      }
   }

   private String getPdfPath() {
      if (testReport.getType() == TestReportType.VT){
         return OPEN_OFFICE_FILE_PATH_PREFIX+
                 OpenOfficeUtils.getFileNameAccordingOS(
                         getPdfFileName());
      }
      if (testReport.getType() == TestReportType.UT){
         return OPEN_OFFICE_FILE_PATH_PREFIX+
                 OpenOfficeUtils.getFileNameAccordingOS(
                         getPdfFileName());
      }
      throw new RuntimeException("Cannot setup path for storing file");
   }

   private String getPdfFileName(){
      String pathToPdfDir;
      if (testReport.getType() == TestReportType.VT) {
         pathToPdfDir = getProperty(USER_HOME_PATH_PROPERTY_NAME)+getProperty("ProtocolVTPdfPath");
         checkStoreDirectory(pathToPdfDir);
         return  pathToPdfDir+
                 getProperty("ProtocolVTPdfFilePrefix") +
                 testReport.getFormatNumber(4).replace('/', '-') + "_" +
                 Formatter.formatDate(testReport.getDate()).replace('.', '-') +
                 getProperty("PdfFileSuffix");
      }
      if (testReport.getType() == TestReportType.UT){
         pathToPdfDir = getProperty(USER_HOME_PATH_PROPERTY_NAME)+getProperty("ProtocolUTPdfPath");
         checkStoreDirectory(pathToPdfDir);
         return  pathToPdfDir+
                 getProperty("ProtocolUTPdfFilePrefix")+
                 testReport.getFormatNumber(4).replace('/','-')+"_"+
                 Formatter.formatDate(testReport.getDate()).replace('.','-')+
                 getProperty("PdfFileSuffix");
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

   private String getPath(){

      if (testReport.getType() == TestReportType.VT){
         return OPEN_OFFICE_FILE_PATH_PREFIX+
                 OpenOfficeUtils.getFileNameAccordingOS(getRootPath()+
                  getProperty("ProtocolVTPath")+
                  getProtocolFileName()+
                  getProperty("OdsFileSuffix"));
      }
      if (testReport.getType() == TestReportType.UT){
         return OPEN_OFFICE_FILE_PATH_PREFIX+
                 OpenOfficeUtils.getFileNameAccordingOS(getRootPath()+
                  getProperty("ProtocolUTPath")+
                  getProtocolFileName()+
                  getProperty("OdsFileSuffix"));
      }
      throw new RuntimeException("Cannot setup path for storing file");
   }


   private String getProtocolFileName(){
      if (testReport.getType() == TestReportType.VT)
         return getProperty("ProtocolVTFilePrefix")+testReport.getFormatNumber(4).replace('/','-')+"_"+
                 Formatter.formatDate(testReport.getDate()).replace('.','-');
      if (testReport.getType() == TestReportType.UT)
         return getProperty("ProtocolUTFilePrefix")+testReport.getFormatNumber(4).replace('/','-')+"_"+
                 Formatter.formatDate(testReport.getDate()).replace('.','-');
      throw new RuntimeException("Cannot setup file name for storing test report");
   }


}
