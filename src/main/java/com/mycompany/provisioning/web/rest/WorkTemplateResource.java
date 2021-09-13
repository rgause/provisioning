package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.WorkTemplate;
import com.mycompany.provisioning.repository.WorkTemplateRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.WorkTemplate}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WorkTemplateResource {

    private final Logger log = LoggerFactory.getLogger(WorkTemplateResource.class);

    private static final String ENTITY_NAME = "workTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkTemplateRepository workTemplateRepository;

    public WorkTemplateResource(WorkTemplateRepository workTemplateRepository) {
        this.workTemplateRepository = workTemplateRepository;
    }

    /**
     * {@code POST  /work-templates} : Create a new workTemplate.
     *
     * @param workTemplate the workTemplate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workTemplate, or with status {@code 400 (Bad Request)} if the workTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-templates")
    public ResponseEntity<WorkTemplate> createWorkTemplate(@Valid @RequestBody WorkTemplate workTemplate) throws URISyntaxException {
        log.debug("REST request to save WorkTemplate : {}", workTemplate);
        if (workTemplate.getId() != null) {
            throw new BadRequestAlertException("A new workTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkTemplate result = workTemplateRepository.save(workTemplate);
        return ResponseEntity
            .created(new URI("/api/work-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-templates/:id} : Updates an existing workTemplate.
     *
     * @param id the id of the workTemplate to save.
     * @param workTemplate the workTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workTemplate,
     * or with status {@code 400 (Bad Request)} if the workTemplate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-templates/{id}")
    public ResponseEntity<WorkTemplate> updateWorkTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkTemplate workTemplate
    ) throws URISyntaxException {
        log.debug("REST request to update WorkTemplate : {}, {}", id, workTemplate);
        if (workTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkTemplate result = workTemplateRepository.save(workTemplate);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workTemplate.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-templates/:id} : Partial updates given fields of an existing workTemplate, field will ignore if it is null
     *
     * @param id the id of the workTemplate to save.
     * @param workTemplate the workTemplate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workTemplate,
     * or with status {@code 400 (Bad Request)} if the workTemplate is not valid,
     * or with status {@code 404 (Not Found)} if the workTemplate is not found,
     * or with status {@code 500 (Internal Server Error)} if the workTemplate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-templates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<WorkTemplate> partialUpdateWorkTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkTemplate workTemplate
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkTemplate partially : {}, {}", id, workTemplate);
        if (workTemplate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workTemplate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkTemplate> result = workTemplateRepository
            .findById(workTemplate.getId())
            .map(
                existingWorkTemplate -> {
                    if (workTemplate.getName() != null) {
                        existingWorkTemplate.setName(workTemplate.getName());
                    }

                    return existingWorkTemplate;
                }
            )
            .map(workTemplateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workTemplate.getId().toString())
        );
    }

    /**
     * {@code GET  /work-templates} : get all the workTemplates.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workTemplates in body.
     */
    @GetMapping("/work-templates")
    public List<WorkTemplate> getAllWorkTemplates() {
        log.debug("REST request to get all WorkTemplates");
        return workTemplateRepository.findAll();
    }

    /**
     * {@code GET  /work-templates/:id} : get the "id" workTemplate.
     *
     * @param id the id of the workTemplate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workTemplate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-templates/{id}")
    public ResponseEntity<WorkTemplate> getWorkTemplate(@PathVariable Long id) {
        log.debug("REST request to get WorkTemplate : {}", id);
        Optional<WorkTemplate> workTemplate = workTemplateRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workTemplate);
    }

    /**
     * {@code DELETE  /work-templates/:id} : delete the "id" workTemplate.
     *
     * @param id the id of the workTemplate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-templates/{id}")
    public ResponseEntity<Void> deleteWorkTemplate(@PathVariable Long id) {
        log.debug("REST request to delete WorkTemplate : {}", id);
        workTemplateRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
