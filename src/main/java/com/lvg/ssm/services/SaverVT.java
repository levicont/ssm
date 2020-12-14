package com.lvg.ssm.services;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.utils.Formatter;
import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;

/**
 * Created by Victor Levchenko LVG Corp. on 13.12.2020.
 */
public class SaverVT extends Saver {

    private TestReport testReport;

    public SaverVT(TestReport testReport){
        this.testReport = testReport;
    }


    @Override
    public void save(XSpreadsheetDocument xSpreadsheetDocument) {
        try{
            XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, xSpreadsheetDocument);
            String path = PROTOCOL_VT_PATH+getVTProtocolFileName(testReport)+ODS_FILE_SUFFIX;
            xStorable.storeAsURL(path,new PropertyValue[0]);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private String getVTProtocolFileName(TestReport testReport){
        return PROTOCOL_VT_FILE_PREFIX+testReport.getNumber().replace('/','-')+"_"+
                Formatter.formatDate(testReport.getDate()).replace('.','-');
    }
}
