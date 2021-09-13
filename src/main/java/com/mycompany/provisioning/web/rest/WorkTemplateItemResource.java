package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkTemplateItem;
import com.mycompany.provisioning.repository.WorkTemplateItemRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkTemplateItem}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkTemplateItemResource {

    private final Logger log = LoggerFactory.getLogger(WorkTemplateItemResource.class);

    private static final String ENTITY_NAME = "workTemplateItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkTemplateItemRepository workTemplateItemRepository;

    public WorkTemplateItemResource(WorkTemplateItemRepository workTemplateItemRepository) {
        this.workTemplateItemRepository = workTemplateItemRepository;
    }

    /**
     * {@code POST  /work-template-items} : Create a new workTemplateItem.
     *
     * @param workTemplateItem the workTemplateItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workTemplateItem, or with status {@code 400 (Bad Request)} if the workTemplateItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-template-items")
    public ResponseEntity<WorkTemplateItem> createWorkTemplateItem(@Valid @RequestBody WorkTemplateItem workTemplateItem)
        throws URISyntaxException {
        log.debug("REST request to save WorkTemplateItem : {}", workTemplateItem);
        if (workTemplateItem.getId() != null) {
            throw new BadRequestAlertException("A new workTemplateItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkTemplateItem result = workTemplateItemRepository.save(workTemplateItem);
        return ResponseEntity
            .created(new URI("/api/work-template-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-template-items/:id} : Updates an existing workTemplateItem.
     *
     * @param id the id of the workTemplateItem to save.
     * @param workTemplateItem the workTemplateItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workTemplateItem,
     * or with status {@code 400 (Bad Request)} if the workTemplateItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workTemplateItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-template-items/{id}")
    public ResponseEntity<WorkTemplateItem> updateWorkTemplateItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkTemplateItem workTemplateItem
    ) throws URISyntaxException {
        log.debug("REST request to update WorkTemplateItem : {}, {}", id, workTemplateItem);
        if (workTemplateItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workTemplateItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workTemplateItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkTemplateItem result = workTemplateItemRepository.save(workTemplateItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workTemplateItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-template-items/:id} : Partial updates given fields of an existing workTemplateItem, field will ignore if it is null
     *
     * @param id the id of the workTemplateItem to save.
     * @param workTemplateItem the workTemplateItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workTemplateItem,
     * or with status {@code 400 (Bad Request)} if the workTemplateItem is not valid,
     * or with status {@code 404 (Not Found)} if the workTemplateItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the workTemplateItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-template-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkTemplateItem> partialUpdateWorkTemplateItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkTemplateItem workTemplateItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkTemplateItem partially : {}, {}", id, workTemplateItem);
        if (workTemplateItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workTemplateItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workTemplateItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkTemplateItem> result = workTemplateItemRepository
            .findById(workTemplateItem.getId())
            .map(
                existingWorkTemplateItem -> {
                    if (workTemplateItem.getTask() != null) {
                        existingWorkTemplateItem.setTask(workTemplateItem.getTask());
                    }
                    if (workTemplateItem.getRequiredToComplete() != null) {
                        existingWorkTemplateItem.setRequiredToComplete(workTemplateItem.getRequiredToComplete());
                    }
                    if (workTemplateItem.getSequenceOrder() != null) {
                        existingWorkTemplateItem.setSequenceOrder(workTemplateItem.getSequenceOrder());
                    }

                    return existingWorkTemplateItem;
                }
            )
            .map(workTemplateItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workTemplateItem.getId().toString())
        );
    }

    /**
     * {@code GET  /work-template-items} : get all the workTemplateItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workTemplateItems in body.
     */
    @GetMapping("/work-template-items")
    public List<WorkTemplateItem> getAllWorkTemplateItems() {
        log.debug("REST request to get all WorkTemplateItems");
        return workTemplateItemRepository.findAll();
    }

    /**
     * {@code GET  /work-template-items/:id} : get the "id" workTemplateItem.
     *
     * @param id the id of the workTemplateItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workTemplateItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-template-items/{id}")
    public ResponseEntity<WorkTemplateItem> getWorkTemplateItem(@PathVariable Long id) {
        log.debug("REST request to get WorkTemplateItem : {}", id);
        Optional<WorkTemplateItem> workTemplateItem = workTemplateItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workTemplateItem);
    }

    /**
     * {@code DELETE  /work-template-items/:id} : delete the "id" workTemplateItem.
     *
     * @param id the id of the workTemplateItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-template-items/{id}")
    public ResponseEntity<Void> deleteWorkTemplateItem(@PathVariable Long id) {
        log.debug("REST request to delete WorkTemplateItem : {}", id);
        workTemplateItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
