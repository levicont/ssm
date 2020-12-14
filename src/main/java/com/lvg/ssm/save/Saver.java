package com.lvg.ssm.save;

import com.lvg.ssm.TestReport;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;

/**
 * Created by Victor Levchenko LVG Corp. on 13.12.2020.
 */
public abstract class Saver {

    private static final String PROTOCOL_VT_PATH = "file:///home/lvg/tmp/VT-protocols/";
    private static final String ODS_FILE_SUFFIX = ".ods";
    private static final String PROTOCOL_VT_FILE_PREFIX = "прот-VT-";


    abstract void save(XSpreadsheetDocument xSpreadsheetDocument);

    protected String getVTProtocolFileName(TestReport testReport){
        return PROTOCOL_VT_FILE_PREFIX+testReport.getNumber().replace('/','-')+"_"+
                formatDate(testReport.getDate()).replace('.','-');
    }
}
