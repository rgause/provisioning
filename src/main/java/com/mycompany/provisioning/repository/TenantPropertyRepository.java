package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.TenantProperty;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TenantProperty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantPropertyRepository extends JpaRepository<TenantProperty, Long> {}
