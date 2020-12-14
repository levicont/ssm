package com.lvg.ssm;

import com.lvg.ssm.entities.JournalWeldingEntity;
import com.lvg.ssm.entities.ShipmentEntity;
import com.lvg.ssm.entities.TestReport;
import com.lvg.ssm.services.Combiner;
import com.lvg.ssm.services.DataExtractor;
import com.lvg.ssm.services.Filler;
import com.lvg.ssm.utils.OpenOfficeUtils;

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
