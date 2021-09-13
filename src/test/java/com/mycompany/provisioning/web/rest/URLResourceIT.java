package com.mycompany.provisioning.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.provisioning.IntegrationTest;
import com.mycompany.provisioning.domain.URL;
import com.mycompany.provisioning.repository.URLRepository;
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
 * Integration tests for the {@link URLResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class URLResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/urls";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private URLRepository uRLRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restURLMockMvc;

    private URL uRL;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static URL createEntity(EntityManager em) {
        URL uRL = new URL().url(DEFAULT_URL);
        return uRL;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static URL createUpdatedEntity(EntityManager em) {
        URL uRL = new URL().url(UPDATED_URL);
        return uRL;
    }

    @BeforeEach
    public void initTest() {
        uRL = createEntity(em);
    }

    @Test
    @Transactional
    void createURL() throws Exception {
        int databaseSizeBeforeCreate = uRLRepository.findAll().size();
        // Create the URL
        restURLMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isCreated());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeCreate + 1);
        URL testURL = uRLList.get(uRLList.size() - 1);
        assertThat(testURL.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void createURLWithExistingId() throws Exception {
        // Create the URL with an existing ID
        uRL.setId(1L);

        int databaseSizeBeforeCreate = uRLRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restURLMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isBadRequest());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = uRLRepository.findAll().size();
        // set the field null
        uRL.setUrl(null);

        // Create the URL, which fails.

        restURLMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isBadRequest());

        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllURLS() throws Exception {
        // Initialize the database
        uRLRepository.saveAndFlush(uRL);

        // Get all the uRLList
        restURLMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(uRL.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)));
    }

    @Test
    @Transactional
    void getURL() throws Exception {
        // Initialize the database
        uRLRepository.saveAndFlush(uRL);

        // Get the uRL
        restURLMockMvc
            .perform(get(ENTITY_API_URL_ID, uRL.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(uRL.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL));
    }

    @Test
    @Transactional
    void getNonExistingURL() throws Exception {
        // Get the uRL
        restURLMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewURL() throws Exception {
        // Initialize the database
        uRLRepository.saveAndFlush(uRL);

        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();

        // Update the uRL
        URL updatedURL = uRLRepository.findById(uRL.getId()).get();
        // Disconnect from session so that the updates on updatedURL are not directly saved in db
        em.detach(updatedURL);
        updatedURL.url(UPDATED_URL);

        restURLMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedURL.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedURL))
            )
            .andExpect(status().isOk());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
        URL testURL = uRLList.get(uRLList.size() - 1);
        assertThat(testURL.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void putNonExistingURL() throws Exception {
        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();
        uRL.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restURLMockMvc
            .perform(
                put(ENTITY_API_URL_ID, uRL.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isBadRequest());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchURL() throws Exception {
        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();
        uRL.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isBadRequest());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamURL() throws Exception {
        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();
        uRL.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateURLWithPatch() throws Exception {
        // Initialize the database
        uRLRepository.saveAndFlush(uRL);

        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();

        // Update the uRL using partial update
        URL partialUpdatedURL = new URL();
        partialUpdatedURL.setId(uRL.getId());

        restURLMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedURL.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedURL))
            )
            .andExpect(status().isOk());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
        URL testURL = uRLList.get(uRLList.size() - 1);
        assertThat(testURL.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    void fullUpdateURLWithPatch() throws Exception {
        // Initialize the database
        uRLRepository.saveAndFlush(uRL);

        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();

        // Update the uRL using partial update
        URL partialUpdatedURL = new URL();
        partialUpdatedURL.setId(uRL.getId());

        partialUpdatedURL.url(UPDATED_URL);

        restURLMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedURL.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedURL))
            )
            .andExpect(status().isOk());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
        URL testURL = uRLList.get(uRLList.size() - 1);
        assertThat(testURL.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    void patchNonExistingURL() throws Exception {
        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();
        uRL.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restURLMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, uRL.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isBadRequest());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchURL() throws Exception {
        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();
        uRL.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isBadRequest());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamURL() throws Exception {
        int databaseSizeBeforeUpdate = uRLRepository.findAll().size();
        uRL.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restURLMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(uRL))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the URL in the database
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteURL() throws Exception {
        // Initialize the database
        uRLRepository.saveAndFlush(uRL);

        int databaseSizeBeforeDelete = uRLRepository.findAll().size();

        // Delete the uRL
        restURLMockMvc
            .perform(delete(ENTITY_API_URL_ID, uRL.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<URL> uRLList = uRLRepository.findAll();
        assertThat(uRLList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
