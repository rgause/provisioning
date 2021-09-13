package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class URLTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(URLType.class);
        URLType uRLType1 = new URLType();
        uRLType1.setId(1L);
        URLType uRLType2 = new URLType();
        uRLType2.setId(uRLType1.getId());
        assertThat(uRLType1).isEqualTo(uRLType2);
        uRLType2.setId(2L);
        assertThat(uRLType1).isNotEqualTo(uRLType2);
        uRLType1.setId(null);
        assertThat(uRLType1).isNotEqualTo(uRLType2);
    }
}
