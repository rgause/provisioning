package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TenantPropertyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TenantProperty.class);
        TenantProperty tenantProperty1 = new TenantProperty();
        tenantProperty1.setId(1L);
        TenantProperty tenantProperty2 = new TenantProperty();
        tenantProperty2.setId(tenantProperty1.getId());
        assertThat(tenantProperty1).isEqualTo(tenantProperty2);
        tenantProperty2.setId(2L);
        assertThat(tenantProperty1).isNotEqualTo(tenantProperty2);
        tenantProperty1.setId(null);
        assertThat(tenantProperty1).isNotEqualTo(tenantProperty2);
    }
}
