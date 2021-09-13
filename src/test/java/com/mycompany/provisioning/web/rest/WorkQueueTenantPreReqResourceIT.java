package com.mycompany.provisioning.web.rest;

import static com.mycompany.provisioning.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkQueueTenantPreReq;
import com.mycompany.provisioning.repository.WorkQueueTenantPreReqRepository;
import java.math.BigDecimal;
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
 * Integration tests for the {@link WorkQueueTenantPreReqResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkQueueTenantPreReqResourceIT {

    private static final BigDecimal DEFAULT_PRE_REQUISITE_ITEM = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRE_REQUISITE_ITEM = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-queue-tenant-pre-reqs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkQueueTenantPreReqRepository workQueueTenantPreReqRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkQueueTenantPreReqMockMvc;

    private WorkQueueTenantPreReq workQueueTenantPreReq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantPreReq createEntity(EntityManager em) {
        WorkQueueTenantPreReq workQueueTenantPreReq = new WorkQueueTenantPreReq().preRequisiteItem(DEFAULT_PRE_REQUISITE_ITEM);
        return workQueueTenantPreReq;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantPreReq createUpdatedEntity(EntityManager em) {
        WorkQueueTenantPreReq workQueueTenantPreReq = new WorkQueueTenantPreReq().preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);
        return workQueueTenantPreReq;
    }

    @BeforeEach
    public void initTest() {
        workQueueTenantPreReq = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeCreate = workQueueTenantPreReqRepository.findAll().size();
        // Create the WorkQueueTenantPreReq
        restWorkQueueTenantPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isCreated());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeCreate + 1);
        WorkQueueTenantPreReq testWorkQueueTenantPreReq = workQueueTenantPreReqList.get(workQueueTenantPreReqList.size() - 1);
        assertThat(testWorkQueueTenantPreReq.getPreRequisiteItem()).isEqualByComparingTo(DEFAULT_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void createWorkQueueTenantPreReqWithExistingId() throws Exception {
        // Create the WorkQueueTenantPreReq with an existing ID
        workQueueTenantPreReq.setId(1L);

        int databaseSizeBeforeCreate = workQueueTenantPreReqRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkQueueTenantPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPreRequisiteItemIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantPreReqRepository.findAll().size();
        // set the field null
        workQueueTenantPreReq.setPreRequisiteItem(null);

        // Create the WorkQueueTenantPreReq, which fails.

        restWorkQueueTenantPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkQueueTenantPreReqs() throws Exception {
        // Initialize the database
        workQueueTenantPreReqRepository.saveAndFlush(workQueueTenantPreReq);

        // Get all the workQueueTenantPreReqList
        restWorkQueueTenantPreReqMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workQueueTenantPreReq.getId().intValue())))
            .andExpect(jsonPath("$.[*].preRequisiteItem").value(hasItem(sameNumber(DEFAULT_PRE_REQUISITE_ITEM))));
    }

    @Test
    @Transactional
    void getWorkQueueTenantPreReq() throws Exception {
        // Initialize the database
        workQueueTenantPreReqRepository.saveAndFlush(workQueueTenantPreReq);

        // Get the workQueueTenantPreReq
        restWorkQueueTenantPreReqMockMvc
            .perform(get(ENTITY_API_URL_ID, workQueueTenantPreReq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workQueueTenantPreReq.getId().intValue()))
            .andExpect(jsonPath("$.preRequisiteItem").value(sameNumber(DEFAULT_PRE_REQUISITE_ITEM)));
    }

    @Test
    @Transactional
    void getNonExistingWorkQueueTenantPreReq() throws Exception {
        // Get the workQueueTenantPreReq
        restWorkQueueTenantPreReqMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkQueueTenantPreReq() throws Exception {
        // Initialize the database
        workQueueTenantPreReqRepository.saveAndFlush(workQueueTenantPreReq);

        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();

        // Update the workQueueTenantPreReq
        WorkQueueTenantPreReq updatedWorkQueueTenantPreReq = workQueueTenantPreReqRepository.findById(workQueueTenantPreReq.getId()).get();
        // Disconnect from session so that the updates on updatedWorkQueueTenantPreReq are not directly saved in db
        em.detach(updatedWorkQueueTenantPreReq);
        updatedWorkQueueTenantPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkQueueTenantPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkQueueTenantPreReq.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkQueueTenantPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantPreReq testWorkQueueTenantPreReq = workQueueTenantPreReqList.get(workQueueTenantPreReqList.size() - 1);
        assertThat(testWorkQueueTenantPreReq.getPreRequisiteItem()).isEqualTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void putNonExistingWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();
        workQueueTenantPreReq.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workQueueTenantPreReq.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();
        workQueueTenantPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();
        workQueueTenantPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantPreReqMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkQueueTenantPreReqWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantPreReqRepository.saveAndFlush(workQueueTenantPreReq);

        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();

        // Update the workQueueTenantPreReq using partial update
        WorkQueueTenantPreReq partialUpdatedWorkQueueTenantPreReq = new WorkQueueTenantPreReq();
        partialUpdatedWorkQueueTenantPreReq.setId(workQueueTenantPreReq.getId());

        partialUpdatedWorkQueueTenantPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkQueueTenantPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantPreReq testWorkQueueTenantPreReq = workQueueTenantPreReqList.get(workQueueTenantPreReqList.size() - 1);
        assertThat(testWorkQueueTenantPreReq.getPreRequisiteItem()).isEqualByComparingTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void fullUpdateWorkQueueTenantPreReqWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantPreReqRepository.saveAndFlush(workQueueTenantPreReq);

        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();

        // Update the workQueueTenantPreReq using partial update
        WorkQueueTenantPreReq partialUpdatedWorkQueueTenantPreReq = new WorkQueueTenantPreReq();
        partialUpdatedWorkQueueTenantPreReq.setId(workQueueTenantPreReq.getId());

        partialUpdatedWorkQueueTenantPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkQueueTenantPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantPreReq testWorkQueueTenantPreReq = workQueueTenantPreReqList.get(workQueueTenantPreReqList.size() - 1);
        assertThat(testWorkQueueTenantPreReq.getPreRequisiteItem()).isEqualByComparingTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void patchNonExistingWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();
        workQueueTenantPreReq.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workQueueTenantPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();
        workQueueTenantPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkQueueTenantPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantPreReqRepository.findAll().size();
        workQueueTenantPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantPreReq))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantPreReq in the database
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkQueueTenantPreReq() throws Exception {
        // Initialize the database
        workQueueTenantPreReqRepository.saveAndFlush(workQueueTenantPreReq);

        int databaseSizeBeforeDelete = workQueueTenantPreReqRepository.findAll().size();

        // Delete the workQueueTenantPreReq
        restWorkQueueTenantPreReqMockMvc
            .perform(delete(ENTITY_API_URL_ID, workQueueTenantPreReq.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkQueueTenantPreReq> workQueueTenantPreReqList = workQueueTenantPreReqRepository.findAll();
        assertThat(workQueueTenantPreReqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
