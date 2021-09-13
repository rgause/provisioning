package com.mycompany.provisioning.web.rest;

import static com.mycompany.provisioning.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkTemplateItem;
import com.mycompany.provisioning.repository.WorkTemplateItemRepository;
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
 * Integration tests for the {@link WorkTemplateItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkTemplateItemResourceIT {

    private static final String DEFAULT_TASK = "AAAAAAAAAA";
    private static final String UPDATED_TASK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_REQUIRED_TO_COMPLETE = false;
    private static final Boolean UPDATED_REQUIRED_TO_COMPLETE = true;

    private static final BigDecimal DEFAULT_SEQUENCE_ORDER = new BigDecimal(1);
    private static final BigDecimal UPDATED_SEQUENCE_ORDER = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/work-template-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkTemplateItemRepository workTemplateItemRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkTemplateItemMockMvc;

    private WorkTemplateItem workTemplateItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkTemplateItem createEntity(EntityManager em) {
        WorkTemplateItem workTemplateItem = new WorkTemplateItem()
            .task(DEFAULT_TASK)
            .requiredToComplete(DEFAULT_REQUIRED_TO_COMPLETE)
            .sequenceOrder(DEFAULT_SEQUENCE_ORDER);
        return workTemplateItem;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkTemplateItem createUpdatedEntity(EntityManager em) {
        WorkTemplateItem workTemplateItem = new WorkTemplateItem()
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);
        return workTemplateItem;
    }

    @BeforeEach
    public void initTest() {
        workTemplateItem = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkTemplateItem() throws Exception {
        int databaseSizeBeforeCreate = workTemplateItemRepository.findAll().size();
        // Create the WorkTemplateItem
        restWorkTemplateItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isCreated());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeCreate + 1);
        WorkTemplateItem testWorkTemplateItem = workTemplateItemList.get(workTemplateItemList.size() - 1);
        assertThat(testWorkTemplateItem.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testWorkTemplateItem.getRequiredToComplete()).isEqualTo(DEFAULT_REQUIRED_TO_COMPLETE);
        assertThat(testWorkTemplateItem.getSequenceOrder()).isEqualByComparingTo(DEFAULT_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void createWorkTemplateItemWithExistingId() throws Exception {
        // Create the WorkTemplateItem with an existing ID
        workTemplateItem.setId(1L);

        int databaseSizeBeforeCreate = workTemplateItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTemplateItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTaskIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTemplateItemRepository.findAll().size();
        // set the field null
        workTemplateItem.setTask(null);

        // Create the WorkTemplateItem, which fails.

        restWorkTemplateItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRequiredToCompleteIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTemplateItemRepository.findAll().size();
        // set the field null
        workTemplateItem.setRequiredToComplete(null);

        // Create the WorkTemplateItem, which fails.

        restWorkTemplateItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSequenceOrderIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTemplateItemRepository.findAll().size();
        // set the field null
        workTemplateItem.setSequenceOrder(null);

        // Create the WorkTemplateItem, which fails.

        restWorkTemplateItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkTemplateItems() throws Exception {
        // Initialize the database
        workTemplateItemRepository.saveAndFlush(workTemplateItem);

        // Get all the workTemplateItemList
        restWorkTemplateItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workTemplateItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].task").value(hasItem(DEFAULT_TASK)))
            .andExpect(jsonPath("$.[*].requiredToComplete").value(hasItem(DEFAULT_REQUIRED_TO_COMPLETE.booleanValue())))
            .andExpect(jsonPath("$.[*].sequenceOrder").value(hasItem(sameNumber(DEFAULT_SEQUENCE_ORDER))));
    }

    @Test
    @Transactional
    void getWorkTemplateItem() throws Exception {
        // Initialize the database
        workTemplateItemRepository.saveAndFlush(workTemplateItem);

        // Get the workTemplateItem
        restWorkTemplateItemMockMvc
            .perform(get(ENTITY_API_URL_ID, workTemplateItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workTemplateItem.getId().intValue()))
            .andExpect(jsonPath("$.task").value(DEFAULT_TASK))
            .andExpect(jsonPath("$.requiredToComplete").value(DEFAULT_REQUIRED_TO_COMPLETE.booleanValue()))
            .andExpect(jsonPath("$.sequenceOrder").value(sameNumber(DEFAULT_SEQUENCE_ORDER)));
    }

    @Test
    @Transactional
    void getNonExistingWorkTemplateItem() throws Exception {
        // Get the workTemplateItem
        restWorkTemplateItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkTemplateItem() throws Exception {
        // Initialize the database
        workTemplateItemRepository.saveAndFlush(workTemplateItem);

        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();

        // Update the workTemplateItem
        WorkTemplateItem updatedWorkTemplateItem = workTemplateItemRepository.findById(workTemplateItem.getId()).get();
        // Disconnect from session so that the updates on updatedWorkTemplateItem are not directly saved in db
        em.detach(updatedWorkTemplateItem);
        updatedWorkTemplateItem.task(UPDATED_TASK).requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE).sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkTemplateItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkTemplateItem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkTemplateItem))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplateItem testWorkTemplateItem = workTemplateItemList.get(workTemplateItemList.size() - 1);
        assertThat(testWorkTemplateItem.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkTemplateItem.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkTemplateItem.getSequenceOrder()).isEqualTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void putNonExistingWorkTemplateItem() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();
        workTemplateItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTemplateItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workTemplateItem.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkTemplateItem() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();
        workTemplateItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkTemplateItem() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();
        workTemplateItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkTemplateItemWithPatch() throws Exception {
        // Initialize the database
        workTemplateItemRepository.saveAndFlush(workTemplateItem);

        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();

        // Update the workTemplateItem using partial update
        WorkTemplateItem partialUpdatedWorkTemplateItem = new WorkTemplateItem();
        partialUpdatedWorkTemplateItem.setId(workTemplateItem.getId());

        partialUpdatedWorkTemplateItem.requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE).sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkTemplateItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkTemplateItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkTemplateItem))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplateItem testWorkTemplateItem = workTemplateItemList.get(workTemplateItemList.size() - 1);
        assertThat(testWorkTemplateItem.getTask()).isEqualTo(DEFAULT_TASK);
        assertThat(testWorkTemplateItem.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkTemplateItem.getSequenceOrder()).isEqualByComparingTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void fullUpdateWorkTemplateItemWithPatch() throws Exception {
        // Initialize the database
        workTemplateItemRepository.saveAndFlush(workTemplateItem);

        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();

        // Update the workTemplateItem using partial update
        WorkTemplateItem partialUpdatedWorkTemplateItem = new WorkTemplateItem();
        partialUpdatedWorkTemplateItem.setId(workTemplateItem.getId());

        partialUpdatedWorkTemplateItem
            .task(UPDATED_TASK)
            .requiredToComplete(UPDATED_REQUIRED_TO_COMPLETE)
            .sequenceOrder(UPDATED_SEQUENCE_ORDER);

        restWorkTemplateItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkTemplateItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkTemplateItem))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplateItem testWorkTemplateItem = workTemplateItemList.get(workTemplateItemList.size() - 1);
        assertThat(testWorkTemplateItem.getTask()).isEqualTo(UPDATED_TASK);
        assertThat(testWorkTemplateItem.getRequiredToComplete()).isEqualTo(UPDATED_REQUIRED_TO_COMPLETE);
        assertThat(testWorkTemplateItem.getSequenceOrder()).isEqualByComparingTo(UPDATED_SEQUENCE_ORDER);
    }

    @Test
    @Transactional
    void patchNonExistingWorkTemplateItem() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();
        workTemplateItem.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTemplateItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workTemplateItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkTemplateItem() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();
        workTemplateItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkTemplateItem() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateItemRepository.findAll().size();
        workTemplateItem.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplateItem))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkTemplateItem in the database
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkTemplateItem() throws Exception {
        // Initialize the database
        workTemplateItemRepository.saveAndFlush(workTemplateItem);

        int databaseSizeBeforeDelete = workTemplateItemRepository.findAll().size();

        // Delete the workTemplateItem
        restWorkTemplateItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, workTemplateItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkTemplateItem> workTemplateItemList = workTemplateItemRepository.findAll();
        assertThat(workTemplateItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
