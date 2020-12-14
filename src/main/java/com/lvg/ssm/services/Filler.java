package com.lvg.ssm.services;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.utils.Formatter;
import com.lvg.ssm.utils.OpenOfficeUtils;
import com.lvg.ssm.utils.PdfPrinter;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XStorable;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XHeaderFooterContent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.uno.UnoRuntime;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import static com.lvg.ssm.utils.OpenOfficeUtils.*;

public class Filler {

    private static final int APPENDIX_ENTITIES_START_ROW = 76;
    private static final int APPENDIX_ENTITIES_FINAL_ROW = 198;
    private static final String PROTOCOL_TAMPLATE_PATH = "file://"+ Objects.requireNonNull(Filler.class
            .getClassLoader().getResource("templates/prot-VT-template.ods")).getPath();
    private static final String PROTOCOL_VT_PAGE_STYLE_NAME = "protocol-VT";
    private static final String PROTOCOL_UT_PAGE_STYLE_NAME = "protocol-UT";



    private XSpreadsheetDocument xSpreadsheetDocument;
    private XComponent xComponent;
    private TestReport testReport;



    public Filler(TestReport testReport){
        PropertyValue[] loadProperties = getHiddenAsTemplateProperties();
        initXComponent(loadProperties);
        this.xSpreadsheetDocument = getxSpreadsheetDocument();
        this.testReport = testReport;
    }


    private void initXComponent(PropertyValue[] loadProperties){
        try{
            XMultiComponentFactory xRemoteContextServiceManager = getXComponentContext().getServiceManager();
            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,getXComponentContext());
            XComponentLoader xComponentLoader =
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            this.xComponent = xComponentLoader.loadComponentFromURL(PROTOCOL_TAMPLATE_PATH,BLANK_STR,0,loadProperties);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private XSpreadsheetDocument getxSpreadsheetDocument(){
        try {
            if (this.xSpreadsheetDocument == null) {
                this.xSpreadsheetDocument = UnoRuntime.queryInterface(XSpreadsheetDocument.class,
                        this.xComponent);
            }
            return this.xSpreadsheetDocument;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void print(){

      PdfPrinter.print(testReport,xSpreadsheetDocument);

    }

    public void save(){
        Saver saver = new SaverVT(testReport);
        saver.save(xSpreadsheetDocument);
    }



    public void close(){
        try {
           OpenOfficeUtils.close(xComponent);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void fillUpVTReport(){
        System.out.println("Starting filling report");
        PropertyValue[] props = getHiddenAsTemplateProperties();
        props[0].Value = Boolean.FALSE;
        try {
            XSpreadsheets xSpreadsheets = this.xSpreadsheetDocument.getSheets();
            Object sheet = xSpreadsheets.getByName(xSpreadsheets.getElementNames()[0]);
            XSpreadsheet xSpreadsheet = UnoRuntime.queryInterface(XSpreadsheet.class, sheet);
            System.out.println("Number: "+ testReport.getNumber());
            System.out.println("Date: "+ Formatter.formatDate(testReport.getDate()));
            setCellTextByPosition(xSpreadsheet,5,9,testReport.getNumber());
            setCellTextByPosition(xSpreadsheet,7,9,Formatter.formatDate(testReport.getDate()));
            setCellTextByPosition(xSpreadsheet,3, 13, testReport.getWorkingDrawings());
            setCellTextByPosition(xSpreadsheet,8, 17, testReport.getWelder());
            setCellTextByPosition(xSpreadsheet,8, 18, testReport.getWelderEng());
            setCellTextByPosition(xSpreadsheet,8, 20, testReport.getWelderMark());
            setCellTextByPosition(xSpreadsheet,8, 21, testReport.getWelderMarkEng());

            int appendixStartRow = APPENDIX_ENTITIES_START_ROW;
            int appendixEntitiesCount = testReport.getAppendixEntities().size();
            for(int i = 0; i < appendixEntitiesCount; i++ ){
                TestReport.AppendixEntity appendixEntity = testReport.getAppendixEntities().get(i);
                try {
                    setCellTextByPosition(xSpreadsheet,0, appendixStartRow, (i+1)+"");
                    setCellTextByPosition(xSpreadsheet,1, appendixStartRow, appendixEntity.getWorkingDrawings());
                    setCellTextByPosition(xSpreadsheet,3, appendixStartRow, appendixEntity.getPosition());
                    setCellTextByPosition(xSpreadsheet,4, appendixStartRow, appendixEntity.getPositionName());
                    setCellTextByPosition(xSpreadsheet,6, appendixStartRow, appendixEntity.getAmount()+"");
                    setCellTextByPosition(xSpreadsheet,7, appendixStartRow, appendixEntity.getCondition().toString());
                    setCellTextByPosition(xSpreadsheet,8, appendixStartRow, appendixEntity.getComments());
                    appendixStartRow++;
                }catch (Exception ex){
                    throw new RuntimeException(ex);
                }
            }

            showTableRows(xSpreadsheet,APPENDIX_ENTITIES_START_ROW,appendixEntitiesCount);
            hideTableRows(xSpreadsheet,APPENDIX_ENTITIES_START_ROW+appendixEntitiesCount,APPENDIX_ENTITIES_FINAL_ROW);
            fillUpFooter(testReport);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void fillUpFooter(TestReport testReport){
        XStyleFamiliesSupplier xStyleFamiliesSupplier = UnoRuntime.queryInterface(XStyleFamiliesSupplier.class,
                this.xSpreadsheetDocument);

        try {
            Object pageStyle = xStyleFamiliesSupplier.getStyleFamilies().getByName("PageStyles");
            XNameContainer xNameContainer = UnoRuntime.queryInterface(XNameContainer.class,
                    pageStyle);
            XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class,
                    xNameContainer.getByName(PROTOCOL_VT_PAGE_STYLE_NAME));

            XHeaderFooterContent rightPageFooterContent = UnoRuntime.queryInterface(XHeaderFooterContent.class,
                    xPropertySet.getPropertyValue("RightPageFooterContent"));
            rightPageFooterContent.getLeftText().setString(getLeftFooterText(testReport));
            xPropertySet.setPropertyValue("RightPageFooterContent",rightPageFooterContent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLeftFooterText(TestReport testReport){
        String number = testReport.getNumber();
        String date = Formatter.formatDate(testReport.getDate());
        return "Протокол VT № "+number+" от "+date+"\n" +
                "Report VT #"+number+" of "+date;
    }



}
