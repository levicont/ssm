package com.lvg.ssm;

import com.lvg.ssm.entities.JournalWeldingEntity;
import com.lvg.ssm.entities.ShipmentEntity;
import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.entities.TestReportType;
import com.lvg.ssm.services.Combiner;
import com.lvg.ssm.services.DataExtractor;
import com.lvg.ssm.services.Filler;
import com.lvg.ssm.utils.ApplicationProperties;
import com.lvg.ssm.utils.OpenOfficeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class Generator
{
    private static boolean isVT = false;
    private static boolean isUT = false;

    public static void main( String[] args )
    {
        Arrays.stream(args).forEach(arg ->{
            if (arg.equalsIgnoreCase(TestReportType.VT.toString()))
                isVT = true;
            if (arg.equalsIgnoreCase(TestReportType.UT.toString()))
                isUT = true;
        });


        try{
            List<ShipmentEntity> shipmentEntities = DataExtractor.getShipmentEntities();
            List<JournalWeldingEntity> journalWeldingEntities = DataExtractor.getJournalWeldingEntities();

            if (isVT){
                Set<TestReport> testReportsVT = Combiner.combineJournalWelding(shipmentEntities,
                        journalWeldingEntities, TestReportType.VT);
                testReportsVT.forEach(tr ->{
                    Filler filler = new Filler(tr);
                    filler.fillUpReport();
                    filler.save();
                    filler.exportPDF();
                    filler.close();
                });
            }
            if(isUT){
                List<ShipmentEntity> shipmentEntitiesUT = DataExtractor.getFilteredShipmentEntities(
                        Double.valueOf(ApplicationProperties.getProperty("MinWeightOFMarkKg")));
                Set<TestReport> testReportsUT = Combiner.combineJournalWelding(shipmentEntitiesUT,journalWeldingEntities,TestReportType.UT);

                testReportsUT.forEach(tr ->{
                    Filler filler = new Filler(tr);
                    filler.fillUpReport();
                    filler.save();
                    filler.exportPDF();
                    filler.close();
                });


            }

        }catch (Exception ex){
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        finally {
            OpenOfficeUtils.closeContext();
        }




    }
}
