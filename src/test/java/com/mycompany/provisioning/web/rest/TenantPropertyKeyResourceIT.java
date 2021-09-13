package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.TenantPropertyKey;
import com.mycompany.provisioning.repository.TenantPropertyKeyRepository;
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
 * Integration tests for the {@link TenantPropertyKeyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantPropertyKeyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tenant-property-keys";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantPropertyKeyRepository tenantPropertyKeyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantPropertyKeyMockMvc;

    private TenantPropertyKey tenantPropertyKey;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantPropertyKey createEntity(EntityManager em) {
        TenantPropertyKey tenantPropertyKey = new TenantPropertyKey().name(DEFAULT_NAME);
        return tenantPropertyKey;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantPropertyKey createUpdatedEntity(EntityManager em) {
        TenantPropertyKey tenantPropertyKey = new TenantPropertyKey().name(UPDATED_NAME);
        return tenantPropertyKey;
    }

    @BeforeEach
    public void initTest() {
        tenantPropertyKey = createEntity(em);
    }

    @Test
    @Transactional
    void createTenantPropertyKey() throws Exception {
        int databaseSizeBeforeCreate = tenantPropertyKeyRepository.findAll().size();
        // Create the TenantPropertyKey
        restTenantPropertyKeyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isCreated());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeCreate + 1);
        TenantPropertyKey testTenantPropertyKey = tenantPropertyKeyList.get(tenantPropertyKeyList.size() - 1);
        assertThat(testTenantPropertyKey.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createTenantPropertyKeyWithExistingId() throws Exception {
        // Create the TenantPropertyKey with an existing ID
        tenantPropertyKey.setId(1L);

        int databaseSizeBeforeCreate = tenantPropertyKeyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantPropertyKeyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantPropertyKeyRepository.findAll().size();
        // set the field null
        tenantPropertyKey.setName(null);

        // Create the TenantPropertyKey, which fails.

        restTenantPropertyKeyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isBadRequest());

        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantPropertyKeys() throws Exception {
        // Initialize the database
        tenantPropertyKeyRepository.saveAndFlush(tenantPropertyKey);

        // Get all the tenantPropertyKeyList
        restTenantPropertyKeyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantPropertyKey.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTenantPropertyKey() throws Exception {
        // Initialize the database
        tenantPropertyKeyRepository.saveAndFlush(tenantPropertyKey);

        // Get the tenantPropertyKey
        restTenantPropertyKeyMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantPropertyKey.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantPropertyKey.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTenantPropertyKey() throws Exception {
        // Get the tenantPropertyKey
        restTenantPropertyKeyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTenantPropertyKey() throws Exception {
        // Initialize the database
        tenantPropertyKeyRepository.saveAndFlush(tenantPropertyKey);

        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();

        // Update the tenantPropertyKey
        TenantPropertyKey updatedTenantPropertyKey = tenantPropertyKeyRepository.findById(tenantPropertyKey.getId()).get();
        // Disconnect from session so that the updates on updatedTenantPropertyKey are not directly saved in db
        em.detach(updatedTenantPropertyKey);
        updatedTenantPropertyKey.name(UPDATED_NAME);

        restTenantPropertyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTenantPropertyKey.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTenantPropertyKey))
            )
            .andExpect(status().isOk());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
        TenantPropertyKey testTenantPropertyKey = tenantPropertyKeyList.get(tenantPropertyKeyList.size() - 1);
        assertThat(testTenantPropertyKey.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingTenantPropertyKey() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();
        tenantPropertyKey.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantPropertyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantPropertyKey.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantPropertyKey() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();
        tenantPropertyKey.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyKeyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantPropertyKey() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();
        tenantPropertyKey.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyKeyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantPropertyKeyWithPatch() throws Exception {
        // Initialize the database
        tenantPropertyKeyRepository.saveAndFlush(tenantPropertyKey);

        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();

        // Update the tenantPropertyKey using partial update
        TenantPropertyKey partialUpdatedTenantPropertyKey = new TenantPropertyKey();
        partialUpdatedTenantPropertyKey.setId(tenantPropertyKey.getId());

        partialUpdatedTenantPropertyKey.name(UPDATED_NAME);

        restTenantPropertyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantPropertyKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantPropertyKey))
            )
            .andExpect(status().isOk());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
        TenantPropertyKey testTenantPropertyKey = tenantPropertyKeyList.get(tenantPropertyKeyList.size() - 1);
        assertThat(testTenantPropertyKey.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateTenantPropertyKeyWithPatch() throws Exception {
        // Initialize the database
        tenantPropertyKeyRepository.saveAndFlush(tenantPropertyKey);

        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();

        // Update the tenantPropertyKey using partial update
        TenantPropertyKey partialUpdatedTenantPropertyKey = new TenantPropertyKey();
        partialUpdatedTenantPropertyKey.setId(tenantPropertyKey.getId());

        partialUpdatedTenantPropertyKey.name(UPDATED_NAME);

        restTenantPropertyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantPropertyKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantPropertyKey))
            )
            .andExpect(status().isOk());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
        TenantPropertyKey testTenantPropertyKey = tenantPropertyKeyList.get(tenantPropertyKeyList.size() - 1);
        assertThat(testTenantPropertyKey.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingTenantPropertyKey() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();
        tenantPropertyKey.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantPropertyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantPropertyKey.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantPropertyKey() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();
        tenantPropertyKey.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantPropertyKey() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyKeyRepository.findAll().size();
        tenantPropertyKey.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyKeyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantPropertyKey))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantPropertyKey in the database
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantPropertyKey() throws Exception {
        // Initialize the database
        tenantPropertyKeyRepository.saveAndFlush(tenantPropertyKey);

        int databaseSizeBeforeDelete = tenantPropertyKeyRepository.findAll().size();

        // Delete the tenantPropertyKey
        restTenantPropertyKeyMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantPropertyKey.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TenantPropertyKey> tenantPropertyKeyList = tenantPropertyKeyRepository.findAll();
        assertThat(tenantPropertyKeyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
