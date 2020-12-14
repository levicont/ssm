package com.lvg.ssm;

import java.util.List;
import java.util.Set;


public class Generator
{
    public static void main( String[] args )
    {
        Set<ShipmentEntity> shipmentEntities = DataExtractor.getSortedShipmentEntities(
                DataExtractor.getShipmentEntities());
        List<JournalWeldingEntity> journalWeldingEntities = DataExtractor.getJournalWeldingEntities();
        Set<TestReport> testReports = Combiner.combineJournalWelding(shipmentEntities,journalWeldingEntities);

        testReports.forEach(tr ->{
            Filler filler = new Filler(tr);
            filler.fillUpVTReport();
            filler.print();
            filler.save();
            filler.close();
        });
        OpenOfficeUtils.closeContext();

    }
}
