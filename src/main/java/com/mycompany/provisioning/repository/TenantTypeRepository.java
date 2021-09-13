package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.TenantType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TenantType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantTypeRepository extends JpaRepository<TenantType, Long> {}
