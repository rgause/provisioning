package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LanUserTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LanUser.class);
        LanUser lanUser1 = new LanUser();
        lanUser1.setId(1L);
        LanUser lanUser2 = new LanUser();
        lanUser2.setId(lanUser1.getId());
        assertThat(lanUser1).isEqualTo(lanUser2);
        lanUser2.setId(2L);
        assertThat(lanUser1).isNotEqualTo(lanUser2);
        lanUser1.setId(null);
        assertThat(lanUser1).isNotEqualTo(lanUser2);
    }
}
