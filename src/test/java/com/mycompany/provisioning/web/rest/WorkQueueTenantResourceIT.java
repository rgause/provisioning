package com.mycompany.provisioning.web.rest;

import static com.mycompany.provisioning.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkQueueTenant;
import com.mycompany.provisioning.repository.WorkQueueTenantRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link WorkQueueTenantResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkQueueTenantResourceIT {

    private static final String DEFAULT_TASK = "AAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUIRED_TO_COMPLETE = false;
    private static final Boolean UPDATED_REQUIRED_TO_COMPLETE = true;

    private static final LocalDate DEFAULT_DATE_CREATED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CREATED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_CANCELLED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_CANCELLED = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_COMPLETED = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_COMPLETED = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_SEQUENCE_ORDER = new BigDecimal(1);
    private static final BigDecimal UPDATED_SEQUENCE_ORDER = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-queue-tenants";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkQueueTenantRepository workQueueTenantRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkQueueTenantMockMvc;

    private WorkQueueTenant workQueueTenant;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenant createEntity(EntityManager em) {
        WorkQueueTenant workQueueTenant = new WorkQueueTenant()
            .task(DEFAULT_TASK)
            .requiredToComplete(DEFAULT_REQUIRED_TO_COMPLETE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateCancelled(DEFAULT_DATE_CANCELLED)
            .dateCompleted(DEFAULT_DATE_COMPLETED)
            .sequenceOrder(DEFAULT_SEQUENCE_ORDER);
        return workQueueTenant;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenant createUpdatedEntity(EntityManager em) {
        WorkQueueTenant workQueueTenant = new WorkQueueTenant()
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCancelled(UPDATED_DATE_CANCELLED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);
        return workQueueTenant;
    }

    @BeforeEach
    public void initTest() {
        workQueueTenant = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkQueueTenant() throws Exception {
        int databaseSizeBeforeCreate = workQueueTenantRepository.findAll().size();
        // Create the WorkQueueTenant
        restWorkQueueTenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isCreated());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeCreate + 1);
        WorkQueueTenant testWorkQueueTenant = workQueueTenantList.get(workQueueTenantList.size() - 1);
        assertThat(testWorkQueueTenant.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testWorkQueueTenant.getRequiredToComplete()).isEqualTo(DEFAULT_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenant.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testWorkQueueTenant.getDateCancelled()).isEqualTo(DEFAULT_DATE_CANCELLED);
        assertThat(testWorkQueueTenant.getDateCompleted()).isEqualTo(DEFAULT_DATE_COMPLETED);
        assertThat(testWorkQueueTenant.getSequenceOrder()).isEqualByComparingTo(DEFAULT_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void createWorkQueueTenantWithExistingId() throws Exception {
        // Create the WorkQueueTenant with an existing ID
        workQueueTenant.setId(1L);

        int databaseSizeBeforeCreate = workQueueTenantRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkQueueTenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantRepository.findAll().size();
        // set the field null
        workQueueTenant.setTask(null);

        // Create the WorkQueueTenant, which fails.

        restWorkQueueTenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequiredToCompleteIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantRepository.findAll().size();
        // set the field null
        workQueueTenant.setRequiredToComplete(null);

        // Create the WorkQueueTenant, which fails.

        restWorkQueueTenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantRepository.findAll().size();
        // set the field null
        workQueueTenant.setSequenceOrder(null);

        // Create the WorkQueueTenant, which fails.

        restWorkQueueTenantMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkQueueTenants() throws Exception {
        // Initialize the database
        workQueueTenantRepository.saveAndFlush(workQueueTenant);

        // Get all the workQueueTenantList
        restWorkQueueTenantMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workQueueTenant.getId().intValue())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK)))
            .andExpect(jsonPath("$.[*].requiredToComplete").value(hasItem(DEFAULT_REQUIRED_TO_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateCancelled").value(hasItem(DEFAULT_DATE_CANCELLED.toString())))
            .andExpect(jsonPath("$.[*].dateCompleted").value(hasItem(DEFAULT_DATE_COMPLETED.toString())))
            .andExpect(jsonPath("$.[*].sequenceOrder").value(hasItem(sameNumber(DEFAULT_SEQUENCE_ORDER))));
    }

    @Test
    @Transactional
    void getWorkQueueTenant() throws Exception {
        // Initialize the database
        workQueueTenantRepository.saveAndFlush(workQueueTenant);

        // Get the workQueueTenant
        restWorkQueueTenantMockMvc
            .perform(get(ENTITY_API_URL_ID, workQueueTenant.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workQueueTenant.getId().intValue()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK))
            .andExpect(jsonPath("$.requiredToComplete").value(DEFAULT_REQUIRED_TO_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateCancelled").value(DEFAULT_DATE_CANCELLED.toString()))
            .andExpect(jsonPath("$.dateCompleted").value(DEFAULT_DATE_COMPLETED.toString()))
            .andExpect(jsonPath("$.sequenceOrder").value(sameNumber(DEFAULT_SEQUENCE_ORDER)));
    }

    @Test
    @Transactional
    void getNonExistingWorkQueueTenant() throws Exception {
        // Get the workQueueTenant
        restWorkQueueTenantMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkQueueTenant() throws Exception {
        // Initialize the database
        workQueueTenantRepository.saveAndFlush(workQueueTenant);

        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();

        // Update the workQueueTenant
        WorkQueueTenant updatedWorkQueueTenant = workQueueTenantRepository.findById(workQueueTenant.getId()).get();
        // Disconnect from session so that the updates on updatedWorkQueueTenant are not directly saved in db
        em.detach(updatedWorkQueueTenant);
        updatedWorkQueueTenant
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCancelled(UPDATED_DATE_CANCELLED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkQueueTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkQueueTenant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkQueueTenant))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenant testWorkQueueTenant = workQueueTenantList.get(workQueueTenantList.size() - 1);
        assertThat(testWorkQueueTenant.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkQueueTenant.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenant.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testWorkQueueTenant.getDateCancelled()).isEqualTo(UPDATED_DATE_CANCELLED);
        assertThat(testWorkQueueTenant.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
        assertThat(testWorkQueueTenant.getSequenceOrder()).isEqualTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingWorkQueueTenant() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();
        workQueueTenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workQueueTenant.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkQueueTenant() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();
        workQueueTenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkQueueTenant() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();
        workQueueTenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkQueueTenantWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantRepository.saveAndFlush(workQueueTenant);

        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();

        // Update the workQueueTenant using partial update
        WorkQueueTenant partialUpdatedWorkQueueTenant = new WorkQueueTenant();
        partialUpdatedWorkQueueTenant.setId(workQueueTenant.getId());

        partialUpdatedWorkQueueTenant
            .task(UPDATED_TASK)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkQueueTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenant))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenant testWorkQueueTenant = workQueueTenantList.get(workQueueTenantList.size() - 1);
        assertThat(testWorkQueueTenant.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkQueueTenant.getRequiredToComplete()).isEqualTo(DEFAULT_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenant.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testWorkQueueTenant.getDateCancelled()).isEqualTo(DEFAULT_DATE_CANCELLED);
        assertThat(testWorkQueueTenant.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
        assertThat(testWorkQueueTenant.getSequenceOrder()).isEqualByComparingTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateWorkQueueTenantWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantRepository.saveAndFlush(workQueueTenant);

        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();

        // Update the workQueueTenant using partial update
        WorkQueueTenant partialUpdatedWorkQueueTenant = new WorkQueueTenant();
        partialUpdatedWorkQueueTenant.setId(workQueueTenant.getId());

        partialUpdatedWorkQueueTenant
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCancelled(UPDATED_DATE_CANCELLED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkQueueTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenant))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenant testWorkQueueTenant = workQueueTenantList.get(workQueueTenantList.size() - 1);
        assertThat(testWorkQueueTenant.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkQueueTenant.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenant.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testWorkQueueTenant.getDateCancelled()).isEqualTo(UPDATED_DATE_CANCELLED);
        assertThat(testWorkQueueTenant.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
        assertThat(testWorkQueueTenant.getSequenceOrder()).isEqualByComparingTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkQueueTenant() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();
        workQueueTenant.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workQueueTenant.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkQueueTenant() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();
        workQueueTenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkQueueTenant() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantRepository.findAll().size();
        workQueueTenant.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenant))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenant in the database
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkQueueTenant() throws Exception {
        // Initialize the database
        workQueueTenantRepository.saveAndFlush(workQueueTenant);

        int databaseSizeBeforeDelete = workQueueTenantRepository.findAll().size();

        // Delete the workQueueTenant
        restWorkQueueTenantMockMvc
            .perform(delete(ENTITY_API_URL_ID, workQueueTenant.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkQueueTenant> workQueueTenantList = workQueueTenantRepository.findAll();
        assertThat(workQueueTenantList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
