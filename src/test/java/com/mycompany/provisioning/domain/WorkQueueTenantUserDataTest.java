package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkQueueTenantUserDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkQueueTenantUserData.class);
        WorkQueueTenantUserData workQueueTenantUserData1 = new WorkQueueTenantUserData();
        workQueueTenantUserData1.setId(1L);
        WorkQueueTenantUserData workQueueTenantUserData2 = new WorkQueueTenantUserData();
        workQueueTenantUserData2.setId(workQueueTenantUserData1.getId());
        assertThat(workQueueTenantUserData1).isEqualTo(workQueueTenantUserData2);
        workQueueTenantUserData2.setId(2L);
        assertThat(workQueueTenantUserData1).isNotEqualTo(workQueueTenantUserData2);
        workQueueTenantUserData1.setId(null);
        assertThat(workQueueTenantUserData1).isNotEqualTo(workQueueTenantUserData2);
    }
}
