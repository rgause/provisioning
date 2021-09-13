package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.LanUser;
import com.mycompany.provisioning.repository.LanUserRepository;
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
 * Integration tests for the {@link LanUserResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LanUserResourceIT {

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String DEFAULT_LAN_ID = "AAAAAAAAAA";
    private static final String UPDATED_LAN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PWP_ID = "AAAAAAAAAA";
    private static final String UPDATED_PWP_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/lan-users";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private LanUserRepository lanUserRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanUserMockMvc;

    private LanUser lanUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LanUser createEntity(EntityManager em) {
        LanUser lanUser = new LanUser()
            .active(DEFAULT_ACTIVE)
            .lanId(DEFAULT_LAN_ID)
            .pwpId(DEFAULT_PWP_ID)
            .lastName(DEFAULT_LAST_NAME)
            .firstName(DEFAULT_FIRST_NAME);
        return lanUser;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LanUser createUpdatedEntity(EntityManager em) {
        LanUser lanUser = new LanUser()
            .active(UPDATED_ACTIVE)
            .lanId(UPDATED_LAN_ID)
            .pwpId(UPDATED_PWP_ID)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME);
        return lanUser;
    }

    @BeforeEach
    public void initTest() {
        lanUser = createEntity(em);
    }

    @Test
    @Transactional
    void createLanUser() throws Exception {
        int databaseSizeBeforeCreate = lanUserRepository.findAll().size();
        // Create the LanUser
        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isCreated());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeCreate + 1);
        LanUser testLanUser = lanUserList.get(lanUserList.size() - 1);
        assertThat(testLanUser.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testLanUser.getLanId()).isEqualTo(DEFAULT_LAN_ID);
        assertThat(testLanUser.getPwpId()).isEqualTo(DEFAULT_PWP_ID);
        assertThat(testLanUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testLanUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void createLanUserWithExistingId() throws Exception {
        // Create the LanUser with an existing ID
        lanUser.setId(1L);

        int databaseSizeBeforeCreate = lanUserRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = lanUserRepository.findAll().size();
        // set the field null
        lanUser.setActive(null);

        // Create the LanUser, which fails.

        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLanIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lanUserRepository.findAll().size();
        // set the field null
        lanUser.setLanId(null);

        // Create the LanUser, which fails.

        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPwpIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = lanUserRepository.findAll().size();
        // set the field null
        lanUser.setPwpId(null);

        // Create the LanUser, which fails.

        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lanUserRepository.findAll().size();
        // set the field null
        lanUser.setLastName(null);

        // Create the LanUser, which fails.

        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = lanUserRepository.findAll().size();
        // set the field null
        lanUser.setFirstName(null);

        // Create the LanUser, which fails.

        restLanUserMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLanUsers() throws Exception {
        // Initialize the database
        lanUserRepository.saveAndFlush(lanUser);

        // Get all the lanUserList
        restLanUserMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lanUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lanId").value(hasItem(DEFAULT_LAN_ID)))
            .andExpect(jsonPath("$.[*].pwpId").value(hasItem(DEFAULT_PWP_ID)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)));
    }

    @Test
    @Transactional
    void getLanUser() throws Exception {
        // Initialize the database
        lanUserRepository.saveAndFlush(lanUser);

        // Get the lanUser
        restLanUserMockMvc
            .perform(get(ENTITY_API_URL_ID, lanUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lanUser.getId().intValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lanId").value(DEFAULT_LAN_ID))
            .andExpect(jsonPath("$.pwpId").value(DEFAULT_PWP_ID))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME));
    }

    @Test
    @Transactional
    void getNonExistingLanUser() throws Exception {
        // Get the lanUser
        restLanUserMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewLanUser() throws Exception {
        // Initialize the database
        lanUserRepository.saveAndFlush(lanUser);

        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();

        // Update the lanUser
        LanUser updatedLanUser = lanUserRepository.findById(lanUser.getId()).get();
        // Disconnect from session so that the updates on updatedLanUser are not directly saved in db
        em.detach(updatedLanUser);
        updatedLanUser
            .active(UPDATED_ACTIVE)
            .lanId(UPDATED_LAN_ID)
            .pwpId(UPDATED_PWP_ID)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME);

        restLanUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLanUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedLanUser))
            )
            .andExpect(status().isOk());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
        LanUser testLanUser = lanUserList.get(lanUserList.size() - 1);
        assertThat(testLanUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testLanUser.getLanId()).isEqualTo(UPDATED_LAN_ID);
        assertThat(testLanUser.getPwpId()).isEqualTo(UPDATED_PWP_ID);
        assertThat(testLanUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLanUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void putNonExistingLanUser() throws Exception {
        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();
        lanUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lanUser.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanUser() throws Exception {
        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();
        lanUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanUserMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanUser() throws Exception {
        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();
        lanUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanUserMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLanUserWithPatch() throws Exception {
        // Initialize the database
        lanUserRepository.saveAndFlush(lanUser);

        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();

        // Update the lanUser using partial update
        LanUser partialUpdatedLanUser = new LanUser();
        partialUpdatedLanUser.setId(lanUser.getId());

        partialUpdatedLanUser.active(UPDATED_ACTIVE).lastName(UPDATED_LAST_NAME);

        restLanUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLanUser))
            )
            .andExpect(status().isOk());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
        LanUser testLanUser = lanUserList.get(lanUserList.size() - 1);
        assertThat(testLanUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testLanUser.getLanId()).isEqualTo(DEFAULT_LAN_ID);
        assertThat(testLanUser.getPwpId()).isEqualTo(DEFAULT_PWP_ID);
        assertThat(testLanUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLanUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
    }

    @Test
    @Transactional
    void fullUpdateLanUserWithPatch() throws Exception {
        // Initialize the database
        lanUserRepository.saveAndFlush(lanUser);

        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();

        // Update the lanUser using partial update
        LanUser partialUpdatedLanUser = new LanUser();
        partialUpdatedLanUser.setId(lanUser.getId());

        partialUpdatedLanUser
            .active(UPDATED_ACTIVE)
            .lanId(UPDATED_LAN_ID)
            .pwpId(UPDATED_PWP_ID)
            .lastName(UPDATED_LAST_NAME)
            .firstName(UPDATED_FIRST_NAME);

        restLanUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedLanUser))
            )
            .andExpect(status().isOk());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
        LanUser testLanUser = lanUserList.get(lanUserList.size() - 1);
        assertThat(testLanUser.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testLanUser.getLanId()).isEqualTo(UPDATED_LAN_ID);
        assertThat(testLanUser.getPwpId()).isEqualTo(UPDATED_PWP_ID);
        assertThat(testLanUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testLanUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingLanUser() throws Exception {
        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();
        lanUser.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lanUser.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanUser() throws Exception {
        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();
        lanUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanUserMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanUser() throws Exception {
        int databaseSizeBeforeUpdate = lanUserRepository.findAll().size();
        lanUser.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanUserMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(lanUser))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the LanUser in the database
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanUser() throws Exception {
        // Initialize the database
        lanUserRepository.saveAndFlush(lanUser);

        int databaseSizeBeforeDelete = lanUserRepository.findAll().size();

        // Delete the lanUser
        restLanUserMockMvc
            .perform(delete(ENTITY_API_URL_ID, lanUser.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<LanUser> lanUserList = lanUserRepository.findAll();
        assertThat(lanUserList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
