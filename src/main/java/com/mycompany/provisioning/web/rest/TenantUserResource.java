package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.TenantUser;
import com.mycompany.provisioning.repository.TenantUserRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.TenantUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TenantUserResource {

    private final Logger log = LoggerFactory.getLogger(TenantUserResource.class);

    private static final String ENTITY_NAME = "tenantUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TenantUserRepository tenantUserRepository;

    public TenantUserResource(TenantUserRepository tenantUserRepository) {
        this.tenantUserRepository = tenantUserRepository;
    }

    /**
     * {@code POST  /tenant-users} : Create a new tenantUser.
     *
     * @param tenantUser the tenantUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tenantUser, or with status {@code 400 (Bad Request)} if the tenantUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tenant-users")
    public ResponseEntity<TenantUser> createTenantUser(@Valid @RequestBody TenantUser tenantUser) throws URISyntaxException {
        log.debug("REST request to save TenantUser : {}", tenantUser);
        if (tenantUser.getId() != null) {
            throw new BadRequestAlertException("A new tenantUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TenantUser result = tenantUserRepository.save(tenantUser);
        return ResponseEntity
            .created(new URI("/api/tenant-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tenant-users/:id} : Updates an existing tenantUser.
     *
     * @param id the id of the tenantUser to save.
     * @param tenantUser the tenantUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantUser,
     * or with status {@code 400 (Bad Request)} if the tenantUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tenantUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tenant-users/{id}")
    public ResponseEntity<TenantUser> updateTenantUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TenantUser tenantUser
    ) throws URISyntaxException {
        log.debug("REST request to update TenantUser : {}, {}", id, tenantUser);
        if (tenantUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TenantUser result = tenantUserRepository.save(tenantUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tenant-users/:id} : Partial updates given fields of an existing tenantUser, field will ignore if it is null
     *
     * @param id the id of the tenantUser to save.
     * @param tenantUser the tenantUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tenantUser,
     * or with status {@code 400 (Bad Request)} if the tenantUser is not valid,
     * or with status {@code 404 (Not Found)} if the tenantUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the tenantUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tenant-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TenantUser> partialUpdateTenantUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TenantUser tenantUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update TenantUser partially : {}, {}", id, tenantUser);
        if (tenantUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tenantUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tenantUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TenantUser> result = tenantUserRepository
            .findById(tenantUser.getId())
            .map(
                existingTenantUser -> {
                    if (tenantUser.getActive() != null) {
                        existingTenantUser.setActive(tenantUser.getActive());
                    }
                    if (tenantUser.getProvisioned() != null) {
                        existingTenantUser.setProvisioned(tenantUser.getProvisioned());
                    }

                    return existingTenantUser;
                }
            )
            .map(tenantUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, tenantUser.getId().toString())
        );
    }

    /**
     * {@code GET  /tenant-users} : get all the tenantUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tenantUsers in body.
     */
    @GetMapping("/tenant-users")
    public List<TenantUser> getAllTenantUsers() {
        log.debug("REST request to get all TenantUsers");
        return tenantUserRepository.findAll();
    }

    /**
     * {@code GET  /tenant-users/:id} : get the "id" tenantUser.
     *
     * @param id the id of the tenantUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tenantUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tenant-users/{id}")
    public ResponseEntity<TenantUser> getTenantUser(@PathVariable Long id) {
        log.debug("REST request to get TenantUser : {}", id);
        Optional<TenantUser> tenantUser = tenantUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(tenantUser);
    }

    /**
     * {@code DELETE  /tenant-users/:id} : delete the "id" tenantUser.
     *
     * @param id the id of the tenantUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tenant-users/{id}")
    public ResponseEntity<Void> deleteTenantUser(@PathVariable Long id) {
        log.debug("REST request to delete TenantUser : {}", id);
        tenantUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
