package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkTemplateItemPreReq;
import com.mycompany.provisioning.repository.WorkTemplateItemPreReqRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkTemplateItemPreReq}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkTemplateItemPreReqResource {

    private final Logger log = LoggerFactory.getLogger(WorkTemplateItemPreReqResource.class);

    private static final String ENTITY_NAME = "workTemplateItemPreReq";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkTemplateItemPreReqRepository workTemplateItemPreReqRepository;

    public WorkTemplateItemPreReqResource(WorkTemplateItemPreReqRepository workTemplateItemPreReqRepository) {
        this.workTemplateItemPreReqRepository = workTemplateItemPreReqRepository;
    }

    /**
     * {@code POST  /work-template-item-pre-reqs} : Create a new workTemplateItemPreReq.
     *
     * @param workTemplateItemPreReq the workTemplateItemPreReq to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workTemplateItemPreReq, or with status {@code 400 (Bad Request)} if the workTemplateItemPreReq has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-template-item-pre-reqs")
    public ResponseEntity<WorkTemplateItemPreReq> createWorkTemplateItemPreReq(
        @Valid @RequestBody WorkTemplateItemPreReq workTemplateItemPreReq
    ) throws URISyntaxException {
        log.debug("REST request to save WorkTemplateItemPreReq : {}", workTemplateItemPreReq);
        if (workTemplateItemPreReq.getId() != null) {
            throw new BadRequestAlertException("A new workTemplateItemPreReq cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkTemplateItemPreReq result = workTemplateItemPreReqRepository.save(workTemplateItemPreReq);
        return ResponseEntity
            .created(new URI("/api/work-template-item-pre-reqs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-template-item-pre-reqs/:id} : Updates an existing workTemplateItemPreReq.
     *
     * @param id the id of the workTemplateItemPreReq to save.
     * @param workTemplateItemPreReq the workTemplateItemPreReq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workTemplateItemPreReq,
     * or with status {@code 400 (Bad Request)} if the workTemplateItemPreReq is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workTemplateItemPreReq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-template-item-pre-reqs/{id}")
    public ResponseEntity<WorkTemplateItemPreReq> updateWorkTemplateItemPreReq(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkTemplateItemPreReq workTemplateItemPreReq
    ) throws URISyntaxException {
        log.debug("REST request to update WorkTemplateItemPreReq : {}, {}", id, workTemplateItemPreReq);
        if (workTemplateItemPreReq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workTemplateItemPreReq.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workTemplateItemPreReqRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkTemplateItemPreReq result = workTemplateItemPreReqRepository.save(workTemplateItemPreReq);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workTemplateItemPreReq.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-template-item-pre-reqs/:id} : Partial updates given fields of an existing workTemplateItemPreReq, field will ignore if it is null
     *
     * @param id the id of the workTemplateItemPreReq to save.
     * @param workTemplateItemPreReq the workTemplateItemPreReq to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workTemplateItemPreReq,
     * or with status {@code 400 (Bad Request)} if the workTemplateItemPreReq is not valid,
     * or with status {@code 404 (Not Found)} if the workTemplateItemPreReq is not found,
     * or with status {@code 500 (Internal Server Error)} if the workTemplateItemPreReq couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-template-item-pre-reqs/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkTemplateItemPreReq> partialUpdateWorkTemplateItemPreReq(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkTemplateItemPreReq workTemplateItemPreReq
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkTemplateItemPreReq partially : {}, {}", id, workTemplateItemPreReq);
        if (workTemplateItemPreReq.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workTemplateItemPreReq.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workTemplateItemPreReqRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkTemplateItemPreReq> result = workTemplateItemPreReqRepository
            .findById(workTemplateItemPreReq.getId())
            .map(
                existingWorkTemplateItemPreReq -> {
                    if (workTemplateItemPreReq.getPreRequisiteItem() != null) {
                        existingWorkTemplateItemPreReq.setPreRequisiteItem(workTemplateItemPreReq.getPreRequisiteItem());
                    }

                    return existingWorkTemplateItemPreReq;
                }
            )
            .map(workTemplateItemPreReqRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workTemplateItemPreReq.getId().toString())
        );
    }

    /**
     * {@code GET  /work-template-item-pre-reqs} : get all the workTemplateItemPreReqs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workTemplateItemPreReqs in body.
     */
    @GetMapping("/work-template-item-pre-reqs")
    public List<WorkTemplateItemPreReq> getAllWorkTemplateItemPreReqs() {
        log.debug("REST request to get all WorkTemplateItemPreReqs");
        return workTemplateItemPreReqRepository.findAll();
    }

    /**
     * {@code GET  /work-template-item-pre-reqs/:id} : get the "id" workTemplateItemPreReq.
     *
     * @param id the id of the workTemplateItemPreReq to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workTemplateItemPreReq, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-template-item-pre-reqs/{id}")
    public ResponseEntity<WorkTemplateItemPreReq> getWorkTemplateItemPreReq(@PathVariable Long id) {
        log.debug("REST request to get WorkTemplateItemPreReq : {}", id);
        Optional<WorkTemplateItemPreReq> workTemplateItemPreReq = workTemplateItemPreReqRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workTemplateItemPreReq);
    }

    /**
     * {@code DELETE  /work-template-item-pre-reqs/:id} : delete the "id" workTemplateItemPreReq.
     *
     * @param id the id of the workTemplateItemPreReq to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-template-item-pre-reqs/{id}")
    public ResponseEntity<Void> deleteWorkTemplateItemPreReq(@PathVariable Long id) {
        log.debug("REST request to delete WorkTemplateItemPreReq : {}", id);
        workTemplateItemPreReqRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
