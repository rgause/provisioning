package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkQueueTenantPreReq;
import com.mycompany.provisioning.repository.WorkQueueTenantPreReqRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkQueueTenantPreReq}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkQueueTenantPreReqResource {

    private final Logger log = LoggerFactory.getLogger(WorkQueueTenantPreReqResource.class);

    private static final String ENTITY_NAME = "workQueueTenantPreReq";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkQueueTenantPreReqRepository workQueueTenantPreReqRepository;

    public WorkQueueTenantPreReqResource(WorkQueueTenantPreReqRepository workQueueTenantPreReqRepository) {
        this.workQueueTenantPreReqRepository = workQueueTenantPreReqRepository;
    }

    /**
     * {@code POST  /work-queue-tenant-pre-reqs} : Create a new workQueueTenantPreReq.
     *
     * @param workQueueTenantPreReq the workQueueTenantPreReq to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workQueueTenantPreReq, or with status {@code 400 (Bad Request)} if the workQueueTenantPreReq has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-queue-tenant-pre-reqs")
    public ResponseEntity<WorkQueueTenantPreReq> createWorkQueueTenantPreReq(
        @Valid @RequestBody WorkQueueTenantPreReq workQueueTenantPreReq
    ) throws URISyntaxException {
        log.debug("REST request to save WorkQueueTenantPreReq : {}", workQueueTenantPreReq);
        if (workQueueTenantPreReq.getId() != null) {
            throw new BadRequestAlertException("A new workQueueTenantPreReq cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkQueueTenantPreReq result = workQueueTenantPreReqRepository.save(workQueueTenantPreReq);
        return ResponseEntity
            .created(new URI("/api/work-queue-tenant-pre-reqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-queue-tenant-pre-reqs/:id} : Updates an existing workQueueTenantPreReq.
     *
     * @param id the id of the workQueueTenantPreReq to save.
     * @param workQueueTenantPreReq the workQueueTenantPreReq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantPreReq,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantPreReq is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantPreReq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-queue-tenant-pre-reqs/{id}")
    public ResponseEntity<WorkQueueTenantPreReq> updateWorkQueueTenantPreReq(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkQueueTenantPreReq workQueueTenantPreReq
    ) throws URISyntaxException {
        log.debug("REST request to update WorkQueueTenantPreReq : {}, {}", id, workQueueTenantPreReq);
        if (workQueueTenantPreReq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantPreReq.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantPreReqRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkQueueTenantPreReq result = workQueueTenantPreReqRepository.save(workQueueTenantPreReq);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantPreReq.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-queue-tenant-pre-reqs/:id} : Partial updates given fields of an existing workQueueTenantPreReq, field will ignore if it is null
     *
     * @param id the id of the workQueueTenantPreReq to save.
     * @param workQueueTenantPreReq the workQueueTenantPreReq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantPreReq,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantPreReq is not valid,
     * or with status {@code 404 (Not Found)} if the workQueueTenantPreReq is not found,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantPreReq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-queue-tenant-pre-reqs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkQueueTenantPreReq> partialUpdateWorkQueueTenantPreReq(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkQueueTenantPreReq workQueueTenantPreReq
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkQueueTenantPreReq partially : {}, {}", id, workQueueTenantPreReq);
        if (workQueueTenantPreReq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantPreReq.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantPreReqRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkQueueTenantPreReq> result = workQueueTenantPreReqRepository
            .findById(workQueueTenantPreReq.getId())
            .map(
                existingWorkQueueTenantPreReq -> {
                    if (workQueueTenantPreReq.getPreRequisiteItem() != null) {
                        existingWorkQueueTenantPreReq.setPreRequisiteItem(workQueueTenantPreReq.getPreRequisiteItem());
                    }

                    return existingWorkQueueTenantPreReq;
                }
            )
            .map(workQueueTenantPreReqRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantPreReq.getId().toString())
        );
    }

    /**
     * {@code GET  /work-queue-tenant-pre-reqs} : get all the workQueueTenantPreReqs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workQueueTenantPreReqs in body.
     */
    @GetMapping("/work-queue-tenant-pre-reqs")
    public List<WorkQueueTenantPreReq> getAllWorkQueueTenantPreReqs() {
        log.debug("REST request to get all WorkQueueTenantPreReqs");
        return workQueueTenantPreReqRepository.findAll();
    }

    /**
     * {@code GET  /work-queue-tenant-pre-reqs/:id} : get the "id" workQueueTenantPreReq.
     *
     * @param id the id of the workQueueTenantPreReq to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workQueueTenantPreReq, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-queue-tenant-pre-reqs/{id}")
    public ResponseEntity<WorkQueueTenantPreReq> getWorkQueueTenantPreReq(@PathVariable Long id) {
        log.debug("REST request to get WorkQueueTenantPreReq : {}", id);
        Optional<WorkQueueTenantPreReq> workQueueTenantPreReq = workQueueTenantPreReqRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workQueueTenantPreReq);
    }

    /**
     * {@code DELETE  /work-queue-tenant-pre-reqs/:id} : delete the "id" workQueueTenantPreReq.
     *
     * @param id the id of the workQueueTenantPreReq to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-queue-tenant-pre-reqs/{id}")
    public ResponseEntity<Void> deleteWorkQueueTenantPreReq(@PathVariable Long id) {
        log.debug("REST request to delete WorkQueueTenantPreReq : {}", id);
        workQueueTenantPreReqRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
