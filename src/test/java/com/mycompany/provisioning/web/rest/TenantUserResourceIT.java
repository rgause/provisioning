package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.TenantUser;
import com.mycompany.provisioning.repository.TenantUserRepository;
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
 * Integration tests for the {@link TenantUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TenantUserResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_PROVISIONED = false;
    private static final Boolean UPDATED_PROVISIONED = true;

    private static final String ENTITY_API_URL = "/api/tenant-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TenantUserRepository tenantUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTenantUserMockMvc;

    private TenantUser tenantUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantUser createEntity(EntityManager em) {
        TenantUser tenantUser = new TenantUser().active(DEFAULT_ACTIVE).provisioned(DEFAULT_PROVISIONED);
        return tenantUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TenantUser createUpdatedEntity(EntityManager em) {
        TenantUser tenantUser = new TenantUser().active(UPDATED_ACTIVE).provisioned(UPDATED_PROVISIONED);
        return tenantUser;
    }

    @BeforeEach
    public void initTest() {
        tenantUser = createEntity(em);
    }

    @Test
    @Transactional
    void createTenantUser() throws Exception {
        int databaseSizeBeforeCreate = tenantUserRepository.findAll().size();
        // Create the TenantUser
        restTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isCreated());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeCreate + 1);
        TenantUser testTenantUser = tenantUserList.get(tenantUserList.size() - 1);
        assertThat(testTenantUser.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testTenantUser.getProvisioned()).isEqualTo(DEFAULT_PROVISIONED);
    }

    @Test
    @Transactional
    void createTenantUserWithExistingId() throws Exception {
        // Create the TenantUser with an existing ID
        tenantUser.setId(1L);

        int databaseSizeBeforeCreate = tenantUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantUserRepository.findAll().size();
        // set the field null
        tenantUser.setActive(null);

        // Create the TenantUser, which fails.

        restTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProvisionedIsRequired() throws Exception {
        int databaseSizeBeforeTest = tenantUserRepository.findAll().size();
        // set the field null
        tenantUser.setProvisioned(null);

        // Create the TenantUser, which fails.

        restTenantUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTenantUsers() throws Exception {
        // Initialize the database
        tenantUserRepository.saveAndFlush(tenantUser);

        // Get all the tenantUserList
        restTenantUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tenantUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].provisioned").value(hasItem(DEFAULT_PROVISIONED.booleanValue())));
    }

    @Test
    @Transactional
    void getTenantUser() throws Exception {
        // Initialize the database
        tenantUserRepository.saveAndFlush(tenantUser);

        // Get the tenantUser
        restTenantUserMockMvc
            .perform(get(ENTITY_API_URL_ID, tenantUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tenantUser.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.provisioned").value(DEFAULT_PROVISIONED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingTenantUser() throws Exception {
        // Get the tenantUser
        restTenantUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTenantUser() throws Exception {
        // Initialize the database
        tenantUserRepository.saveAndFlush(tenantUser);

        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();

        // Update the tenantUser
        TenantUser updatedTenantUser = tenantUserRepository.findById(tenantUser.getId()).get();
        // Disconnect from session so that the updates on updatedTenantUser are not directly saved in db
        em.detach(updatedTenantUser);
        updatedTenantUser.active(UPDATED_ACTIVE).provisioned(UPDATED_PROVISIONED);

        restTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTenantUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTenantUser))
            )
            .andExpect(status().isOk());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
        TenantUser testTenantUser = tenantUserList.get(tenantUserList.size() - 1);
        assertThat(testTenantUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTenantUser.getProvisioned()).isEqualTo(UPDATED_PROVISIONED);
    }

    @Test
    @Transactional
    void putNonExistingTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();
        tenantUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tenantUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();
        tenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();
        tenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantUserMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTenantUserWithPatch() throws Exception {
        // Initialize the database
        tenantUserRepository.saveAndFlush(tenantUser);

        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();

        // Update the tenantUser using partial update
        TenantUser partialUpdatedTenantUser = new TenantUser();
        partialUpdatedTenantUser.setId(tenantUser.getId());

        partialUpdatedTenantUser.active(UPDATED_ACTIVE);

        restTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantUser))
            )
            .andExpect(status().isOk());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
        TenantUser testTenantUser = tenantUserList.get(tenantUserList.size() - 1);
        assertThat(testTenantUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTenantUser.getProvisioned()).isEqualTo(DEFAULT_PROVISIONED);
    }

    @Test
    @Transactional
    void fullUpdateTenantUserWithPatch() throws Exception {
        // Initialize the database
        tenantUserRepository.saveAndFlush(tenantUser);

        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();

        // Update the tenantUser using partial update
        TenantUser partialUpdatedTenantUser = new TenantUser();
        partialUpdatedTenantUser.setId(tenantUser.getId());

        partialUpdatedTenantUser.active(UPDATED_ACTIVE).provisioned(UPDATED_PROVISIONED);

        restTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTenantUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTenantUser))
            )
            .andExpect(status().isOk());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
        TenantUser testTenantUser = tenantUserList.get(tenantUserList.size() - 1);
        assertThat(testTenantUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testTenantUser.getProvisioned()).isEqualTo(UPDATED_PROVISIONED);
    }

    @Test
    @Transactional
    void patchNonExistingTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();
        tenantUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tenantUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();
        tenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTenantUser() throws Exception {
        int databaseSizeBeforeUpdate = tenantUserRepository.findAll().size();
        tenantUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTenantUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(tenantUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TenantUser in the database
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTenantUser() throws Exception {
        // Initialize the database
        tenantUserRepository.saveAndFlush(tenantUser);

        int databaseSizeBeforeDelete = tenantUserRepository.findAll().size();

        // Delete the tenantUser
        restTenantUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, tenantUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TenantUser> tenantUserList = tenantUserRepository.findAll();
        assertThat(tenantUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
