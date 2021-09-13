package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkQueueTenantUserData;
import com.mycompany.provisioning.repository.WorkQueueTenantUserDataRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkQueueTenantUserData}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkQueueTenantUserDataResource {

    private final Logger log = LoggerFactory.getLogger(WorkQueueTenantUserDataResource.class);

    private static final String ENTITY_NAME = "workQueueTenantUserData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkQueueTenantUserDataRepository workQueueTenantUserDataRepository;

    public WorkQueueTenantUserDataResource(WorkQueueTenantUserDataRepository workQueueTenantUserDataRepository) {
        this.workQueueTenantUserDataRepository = workQueueTenantUserDataRepository;
    }

    /**
     * {@code POST  /work-queue-tenant-user-data} : Create a new workQueueTenantUserData.
     *
     * @param workQueueTenantUserData the workQueueTenantUserData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workQueueTenantUserData, or with status {@code 400 (Bad Request)} if the workQueueTenantUserData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-queue-tenant-user-data")
    public ResponseEntity<WorkQueueTenantUserData> createWorkQueueTenantUserData(
        @Valid @RequestBody WorkQueueTenantUserData workQueueTenantUserData
    ) throws URISyntaxException {
        log.debug("REST request to save WorkQueueTenantUserData : {}", workQueueTenantUserData);
        if (workQueueTenantUserData.getId() != null) {
            throw new BadRequestAlertException("A new workQueueTenantUserData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkQueueTenantUserData result = workQueueTenantUserDataRepository.save(workQueueTenantUserData);
        return ResponseEntity
            .created(new URI("/api/work-queue-tenant-user-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-queue-tenant-user-data/:id} : Updates an existing workQueueTenantUserData.
     *
     * @param id the id of the workQueueTenantUserData to save.
     * @param workQueueTenantUserData the workQueueTenantUserData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantUserData,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantUserData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantUserData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-queue-tenant-user-data/{id}")
    public ResponseEntity<WorkQueueTenantUserData> updateWorkQueueTenantUserData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkQueueTenantUserData workQueueTenantUserData
    ) throws URISyntaxException {
        log.debug("REST request to update WorkQueueTenantUserData : {}, {}", id, workQueueTenantUserData);
        if (workQueueTenantUserData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantUserData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantUserDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkQueueTenantUserData result = workQueueTenantUserDataRepository.save(workQueueTenantUserData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantUserData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-queue-tenant-user-data/:id} : Partial updates given fields of an existing workQueueTenantUserData, field will ignore if it is null
     *
     * @param id the id of the workQueueTenantUserData to save.
     * @param workQueueTenantUserData the workQueueTenantUserData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantUserData,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantUserData is not valid,
     * or with status {@code 404 (Not Found)} if the workQueueTenantUserData is not found,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantUserData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-queue-tenant-user-data/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkQueueTenantUserData> partialUpdateWorkQueueTenantUserData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkQueueTenantUserData workQueueTenantUserData
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkQueueTenantUserData partially : {}, {}", id, workQueueTenantUserData);
        if (workQueueTenantUserData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantUserData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantUserDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkQueueTenantUserData> result = workQueueTenantUserDataRepository
            .findById(workQueueTenantUserData.getId())
            .map(
                existingWorkQueueTenantUserData -> {
                    if (workQueueTenantUserData.getData() != null) {
                        existingWorkQueueTenantUserData.setData(workQueueTenantUserData.getData());
                    }
                    if (workQueueTenantUserData.getType() != null) {
                        existingWorkQueueTenantUserData.setType(workQueueTenantUserData.getType());
                    }

                    return existingWorkQueueTenantUserData;
                }
            )
            .map(workQueueTenantUserDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantUserData.getId().toString())
        );
    }

    /**
     * {@code GET  /work-queue-tenant-user-data} : get all the workQueueTenantUserData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workQueueTenantUserData in body.
     */
    @GetMapping("/work-queue-tenant-user-data")
    public List<WorkQueueTenantUserData> getAllWorkQueueTenantUserData() {
        log.debug("REST request to get all WorkQueueTenantUserData");
        return workQueueTenantUserDataRepository.findAll();
    }

    /**
     * {@code GET  /work-queue-tenant-user-data/:id} : get the "id" workQueueTenantUserData.
     *
     * @param id the id of the workQueueTenantUserData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workQueueTenantUserData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-queue-tenant-user-data/{id}")
    public ResponseEntity<WorkQueueTenantUserData> getWorkQueueTenantUserData(@PathVariable Long id) {
        log.debug("REST request to get WorkQueueTenantUserData : {}", id);
        Optional<WorkQueueTenantUserData> workQueueTenantUserData = workQueueTenantUserDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workQueueTenantUserData);
    }

    /**
     * {@code DELETE  /work-queue-tenant-user-data/:id} : delete the "id" workQueueTenantUserData.
     *
     * @param id the id of the workQueueTenantUserData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-queue-tenant-user-data/{id}")
    public ResponseEntity<Void> deleteWorkQueueTenantUserData(@PathVariable Long id) {
        log.debug("REST request to delete WorkQueueTenantUserData : {}", id);
        workQueueTenantUserDataRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
