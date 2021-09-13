package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkQueueTenantDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkQueueTenantData.class);
        WorkQueueTenantData workQueueTenantData1 = new WorkQueueTenantData();
        workQueueTenantData1.setId(1L);
        WorkQueueTenantData workQueueTenantData2 = new WorkQueueTenantData();
        workQueueTenantData2.setId(workQueueTenantData1.getId());
        assertThat(workQueueTenantData1).isEqualTo(workQueueTenantData2);
        workQueueTenantData2.setId(2L);
        assertThat(workQueueTenantData1).isNotEqualTo(workQueueTenantData2);
        workQueueTenantData1.setId(null);
        assertThat(workQueueTenantData1).isNotEqualTo(workQueueTenantData2);
    }
}
