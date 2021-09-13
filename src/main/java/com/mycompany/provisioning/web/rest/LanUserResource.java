package com.mycompany.provisioning.web.rest;

import com.mycompany.provisioning.domain.LanUser;
import com.mycompany.provisioning.repository.LanUserRepository;
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
 * REST controller for managing {@link com.mycompany.provisioning.domain.LanUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class LanUserResource {

    private final Logger log = LoggerFactory.getLogger(LanUserResource.class);

    private static final String ENTITY_NAME = "lanUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanUserRepository lanUserRepository;

    public LanUserResource(LanUserRepository lanUserRepository) {
        this.lanUserRepository = lanUserRepository;
    }

    /**
     * {@code POST  /lan-users} : Create a new lanUser.
     *
     * @param lanUser the lanUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lanUser, or with status {@code 400 (Bad Request)} if the lanUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lan-users")
    public ResponseEntity<LanUser> createLanUser(@Valid @RequestBody LanUser lanUser) throws URISyntaxException {
        log.debug("REST request to save LanUser : {}", lanUser);
        if (lanUser.getId() != null) {
            throw new BadRequestAlertException("A new lanUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LanUser result = lanUserRepository.save(lanUser);
        return ResponseEntity
            .created(new URI("/api/lan-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lan-users/:id} : Updates an existing lanUser.
     *
     * @param id the id of the lanUser to save.
     * @param lanUser the lanUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lanUser,
     * or with status {@code 400 (Bad Request)} if the lanUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lanUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lan-users/{id}")
    public ResponseEntity<LanUser> updateLanUser(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LanUser lanUser
    ) throws URISyntaxException {
        log.debug("REST request to update LanUser : {}, {}", id, lanUser);
        if (lanUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lanUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lanUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LanUser result = lanUserRepository.save(lanUser);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lanUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lan-users/:id} : Partial updates given fields of an existing lanUser, field will ignore if it is null
     *
     * @param id the id of the lanUser to save.
     * @param lanUser the lanUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lanUser,
     * or with status {@code 400 (Bad Request)} if the lanUser is not valid,
     * or with status {@code 404 (Not Found)} if the lanUser is not found,
     * or with status {@code 500 (Internal Server Error)} if the lanUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lan-users/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LanUser> partialUpdateLanUser(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LanUser lanUser
    ) throws URISyntaxException {
        log.debug("REST request to partial update LanUser partially : {}, {}", id, lanUser);
        if (lanUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lanUser.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lanUserRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LanUser> result = lanUserRepository
            .findById(lanUser.getId())
            .map(
                existingLanUser -> {
                    if (lanUser.getActive() != null) {
                        existingLanUser.setActive(lanUser.getActive());
                    }
                    if (lanUser.getLanId() != null) {
                        existingLanUser.setLanId(lanUser.getLanId());
                    }
                    if (lanUser.getPwpId() != null) {
                        existingLanUser.setPwpId(lanUser.getPwpId());
                    }
                    if (lanUser.getLastName() != null) {
                        existingLanUser.setLastName(lanUser.getLastName());
                    }
                    if (lanUser.getFirstName() != null) {
                        existingLanUser.setFirstName(lanUser.getFirstName());
                    }

                    return existingLanUser;
                }
            )
            .map(lanUserRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, lanUser.getId().toString())
        );
    }

    /**
     * {@code GET  /lan-users} : get all the lanUsers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lanUsers in body.
     */
    @GetMapping("/lan-users")
    public List<LanUser> getAllLanUsers() {
        log.debug("REST request to get all LanUsers");
        return lanUserRepository.findAll();
    }

    /**
     * {@code GET  /lan-users/:id} : get the "id" lanUser.
     *
     * @param id the id of the lanUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lanUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lan-users/{id}")
    public ResponseEntity<LanUser> getLanUser(@PathVariable Long id) {
        log.debug("REST request to get LanUser : {}", id);
        Optional<LanUser> lanUser = lanUserRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(lanUser);
    }

    /**
     * {@code DELETE  /lan-users/:id} : delete the "id" lanUser.
     *
     * @param id the id of the lanUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lan-users/{id}")
    public ResponseEntity<Void> deleteLanUser(@PathVariable Long id) {
        log.debug("REST request to delete LanUser : {}", id);
        lanUserRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
