package com.lvg.ssm.services;

import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.entities.TestReportType;
import com.lvg.ssm.utils.Formatter;
import com.lvg.ssm.utils.OpenOfficeUtils;
import com.lvg.ssm.utils.PdfPrinter;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.container.XNameContainer;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XHeaderFooterContent;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.style.XStyleFamiliesSupplier;
import com.sun.star.uno.UnoRuntime;

import java.util.Arrays;
import java.util.Objects;

import static com.lvg.ssm.utils.OpenOfficeUtils.*;

public class Filler {

    private static final int PROTOCOL_VT_APPENDIX_ENTITIES_START_ROW = 76;
    private static final int PROTOCOL_VT_APPENDIX_ENTITIES_FINAL_ROW = 198;
    private static final int PROTOCOL_UT_APPENDIX_ENTITIES_START_ROW = 80;
    private static final int PROTOCOL_UT_APPENDIX_ENTITIES_FINAL_ROW = 198;

    private static final String PROTOCOL_VT_TEMPLATE_PATH = "file://"+ Objects.requireNonNull(Filler.class
            .getClassLoader().getResource("templates/prot-VT-template.ods")).getPath();
    private static final String PROTOCOL_UT_TEMPLATE_PATH = "file://"+ Objects.requireNonNull(Filler.class
            .getClassLoader().getResource("templates/prot-UT-template.ods")).getPath();
    private static final String PROTOCOL_VT_PAGE_STYLE_NAME = "protocol-VT";
    private static final String PROTOCOL_UT_PAGE_STYLE_NAME = "protocol-UT";



    private XSpreadsheetDocument xSpreadsheetDocument;
    private XComponent xComponent;
    private TestReport testReport;
    private String currentTemplatePath;
    private String currentPageStyleName;
    private int currentAppendixEntityStartRow;
    private int currentAppendixEntityFinalRow;



    public Filler(TestReport testReport){
        if (null == testReport)
            throw new NullPointerException("Test report cannot be null!");
        this.testReport = testReport;
        initCurrentOptions(testReport);

        PropertyValue[] loadProperties = getHiddenAsTemplateProperties();
        initXComponent(loadProperties);
        this.xSpreadsheetDocument = getXSpreadsheetDocument();

    }

    private void initCurrentOptions(TestReport testReport){
        if (testReport.getType() == TestReportType.VT){
            currentTemplatePath = PROTOCOL_VT_TEMPLATE_PATH;
            currentPageStyleName = PROTOCOL_VT_PAGE_STYLE_NAME;
            currentAppendixEntityStartRow = PROTOCOL_VT_APPENDIX_ENTITIES_START_ROW;
            currentAppendixEntityFinalRow = PROTOCOL_VT_APPENDIX_ENTITIES_FINAL_ROW;
        }
        if (testReport.getType() == TestReportType.UT){
            currentTemplatePath = PROTOCOL_UT_TEMPLATE_PATH;
            currentPageStyleName = PROTOCOL_UT_PAGE_STYLE_NAME;
            currentAppendixEntityStartRow = PROTOCOL_UT_APPENDIX_ENTITIES_START_ROW;
            currentAppendixEntityFinalRow = PROTOCOL_UT_APPENDIX_ENTITIES_FINAL_ROW;
        }
    }


    private void initXComponent(PropertyValue[] loadProperties){
        try{
            XMultiComponentFactory xRemoteContextServiceManager = getXComponentContext().getServiceManager();
            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,getXComponentContext());
            XComponentLoader xComponentLoader =
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            this.xComponent = xComponentLoader.loadComponentFromURL(currentTemplatePath,BLANK_STR,0,loadProperties);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private XSpreadsheetDocument getXSpreadsheetDocument(){
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
        new Saver(testReport).save(xSpreadsheetDocument);

    }

    public void exportPDF(){
        new Saver(testReport).exportPDF(xSpreadsheetDocument);
    }



    public void close(){
        try {
           OpenOfficeUtils.close(xComponent);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    public void fillUpReport(){
        if (testReport.getType() == TestReportType.VT)
            fillUpVTReport();
        if (testReport.getType() == TestReportType.UT)
            fillUpUTReport();
    }

    private void fillUpUTReport(){
        System.out.println("Starting filling UT report");
        try {
            XSpreadsheets xSpreadsheets = xSpreadsheetDocument.getSheets();
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

            int appendixStartRow = currentAppendixEntityStartRow;
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

            showTableRows(xSpreadsheet, currentAppendixEntityStartRow,appendixEntitiesCount);
            hideTableRows(xSpreadsheet, currentAppendixEntityStartRow +appendixEntitiesCount, currentAppendixEntityFinalRow);
            fillUpFooter(testReport);
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void fillUpVTReport(){
        System.out.println("Starting filling VT report");
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

            int appendixStartRow = currentAppendixEntityStartRow;
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

            showTableRows(xSpreadsheet, currentAppendixEntityStartRow,appendixEntitiesCount);
            hideTableRows(xSpreadsheet, currentAppendixEntityStartRow +appendixEntitiesCount, currentAppendixEntityFinalRow);
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
                    xNameContainer.getByName(currentPageStyleName));

            XHeaderFooterContent rightPageFooterContent = UnoRuntime.queryInterface(XHeaderFooterContent.class,
                    xPropertySet.getPropertyValue("RightPageFooterContent"));
            rightPageFooterContent.getLeftText().setString(getLeftFooterText(testReport));
            xPropertySet.setPropertyValue("RightPageFooterContent",rightPageFooterContent);


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private String getLeftFooterText(TestReport testReport){
        String number = testReport.getNumber();
        String date = Formatter.formatDate(testReport.getDate());
        if (testReport.getType()==TestReportType.VT)
            return "Протокол VT № "+number+" от "+date+"\n" +
                    "Report VT #"+number+" of "+date;
        if (testReport.getType()==TestReportType.UT)
            return "Протокол UT № "+number+" от "+date+"\n" +
                "Report UT #"+number+" of "+date;
        throw new IllegalArgumentException("Test report type not supported");
    }



}
