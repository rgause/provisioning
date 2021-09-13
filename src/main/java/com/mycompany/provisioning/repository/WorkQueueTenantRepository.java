package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.WorkQueueTenant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkQueueTenant entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkQueueTenantRepository extends JpaRepository<WorkQueueTenant, Long> {}
