package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkQueueTenantUserData;
import com.mycompany.provisioning.repository.WorkQueueTenantUserDataRepository;
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
 * Integration tests for the {@link WorkQueueTenantUserDataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkQueueTenantUserDataResourceIT {

    private static final String DEFAULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/work-queue-tenant-user-data";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkQueueTenantUserDataRepository workQueueTenantUserDataRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkQueueTenantUserDataMockMvc;

    private WorkQueueTenantUserData workQueueTenantUserData;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantUserData createEntity(EntityManager em) {
        WorkQueueTenantUserData workQueueTenantUserData = new WorkQueueTenantUserData().data(DEFAULT_DATA).type(DEFAULT_TYPE);
        return workQueueTenantUserData;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantUserData createUpdatedEntity(EntityManager em) {
        WorkQueueTenantUserData workQueueTenantUserData = new WorkQueueTenantUserData().data(UPDATED_DATA).type(UPDATED_TYPE);
        return workQueueTenantUserData;
    }

    @BeforeEach
    public void initTest() {
        workQueueTenantUserData = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeCreate = workQueueTenantUserDataRepository.findAll().size();
        // Create the WorkQueueTenantUserData
        restWorkQueueTenantUserDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isCreated());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeCreate + 1);
        WorkQueueTenantUserData testWorkQueueTenantUserData = workQueueTenantUserDataList.get(workQueueTenantUserDataList.size() - 1);
        assertThat(testWorkQueueTenantUserData.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testWorkQueueTenantUserData.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createWorkQueueTenantUserDataWithExistingId() throws Exception {
        // Create the WorkQueueTenantUserData with an existing ID
        workQueueTenantUserData.setId(1L);

        int databaseSizeBeforeCreate = workQueueTenantUserDataRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkQueueTenantUserDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantUserDataRepository.findAll().size();
        // set the field null
        workQueueTenantUserData.setData(null);

        // Create the WorkQueueTenantUserData, which fails.

        restWorkQueueTenantUserDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantUserDataRepository.findAll().size();
        // set the field null
        workQueueTenantUserData.setType(null);

        // Create the WorkQueueTenantUserData, which fails.

        restWorkQueueTenantUserDataMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkQueueTenantUserData() throws Exception {
        // Initialize the database
        workQueueTenantUserDataRepository.saveAndFlush(workQueueTenantUserData);

        // Get all the workQueueTenantUserDataList
        restWorkQueueTenantUserDataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workQueueTenantUserData.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getWorkQueueTenantUserData() throws Exception {
        // Initialize the database
        workQueueTenantUserDataRepository.saveAndFlush(workQueueTenantUserData);

        // Get the workQueueTenantUserData
        restWorkQueueTenantUserDataMockMvc
            .perform(get(ENTITY_API_URL_ID, workQueueTenantUserData.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workQueueTenantUserData.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingWorkQueueTenantUserData() throws Exception {
        // Get the workQueueTenantUserData
        restWorkQueueTenantUserDataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkQueueTenantUserData() throws Exception {
        // Initialize the database
        workQueueTenantUserDataRepository.saveAndFlush(workQueueTenantUserData);

        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();

        // Update the workQueueTenantUserData
        WorkQueueTenantUserData updatedWorkQueueTenantUserData = workQueueTenantUserDataRepository
            .findById(workQueueTenantUserData.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkQueueTenantUserData are not directly saved in db
        em.detach(updatedWorkQueueTenantUserData);
        updatedWorkQueueTenantUserData.data(UPDATED_DATA).type(UPDATED_TYPE);

        restWorkQueueTenantUserDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkQueueTenantUserData.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkQueueTenantUserData))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUserData testWorkQueueTenantUserData = workQueueTenantUserDataList.get(workQueueTenantUserDataList.size() - 1);
        assertThat(testWorkQueueTenantUserData.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testWorkQueueTenantUserData.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();
        workQueueTenantUserData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workQueueTenantUserData.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();
        workQueueTenantUserData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserDataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();
        workQueueTenantUserData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserDataMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkQueueTenantUserDataWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantUserDataRepository.saveAndFlush(workQueueTenantUserData);

        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();

        // Update the workQueueTenantUserData using partial update
        WorkQueueTenantUserData partialUpdatedWorkQueueTenantUserData = new WorkQueueTenantUserData();
        partialUpdatedWorkQueueTenantUserData.setId(workQueueTenantUserData.getId());

        restWorkQueueTenantUserDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantUserData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantUserData))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUserData testWorkQueueTenantUserData = workQueueTenantUserDataList.get(workQueueTenantUserDataList.size() - 1);
        assertThat(testWorkQueueTenantUserData.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testWorkQueueTenantUserData.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateWorkQueueTenantUserDataWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantUserDataRepository.saveAndFlush(workQueueTenantUserData);

        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();

        // Update the workQueueTenantUserData using partial update
        WorkQueueTenantUserData partialUpdatedWorkQueueTenantUserData = new WorkQueueTenantUserData();
        partialUpdatedWorkQueueTenantUserData.setId(workQueueTenantUserData.getId());

        partialUpdatedWorkQueueTenantUserData.data(UPDATED_DATA).type(UPDATED_TYPE);

        restWorkQueueTenantUserDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantUserData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantUserData))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUserData testWorkQueueTenantUserData = workQueueTenantUserDataList.get(workQueueTenantUserDataList.size() - 1);
        assertThat(testWorkQueueTenantUserData.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testWorkQueueTenantUserData.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();
        workQueueTenantUserData.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workQueueTenantUserData.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();
        workQueueTenantUserData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserDataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkQueueTenantUserData() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserDataRepository.findAll().size();
        workQueueTenantUserData.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserDataMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserData))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantUserData in the database
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkQueueTenantUserData() throws Exception {
        // Initialize the database
        workQueueTenantUserDataRepository.saveAndFlush(workQueueTenantUserData);

        int databaseSizeBeforeDelete = workQueueTenantUserDataRepository.findAll().size();

        // Delete the workQueueTenantUserData
        restWorkQueueTenantUserDataMockMvc
            .perform(delete(ENTITY_API_URL_ID, workQueueTenantUserData.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkQueueTenantUserData> workQueueTenantUserDataList = workQueueTenantUserDataRepository.findAll();
        assertThat(workQueueTenantUserDataList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
