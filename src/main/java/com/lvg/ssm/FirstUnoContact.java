package com.lvg.ssm;

import com.sun.star.awt.Point;
import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.comp.helper.BootstrapException;
import com.sun.star.container.XIndexAccess;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XDesktop;
import com.sun.star.frame.XModel;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.*;
import com.sun.star.table.XCell;
import com.sun.star.table.XCellRange;
import com.sun.star.table.XColumnRowRange;
import com.sun.star.table.XTableRows;
import com.sun.star.text.XText;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Victor Levchenko LVG Corp. on 15.11.2020.
 */
public class FirstUnoContact {
    private static final String DESKTOP_SERVICE = "com.sun.star.frame.Desktop";
    private static final String PATH = FirstUnoContact.class.getClassLoader()
            .getResource("templates/prot-VT-UT-template.ods").getFile();
    private static final String SPREADSHEET_COMPONENT_URL = "private:factory/scalc";
    private static final String SPREADSHEET_DOC_URL = "file:///"+PATH;
    private static final String BLANK_STR = "_blank";
    private static final String DEFAULT_SHEET_NAME = "MySheet";

    private XComponent xSpreadsheetComponent;
    private XComponentContext xComponentContext;
    private XMultiComponentFactory xMultiComponentFactory;
    private XDesktop xDesktop;

    public FirstUnoContact(){
        this.xComponentContext = getComponentContext();
        this.xMultiComponentFactory = getComponentFactory(this.xComponentContext);
        this.xDesktop = getDesktop(this.xComponentContext);

    }

    public static void main(String[] args) {
        FirstUnoContact fuc = new FirstUnoContact();
        try {
            XSpreadsheet xSpreadsheet = fuc.getSpreadSheetByURLAndName(SPREADSHEET_COMPONENT_URL, "", null);

            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setValue(1d);

            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setValue(2d);

            XSheetCellRange xSheetCellRange = UnoRuntime.queryInterface(XSheetCellRange.class,
                    xSpreadsheet);
            System.out.println(xSheetCellRange);
            XColumnRowRange xColumnRowRange = UnoRuntime.queryInterface(XColumnRowRange.class,
                    xSheetCellRange);
            XTableRows xTableRows = xColumnRowRange.getRows();
            int count = xTableRows.getCount();
            XPropertySet xPropertySet = UnoRuntime.queryInterface(XPropertySet.class,
                    xTableRows.getByIndex(1));
            Arrays.stream(xPropertySet.getPropertySetInfo().getProperties())
                    .forEach(property -> {
                        System.out.println(property.Name+"\t\t"+property.Type.getTypeName()+
                        "\t\t");

                    });
            xPropertySet.setPropertyValue("IsVisible",false);
            System.out.println("Position: "+ ((Point)xPropertySet.getPropertyValue("Position")).X);

            System.out.println(xTableRows.getByIndex(2));
            System.out.println("count = "+count);

            XCellRangeMovement xCellRangeMovement = UnoRuntime.queryInterface(XCellRangeMovement.class,
                    xSpreadsheet);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        //fuc.xDesktop.terminate();
        try{
            
            /*XSpreadsheet xSpreadsheet = fuc.getSpreadSheetByURLAndName(SPREADSHEET_DOC_URL,DEFAULT_SHEET_NAME,new PropertyValue[0]);
            XCell xCell = xSpreadsheet.getCellByPosition(0, 0);
            xCell.setValue(24);

            xCell = xSpreadsheet.getCellByPosition(0, 1);
            xCell.setValue(21);
            xCell = xSpreadsheet.getCellByPosition(0, 2);
            xCell.setFormula("=sum(A1:A2)");

            XPropertySet xCellProps = (XPropertySet)UnoRuntime.queryInterface(
                    XPropertySet.class, xCell);
            xCellProps.setPropertyValue("CellStyle", "Result");

            XModel xSpreadsheetModel = (XModel)UnoRuntime.queryInterface(
                    XModel.class, fuc.xSpreadsheetComponent);
            XController xSpreadsheetController = xSpreadsheetModel.getCurrentController();
            XSpreadsheetView xSpreadsheetView = (XSpreadsheetView)
                    UnoRuntime.queryInterface(XSpreadsheetView.class,
                            xSpreadsheetController);
            xSpreadsheetView.setActiveSheet(xSpreadsheet);
*/

            //fuc.getDataFromXlsx("");
            //fuc.xSpreadsheetComponent.dispose();
            /*List<ShipmentEntity> shipmentEntities = DataExtractor.getShipmentEntities();
            shipmentEntities.forEach(shipmentEntity -> {
                System.out.println(shipmentEntity.getDate());
                System.out.println(shipmentEntity.getTechnicalDrawings());
                System.out.println(shipmentEntity.getObjectName());
                System.out.println(shipmentEntity.getShippingShop());
                shipmentEntity.getDetailEntities().forEach(System.out::println);
                System.out.println();
            });
            */
            //DataExtractor.getJournalWeldingEntities();


        } finally {

        }
    }

    private XSpreadsheet getSpreadSheetByURLAndName(String url, String spreadsheetName, PropertyValue[] propertyValues){
        try{
            XSpreadsheets xSpreadsheets = getXSpreadsheetDocumentByURL(url, propertyValues).getSheets();
            Object sheet;
            XIndexAccess xIndexAccess;
            if(!xSpreadsheets.hasByName(spreadsheetName)){
                xIndexAccess = UnoRuntime.queryInterface(XIndexAccess.class,
                        xSpreadsheets);
                sheet = xIndexAccess.getByIndex(0);
            }else
                sheet = xSpreadsheets.getByName(spreadsheetName);


            return (XSpreadsheet)UnoRuntime.queryInterface(XSpreadsheet.class,sheet);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private XComponentContext getComponentContext(){
        try{
            return Bootstrap.bootstrap();
        }catch (java.lang.Exception ex){
            System.err.println("ERROR: Could not bootstrap default Office.");
            throw new RuntimeException(ex);
        }
    }

    private XMultiComponentFactory getComponentFactory(XComponentContext context){
        try{
            return context.getServiceManager();
        }catch (Exception ex){
            System.err.println("ERROR: Could not get ServiceManager from context");
            throw new RuntimeException(ex);
        }
    }

    private XDesktop getDesktop(XComponentContext xComponentContext){
        try{
            Object desktop = xMultiComponentFactory.createInstanceWithContext(DESKTOP_SERVICE,xComponentContext);
            XDesktop xDesktop = UnoRuntime.queryInterface(XDesktop.class, desktop);
            return xDesktop;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private XSpreadsheetDocument getXSpreadsheetDocumentByURL(String url, PropertyValue[] propertyValues){
        try{

            String available = (this.xMultiComponentFactory != null ?"available":"not available");
            System.out.println("Office is "+available);

            XComponentLoader xComponentLoader = UnoRuntime.queryInterface(XComponentLoader.class,this.xDesktop);
            this.xSpreadsheetComponent =
                    xComponentLoader.loadComponentFromURL(url,BLANK_STR,0,propertyValues);
            XSpreadsheetDocument xSpreadsheetDocument =
                    UnoRuntime.queryInterface(XSpreadsheetDocument.class, xSpreadsheetComponent);
            return xSpreadsheetDocument;
        }catch (Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private void getDataFromXlsx(String path){
        PropertyValue[] propertyValues = new PropertyValue[1];
        propertyValues[0] = new PropertyValue();
        propertyValues[0].Name = "Hidden";
        propertyValues[0].Value = Boolean.TRUE;
        String pathToXlsx = FirstUnoContact.class.getClassLoader().getResource("templates/shipping-table.xlsx")
                .getPath();
        System.out.println("Path to xls[ file is: "+pathToXlsx);
        try{
            XSpreadsheet xSpreadsheet =
                    getSpreadSheetByURLAndName("file://"+pathToXlsx,"Ноябрь", propertyValues);
            XCell xCell = xSpreadsheet.getCellByPosition(2,2);
            System.out.println("Date is: " + xCell.getValue());
            LocalDate startDate = LocalDate.of(1899,12,30);
            System.out.println("Start date is: " + startDate);
            LocalDate currentDate = startDate.plusDays((long)xCell.getValue());
            System.out.println("Current date is: "+ currentDate);
            System.out.print(LocalDate.ofEpochDay((long)xCell.getValue()).getDayOfMonth());
            System.out.print("."+LocalDate.ofEpochDay((long)xCell.getValue()).getMonth());
            System.out.println("."+ LocalDate.ofEpochDay((long)xCell.getValue()).getYear());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
