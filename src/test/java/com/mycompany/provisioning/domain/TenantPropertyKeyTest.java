package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantPropertyKeyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantPropertyKey.class);
        TenantPropertyKey tenantPropertyKey1 = new TenantPropertyKey();
        tenantPropertyKey1.setId(1L);
        TenantPropertyKey tenantPropertyKey2 = new TenantPropertyKey();
        tenantPropertyKey2.setId(tenantPropertyKey1.getId());
        assertThat(tenantPropertyKey1).isEqualTo(tenantPropertyKey2);
        tenantPropertyKey2.setId(2L);
        assertThat(tenantPropertyKey1).isNotEqualTo(tenantPropertyKey2);
        tenantPropertyKey1.setId(null);
        assertThat(tenantPropertyKey1).isNotEqualTo(tenantPropertyKey2);
    }
}
