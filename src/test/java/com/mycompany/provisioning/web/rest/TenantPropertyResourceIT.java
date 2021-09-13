package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.TenantProperty;
import com.mycompany.provisioning.repository.TenantPropertyRepository;
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
 * Integration tests for the {@link TenantPropertyResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantPropertyResourceIT {

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tenant-properties";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantPropertyRepository tenantPropertyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantPropertyMockMvc;

    private TenantProperty tenantProperty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantProperty createEntity(EntityManager em) {
        TenantProperty tenantProperty = new TenantProperty().value(DEFAULT_VALUE);
        return tenantProperty;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantProperty createUpdatedEntity(EntityManager em) {
        TenantProperty tenantProperty = new TenantProperty().value(UPDATED_VALUE);
        return tenantProperty;
    }

    @BeforeEach
    public void initTest() {
        tenantProperty = createEntity(em);
    }

    @Test
    @Transactional
    void createTenantProperty() throws Exception {
        int databaseSizeBeforeCreate = tenantPropertyRepository.findAll().size();
        // Create the TenantProperty
        restTenantPropertyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isCreated());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeCreate + 1);
        TenantProperty testTenantProperty = tenantPropertyList.get(tenantPropertyList.size() - 1);
        assertThat(testTenantProperty.getValue()).isEqualTo(DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void createTenantPropertyWithExistingId() throws Exception {
        // Create the TenantProperty with an existing ID
        tenantProperty.setId(1L);

        int databaseSizeBeforeCreate = tenantPropertyRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantPropertyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantPropertyRepository.findAll().size();
        // set the field null
        tenantProperty.setValue(null);

        // Create the TenantProperty, which fails.

        restTenantPropertyMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isBadRequest());

        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantProperties() throws Exception {
        // Initialize the database
        tenantPropertyRepository.saveAndFlush(tenantProperty);

        // Get all the tenantPropertyList
        restTenantPropertyMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantProperty.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)));
    }

    @Test
    @Transactional
    void getTenantProperty() throws Exception {
        // Initialize the database
        tenantPropertyRepository.saveAndFlush(tenantProperty);

        // Get the tenantProperty
        restTenantPropertyMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantProperty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantProperty.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingTenantProperty() throws Exception {
        // Get the tenantProperty
        restTenantPropertyMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTenantProperty() throws Exception {
        // Initialize the database
        tenantPropertyRepository.saveAndFlush(tenantProperty);

        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();

        // Update the tenantProperty
        TenantProperty updatedTenantProperty = tenantPropertyRepository.findById(tenantProperty.getId()).get();
        // Disconnect from session so that the updates on updatedTenantProperty are not directly saved in db
        em.detach(updatedTenantProperty);
        updatedTenantProperty.value(UPDATED_VALUE);

        restTenantPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTenantProperty.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTenantProperty))
            )
            .andExpect(status().isOk());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
        TenantProperty testTenantProperty = tenantPropertyList.get(tenantPropertyList.size() - 1);
        assertThat(testTenantProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingTenantProperty() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();
        tenantProperty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantProperty.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantProperty() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();
        tenantProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantProperty() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();
        tenantProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantPropertyWithPatch() throws Exception {
        // Initialize the database
        tenantPropertyRepository.saveAndFlush(tenantProperty);

        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();

        // Update the tenantProperty using partial update
        TenantProperty partialUpdatedTenantProperty = new TenantProperty();
        partialUpdatedTenantProperty.setId(tenantProperty.getId());

        partialUpdatedTenantProperty.value(UPDATED_VALUE);

        restTenantPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantProperty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantProperty))
            )
            .andExpect(status().isOk());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
        TenantProperty testTenantProperty = tenantPropertyList.get(tenantPropertyList.size() - 1);
        assertThat(testTenantProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateTenantPropertyWithPatch() throws Exception {
        // Initialize the database
        tenantPropertyRepository.saveAndFlush(tenantProperty);

        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();

        // Update the tenantProperty using partial update
        TenantProperty partialUpdatedTenantProperty = new TenantProperty();
        partialUpdatedTenantProperty.setId(tenantProperty.getId());

        partialUpdatedTenantProperty.value(UPDATED_VALUE);

        restTenantPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantProperty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantProperty))
            )
            .andExpect(status().isOk());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
        TenantProperty testTenantProperty = tenantPropertyList.get(tenantPropertyList.size() - 1);
        assertThat(testTenantProperty.getValue()).isEqualTo(UPDATED_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingTenantProperty() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();
        tenantProperty.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantProperty.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantProperty() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();
        tenantProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantProperty() throws Exception {
        int databaseSizeBeforeUpdate = tenantPropertyRepository.findAll().size();
        tenantProperty.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantPropertyMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantProperty))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantProperty in the database
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantProperty() throws Exception {
        // Initialize the database
        tenantPropertyRepository.saveAndFlush(tenantProperty);

        int databaseSizeBeforeDelete = tenantPropertyRepository.findAll().size();

        // Delete the tenantProperty
        restTenantPropertyMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantProperty.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TenantProperty> tenantPropertyList = tenantPropertyRepository.findAll();
        assertThat(tenantPropertyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
