package com.mycompany.provisioning.web.rest;

import static com.mycompany.provisioning.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkQueueTenantUser;
import com.mycompany.provisioning.repository.WorkQueueTenantUserRepository;
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
 * Integration tests for the {@link WorkQueueTenantUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkQueueTenantUserResourceIT {

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

    private static final String ENTITY_API_URL = "/api/work-queue-tenant-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkQueueTenantUserRepository workQueueTenantUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkQueueTenantUserMockMvc;

    private WorkQueueTenantUser workQueueTenantUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantUser createEntity(EntityManager em) {
        WorkQueueTenantUser workQueueTenantUser = new WorkQueueTenantUser()
            .task(DEFAULT_TASK)
            .requiredToComplete(DEFAULT_REQUIRED_TO_COMPLETE)
            .dateCreated(DEFAULT_DATE_CREATED)
            .dateCancelled(DEFAULT_DATE_CANCELLED)
            .dateCompleted(DEFAULT_DATE_COMPLETED)
            .sequenceOrder(DEFAULT_SEQUENCE_ORDER);
        return workQueueTenantUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkQueueTenantUser createUpdatedEntity(EntityManager em) {
        WorkQueueTenantUser workQueueTenantUser = new WorkQueueTenantUser()
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCancelled(UPDATED_DATE_CANCELLED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);
        return workQueueTenantUser;
    }

    @BeforeEach
    public void initTest() {
        workQueueTenantUser = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeCreate = workQueueTenantUserRepository.findAll().size();
        // Create the WorkQueueTenantUser
        restWorkQueueTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isCreated());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeCreate + 1);
        WorkQueueTenantUser testWorkQueueTenantUser = workQueueTenantUserList.get(workQueueTenantUserList.size() - 1);
        assertThat(testWorkQueueTenantUser.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testWorkQueueTenantUser.getRequiredToComplete()).isEqualTo(DEFAULT_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenantUser.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testWorkQueueTenantUser.getDateCancelled()).isEqualTo(DEFAULT_DATE_CANCELLED);
        assertThat(testWorkQueueTenantUser.getDateCompleted()).isEqualTo(DEFAULT_DATE_COMPLETED);
        assertThat(testWorkQueueTenantUser.getSequenceOrder()).isEqualByComparingTo(DEFAULT_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void createWorkQueueTenantUserWithExistingId() throws Exception {
        // Create the WorkQueueTenantUser with an existing ID
        workQueueTenantUser.setId(1L);

        int databaseSizeBeforeCreate = workQueueTenantUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkQueueTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantUserRepository.findAll().size();
        // set the field null
        workQueueTenantUser.setTask(null);

        // Create the WorkQueueTenantUser, which fails.

        restWorkQueueTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequiredToCompleteIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantUserRepository.findAll().size();
        // set the field null
        workQueueTenantUser.setRequiredToComplete(null);

        // Create the WorkQueueTenantUser, which fails.

        restWorkQueueTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = workQueueTenantUserRepository.findAll().size();
        // set the field null
        workQueueTenantUser.setSequenceOrder(null);

        // Create the WorkQueueTenantUser, which fails.

        restWorkQueueTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkQueueTenantUsers() throws Exception {
        // Initialize the database
        workQueueTenantUserRepository.saveAndFlush(workQueueTenantUser);

        // Get all the workQueueTenantUserList
        restWorkQueueTenantUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workQueueTenantUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK)))
            .andExpect(jsonPath("$.[*].requiredToComplete").value(hasItem(DEFAULT_REQUIRED_TO_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].dateCreated").value(hasItem(DEFAULT_DATE_CREATED.toString())))
            .andExpect(jsonPath("$.[*].dateCancelled").value(hasItem(DEFAULT_DATE_CANCELLED.toString())))
            .andExpect(jsonPath("$.[*].dateCompleted").value(hasItem(DEFAULT_DATE_COMPLETED.toString())))
            .andExpect(jsonPath("$.[*].sequenceOrder").value(hasItem(sameNumber(DEFAULT_SEQUENCE_ORDER))));
    }

    @Test
    @Transactional
    void getWorkQueueTenantUser() throws Exception {
        // Initialize the database
        workQueueTenantUserRepository.saveAndFlush(workQueueTenantUser);

        // Get the workQueueTenantUser
        restWorkQueueTenantUserMockMvc
            .perform(get(ENTITY_API_URL_ID, workQueueTenantUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workQueueTenantUser.getId().intValue()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK))
            .andExpect(jsonPath("$.requiredToComplete").value(DEFAULT_REQUIRED_TO_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.dateCreated").value(DEFAULT_DATE_CREATED.toString()))
            .andExpect(jsonPath("$.dateCancelled").value(DEFAULT_DATE_CANCELLED.toString()))
            .andExpect(jsonPath("$.dateCompleted").value(DEFAULT_DATE_COMPLETED.toString()))
            .andExpect(jsonPath("$.sequenceOrder").value(sameNumber(DEFAULT_SEQUENCE_ORDER)));
    }

    @Test
    @Transactional
    void getNonExistingWorkQueueTenantUser() throws Exception {
        // Get the workQueueTenantUser
        restWorkQueueTenantUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkQueueTenantUser() throws Exception {
        // Initialize the database
        workQueueTenantUserRepository.saveAndFlush(workQueueTenantUser);

        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();

        // Update the workQueueTenantUser
        WorkQueueTenantUser updatedWorkQueueTenantUser = workQueueTenantUserRepository.findById(workQueueTenantUser.getId()).get();
        // Disconnect from session so that the updates on updatedWorkQueueTenantUser are not directly saved in db
        em.detach(updatedWorkQueueTenantUser);
        updatedWorkQueueTenantUser
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCancelled(UPDATED_DATE_CANCELLED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkQueueTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkQueueTenantUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkQueueTenantUser))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUser testWorkQueueTenantUser = workQueueTenantUserList.get(workQueueTenantUserList.size() - 1);
        assertThat(testWorkQueueTenantUser.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkQueueTenantUser.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenantUser.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testWorkQueueTenantUser.getDateCancelled()).isEqualTo(UPDATED_DATE_CANCELLED);
        assertThat(testWorkQueueTenantUser.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
        assertThat(testWorkQueueTenantUser.getSequenceOrder()).isEqualTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();
        workQueueTenantUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workQueueTenantUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();
        workQueueTenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();
        workQueueTenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkQueueTenantUserWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantUserRepository.saveAndFlush(workQueueTenantUser);

        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();

        // Update the workQueueTenantUser using partial update
        WorkQueueTenantUser partialUpdatedWorkQueueTenantUser = new WorkQueueTenantUser();
        partialUpdatedWorkQueueTenantUser.setId(workQueueTenantUser.getId());

        partialUpdatedWorkQueueTenantUser.dateCancelled(UPDATED_DATE_CANCELLED).dateCompleted(UPDATED_DATE_COMPLETED);

        restWorkQueueTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantUser))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUser testWorkQueueTenantUser = workQueueTenantUserList.get(workQueueTenantUserList.size() - 1);
        assertThat(testWorkQueueTenantUser.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testWorkQueueTenantUser.getRequiredToComplete()).isEqualTo(DEFAULT_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenantUser.getDateCreated()).isEqualTo(DEFAULT_DATE_CREATED);
        assertThat(testWorkQueueTenantUser.getDateCancelled()).isEqualTo(UPDATED_DATE_CANCELLED);
        assertThat(testWorkQueueTenantUser.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
        assertThat(testWorkQueueTenantUser.getSequenceOrder()).isEqualByComparingTo(DEFAULT_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateWorkQueueTenantUserWithPatch() throws Exception {
        // Initialize the database
        workQueueTenantUserRepository.saveAndFlush(workQueueTenantUser);

        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();

        // Update the workQueueTenantUser using partial update
        WorkQueueTenantUser partialUpdatedWorkQueueTenantUser = new WorkQueueTenantUser();
        partialUpdatedWorkQueueTenantUser.setId(workQueueTenantUser.getId());

        partialUpdatedWorkQueueTenantUser
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .dateCreated(UPDATED_DATE_CREATED)
            .dateCancelled(UPDATED_DATE_CANCELLED)
            .dateCompleted(UPDATED_DATE_COMPLETED)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkQueueTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkQueueTenantUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkQueueTenantUser))
            )
            .andExpect(status().isOk());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
        WorkQueueTenantUser testWorkQueueTenantUser = workQueueTenantUserList.get(workQueueTenantUserList.size() - 1);
        assertThat(testWorkQueueTenantUser.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkQueueTenantUser.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkQueueTenantUser.getDateCreated()).isEqualTo(UPDATED_DATE_CREATED);
        assertThat(testWorkQueueTenantUser.getDateCancelled()).isEqualTo(UPDATED_DATE_CANCELLED);
        assertThat(testWorkQueueTenantUser.getDateCompleted()).isEqualTo(UPDATED_DATE_COMPLETED);
        assertThat(testWorkQueueTenantUser.getSequenceOrder()).isEqualByComparingTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();
        workQueueTenantUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workQueueTenantUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();
        workQueueTenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkQueueTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = workQueueTenantUserRepository.findAll().size();
        workQueueTenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkQueueTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workQueueTenantUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkQueueTenantUser in the database
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkQueueTenantUser() throws Exception {
        // Initialize the database
        workQueueTenantUserRepository.saveAndFlush(workQueueTenantUser);

        int databaseSizeBeforeDelete = workQueueTenantUserRepository.findAll().size();

        // Delete the workQueueTenantUser
        restWorkQueueTenantUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, workQueueTenantUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkQueueTenantUser> workQueueTenantUserList = workQueueTenantUserRepository.findAll();
        assertThat(workQueueTenantUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
