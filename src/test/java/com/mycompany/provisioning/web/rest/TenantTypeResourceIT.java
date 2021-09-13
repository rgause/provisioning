package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.TenantType;
import com.mycompany.provisioning.repository.TenantTypeRepository;
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
 * Integration tests for the {@link TenantTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tenant-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantTypeRepository tenantTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantTypeMockMvc;

    private TenantType tenantType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantType createEntity(EntityManager em) {
        TenantType tenantType = new TenantType().name(DEFAULT_NAME);
        return tenantType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantType createUpdatedEntity(EntityManager em) {
        TenantType tenantType = new TenantType().name(UPDATED_NAME);
        return tenantType;
    }

    @BeforeEach
    public void initTest() {
        tenantType = createEntity(em);
    }

    @Test
    @Transactional
    void createTenantType() throws Exception {
        int databaseSizeBeforeCreate = tenantTypeRepository.findAll().size();
        // Create the TenantType
        restTenantTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isCreated());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeCreate + 1);
        TenantType testTenantType = tenantTypeList.get(tenantTypeList.size() - 1);
        assertThat(testTenantType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTenantTypeWithExistingId() throws Exception {
        // Create the TenantType with an existing ID
        tenantType.setId(1L);

        int databaseSizeBeforeCreate = tenantTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantTypeRepository.findAll().size();
        // set the field null
        tenantType.setName(null);

        // Create the TenantType, which fails.

        restTenantTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isBadRequest());

        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantTypes() throws Exception {
        // Initialize the database
        tenantTypeRepository.saveAndFlush(tenantType);

        // Get all the tenantTypeList
        restTenantTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTenantType() throws Exception {
        // Initialize the database
        tenantTypeRepository.saveAndFlush(tenantType);

        // Get the tenantType
        restTenantTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTenantType() throws Exception {
        // Get the tenantType
        restTenantTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTenantType() throws Exception {
        // Initialize the database
        tenantTypeRepository.saveAndFlush(tenantType);

        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();

        // Update the tenantType
        TenantType updatedTenantType = tenantTypeRepository.findById(tenantType.getId()).get();
        // Disconnect from session so that the updates on updatedTenantType are not directly saved in db
        em.detach(updatedTenantType);
        updatedTenantType.name(UPDATED_NAME);

        restTenantTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTenantType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTenantType))
            )
            .andExpect(status().isOk());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
        TenantType testTenantType = tenantTypeList.get(tenantTypeList.size() - 1);
        assertThat(testTenantType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTenantType() throws Exception {
        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();
        tenantType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantType.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantType() throws Exception {
        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();
        tenantType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantType() throws Exception {
        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();
        tenantType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantTypeWithPatch() throws Exception {
        // Initialize the database
        tenantTypeRepository.saveAndFlush(tenantType);

        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();

        // Update the tenantType using partial update
        TenantType partialUpdatedTenantType = new TenantType();
        partialUpdatedTenantType.setId(tenantType.getId());

        restTenantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantType))
            )
            .andExpect(status().isOk());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
        TenantType testTenantType = tenantTypeList.get(tenantTypeList.size() - 1);
        assertThat(testTenantType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTenantTypeWithPatch() throws Exception {
        // Initialize the database
        tenantTypeRepository.saveAndFlush(tenantType);

        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();

        // Update the tenantType using partial update
        TenantType partialUpdatedTenantType = new TenantType();
        partialUpdatedTenantType.setId(tenantType.getId());

        partialUpdatedTenantType.name(UPDATED_NAME);

        restTenantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantType))
            )
            .andExpect(status().isOk());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
        TenantType testTenantType = tenantTypeList.get(tenantTypeList.size() - 1);
        assertThat(testTenantType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTenantType() throws Exception {
        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();
        tenantType.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantType() throws Exception {
        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();
        tenantType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantType() throws Exception {
        int databaseSizeBeforeUpdate = tenantTypeRepository.findAll().size();
        tenantType.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantType in the database
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantType() throws Exception {
        // Initialize the database
        tenantTypeRepository.saveAndFlush(tenantType);

        int databaseSizeBeforeDelete = tenantTypeRepository.findAll().size();

        // Delete the tenantType
        restTenantTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TenantType> tenantTypeList = tenantTypeRepository.findAll();
        assertThat(tenantTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
