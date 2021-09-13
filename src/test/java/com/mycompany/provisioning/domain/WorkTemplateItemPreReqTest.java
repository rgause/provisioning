package com.mycompany.provisioning.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.provisioning.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WorkTemplateItemPreReqTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WorkTemplateItemPreReq.class);
        WorkTemplateItemPreReq workTemplateItemPreReq1 = new WorkTemplateItemPreReq();
        workTemplateItemPreReq1.setId(1L);
        WorkTemplateItemPreReq workTemplateItemPreReq2 = new WorkTemplateItemPreReq();
        workTemplateItemPreReq2.setId(workTemplateItemPreReq1.getId());
        assertThat(workTemplateItemPreReq1).isEqualTo(workTemplateItemPreReq2);
        workTemplateItemPreReq2.setId(2L);
        assertThat(workTemplateItemPreReq1).isNotEqualTo(workTemplateItemPreReq2);
        workTemplateItemPreReq1.setId(null);
        assertThat(workTemplateItemPreReq1).isNotEqualTo(workTemplateItemPreReq2);
    }
}
