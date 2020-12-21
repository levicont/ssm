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

import java.util.List;
import java.util.Set;


public class Generator
{
    public static void main( String[] args )
    {
        List<ShipmentEntity> shipmentEntities = DataExtractor.getShipmentEntities();
        List<JournalWeldingEntity> journalWeldingEntities = DataExtractor.getJournalWeldingEntities();
        Set<TestReport> testReportsVT = Combiner.combineJournalWelding(shipmentEntities,journalWeldingEntities, TestReportType.VT);
        List<ShipmentEntity> shipmentEntitiesUT = DataExtractor.getFilteredShipmentEntities(
                Double.valueOf(ApplicationProperties.getProperty("MinWeightOFMarkKg")));
        Set<TestReport> testReportsUT = Combiner.combineJournalWelding(shipmentEntitiesUT,journalWeldingEntities,TestReportType.UT);

        testReportsVT.forEach(tr ->{
            Filler filler = new Filler(tr);
            filler.fillUpReport();
            filler.print();
            filler.save();
            filler.close();
        });
        testReportsUT.forEach(tr ->{
            Filler filler = new Filler(tr);
            filler.fillUpReport();
            filler.print();
            filler.save();
            filler.close();
        });


        OpenOfficeUtils.closeContext();


    }
}
