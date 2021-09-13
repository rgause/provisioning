package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.URLType;
import com.mycompany.provisioning.repository.URLTypeRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.URLType}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class URLTypeResource {

    private final Logger log = LoggerFactory.getLogger(URLTypeResource.class);

    private static final String ENTITY_NAME = "uRLType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final URLTypeRepository uRLTypeRepository;

    public URLTypeResource(URLTypeRepository uRLTypeRepository) {
        this.uRLTypeRepository = uRLTypeRepository;
    }

    /**
     * {@code POST  /url-types} : Create a new uRLType.
     *
     * @param uRLType the uRLType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uRLType, or with status {@code 400 (Bad Request)} if the uRLType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/url-types")
    public ResponseEntity<URLType> createURLType(@Valid @RequestBody URLType uRLType) throws URISyntaxException {
        log.debug("REST request to save URLType : {}", uRLType);
        if (uRLType.getId() != null) {
            throw new BadRequestAlertException("A new uRLType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        URLType result = uRLTypeRepository.save(uRLType);
        return ResponseEntity
            .created(new URI("/api/url-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /url-types/:id} : Updates an existing uRLType.
     *
     * @param id the id of the uRLType to save.
     * @param uRLType the uRLType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uRLType,
     * or with status {@code 400 (Bad Request)} if the uRLType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uRLType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/url-types/{id}")
    public ResponseEntity<URLType> updateURLType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody URLType uRLType
    ) throws URISyntaxException {
        log.debug("REST request to update URLType : {}, {}", id, uRLType);
        if (uRLType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uRLType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uRLTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        URLType result = uRLTypeRepository.save(uRLType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uRLType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /url-types/:id} : Partial updates given fields of an existing uRLType, field will ignore if it is null
     *
     * @param id the id of the uRLType to save.
     * @param uRLType the uRLType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uRLType,
     * or with status {@code 400 (Bad Request)} if the uRLType is not valid,
     * or with status {@code 404 (Not Found)} if the uRLType is not found,
     * or with status {@code 500 (Internal Server Error)} if the uRLType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/url-types/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<URLType> partialUpdateURLType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody URLType uRLType
    ) throws URISyntaxException {
        log.debug("REST request to partial update URLType partially : {}, {}", id, uRLType);
        if (uRLType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uRLType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uRLTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<URLType> result = uRLTypeRepository
            .findById(uRLType.getId())
            .map(
                existingURLType -> {
                    if (uRLType.getName() != null) {
                        existingURLType.setName(uRLType.getName());
                    }

                    return existingURLType;
                }
            )
            .map(uRLTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uRLType.getId().toString())
        );
    }

    /**
     * {@code GET  /url-types} : get all the uRLTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uRLTypes in body.
     */
    @GetMapping("/url-types")
    public List<URLType> getAllURLTypes() {
        log.debug("REST request to get all URLTypes");
        return uRLTypeRepository.findAll();
    }

    /**
     * {@code GET  /url-types/:id} : get the "id" uRLType.
     *
     * @param id the id of the uRLType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uRLType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/url-types/{id}")
    public ResponseEntity<URLType> getURLType(@PathVariable Long id) {
        log.debug("REST request to get URLType : {}", id);
        Optional<URLType> uRLType = uRLTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uRLType);
    }

    /**
     * {@code DELETE  /url-types/:id} : delete the "id" uRLType.
     *
     * @param id the id of the uRLType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/url-types/{id}")
    public ResponseEntity<Void> deleteURLType(@PathVariable Long id) {
        log.debug("REST request to delete URLType : {}", id);
        uRLTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
