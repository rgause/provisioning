package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkQueueTenantPreReqTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkQueueTenantPreReq.class);
        WorkQueueTenantPreReq workQueueTenantPreReq1 = new WorkQueueTenantPreReq();
        workQueueTenantPreReq1.setId(1L);
        WorkQueueTenantPreReq workQueueTenantPreReq2 = new WorkQueueTenantPreReq();
        workQueueTenantPreReq2.setId(workQueueTenantPreReq1.getId());
        assertThat(workQueueTenantPreReq1).isEqualTo(workQueueTenantPreReq2);
        workQueueTenantPreReq2.setId(2L);
        assertThat(workQueueTenantPreReq1).isNotEqualTo(workQueueTenantPreReq2);
        workQueueTenantPreReq1.setId(null);
        assertThat(workQueueTenantPreReq1).isNotEqualTo(workQueueTenantPreReq2);
    }
}
