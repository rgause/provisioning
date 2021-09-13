package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkQueueTenantUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkQueueTenantUser.class);
        WorkQueueTenantUser workQueueTenantUser1 = new WorkQueueTenantUser();
        workQueueTenantUser1.setId(1L);
        WorkQueueTenantUser workQueueTenantUser2 = new WorkQueueTenantUser();
        workQueueTenantUser2.setId(workQueueTenantUser1.getId());
        assertThat(workQueueTenantUser1).isEqualTo(workQueueTenantUser2);
        workQueueTenantUser2.setId(2L);
        assertThat(workQueueTenantUser1).isNotEqualTo(workQueueTenantUser2);
        workQueueTenantUser1.setId(null);
        assertThat(workQueueTenantUser1).isNotEqualTo(workQueueTenantUser2);
    }
}
