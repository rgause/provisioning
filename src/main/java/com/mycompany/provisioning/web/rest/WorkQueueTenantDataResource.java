package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkQueueTenantData;
import com.mycompany.provisioning.repository.WorkQueueTenantDataRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkQueueTenantData}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkQueueTenantDataResource {

    private final Logger log = LoggerFactory.getLogger(WorkQueueTenantDataResource.class);

    private static final String ENTITY_NAME = "workQueueTenantData";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkQueueTenantDataRepository workQueueTenantDataRepository;

    public WorkQueueTenantDataResource(WorkQueueTenantDataRepository workQueueTenantDataRepository) {
        this.workQueueTenantDataRepository = workQueueTenantDataRepository;
    }

    /**
     * {@code POST  /work-queue-tenant-data} : Create a new workQueueTenantData.
     *
     * @param workQueueTenantData the workQueueTenantData to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workQueueTenantData, or with status {@code 400 (Bad Request)} if the workQueueTenantData has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-queue-tenant-data")
    public ResponseEntity<WorkQueueTenantData> createWorkQueueTenantData(@Valid @RequestBody WorkQueueTenantData workQueueTenantData)
        throws URISyntaxException {
        log.debug("REST request to save WorkQueueTenantData : {}", workQueueTenantData);
        if (workQueueTenantData.getId() != null) {
            throw new BadRequestAlertException("A new workQueueTenantData cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkQueueTenantData result = workQueueTenantDataRepository.save(workQueueTenantData);
        return ResponseEntity
            .created(new URI("/api/work-queue-tenant-data/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-queue-tenant-data/:id} : Updates an existing workQueueTenantData.
     *
     * @param id the id of the workQueueTenantData to save.
     * @param workQueueTenantData the workQueueTenantData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantData,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantData is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-queue-tenant-data/{id}")
    public ResponseEntity<WorkQueueTenantData> updateWorkQueueTenantData(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkQueueTenantData workQueueTenantData
    ) throws URISyntaxException {
        log.debug("REST request to update WorkQueueTenantData : {}, {}", id, workQueueTenantData);
        if (workQueueTenantData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkQueueTenantData result = workQueueTenantDataRepository.save(workQueueTenantData);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantData.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-queue-tenant-data/:id} : Partial updates given fields of an existing workQueueTenantData, field will ignore if it is null
     *
     * @param id the id of the workQueueTenantData to save.
     * @param workQueueTenantData the workQueueTenantData to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workQueueTenantData,
     * or with status {@code 400 (Bad Request)} if the workQueueTenantData is not valid,
     * or with status {@code 404 (Not Found)} if the workQueueTenantData is not found,
     * or with status {@code 500 (Internal Server Error)} if the workQueueTenantData couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-queue-tenant-data/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkQueueTenantData> partialUpdateWorkQueueTenantData(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkQueueTenantData workQueueTenantData
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkQueueTenantData partially : {}, {}", id, workQueueTenantData);
        if (workQueueTenantData.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workQueueTenantData.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workQueueTenantDataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkQueueTenantData> result = workQueueTenantDataRepository
            .findById(workQueueTenantData.getId())
            .map(
                existingWorkQueueTenantData -> {
                    if (workQueueTenantData.getData() != null) {
                        existingWorkQueueTenantData.setData(workQueueTenantData.getData());
                    }
                    if (workQueueTenantData.getType() != null) {
                        existingWorkQueueTenantData.setType(workQueueTenantData.getType());
                    }

                    return existingWorkQueueTenantData;
                }
            )
            .map(workQueueTenantDataRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workQueueTenantData.getId().toString())
        );
    }

    /**
     * {@code GET  /work-queue-tenant-data} : get all the workQueueTenantData.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workQueueTenantData in body.
     */
    @GetMapping("/work-queue-tenant-data")
    public List<WorkQueueTenantData> getAllWorkQueueTenantData() {
        log.debug("REST request to get all WorkQueueTenantData");
        return workQueueTenantDataRepository.findAll();
    }

    /**
     * {@code GET  /work-queue-tenant-data/:id} : get the "id" workQueueTenantData.
     *
     * @param id the id of the workQueueTenantData to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workQueueTenantData, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-queue-tenant-data/{id}")
    public ResponseEntity<WorkQueueTenantData> getWorkQueueTenantData(@PathVariable Long id) {
        log.debug("REST request to get WorkQueueTenantData : {}", id);
        Optional<WorkQueueTenantData> workQueueTenantData = workQueueTenantDataRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workQueueTenantData);
    }

    /**
     * {@code DELETE  /work-queue-tenant-data/:id} : delete the "id" workQueueTenantData.
     *
     * @param id the id of the workQueueTenantData to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-queue-tenant-data/{id}")
    public ResponseEntity<Void> deleteWorkQueueTenantData(@PathVariable Long id) {
        log.debug("REST request to delete WorkQueueTenantData : {}", id);
        workQueueTenantDataRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
