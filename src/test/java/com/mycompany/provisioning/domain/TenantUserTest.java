package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantUser.class);
        TenantUser tenantUser1 = new TenantUser();
        tenantUser1.setId(1L);
        TenantUser tenantUser2 = new TenantUser();
        tenantUser2.setId(tenantUser1.getId());
        assertThat(tenantUser1).isEqualTo(tenantUser2);
        tenantUser2.setId(2L);
        assertThat(tenantUser1).isNotEqualTo(tenantUser2);
        tenantUser1.setId(null);
        assertThat(tenantUser1).isNotEqualTo(tenantUser2);
    }
}
