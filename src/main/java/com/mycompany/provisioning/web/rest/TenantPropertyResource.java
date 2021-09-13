package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.TenantProperty;
import com.mycompany.provisioning.repository.TenantPropertyRepository;
import com.mycompany.provisioning.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.provisioning.domain.TenantProperty}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TenantPropertyResource {

    private final Logger log = LoggerFactory.getLogger(TenantPropertyResource.class);

    private static final String ENTITY_NAME = "tenantProperty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantPropertyRepository tenantPropertyRepository;

    public TenantPropertyResource(TenantPropertyRepository tenantPropertyRepository) {
        this.tenantPropertyRepository = tenantPropertyRepository;
    }

    /**
     * {@code POST  /tenant-properties} : Create a new tenantProperty.
     *
     * @param tenantProperty the tenantProperty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantProperty, or with status {@code 400 (Bad Request)} if the tenantProperty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tenant-properties")
    public ResponseEntity<TenantProperty> createTenantProperty(@Valid @RequestBody TenantProperty tenantProperty)
        throws URISyntaxException {
        log.debug("REST request to save TenantProperty : {}", tenantProperty);
        if (tenantProperty.getId() != null) {
            throw new BadRequestAlertException("A new tenantProperty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TenantProperty result = tenantPropertyRepository.save(tenantProperty);
        return ResponseEntity
            .created(new URI("/api/tenant-properties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tenant-properties/:id} : Updates an existing tenantProperty.
     *
     * @param id the id of the tenantProperty to save.
     * @param tenantProperty the tenantProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantProperty,
     * or with status {@code 400 (Bad Request)} if the tenantProperty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tenant-properties/{id}")
    public ResponseEntity<TenantProperty> updateTenantProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantProperty tenantProperty
    ) throws URISyntaxException {
        log.debug("REST request to update TenantProperty : {}, {}", id, tenantProperty);
        if (tenantProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantProperty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TenantProperty result = tenantPropertyRepository.save(tenantProperty);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantProperty.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tenant-properties/:id} : Partial updates given fields of an existing tenantProperty, field will ignore if it is null
     *
     * @param id the id of the tenantProperty to save.
     * @param tenantProperty the tenantProperty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantProperty,
     * or with status {@code 400 (Bad Request)} if the tenantProperty is not valid,
     * or with status {@code 404 (Not Found)} if the tenantProperty is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantProperty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tenant-properties/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TenantProperty> partialUpdateTenantProperty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantProperty tenantProperty
    ) throws URISyntaxException {
        log.debug("REST request to partial update TenantProperty partially : {}, {}", id, tenantProperty);
        if (tenantProperty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantProperty.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantPropertyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantProperty> result = tenantPropertyRepository
            .findById(tenantProperty.getId())
            .map(
                existingTenantProperty -> {
                    if (tenantProperty.getValue() != null) {
                        existingTenantProperty.setValue(tenantProperty.getValue());
                    }

                    return existingTenantProperty;
                }
            )
            .map(tenantPropertyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantProperty.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-properties} : get all the tenantProperties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantProperties in body.
     */
    @GetMapping("/tenant-properties")
    public List<TenantProperty> getAllTenantProperties() {
        log.debug("REST request to get all TenantProperties");
        return tenantPropertyRepository.findAll();
    }

    /**
     * {@code GET  /tenant-properties/:id} : get the "id" tenantProperty.
     *
     * @param id the id of the tenantProperty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantProperty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tenant-properties/{id}")
    public ResponseEntity<TenantProperty> getTenantProperty(@PathVariable Long id) {
        log.debug("REST request to get TenantProperty : {}", id);
        Optional<TenantProperty> tenantProperty = tenantPropertyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tenantProperty);
    }

    /**
     * {@code DELETE  /tenant-properties/:id} : delete the "id" tenantProperty.
     *
     * @param id the id of the tenantProperty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tenant-properties/{id}")
    public ResponseEntity<Void> deleteTenantProperty(@PathVariable Long id) {
        log.debug("REST request to delete TenantProperty : {}", id);
        tenantPropertyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
