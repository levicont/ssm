package com.lvg.ssm.entities;

import com.lvg.ssm.utils.ApplicationProperties;

/**
 * Created by Victor Levchenko LVG Corp. on 15.12.2020.
 */
public class TestReportUT extends TestReport {
    private static Long lastIndex = 1L;
    static {
        lastIndex = Long.valueOf(ApplicationProperties.getProperty("StartUTProtocolsNumber"));
    }

    private Long index;

    public TestReportUT(){
        index = lastIndex++;
    }

    @Override
    public Long getIndex() {
        return this.index;
    }

    @Override
    public TestReportType getType() {
        return TestReportType.UT;
    }
}
