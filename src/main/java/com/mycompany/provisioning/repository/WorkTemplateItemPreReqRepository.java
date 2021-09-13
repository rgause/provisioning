package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.WorkTemplateItemPreReq;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkTemplateItemPreReq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkTemplateItemPreReqRepository extends JpaRepository<WorkTemplateItemPreReq, Long> {}
