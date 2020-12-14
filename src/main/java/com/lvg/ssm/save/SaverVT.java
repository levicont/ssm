package com.lvg.ssm.save;

import com.sun.star.beans.PropertyValue;
import com.sun.star.frame.XStorable;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.uno.UnoRuntime;

/**
 * Created by Victor Levchenko LVG Corp. on 13.12.2020.
 */
public class SaverVT implements Saver {


    @Override
    public void save(XSpreadsheetDocument xSpreadsheetDocument) {
        try{
            XStorable xStorable = UnoRuntime.queryInterface(XStorable.class, this.xSpreadsheetDocument);
            String path = PROTOCOL_VT_PATH+getVTProtocolFileName()+ODS_FILE_SUFFIX;
            xStorable.storeAsURL(path,new PropertyValue[0]);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
