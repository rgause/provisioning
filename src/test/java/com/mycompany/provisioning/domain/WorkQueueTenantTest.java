package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkQueueTenantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkQueueTenant.class);
        WorkQueueTenant workQueueTenant1 = new WorkQueueTenant();
        workQueueTenant1.setId(1L);
        WorkQueueTenant workQueueTenant2 = new WorkQueueTenant();
        workQueueTenant2.setId(workQueueTenant1.getId());
        assertThat(workQueueTenant1).isEqualTo(workQueueTenant2);
        workQueueTenant2.setId(2L);
        assertThat(workQueueTenant1).isNotEqualTo(workQueueTenant2);
        workQueueTenant1.setId(null);
        assertThat(workQueueTenant1).isNotEqualTo(workQueueTenant2);
    }
}
