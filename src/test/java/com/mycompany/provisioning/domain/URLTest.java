package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class URLTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(URL.class);
        URL uRL1 = new URL();
        uRL1.setId(1L);
        URL uRL2 = new URL();
        uRL2.setId(uRL1.getId());
        assertThat(uRL1).isEqualTo(uRL2);
        uRL2.setId(2L);
        assertThat(uRL1).isNotEqualTo(uRL2);
        uRL1.setId(null);
        assertThat(uRL1).isNotEqualTo(uRL2);
    }
}
