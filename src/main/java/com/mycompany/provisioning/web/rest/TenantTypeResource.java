package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.TenantType;
import com.mycompany.provisioning.repository.TenantTypeRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.TenantType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TenantTypeResource {

    private final Logger log = LoggerFactory.getLogger(TenantTypeResource.class);

    private static final String ENTITY_NAME = "tenantType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantTypeRepository tenantTypeRepository;

    public TenantTypeResource(TenantTypeRepository tenantTypeRepository) {
        this.tenantTypeRepository = tenantTypeRepository;
    }

    /**
     * {@code POST  /tenant-types} : Create a new tenantType.
     *
     * @param tenantType the tenantType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantType, or with status {@code 400 (Bad Request)} if the tenantType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tenant-types")
    public ResponseEntity<TenantType> createTenantType(@Valid @RequestBody TenantType tenantType) throws URISyntaxException {
        log.debug("REST request to save TenantType : {}", tenantType);
        if (tenantType.getId() != null) {
            throw new BadRequestAlertException("A new tenantType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TenantType result = tenantTypeRepository.save(tenantType);
        return ResponseEntity
            .created(new URI("/api/tenant-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tenant-types/:id} : Updates an existing tenantType.
     *
     * @param id the id of the tenantType to save.
     * @param tenantType the tenantType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantType,
     * or with status {@code 400 (Bad Request)} if the tenantType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tenant-types/{id}")
    public ResponseEntity<TenantType> updateTenantType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantType tenantType
    ) throws URISyntaxException {
        log.debug("REST request to update TenantType : {}, {}", id, tenantType);
        if (tenantType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TenantType result = tenantTypeRepository.save(tenantType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tenant-types/:id} : Partial updates given fields of an existing tenantType, field will ignore if it is null
     *
     * @param id the id of the tenantType to save.
     * @param tenantType the tenantType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantType,
     * or with status {@code 400 (Bad Request)} if the tenantType is not valid,
     * or with status {@code 404 (Not Found)} if the tenantType is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tenant-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TenantType> partialUpdateTenantType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantType tenantType
    ) throws URISyntaxException {
        log.debug("REST request to partial update TenantType partially : {}, {}", id, tenantType);
        if (tenantType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantType> result = tenantTypeRepository
            .findById(tenantType.getId())
            .map(
                existingTenantType -> {
                    if (tenantType.getName() != null) {
                        existingTenantType.setName(tenantType.getName());
                    }

                    return existingTenantType;
                }
            )
            .map(tenantTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantType.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-types} : get all the tenantTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantTypes in body.
     */
    @GetMapping("/tenant-types")
    public List<TenantType> getAllTenantTypes() {
        log.debug("REST request to get all TenantTypes");
        return tenantTypeRepository.findAll();
    }

    /**
     * {@code GET  /tenant-types/:id} : get the "id" tenantType.
     *
     * @param id the id of the tenantType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tenant-types/{id}")
    public ResponseEntity<TenantType> getTenantType(@PathVariable Long id) {
        log.debug("REST request to get TenantType : {}", id);
        Optional<TenantType> tenantType = tenantTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tenantType);
    }

    /**
     * {@code DELETE  /tenant-types/:id} : delete the "id" tenantType.
     *
     * @param id the id of the tenantType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tenant-types/{id}")
    public ResponseEntity<Void> deleteTenantType(@PathVariable Long id) {
        log.debug("REST request to delete TenantType : {}", id);
        tenantTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
