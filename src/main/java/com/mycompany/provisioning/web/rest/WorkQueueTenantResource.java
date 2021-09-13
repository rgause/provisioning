package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkQueueTenant;
import com.mycompany.provisioning.repository.WorkQueueTenantRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkQueueTenant}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkQueueTenantResource {

    private final Logger log = LoggerFactory.getLogger(WorkQueueTenantResource.class);

    private static final String ENTITY_NAME = "workQueueTenant";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkQueueTenantRepository workQueueTenantRepository;

    public WorkQueueTenantResource(WorkQueueTenantRepository workQueueTenantRepository) {
        this.workQueueTenantRepository = workQueueTenantRepository;
    }

    /**
     * {@code POST  /work-queue-tenants} : Create a new workQueueTenant.
     *
     * @param workQueueTenant the workQueueTenant to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workQueueTenant, or with status {@code 400 (Bad Request)} if the workQueueTenant has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-queue-tenants")
    public ResponseEntity<WorkQueueTenant> createWorkQueueTenant(@Valid @RequestBody WorkQueueTenant workQueueTenant)
        throws URISyntaxException {
        log.debug("REST request to save WorkQueueTenant : {}", workQueueTenant);
        if (workQueueTenant.getId() != null) {
            throw new BadRequestAlertException("A new workQueueTenant cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkQueueTenant result = workQueueTenantRepository.save(workQueueTenant);
        return ResponseEntity
            .created(new URI("/api/work-queue-tenants/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-queue-tenants/:id} : Updates an existing workQueueTenant.
     *
     * @param id the id of the workQueueTenant to save.
     * @param workQueueTenant the workQueueTenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenant,
     * or with status {@code 400 (Bad Request)} if the workQueueTenant is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-queue-tenants/{id}")
    public ResponseEntity<WorkQueueTenant> updateWorkQueueTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkQueueTenant workQueueTenant
    ) throws URISyntaxException {
        log.debug("REST request to update WorkQueueTenant : {}, {}", id, workQueueTenant);
        if (workQueueTenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkQueueTenant result = workQueueTenantRepository.save(workQueueTenant);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenant.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-queue-tenants/:id} : Partial updates given fields of an existing workQueueTenant, field will ignore if it is null
     *
     * @param id the id of the workQueueTenant to save.
     * @param workQueueTenant the workQueueTenant to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenant,
     * or with status {@code 400 (Bad Request)} if the workQueueTenant is not valid,
     * or with status {@code 404 (Not Found)} if the workQueueTenant is not found,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenant couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-queue-tenants/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkQueueTenant> partialUpdateWorkQueueTenant(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkQueueTenant workQueueTenant
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkQueueTenant partially : {}, {}", id, workQueueTenant);
        if (workQueueTenant.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenant.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkQueueTenant> result = workQueueTenantRepository
            .findById(workQueueTenant.getId())
            .map(
                existingWorkQueueTenant -> {
                    if (workQueueTenant.getTask() != null) {
                        existingWorkQueueTenant.setTask(workQueueTenant.getTask());
                    }
                    if (workQueueTenant.getRequiredToComplete() != null) {
                        existingWorkQueueTenant.setRequiredToComplete(workQueueTenant.getRequiredToComplete());
                    }
                    if (workQueueTenant.getDateCreated() != null) {
                        existingWorkQueueTenant.setDateCreated(workQueueTenant.getDateCreated());
                    }
                    if (workQueueTenant.getDateCancelled() != null) {
                        existingWorkQueueTenant.setDateCancelled(workQueueTenant.getDateCancelled());
                    }
                    if (workQueueTenant.getDateCompleted() != null) {
                        existingWorkQueueTenant.setDateCompleted(workQueueTenant.getDateCompleted());
                    }
                    if (workQueueTenant.getSequenceOrder() != null) {
                        existingWorkQueueTenant.setSequenceOrder(workQueueTenant.getSequenceOrder());
                    }

                    return existingWorkQueueTenant;
                }
            )
            .map(workQueueTenantRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenant.getId().toString())
        );
    }

    /**
     * {@code GET  /work-queue-tenants} : get all the workQueueTenants.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workQueueTenants in body.
     */
    @GetMapping("/work-queue-tenants")
    public List<WorkQueueTenant> getAllWorkQueueTenants() {
        log.debug("REST request to get all WorkQueueTenants");
        return workQueueTenantRepository.findAll();
    }

    /**
     * {@code GET  /work-queue-tenants/:id} : get the "id" workQueueTenant.
     *
     * @param id the id of the workQueueTenant to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workQueueTenant, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-queue-tenants/{id}")
    public ResponseEntity<WorkQueueTenant> getWorkQueueTenant(@PathVariable Long id) {
        log.debug("REST request to get WorkQueueTenant : {}", id);
        Optional<WorkQueueTenant> workQueueTenant = workQueueTenantRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workQueueTenant);
    }

    /**
     * {@code DELETE  /work-queue-tenants/:id} : delete the "id" workQueueTenant.
     *
     * @param id the id of the workQueueTenant to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-queue-tenants/{id}")
    public ResponseEntity<Void> deleteWorkQueueTenant(@PathVariable Long id) {
        log.debug("REST request to delete WorkQueueTenant : {}", id);
        workQueueTenantRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
