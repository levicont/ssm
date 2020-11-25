package com.lvg.ssm;

import com.sun.star.beans.PropertyValue;
import com.sun.star.beans.XPropertySet;
import com.sun.star.comp.helper.Bootstrap;
import com.sun.star.frame.XComponentLoader;
import com.sun.star.frame.XController;
import com.sun.star.frame.XModel;
import com.sun.star.lang.EventObject;
import com.sun.star.lang.XComponent;
import com.sun.star.lang.XEventListener;
import com.sun.star.lang.XMultiComponentFactory;
import com.sun.star.sheet.XSpreadsheet;
import com.sun.star.sheet.XSpreadsheetDocument;
import com.sun.star.sheet.XSpreadsheetView;
import com.sun.star.sheet.XSpreadsheets;
import com.sun.star.table.XCell;
import com.sun.star.uno.UnoRuntime;
import com.sun.star.uno.XComponentContext;

import java.time.LocalDate;
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



    public static void main(String[] args) {
        FirstUnoContact fuc = new FirstUnoContact();
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

            fuc.getDataFromXlsx("");
            fuc.xSpreadsheetComponent.dispose();
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
            DataExtractor.getJournalWeldingEntities();


        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            System.exit(0);
        }
    }

    private XSpreadsheet getSpreadSheetByURLAndName(String url, String spreadsheetName, PropertyValue[] propertyValues){
        try{
            XSpreadsheets xSpreadsheets = getXSpreadsheetDocumentByURL(url, propertyValues).getSheets();
            Object sheet = xSpreadsheets.getByName(spreadsheetName);
            return (XSpreadsheet)UnoRuntime.queryInterface(XSpreadsheet.class,sheet);
        }catch(Exception ex){
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }

    private XSpreadsheetDocument getXSpreadsheetDocumentByURL(String url, PropertyValue[] propertyValues){
        try{
            XComponentContext xRemoteContext = Bootstrap.bootstrap();
            if (xRemoteContext == null) {
                System.err.println("ERROR: Could not bootstrap default Office.");
            }
            System.out.println("Connected to a running office ...");

            XMultiComponentFactory xRemoteContextServiceManager = xRemoteContext.getServiceManager();
            String available = (xRemoteContextServiceManager != null ?"available":"not available");
            System.out.println("Office is "+available);

            Object desktop = xRemoteContextServiceManager.createInstanceWithContext(DESKTOP_SERVICE,xRemoteContext);
            XComponentLoader xComponentLoader = (XComponentLoader)
                    UnoRuntime.queryInterface(XComponentLoader.class,desktop);
            this.xSpreadsheetComponent =
                    xComponentLoader.loadComponentFromURL(url,BLANK_STR,0,propertyValues);
            XSpreadsheetDocument xSpreadsheetDocument = (XSpreadsheetDocument)
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
