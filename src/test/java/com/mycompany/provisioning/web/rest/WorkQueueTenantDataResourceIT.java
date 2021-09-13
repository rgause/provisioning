package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkQueueTenantData;
import com.mycompany.provisioning.repository.WorkQueueTenantDataRepository;
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
 * Integration tests for the {@link WorkQueueTenantDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkQueueTenantDataResourceIT {

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/work-queue-tenant-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkQueueTenantDataRepository workQueueTenantDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkQueueTenantDataMockMvc;

    private WorkQueueTenantData workQueueTenantData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantData createEntity(EntityManager em) {
        WorkQueueTenantData workQueueTenantData = new WorkQueueTenantData().data(DEFAULT_DATA).type(DEFAULT_TYPE);
        return workQueueTenantData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantData createUpdatedEntity(EntityManager em) {
        WorkQueueTenantData workQueueTenantData = new WorkQueueTenantData().data(UPDATED_DATA).type(UPDATED_TYPE);
        return workQueueTenantData;
    }

    @BeforeEach
    public void initTest() {
        workQueueTenantData = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeCreate = workQueueTenantDataRepository.findAll().size();
        // Create the WorkQueueTenantData
        restWorkQueueTenantDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isCreated());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeCreate + 1);
        WorkQueueTenantData testWorkQueueTenantData = workQueueTenantDataList.get(workQueueTenantDataList.size() - 1);
        assertThat(testWorkQueueTenantData.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testWorkQueueTenantData.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createWorkQueueTenantDataWithExistingId() throws Exception {
        // Create the WorkQueueTenantData with an existing ID
        workQueueTenantData.setId(1L);

        int databaseSizeBeforeCreate = workQueueTenantDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkQueueTenantDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantDataRepository.findAll().size();
        // set the field null
        workQueueTenantData.setData(null);

        // Create the WorkQueueTenantData, which fails.

        restWorkQueueTenantDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantDataRepository.findAll().size();
        // set the field null
        workQueueTenantData.setType(null);

        // Create the WorkQueueTenantData, which fails.

        restWorkQueueTenantDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkQueueTenantData() throws Exception {
        // Initialize the database
        workQueueTenantDataRepository.saveAndFlush(workQueueTenantData);

        // Get all the workQueueTenantDataList
        restWorkQueueTenantDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workQueueTenantData.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getWorkQueueTenantData() throws Exception {
        // Initialize the database
        workQueueTenantDataRepository.saveAndFlush(workQueueTenantData);

        // Get the workQueueTenantData
        restWorkQueueTenantDataMockMvc
            .perform(get(ENTITY_API_URL_ID, workQueueTenantData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workQueueTenantData.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingWorkQueueTenantData() throws Exception {
        // Get the workQueueTenantData
        restWorkQueueTenantDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkQueueTenantData() throws Exception {
        // Initialize the database
        workQueueTenantDataRepository.saveAndFlush(workQueueTenantData);

        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();

        // Update the workQueueTenantData
        WorkQueueTenantData updatedWorkQueueTenantData = workQueueTenantDataRepository.findById(workQueueTenantData.getId()).get();
        // Disconnect from session so that the updates on updatedWorkQueueTenantData are not directly saved in db
        em.detach(updatedWorkQueueTenantData);
        updatedWorkQueueTenantData.data(UPDATED_DATA).type(UPDATED_TYPE);

        restWorkQueueTenantDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkQueueTenantData.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkQueueTenantData))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantData testWorkQueueTenantData = workQueueTenantDataList.get(workQueueTenantDataList.size() - 1);
        assertThat(testWorkQueueTenantData.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testWorkQueueTenantData.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();
        workQueueTenantData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workQueueTenantData.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();
        workQueueTenantData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();
        workQueueTenantData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkQueueTenantDataWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantDataRepository.saveAndFlush(workQueueTenantData);

        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();

        // Update the workQueueTenantData using partial update
        WorkQueueTenantData partialUpdatedWorkQueueTenantData = new WorkQueueTenantData();
        partialUpdatedWorkQueueTenantData.setId(workQueueTenantData.getId());

        partialUpdatedWorkQueueTenantData.type(UPDATED_TYPE);

        restWorkQueueTenantDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantData))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantData testWorkQueueTenantData = workQueueTenantDataList.get(workQueueTenantDataList.size() - 1);
        assertThat(testWorkQueueTenantData.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testWorkQueueTenantData.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkQueueTenantDataWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantDataRepository.saveAndFlush(workQueueTenantData);

        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();

        // Update the workQueueTenantData using partial update
        WorkQueueTenantData partialUpdatedWorkQueueTenantData = new WorkQueueTenantData();
        partialUpdatedWorkQueueTenantData.setId(workQueueTenantData.getId());

        partialUpdatedWorkQueueTenantData.data(UPDATED_DATA).type(UPDATED_TYPE);

        restWorkQueueTenantDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantData))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantData testWorkQueueTenantData = workQueueTenantDataList.get(workQueueTenantDataList.size() - 1);
        assertThat(testWorkQueueTenantData.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testWorkQueueTenantData.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();
        workQueueTenantData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workQueueTenantData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();
        workQueueTenantData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkQueueTenantData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantDataRepository.findAll().size();
        workQueueTenantData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantData in the database
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkQueueTenantData() throws Exception {
        // Initialize the database
        workQueueTenantDataRepository.saveAndFlush(workQueueTenantData);

        int databaseSizeBeforeDelete = workQueueTenantDataRepository.findAll().size();

        // Delete the workQueueTenantData
        restWorkQueueTenantDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, workQueueTenantData.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkQueueTenantData> workQueueTenantDataList = workQueueTenantDataRepository.findAll();
        assertThat(workQueueTenantDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
