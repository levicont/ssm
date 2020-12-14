package com.lvg.ssm;

import com.lvg.ssm.entities.JournalWeldingEntity;
import com.lvg.ssm.entities.ShipmentEntity;
import com.lvg.ssm.services.DataExtractor;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Victor Levchenko LVG Corp. on 27.11.2020.
 */
public class DataExtractorTest {


    @Test
    public void getShipmentEntitiesTest(){
        List<ShipmentEntity> result = DataExtractor.getShipmentEntities();
        assertNotNull(result);
        assertTrue(result.size()>0);
        System.out.println("Shipment entities size: "+ result.size());
        ShipmentEntity firstEntity = result.get(0);
        assertEquals(firstEntity.getDate(), LocalDate.of(2020,9,2));
        assertEquals(firstEntity.getTechnicalDrawings(),"СМК 20029-КМД");
        assertEquals(firstEntity.getObjectName(),"Бахмутовская молочно-товарная ферма");
        assertEquals(firstEntity.getShippingShop(),"Харьков + Чугуев");
        assertEquals(firstEntity.getDetailEntities().size(),7);
        assertEquals(firstEntity.getDetailEntities().get(0).getMark(), "Пр1-1");
        assertEquals(firstEntity.getDetailEntities().get(0).getMarkName(), "Прогон");
        assertEquals(firstEntity.getDetailEntities().get(0).getCount(), Integer.valueOf(79));
        assertEquals(firstEntity.getDetailEntities().get(0).getWeightOfMarkKg(), Double.valueOf(73.8));

        ShipmentEntity lastEntity = result.get(result.size()-1);
        assertEquals(lastEntity.getDate(), LocalDate.of(2020,11,20));
        assertEquals(lastEntity.getTechnicalDrawings(),"СМК 20024 КМД");
        assertEquals(lastEntity.getObjectName(),"Офисно-складской комплекс");
        assertEquals(lastEntity.getShippingShop(),"Харьков");
        assertEquals(lastEntity.getDetailEntities().size(),18);
        assertEquals(lastEntity.getDetailEntities().get(0).getMark(), "Б1-1");
        assertEquals(lastEntity.getDetailEntities().get(0).getMarkName(), "Балка");
        assertEquals(lastEntity.getDetailEntities().get(0).getCount(), Integer.valueOf(2));
        assertEquals(lastEntity.getDetailEntities().get(0).getWeightOfMarkKg(), Double.valueOf(803.1));
    }

    @Test
    public void getJournalWeldingEntitiesTest(){
        List<JournalWeldingEntity> journalWeldingEntities = DataExtractor.getJournalWeldingEntities();
        assertNotNull(journalWeldingEntities);
        assertEquals(journalWeldingEntities.size(),34);
        JournalWeldingEntity tensEntity = journalWeldingEntities.get(9);
        assertEquals("10/1", tensEntity.getNumber());

    }
}
