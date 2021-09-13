package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.WorkQueueTenantPreReq;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkQueueTenantPreReq entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkQueueTenantPreReqRepository extends JpaRepository<WorkQueueTenantPreReq, Long> {}
