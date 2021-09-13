package com.mycompany.provisioning.web.rest;

import static com.mycompany.provisioning.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkTemplateItemPreReq;
import com.mycompany.provisioning.repository.WorkTemplateItemPreReqRepository;
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
 * Integration tests for the {@link WorkTemplateItemPreReqResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkTemplateItemPreReqResourceIT {

    private static final BigDecimal DEFAULT_PRE_REQUISITE_ITEM = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRE_REQUISITE_ITEM = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-template-item-pre-reqs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkTemplateItemPreReqRepository workTemplateItemPreReqRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkTemplateItemPreReqMockMvc;

    private WorkTemplateItemPreReq workTemplateItemPreReq;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkTemplateItemPreReq createEntity(EntityManager em) {
        WorkTemplateItemPreReq workTemplateItemPreReq = new WorkTemplateItemPreReq().preRequisiteItem(DEFAULT_PRE_REQUISITE_ITEM);
        return workTemplateItemPreReq;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkTemplateItemPreReq createUpdatedEntity(EntityManager em) {
        WorkTemplateItemPreReq workTemplateItemPreReq = new WorkTemplateItemPreReq().preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);
        return workTemplateItemPreReq;
    }

    @BeforeEach
    public void initTest() {
        workTemplateItemPreReq = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeCreate = workTemplateItemPreReqRepository.findAll().size();
        // Create the WorkTemplateItemPreReq
        restWorkTemplateItemPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isCreated());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeCreate + 1);
        WorkTemplateItemPreReq testWorkTemplateItemPreReq = workTemplateItemPreReqList.get(workTemplateItemPreReqList.size() - 1);
        assertThat(testWorkTemplateItemPreReq.getPreRequisiteItem()).isEqualByComparingTo(DEFAULT_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void createWorkTemplateItemPreReqWithExistingId() throws Exception {
        // Create the WorkTemplateItemPreReq with an existing ID
        workTemplateItemPreReq.setId(1L);

        int databaseSizeBeforeCreate = workTemplateItemPreReqRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTemplateItemPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPreRequisiteItemIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTemplateItemPreReqRepository.findAll().size();
        // set the field null
        workTemplateItemPreReq.setPreRequisiteItem(null);

        // Create the WorkTemplateItemPreReq, which fails.

        restWorkTemplateItemPreReqMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isBadRequest());

        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkTemplateItemPreReqs() throws Exception {
        // Initialize the database
        workTemplateItemPreReqRepository.saveAndFlush(workTemplateItemPreReq);

        // Get all the workTemplateItemPreReqList
        restWorkTemplateItemPreReqMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workTemplateItemPreReq.getId().intValue())))
            .andExpect(jsonPath("$.[*].preRequisiteItem").value(hasItem(sameNumber(DEFAULT_PRE_REQUISITE_ITEM))));
    }

    @Test
    @Transactional
    void getWorkTemplateItemPreReq() throws Exception {
        // Initialize the database
        workTemplateItemPreReqRepository.saveAndFlush(workTemplateItemPreReq);

        // Get the workTemplateItemPreReq
        restWorkTemplateItemPreReqMockMvc
            .perform(get(ENTITY_API_URL_ID, workTemplateItemPreReq.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workTemplateItemPreReq.getId().intValue()))
            .andExpect(jsonPath("$.preRequisiteItem").value(sameNumber(DEFAULT_PRE_REQUISITE_ITEM)));
    }

    @Test
    @Transactional
    void getNonExistingWorkTemplateItemPreReq() throws Exception {
        // Get the workTemplateItemPreReq
        restWorkTemplateItemPreReqMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkTemplateItemPreReq() throws Exception {
        // Initialize the database
        workTemplateItemPreReqRepository.saveAndFlush(workTemplateItemPreReq);

        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();

        // Update the workTemplateItemPreReq
        WorkTemplateItemPreReq updatedWorkTemplateItemPreReq = workTemplateItemPreReqRepository
            .findById(workTemplateItemPreReq.getId())
            .get();
        // Disconnect from session so that the updates on updatedWorkTemplateItemPreReq are not directly saved in db
        em.detach(updatedWorkTemplateItemPreReq);
        updatedWorkTemplateItemPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkTemplateItemPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkTemplateItemPreReq.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkTemplateItemPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplateItemPreReq testWorkTemplateItemPreReq = workTemplateItemPreReqList.get(workTemplateItemPreReqList.size() - 1);
        assertThat(testWorkTemplateItemPreReq.getPreRequisiteItem()).isEqualTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void putNonExistingWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();
        workTemplateItemPreReq.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTemplateItemPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workTemplateItemPreReq.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();
        workTemplateItemPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemPreReqMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();
        workTemplateItemPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemPreReqMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkTemplateItemPreReqWithPatch() throws Exception {
        // Initialize the database
        workTemplateItemPreReqRepository.saveAndFlush(workTemplateItemPreReq);

        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();

        // Update the workTemplateItemPreReq using partial update
        WorkTemplateItemPreReq partialUpdatedWorkTemplateItemPreReq = new WorkTemplateItemPreReq();
        partialUpdatedWorkTemplateItemPreReq.setId(workTemplateItemPreReq.getId());

        restWorkTemplateItemPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkTemplateItemPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkTemplateItemPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplateItemPreReq testWorkTemplateItemPreReq = workTemplateItemPreReqList.get(workTemplateItemPreReqList.size() - 1);
        assertThat(testWorkTemplateItemPreReq.getPreRequisiteItem()).isEqualByComparingTo(DEFAULT_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void fullUpdateWorkTemplateItemPreReqWithPatch() throws Exception {
        // Initialize the database
        workTemplateItemPreReqRepository.saveAndFlush(workTemplateItemPreReq);

        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();

        // Update the workTemplateItemPreReq using partial update
        WorkTemplateItemPreReq partialUpdatedWorkTemplateItemPreReq = new WorkTemplateItemPreReq();
        partialUpdatedWorkTemplateItemPreReq.setId(workTemplateItemPreReq.getId());

        partialUpdatedWorkTemplateItemPreReq.preRequisiteItem(UPDATED_PRE_REQUISITE_ITEM);

        restWorkTemplateItemPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkTemplateItemPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkTemplateItemPreReq))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplateItemPreReq testWorkTemplateItemPreReq = workTemplateItemPreReqList.get(workTemplateItemPreReqList.size() - 1);
        assertThat(testWorkTemplateItemPreReq.getPreRequisiteItem()).isEqualByComparingTo(UPDATED_PRE_REQUISITE_ITEM);
    }

    @Test
    @Transactional
    void patchNonExistingWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();
        workTemplateItemPreReq.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTemplateItemPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workTemplateItemPreReq.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();
        workTemplateItemPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkTemplateItemPreReq() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemPreReqRepository.findAll().size();
        workTemplateItemPreReq.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemPreReqMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItemPreReq))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkTemplateItemPreReq in the database
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkTemplateItemPreReq() throws Exception {
        // Initialize the database
        workTemplateItemPreReqRepository.saveAndFlush(workTemplateItemPreReq);

        int databaseSizeBeforeDelete = workTemplateItemPreReqRepository.findAll().size();

        // Delete the workTemplateItemPreReq
        restWorkTemplateItemPreReqMockMvc
            .perform(delete(ENTITY_API_URL_ID, workTemplateItemPreReq.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkTemplateItemPreReq> workTemplateItemPreReqList = workTemplateItemPreReqRepository.findAll();
        assertThat(workTemplateItemPreReqList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
