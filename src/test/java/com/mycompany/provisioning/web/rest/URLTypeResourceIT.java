package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.URLType;
import com.mycompany.provisioning.repository.URLTypeRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link URLTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class URLTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/url-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private URLTypeRepository uRLTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restURLTypeMockMvc;

    private URLType uRLType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static URLType createEntity(EntityManager em) {
        URLType uRLType = new URLType().name(DEFAULT_NAME);
        return uRLType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static URLType createUpdatedEntity(EntityManager em) {
        URLType uRLType = new URLType().name(UPDATED_NAME);
        return uRLType;
    }

    @BeforeEach
    public void initTest() {
        uRLType = createEntity(em);
    }

    @Test
    @Transactional
    void createURLType() throws Exception {
        int databaseSizeBeforeCreate = uRLTypeRepository.findAll().size();
        // Create the URLType
        restURLTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isCreated());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeCreate + 1);
        URLType testURLType = uRLTypeList.get(uRLTypeList.size() - 1);
        assertThat(testURLType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createURLTypeWithExistingId() throws Exception {
        // Create the URLType with an existing ID
        uRLType.setId(1L);

        int databaseSizeBeforeCreate = uRLTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restURLTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isBadRequest());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = uRLTypeRepository.findAll().size();
        // set the field null
        uRLType.setName(null);

        // Create the URLType, which fails.

        restURLTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isBadRequest());

        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllURLTypes() throws Exception {
        // Initialize the database
        uRLTypeRepository.saveAndFlush(uRLType);

        // Get all the uRLTypeList
        restURLTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uRLType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getURLType() throws Exception {
        // Initialize the database
        uRLTypeRepository.saveAndFlush(uRLType);

        // Get the uRLType
        restURLTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, uRLType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uRLType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingURLType() throws Exception {
        // Get the uRLType
        restURLTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewURLType() throws Exception {
        // Initialize the database
        uRLTypeRepository.saveAndFlush(uRLType);

        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();

        // Update the uRLType
        URLType updatedURLType = uRLTypeRepository.findById(uRLType.getId()).get();
        // Disconnect from session so that the updates on updatedURLType are not directly saved in db
        em.detach(updatedURLType);
        updatedURLType.name(UPDATED_NAME);

        restURLTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedURLType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedURLType))
            )
            .andExpect(status().isOk());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
        URLType testURLType = uRLTypeList.get(uRLTypeList.size() - 1);
        assertThat(testURLType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingURLType() throws Exception {
        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();
        uRLType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restURLTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uRLType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isBadRequest());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchURLType() throws Exception {
        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();
        uRLType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isBadRequest());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamURLType() throws Exception {
        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();
        uRLType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLTypeMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateURLTypeWithPatch() throws Exception {
        // Initialize the database
        uRLTypeRepository.saveAndFlush(uRLType);

        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();

        // Update the uRLType using partial update
        URLType partialUpdatedURLType = new URLType();
        partialUpdatedURLType.setId(uRLType.getId());

        partialUpdatedURLType.name(UPDATED_NAME);

        restURLTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedURLType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedURLType))
            )
            .andExpect(status().isOk());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
        URLType testURLType = uRLTypeList.get(uRLTypeList.size() - 1);
        assertThat(testURLType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateURLTypeWithPatch() throws Exception {
        // Initialize the database
        uRLTypeRepository.saveAndFlush(uRLType);

        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();

        // Update the uRLType using partial update
        URLType partialUpdatedURLType = new URLType();
        partialUpdatedURLType.setId(uRLType.getId());

        partialUpdatedURLType.name(UPDATED_NAME);

        restURLTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedURLType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedURLType))
            )
            .andExpect(status().isOk());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
        URLType testURLType = uRLTypeList.get(uRLTypeList.size() - 1);
        assertThat(testURLType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingURLType() throws Exception {
        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();
        uRLType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restURLTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uRLType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isBadRequest());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchURLType() throws Exception {
        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();
        uRLType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isBadRequest());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamURLType() throws Exception {
        int databaseSizeBeforeUpdate = uRLTypeRepository.findAll().size();
        uRLType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uRLType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the URLType in the database
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteURLType() throws Exception {
        // Initialize the database
        uRLTypeRepository.saveAndFlush(uRLType);

        int databaseSizeBeforeDelete = uRLTypeRepository.findAll().size();

        // Delete the uRLType
        restURLTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, uRLType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<URLType> uRLTypeList = uRLTypeRepository.findAll();
        assertThat(uRLTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
