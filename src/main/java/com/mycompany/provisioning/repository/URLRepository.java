package com.mycompany.provisioning.repository;

import com.mycompany.provisioning.domain.URL;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the URL entity.
 */
@SuppressWarnings("unused")
@Repository
public interface URLRepository extends JpaRepository<URL, Long> {}
