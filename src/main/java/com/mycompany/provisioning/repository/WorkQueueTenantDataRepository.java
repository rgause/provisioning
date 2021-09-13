package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.WorkQueueTenantData;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkQueueTenantData entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkQueueTenantDataRepository extends JpaRepository<WorkQueueTenantData, Long> {}
