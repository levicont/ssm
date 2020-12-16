package com.lvg.ssm.services;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.entities.TestReportType;
import com.lvg.ssm.utils.ApplicationProperties;
import com.lvg.ssm.utils.Formatter;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;

import static com.lvg.ssm.utils.ApplicationProperties.OPEN_OFFICE_FILE_PATH_PREFIX;
import static com.lvg.ssm.utils.ApplicationProperties.getProperty;

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
         System.out.println("Storing at url: "+path);
         xStorable.storeAsURL(path,new PropertyValue[0]);
      }catch (Exception ex){
         throw new RuntimeException(ex);
      }
   };

   private String getPath(){

      if (testReport.getType() == TestReportType.VT){
         return OPEN_OFFICE_FILE_PATH_PREFIX+
                 getRootPath()+
                 getProperty("ProtocolVTPath")+
                 getProtocolFileName()+
                 getProperty("OdsFileSuffix");
      }
      if (testReport.getType() == TestReportType.UT){
         return OPEN_OFFICE_FILE_PATH_PREFIX+
                 getRootPath()+
                 getProperty("ProtocolUTPath")+
                 getProtocolFileName()+
                 getProperty("OdsFileSuffix");
      }
      throw new RuntimeException("Cannot setup path for storing file");
   }


   private String getProtocolFileName(){
      if (testReport.getType() == TestReportType.VT)
         return getProperty("ProtocolVTFilePrefix")+testReport.getNumber().replace('/','-')+"_"+
                 Formatter.formatDate(testReport.getDate()).replace('.','-');
      if (testReport.getType() == TestReportType.UT)
         return getProperty("ProtocolUTFilePrefix")+testReport.getNumber().replace('/','-')+"_"+
                 Formatter.formatDate(testReport.getDate()).replace('.','-');
      throw new RuntimeException("Cannot setup file name for storing test report");
   }


}
