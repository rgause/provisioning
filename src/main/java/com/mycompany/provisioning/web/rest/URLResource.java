package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.URL;
import com.mycompany.provisioning.repository.URLRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.URL}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class URLResource {

    private final Logger log = LoggerFactory.getLogger(URLResource.class);

    private static final String ENTITY_NAME = "uRL";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final URLRepository uRLRepository;

    public URLResource(URLRepository uRLRepository) {
        this.uRLRepository = uRLRepository;
    }

    /**
     * {@code POST  /urls} : Create a new uRL.
     *
     * @param uRL the uRL to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new uRL, or with status {@code 400 (Bad Request)} if the uRL has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/urls")
    public ResponseEntity<URL> createURL(@Valid @RequestBody URL uRL) throws URISyntaxException {
        log.debug("REST request to save URL : {}", uRL);
        if (uRL.getId() != null) {
            throw new BadRequestAlertException("A new uRL cannot already have an ID", ENTITY_NAME, "idexists");
        }
        URL result = uRLRepository.save(uRL);
        return ResponseEntity
            .created(new URI("/api/urls/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /urls/:id} : Updates an existing uRL.
     *
     * @param id the id of the uRL to save.
     * @param uRL the uRL to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uRL,
     * or with status {@code 400 (Bad Request)} if the uRL is not valid,
     * or with status {@code 500 (Internal Server Error)} if the uRL couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/urls/{id}")
    public ResponseEntity<URL> updateURL(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody URL uRL)
        throws URISyntaxException {
        log.debug("REST request to update URL : {}, {}", id, uRL);
        if (uRL.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uRL.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uRLRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        URL result = uRLRepository.save(uRL);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uRL.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /urls/:id} : Partial updates given fields of an existing uRL, field will ignore if it is null
     *
     * @param id the id of the uRL to save.
     * @param uRL the uRL to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated uRL,
     * or with status {@code 400 (Bad Request)} if the uRL is not valid,
     * or with status {@code 404 (Not Found)} if the uRL is not found,
     * or with status {@code 500 (Internal Server Error)} if the uRL couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/urls/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<URL> partialUpdateURL(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody URL uRL)
        throws URISyntaxException {
        log.debug("REST request to partial update URL partially : {}, {}", id, uRL);
        if (uRL.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, uRL.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!uRLRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<URL> result = uRLRepository
            .findById(uRL.getId())
            .map(
                existingURL -> {
                    if (uRL.getUrl() != null) {
                        existingURL.setUrl(uRL.getUrl());
                    }

                    return existingURL;
                }
            )
            .map(uRLRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, uRL.getId().toString())
        );
    }

    /**
     * {@code GET  /urls} : get all the uRLS.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of uRLS in body.
     */
    @GetMapping("/urls")
    public List<URL> getAllURLS() {
        log.debug("REST request to get all URLS");
        return uRLRepository.findAll();
    }

    /**
     * {@code GET  /urls/:id} : get the "id" uRL.
     *
     * @param id the id of the uRL to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the uRL, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/urls/{id}")
    public ResponseEntity<URL> getURL(@PathVariable Long id) {
        log.debug("REST request to get URL : {}", id);
        Optional<URL> uRL = uRLRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(uRL);
    }

    /**
     * {@code DELETE  /urls/:id} : delete the "id" uRL.
     *
     * @param id the id of the uRL to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/urls/{id}")
    public ResponseEntity<Void> deleteURL(@PathVariable Long id) {
        log.debug("REST request to delete URL : {}", id);
        uRLRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
