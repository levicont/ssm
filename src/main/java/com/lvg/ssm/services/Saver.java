package com.lvg.ssm.services;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.utils.Formatter;
import com.sun.star.sheet.XSpreadsheetDocument;

/**
 * Created by Victor Levchenko LVG Corp. on 13.12.2020.
 */
public abstract class Saver {

    protected static final String PROTOCOL_VT_PATH = "file:///home/lvg/tmp/VT-protocols/";
    protected static final String ODS_FILE_SUFFIX = ".ods";
    protected static final String PROTOCOL_VT_FILE_PREFIX = "прот-VT-";


    abstract void save(XSpreadsheetDocument xSpreadsheetDocument);


}
