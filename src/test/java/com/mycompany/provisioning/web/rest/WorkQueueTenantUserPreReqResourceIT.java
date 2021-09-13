package com.mycompany.provisioning.web.rest;

import static com.mycompany.provisioning.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkQueueTenantUserPreReq;
import com.mycompany.provisioning.repository.WorkQueueTenantUserPreReqRepository;
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
 * Integration tests for the {@link WorkQueueTenantUserPreReqResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkQueueTenantUserPreReqResourceIT {

    private static final BigDecimal DEFAULT_PRE_REQUISITE_ITEM = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRE_REQUISITE_ITEM = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-queue-tenant-user-pre-reqs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkQueueTenantUserPreReqRepository workQueueTenantUserPreReqRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkQueueTenantUserPreReqMockMvc;

    private WorkQueueTenantUserPreReq workQueueTenantUserPreReq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantUserPreReq createEntity(EntityManager em) {
        WorkQueueTenantUserPreReq workQueueTenantUserPreReq = new WorkQueueTenantUserPreReq().preRequisiteItem(DEFAULT_PRE_REQUISITE_ITEM);
        return workQueueTenantUserPreReq;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantUserPreReq createUpdatedEntity(EntityManager em) {
        WorkQueueTenantUserPreReq workQueueTenantUserPreReq = new WorkQueueTenantUserPreReq().preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);
        return workQueueTenantUserPreReq;
    }

    @BeforeEach
    public void initTest() {
        workQueueTenantUserPreReq = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeCreate = workQueueTenantUserPreReqRepository.findAll().size();
        // Create the WorkQueueTenantUserPreReq
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isCreated());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeCreate + 1);
        WorkQueueTenantUserPreReq testWorkQueueTenantUserPreReq = workQueueTenantUserPreReqList.get(
            workQueueTenantUserPreReqList.size() - 1
        );
        assertThat(testWorkQueueTenantUserPreReq.getPreRequisiteItem()).isEqualByComparingTo(DEFAULT_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void createWorkQueueTenantUserPreReqWithExistingId() throws Exception {
        // Create the WorkQueueTenantUserPreReq with an existing ID
        workQueueTenantUserPreReq.setId(1L);

        int databaseSizeBeforeCreate = workQueueTenantUserPreReqRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPreRequisiteItemIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantUserPreReqRepository.findAll().size();
        // set the field null
        workQueueTenantUserPreReq.setPreRequisiteItem(null);

        // Create the WorkQueueTenantUserPreReq, which fails.

        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkQueueTenantUserPreReqs() throws Exception {
        // Initialize the database
        workQueueTenantUserPreReqRepository.saveAndFlush(workQueueTenantUserPreReq);

        // Get all the workQueueTenantUserPreReqList
        restWorkQueueTenantUserPreReqMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workQueueTenantUserPreReq.getId().intValue())))
            .andExpect(jsonPath("$.[*].preRequisiteItem").value(hasItem(sameNumber(DEFAULT_PRE_REQUISITE_ITEM))));
    }

    @Test
    @Transactional
    void getWorkQueueTenantUserPreReq() throws Exception {
        // Initialize the database
        workQueueTenantUserPreReqRepository.saveAndFlush(workQueueTenantUserPreReq);

        // Get the workQueueTenantUserPreReq
        restWorkQueueTenantUserPreReqMockMvc
            .perform(get(ENTITY_API_URL_ID, workQueueTenantUserPreReq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workQueueTenantUserPreReq.getId().intValue()))
            .andExpect(jsonPath("$.preRequisiteItem").value(sameNumber(DEFAULT_PRE_REQUISITE_ITEM)));
    }

    @Test
    @Transactional
    void getNonExistingWorkQueueTenantUserPreReq() throws Exception {
        // Get the workQueueTenantUserPreReq
        restWorkQueueTenantUserPreReqMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkQueueTenantUserPreReq() throws Exception {
        // Initialize the database
        workQueueTenantUserPreReqRepository.saveAndFlush(workQueueTenantUserPreReq);

        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();

        // Update the workQueueTenantUserPreReq
        WorkQueueTenantUserPreReq updatedWorkQueueTenantUserPreReq = workQueueTenantUserPreReqRepository
            .findById(workQueueTenantUserPreReq.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkQueueTenantUserPreReq are not directly saved in db
        em.detach(updatedWorkQueueTenantUserPreReq);
        updatedWorkQueueTenantUserPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkQueueTenantUserPreReq.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkQueueTenantUserPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUserPreReq testWorkQueueTenantUserPreReq = workQueueTenantUserPreReqList.get(
            workQueueTenantUserPreReqList.size() - 1
        );
        assertThat(testWorkQueueTenantUserPreReq.getPreRequisiteItem()).isEqualTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void putNonExistingWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();
        workQueueTenantUserPreReq.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workQueueTenantUserPreReq.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();
        workQueueTenantUserPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();
        workQueueTenantUserPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkQueueTenantUserPreReqWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantUserPreReqRepository.saveAndFlush(workQueueTenantUserPreReq);

        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();

        // Update the workQueueTenantUserPreReq using partial update
        WorkQueueTenantUserPreReq partialUpdatedWorkQueueTenantUserPreReq = new WorkQueueTenantUserPreReq();
        partialUpdatedWorkQueueTenantUserPreReq.setId(workQueueTenantUserPreReq.getId());

        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantUserPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantUserPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUserPreReq testWorkQueueTenantUserPreReq = workQueueTenantUserPreReqList.get(
            workQueueTenantUserPreReqList.size() - 1
        );
        assertThat(testWorkQueueTenantUserPreReq.getPreRequisiteItem()).isEqualByComparingTo(DEFAULT_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void fullUpdateWorkQueueTenantUserPreReqWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantUserPreReqRepository.saveAndFlush(workQueueTenantUserPreReq);

        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();

        // Update the workQueueTenantUserPreReq using partial update
        WorkQueueTenantUserPreReq partialUpdatedWorkQueueTenantUserPreReq = new WorkQueueTenantUserPreReq();
        partialUpdatedWorkQueueTenantUserPreReq.setId(workQueueTenantUserPreReq.getId());

        partialUpdatedWorkQueueTenantUserPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantUserPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantUserPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUserPreReq testWorkQueueTenantUserPreReq = workQueueTenantUserPreReqList.get(
            workQueueTenantUserPreReqList.size() - 1
        );
        assertThat(testWorkQueueTenantUserPreReq.getPreRequisiteItem()).isEqualByComparingTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void patchNonExistingWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();
        workQueueTenantUserPreReq.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workQueueTenantUserPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();
        workQueueTenantUserPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkQueueTenantUserPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserPreReqRepository.findAll().size();
        workQueueTenantUserPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUserPreReq))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantUserPreReq in the database
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkQueueTenantUserPreReq() throws Exception {
        // Initialize the database
        workQueueTenantUserPreReqRepository.saveAndFlush(workQueueTenantUserPreReq);

        int databaseSizeBeforeDelete = workQueueTenantUserPreReqRepository.findAll().size();

        // Delete the workQueueTenantUserPreReq
        restWorkQueueTenantUserPreReqMockMvc
            .perform(delete(ENTITY_API_URL_ID, workQueueTenantUserPreReq.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkQueueTenantUserPreReq> workQueueTenantUserPreReqList = workQueueTenantUserPreReqRepository.findAll();
        assertThat(workQueueTenantUserPreReqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
