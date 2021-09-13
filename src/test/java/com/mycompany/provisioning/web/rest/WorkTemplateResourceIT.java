package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.WorkTemplate;
import com.mycompany.provisioning.repository.WorkTemplateRepository;
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
 * Integration tests for the {@link WorkTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/work-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WorkTemplateRepository workTemplateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkTemplateMockMvc;

    private WorkTemplate workTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkTemplate createEntity(EntityManager em) {
        WorkTemplate workTemplate = new WorkTemplate().name(DEFAULT_NAME);
        return workTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkTemplate createUpdatedEntity(EntityManager em) {
        WorkTemplate workTemplate = new WorkTemplate().name(UPDATED_NAME);
        return workTemplate;
    }

    @BeforeEach
    public void initTest() {
        workTemplate = createEntity(em);
    }

    @Test
    @Transactional
    void createWorkTemplate() throws Exception {
        int databaseSizeBeforeCreate = workTemplateRepository.findAll().size();
        // Create the WorkTemplate
        restWorkTemplateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isCreated());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeCreate + 1);
        WorkTemplate testWorkTemplate = workTemplateList.get(workTemplateList.size() - 1);
        assertThat(testWorkTemplate.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createWorkTemplateWithExistingId() throws Exception {
        // Create the WorkTemplate with an existing ID
        workTemplate.setId(1L);

        int databaseSizeBeforeCreate = workTemplateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkTemplateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = workTemplateRepository.findAll().size();
        // set the field null
        workTemplate.setName(null);

        // Create the WorkTemplate, which fails.

        restWorkTemplateMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isBadRequest());

        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkTemplates() throws Exception {
        // Initialize the database
        workTemplateRepository.saveAndFlush(workTemplate);

        // Get all the workTemplateList
        restWorkTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getWorkTemplate() throws Exception {
        // Initialize the database
        workTemplateRepository.saveAndFlush(workTemplate);

        // Get the workTemplate
        restWorkTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, workTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingWorkTemplate() throws Exception {
        // Get the workTemplate
        restWorkTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWorkTemplate() throws Exception {
        // Initialize the database
        workTemplateRepository.saveAndFlush(workTemplate);

        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();

        // Update the workTemplate
        WorkTemplate updatedWorkTemplate = workTemplateRepository.findById(workTemplate.getId()).get();
        // Disconnect from session so that the updates on updatedWorkTemplate are not directly saved in db
        em.detach(updatedWorkTemplate);
        updatedWorkTemplate.name(UPDATED_NAME);

        restWorkTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWorkTemplate.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWorkTemplate))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplate testWorkTemplate = workTemplateList.get(workTemplateList.size() - 1);
        assertThat(testWorkTemplate.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingWorkTemplate() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();
        workTemplate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workTemplate.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkTemplate() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();
        workTemplate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkTemplate() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();
        workTemplate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkTemplateWithPatch() throws Exception {
        // Initialize the database
        workTemplateRepository.saveAndFlush(workTemplate);

        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();

        // Update the workTemplate using partial update
        WorkTemplate partialUpdatedWorkTemplate = new WorkTemplate();
        partialUpdatedWorkTemplate.setId(workTemplate.getId());

        restWorkTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkTemplate))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplate testWorkTemplate = workTemplateList.get(workTemplateList.size() - 1);
        assertThat(testWorkTemplate.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateWorkTemplateWithPatch() throws Exception {
        // Initialize the database
        workTemplateRepository.saveAndFlush(workTemplate);

        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();

        // Update the workTemplate using partial update
        WorkTemplate partialUpdatedWorkTemplate = new WorkTemplate();
        partialUpdatedWorkTemplate.setId(workTemplate.getId());

        partialUpdatedWorkTemplate.name(UPDATED_NAME);

        restWorkTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWorkTemplate))
            )
            .andExpect(status().isOk());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
        WorkTemplate testWorkTemplate = workTemplateList.get(workTemplateList.size() - 1);
        assertThat(testWorkTemplate.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingWorkTemplate() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();
        workTemplate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkTemplate() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();
        workTemplate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkTemplate() throws Exception {
        int databaseSizeBeforeUpdate = workTemplateRepository.findAll().size();
        workTemplate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(workTemplate))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkTemplate in the database
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkTemplate() throws Exception {
        // Initialize the database
        workTemplateRepository.saveAndFlush(workTemplate);

        int databaseSizeBeforeDelete = workTemplateRepository.findAll().size();

        // Delete the workTemplate
        restWorkTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, workTemplate.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WorkTemplate> workTemplateList = workTemplateRepository.findAll();
        assertThat(workTemplateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
