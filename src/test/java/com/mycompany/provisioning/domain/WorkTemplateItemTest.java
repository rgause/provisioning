package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkTemplateItemTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkTemplateItem.class);
        WorkTemplateItem workTemplateItem1 = new WorkTemplateItem();
        workTemplateItem1.setId(1L);
        WorkTemplateItem workTemplateItem2 = new WorkTemplateItem();
        workTemplateItem2.setId(workTemplateItem1.getId());
        assertThat(workTemplateItem1).isEqualTo(workTemplateItem2);
        workTemplateItem2.setId(2L);
        assertThat(workTemplateItem1).isNotEqualTo(workTemplateItem2);
        workTemplateItem1.setId(null);
        assertThat(workTemplateItem1).isNotEqualTo(workTemplateItem2);
    }
}
