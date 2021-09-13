package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.TenantPropertyKey;
import com.mycompany.provisioning.repository.TenantPropertyKeyRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.TenantPropertyKey}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TenantPropertyKeyResource {

    private final Logger log = LoggerFactory.getLogger(TenantPropertyKeyResource.class);

    private static final String ENTITY_NAME = "tenantPropertyKey";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantPropertyKeyRepository tenantPropertyKeyRepository;

    public TenantPropertyKeyResource(TenantPropertyKeyRepository tenantPropertyKeyRepository) {
        this.tenantPropertyKeyRepository = tenantPropertyKeyRepository;
    }

    /**
     * {@code POST  /tenant-property-keys} : Create a new tenantPropertyKey.
     *
     * @param tenantPropertyKey the tenantPropertyKey to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantPropertyKey, or with status {@code 400 (Bad Request)} if the tenantPropertyKey has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tenant-property-keys")
    public ResponseEntity<TenantPropertyKey> createTenantPropertyKey(@Valid @RequestBody TenantPropertyKey tenantPropertyKey)
        throws URISyntaxException {
        log.debug("REST request to save TenantPropertyKey : {}", tenantPropertyKey);
        if (tenantPropertyKey.getId() != null) {
            throw new BadRequestAlertException("A new tenantPropertyKey cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TenantPropertyKey result = tenantPropertyKeyRepository.save(tenantPropertyKey);
        return ResponseEntity
            .created(new URI("/api/tenant-property-keys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tenant-property-keys/:id} : Updates an existing tenantPropertyKey.
     *
     * @param id the id of the tenantPropertyKey to save.
     * @param tenantPropertyKey the tenantPropertyKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantPropertyKey,
     * or with status {@code 400 (Bad Request)} if the tenantPropertyKey is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantPropertyKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tenant-property-keys/{id}")
    public ResponseEntity<TenantPropertyKey> updateTenantPropertyKey(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantPropertyKey tenantPropertyKey
    ) throws URISyntaxException {
        log.debug("REST request to update TenantPropertyKey : {}, {}", id, tenantPropertyKey);
        if (tenantPropertyKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantPropertyKey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantPropertyKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TenantPropertyKey result = tenantPropertyKeyRepository.save(tenantPropertyKey);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantPropertyKey.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tenant-property-keys/:id} : Partial updates given fields of an existing tenantPropertyKey, field will ignore if it is null
     *
     * @param id the id of the tenantPropertyKey to save.
     * @param tenantPropertyKey the tenantPropertyKey to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantPropertyKey,
     * or with status {@code 400 (Bad Request)} if the tenantPropertyKey is not valid,
     * or with status {@code 404 (Not Found)} if the tenantPropertyKey is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantPropertyKey couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tenant-property-keys/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TenantPropertyKey> partialUpdateTenantPropertyKey(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantPropertyKey tenantPropertyKey
    ) throws URISyntaxException {
        log.debug("REST request to partial update TenantPropertyKey partially : {}, {}", id, tenantPropertyKey);
        if (tenantPropertyKey.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantPropertyKey.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantPropertyKeyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantPropertyKey> result = tenantPropertyKeyRepository
            .findById(tenantPropertyKey.getId())
            .map(
                existingTenantPropertyKey -> {
                    if (tenantPropertyKey.getName() != null) {
                        existingTenantPropertyKey.setName(tenantPropertyKey.getName());
                    }

                    return existingTenantPropertyKey;
                }
            )
            .map(tenantPropertyKeyRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantPropertyKey.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-property-keys} : get all the tenantPropertyKeys.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantPropertyKeys in body.
     */
    @GetMapping("/tenant-property-keys")
    public List<TenantPropertyKey> getAllTenantPropertyKeys() {
        log.debug("REST request to get all TenantPropertyKeys");
        return tenantPropertyKeyRepository.findAll();
    }

    /**
     * {@code GET  /tenant-property-keys/:id} : get the "id" tenantPropertyKey.
     *
     * @param id the id of the tenantPropertyKey to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantPropertyKey, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tenant-property-keys/{id}")
    public ResponseEntity<TenantPropertyKey> getTenantPropertyKey(@PathVariable Long id) {
        log.debug("REST request to get TenantPropertyKey : {}", id);
        Optional<TenantPropertyKey> tenantPropertyKey = tenantPropertyKeyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tenantPropertyKey);
    }

    /**
     * {@code DELETE  /tenant-property-keys/:id} : delete the "id" tenantPropertyKey.
     *
     * @param id the id of the tenantPropertyKey to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tenant-property-keys/{id}")
    public ResponseEntity<Void> deleteTenantPropertyKey(@PathVariable Long id) {
        log.debug("REST request to delete TenantPropertyKey : {}", id);
        tenantPropertyKeyRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
