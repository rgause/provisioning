package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkTemplateTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkTemplate.class);
        WorkTemplate workTemplate1 = new WorkTemplate();
        workTemplate1.setId(1L);
        WorkTemplate workTemplate2 = new WorkTemplate();
        workTemplate2.setId(workTemplate1.getId());
        assertThat(workTemplate1).isEqualTo(workTemplate2);
        workTemplate2.setId(2L);
        assertThat(workTemplate1).isNotEqualTo(workTemplate2);
        workTemplate1.setId(null);
        assertThat(workTemplate1).isNotEqualTo(workTemplate2);
    }
}
