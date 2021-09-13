package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkQueueTenantUserPreReqTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkQueueTenantUserPreReq.class);
        WorkQueueTenantUserPreReq workQueueTenantUserPreReq1 = new WorkQueueTenantUserPreReq();
        workQueueTenantUserPreReq1.setId(1L);
        WorkQueueTenantUserPreReq workQueueTenantUserPreReq2 = new WorkQueueTenantUserPreReq();
        workQueueTenantUserPreReq2.setId(workQueueTenantUserPreReq1.getId());
        assertThat(workQueueTenantUserPreReq1).isEqualTo(workQueueTenantUserPreReq2);
        workQueueTenantUserPreReq2.setId(2L);
        assertThat(workQueueTenantUserPreReq1).isNotEqualTo(workQueueTenantUserPreReq2);
        workQueueTenantUserPreReq1.setId(null);
        assertThat(workQueueTenantUserPreReq1).isNotEqualTo(workQueueTenantUserPreReq2);
    }
}
