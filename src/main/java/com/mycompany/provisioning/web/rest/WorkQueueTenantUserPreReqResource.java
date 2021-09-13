package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkQueueTenantUserPreReq;
import com.mycompany.provisioning.repository.WorkQueueTenantUserPreReqRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkQueueTenantUserPreReq}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkQueueTenantUserPreReqResource {

    private final Logger log = LoggerFactory.getLogger(WorkQueueTenantUserPreReqResource.class);

    private static final String ENTITY_NAME = "workQueueTenantUserPreReq";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkQueueTenantUserPreReqRepository workQueueTenantUserPreReqRepository;

    public WorkQueueTenantUserPreReqResource(WorkQueueTenantUserPreReqRepository workQueueTenantUserPreReqRepository) {
        this.workQueueTenantUserPreReqRepository = workQueueTenantUserPreReqRepository;
    }

    /**
     * {@code POST  /work-queue-tenant-user-pre-reqs} : Create a new workQueueTenantUserPreReq.
     *
     * @param workQueueTenantUserPreReq the workQueueTenantUserPreReq to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workQueueTenantUserPreReq, or with status {@code 400 (Bad Request)} if the workQueueTenantUserPreReq has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-queue-tenant-user-pre-reqs")
    public ResponseEntity<WorkQueueTenantUserPreReq> createWorkQueueTenantUserPreReq(
        @Valid @RequestBody WorkQueueTenantUserPreReq workQueueTenantUserPreReq
    ) throws URISyntaxException {
        log.debug("REST request to save WorkQueueTenantUserPreReq : {}", workQueueTenantUserPreReq);
        if (workQueueTenantUserPreReq.getId() != null) {
            throw new BadRequestAlertException("A new workQueueTenantUserPreReq cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkQueueTenantUserPreReq result = workQueueTenantUserPreReqRepository.save(workQueueTenantUserPreReq);
        return ResponseEntity
            .created(new URI("/api/work-queue-tenant-user-pre-reqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-queue-tenant-user-pre-reqs/:id} : Updates an existing workQueueTenantUserPreReq.
     *
     * @param id the id of the workQueueTenantUserPreReq to save.
     * @param workQueueTenantUserPreReq the workQueueTenantUserPreReq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantUserPreReq,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantUserPreReq is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantUserPreReq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-queue-tenant-user-pre-reqs/{id}")
    public ResponseEntity<WorkQueueTenantUserPreReq> updateWorkQueueTenantUserPreReq(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkQueueTenantUserPreReq workQueueTenantUserPreReq
    ) throws URISyntaxException {
        log.debug("REST request to update WorkQueueTenantUserPreReq : {}, {}", id, workQueueTenantUserPreReq);
        if (workQueueTenantUserPreReq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantUserPreReq.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantUserPreReqRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkQueueTenantUserPreReq result = workQueueTenantUserPreReqRepository.save(workQueueTenantUserPreReq);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantUserPreReq.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-queue-tenant-user-pre-reqs/:id} : Partial updates given fields of an existing workQueueTenantUserPreReq, field will ignore if it is null
     *
     * @param id the id of the workQueueTenantUserPreReq to save.
     * @param workQueueTenantUserPreReq the workQueueTenantUserPreReq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantUserPreReq,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantUserPreReq is not valid,
     * or with status {@code 404 (Not Found)} if the workQueueTenantUserPreReq is not found,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantUserPreReq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-queue-tenant-user-pre-reqs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkQueueTenantUserPreReq> partialUpdateWorkQueueTenantUserPreReq(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkQueueTenantUserPreReq workQueueTenantUserPreReq
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkQueueTenantUserPreReq partially : {}, {}", id, workQueueTenantUserPreReq);
        if (workQueueTenantUserPreReq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantUserPreReq.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantUserPreReqRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkQueueTenantUserPreReq> result = workQueueTenantUserPreReqRepository
            .findById(workQueueTenantUserPreReq.getId())
            .map(
                existingWorkQueueTenantUserPreReq -> {
                    if (workQueueTenantUserPreReq.getPreRequisiteItem() != null) {
                        existingWorkQueueTenantUserPreReq.setPreRequisiteItem(workQueueTenantUserPreReq.getPreRequisiteItem());
                    }

                    return existingWorkQueueTenantUserPreReq;
                }
            )
            .map(workQueueTenantUserPreReqRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantUserPreReq.getId().toString())
        );
    }

    /**
     * {@code GET  /work-queue-tenant-user-pre-reqs} : get all the workQueueTenantUserPreReqs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workQueueTenantUserPreReqs in body.
     */
    @GetMapping("/work-queue-tenant-user-pre-reqs")
    public List<WorkQueueTenantUserPreReq> getAllWorkQueueTenantUserPreReqs() {
        log.debug("REST request to get all WorkQueueTenantUserPreReqs");
        return workQueueTenantUserPreReqRepository.findAll();
    }

    /**
     * {@code GET  /work-queue-tenant-user-pre-reqs/:id} : get the "id" workQueueTenantUserPreReq.
     *
     * @param id the id of the workQueueTenantUserPreReq to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workQueueTenantUserPreReq, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-queue-tenant-user-pre-reqs/{id}")
    public ResponseEntity<WorkQueueTenantUserPreReq> getWorkQueueTenantUserPreReq(@PathVariable Long id) {
        log.debug("REST request to get WorkQueueTenantUserPreReq : {}", id);
        Optional<WorkQueueTenantUserPreReq> workQueueTenantUserPreReq = workQueueTenantUserPreReqRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workQueueTenantUserPreReq);
    }

    /**
     * {@code DELETE  /work-queue-tenant-user-pre-reqs/:id} : delete the "id" workQueueTenantUserPreReq.
     *
     * @param id the id of the workQueueTenantUserPreReq to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-queue-tenant-user-pre-reqs/{id}")
    public ResponseEntity<Void> deleteWorkQueueTenantUserPreReq(@PathVariable Long id) {
        log.debug("REST request to delete WorkQueueTenantUserPreReq : {}", id);
        workQueueTenantUserPreReqRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
