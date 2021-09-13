package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.WorkQueueTenantUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the WorkQueueTenantUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkQueueTenantUserRepository extends JpaRepository<WorkQueueTenantUser, Long> {}
