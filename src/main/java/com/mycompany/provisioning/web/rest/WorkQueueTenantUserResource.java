package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkQueueTenantUser;
import com.mycompany.provisioning.repository.WorkQueueTenantUserRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkQueueTenantUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkQueueTenantUserResource {

    private final Logger log = LoggerFactory.getLogger(WorkQueueTenantUserResource.class);

    private static final String ENTITY_NAME = "workQueueTenantUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkQueueTenantUserRepository workQueueTenantUserRepository;

    public WorkQueueTenantUserResource(WorkQueueTenantUserRepository workQueueTenantUserRepository) {
        this.workQueueTenantUserRepository = workQueueTenantUserRepository;
    }

    /**
     * {@code POST  /work-queue-tenant-users} : Create a new workQueueTenantUser.
     *
     * @param workQueueTenantUser the workQueueTenantUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workQueueTenantUser, or with status {@code 400 (Bad Request)} if the workQueueTenantUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-queue-tenant-users")
    public ResponseEntity<WorkQueueTenantUser> createWorkQueueTenantUser(@Valid @RequestBody WorkQueueTenantUser workQueueTenantUser)
        throws URISyntaxException {
        log.debug("REST request to save WorkQueueTenantUser : {}", workQueueTenantUser);
        if (workQueueTenantUser.getId() != null) {
            throw new BadRequestAlertException("A new workQueueTenantUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkQueueTenantUser result = workQueueTenantUserRepository.save(workQueueTenantUser);
        return ResponseEntity
            .created(new URI("/api/work-queue-tenant-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-queue-tenant-users/:id} : Updates an existing workQueueTenantUser.
     *
     * @param id the id of the workQueueTenantUser to save.
     * @param workQueueTenantUser the workQueueTenantUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantUser,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-queue-tenant-users/{id}")
    public ResponseEntity<WorkQueueTenantUser> updateWorkQueueTenantUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkQueueTenantUser workQueueTenantUser
    ) throws URISyntaxException {
        log.debug("REST request to update WorkQueueTenantUser : {}, {}", id, workQueueTenantUser);
        if (workQueueTenantUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkQueueTenantUser result = workQueueTenantUserRepository.save(workQueueTenantUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-queue-tenant-users/:id} : Partial updates given fields of an existing workQueueTenantUser, field will ignore if it is null
     *
     * @param id the id of the workQueueTenantUser to save.
     * @param workQueueTenantUser the workQueueTenantUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantUser,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantUser is not valid,
     * or with status {@code 404 (Not Found)} if the workQueueTenantUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-queue-tenant-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkQueueTenantUser> partialUpdateWorkQueueTenantUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkQueueTenantUser workQueueTenantUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkQueueTenantUser partially : {}, {}", id, workQueueTenantUser);
        if (workQueueTenantUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkQueueTenantUser> result = workQueueTenantUserRepository
            .findById(workQueueTenantUser.getId())
            .map(
                existingWorkQueueTenantUser -> {
                    if (workQueueTenantUser.getTask() != null) {
                        existingWorkQueueTenantUser.setTask(workQueueTenantUser.getTask());
                    }
                    if (workQueueTenantUser.getRequiredToComplete() != null) {
                        existingWorkQueueTenantUser.setRequiredToComplete(workQueueTenantUser.getRequiredToComplete());
                    }
                    if (workQueueTenantUser.getDateCreated() != null) {
                        existingWorkQueueTenantUser.setDateCreated(workQueueTenantUser.getDateCreated());
                    }
                    if (workQueueTenantUser.getDateCancelled() != null) {
                        existingWorkQueueTenantUser.setDateCancelled(workQueueTenantUser.getDateCancelled());
                    }
                    if (workQueueTenantUser.getDateCompleted() != null) {
                        existingWorkQueueTenantUser.setDateCompleted(workQueueTenantUser.getDateCompleted());
                    }
                    if (workQueueTenantUser.getSequenceOrder() != null) {
                        existingWorkQueueTenantUser.setSequenceOrder(workQueueTenantUser.getSequenceOrder());
                    }

                    return existingWorkQueueTenantUser;
                }
            )
            .map(workQueueTenantUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantUser.getId().toString())
        );
    }

    /**
     * {@code GET  /work-queue-tenant-users} : get all the workQueueTenantUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workQueueTenantUsers in body.
     */
    @GetMapping("/work-queue-tenant-users")
    public List<WorkQueueTenantUser> getAllWorkQueueTenantUsers() {
        log.debug("REST request to get all WorkQueueTenantUsers");
        return workQueueTenantUserRepository.findAll();
    }

    /**
     * {@code GET  /work-queue-tenant-users/:id} : get the "id" workQueueTenantUser.
     *
     * @param id the id of the workQueueTenantUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workQueueTenantUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-queue-tenant-users/{id}")
    public ResponseEntity<WorkQueueTenantUser> getWorkQueueTenantUser(@PathVariable Long id) {
        log.debug("REST request to get WorkQueueTenantUser : {}", id);
        Optional<WorkQueueTenantUser> workQueueTenantUser = workQueueTenantUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workQueueTenantUser);
    }

    /**
     * {@code DELETE  /work-queue-tenant-users/:id} : delete the "id" workQueueTenantUser.
     *
     * @param id the id of the workQueueTenantUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-queue-tenant-users/{id}")
    public ResponseEntity<Void> deleteWorkQueueTenantUser(@PathVariable Long id) {
        log.debug("REST request to delete WorkQueueTenantUser : {}", id);
        workQueueTenantUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
