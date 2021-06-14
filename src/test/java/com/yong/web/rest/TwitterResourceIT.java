package com.yong.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.yong.IntegrationTest;
import com.yong.domain.Twitter;
import com.yong.repository.TwitterRepository;
import com.yong.service.dto.TwitterDTO;
import com.yong.service.mapper.TwitterMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TwitterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TwitterResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_PUB_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PUB_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_PUBLISHER = "AAAAAAAAAA";
    private static final String UPDATED_PUBLISHER = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/twitters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TwitterRepository twitterRepository;

    @Autowired
    private TwitterMapper twitterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTwitterMockMvc;

    private Twitter twitter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Twitter createEntity(EntityManager em) {
        Twitter twitter = new Twitter().content(DEFAULT_CONTENT).pubDate(DEFAULT_PUB_DATE).publisher(DEFAULT_PUBLISHER);
        return twitter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Twitter createUpdatedEntity(EntityManager em) {
        Twitter twitter = new Twitter().content(UPDATED_CONTENT).pubDate(UPDATED_PUB_DATE).publisher(UPDATED_PUBLISHER);
        return twitter;
    }

    @BeforeEach
    public void initTest() {
        twitter = createEntity(em);
    }

    @Test
    @Transactional
    void createTwitter() throws Exception {
        int databaseSizeBeforeCreate = twitterRepository.findAll().size();
        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);
        restTwitterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeCreate + 1);
        Twitter testTwitter = twitterList.get(twitterList.size() - 1);
        assertThat(testTwitter.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTwitter.getPubDate()).isEqualTo(DEFAULT_PUB_DATE);
        assertThat(testTwitter.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
    }

    @Test
    @Transactional
    void createTwitterWithExistingId() throws Exception {
        // Create the Twitter with an existing ID
        twitter.setId(1L);
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        int databaseSizeBeforeCreate = twitterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTwitterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContentIsRequired() throws Exception {
        int databaseSizeBeforeTest = twitterRepository.findAll().size();
        // set the field null
        twitter.setContent(null);

        // Create the Twitter, which fails.
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        restTwitterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isBadRequest());

        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTwitters() throws Exception {
        // Initialize the database
        twitterRepository.saveAndFlush(twitter);

        // Get all the twitterList
        restTwitterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(twitter.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].pubDate").value(hasItem(DEFAULT_PUB_DATE.toString())))
            .andExpect(jsonPath("$.[*].publisher").value(hasItem(DEFAULT_PUBLISHER)));
    }

    @Test
    @Transactional
    void getTwitter() throws Exception {
        // Initialize the database
        twitterRepository.saveAndFlush(twitter);

        // Get the twitter
        restTwitterMockMvc
            .perform(get(ENTITY_API_URL_ID, twitter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(twitter.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.pubDate").value(DEFAULT_PUB_DATE.toString()))
            .andExpect(jsonPath("$.publisher").value(DEFAULT_PUBLISHER));
    }

    @Test
    @Transactional
    void getNonExistingTwitter() throws Exception {
        // Get the twitter
        restTwitterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTwitter() throws Exception {
        // Initialize the database
        twitterRepository.saveAndFlush(twitter);

        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();

        // Update the twitter
        Twitter updatedTwitter = twitterRepository.findById(twitter.getId()).get();
        // Disconnect from session so that the updates on updatedTwitter are not directly saved in db
        em.detach(updatedTwitter);
        updatedTwitter.content(UPDATED_CONTENT).pubDate(UPDATED_PUB_DATE).publisher(UPDATED_PUBLISHER);
        TwitterDTO twitterDTO = twitterMapper.toDto(updatedTwitter);

        restTwitterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, twitterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
        Twitter testTwitter = twitterList.get(twitterList.size() - 1);
        assertThat(testTwitter.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTwitter.getPubDate()).isEqualTo(UPDATED_PUB_DATE);
        assertThat(testTwitter.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    void putNonExistingTwitter() throws Exception {
        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();
        twitter.setId(count.incrementAndGet());

        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTwitterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, twitterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTwitter() throws Exception {
        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();
        twitter.setId(count.incrementAndGet());

        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTwitterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTwitter() throws Exception {
        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();
        twitter.setId(count.incrementAndGet());

        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTwitterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTwitterWithPatch() throws Exception {
        // Initialize the database
        twitterRepository.saveAndFlush(twitter);

        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();

        // Update the twitter using partial update
        Twitter partialUpdatedTwitter = new Twitter();
        partialUpdatedTwitter.setId(twitter.getId());

        partialUpdatedTwitter.pubDate(UPDATED_PUB_DATE);

        restTwitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTwitter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTwitter))
            )
            .andExpect(status().isOk());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
        Twitter testTwitter = twitterList.get(twitterList.size() - 1);
        assertThat(testTwitter.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testTwitter.getPubDate()).isEqualTo(UPDATED_PUB_DATE);
        assertThat(testTwitter.getPublisher()).isEqualTo(DEFAULT_PUBLISHER);
    }

    @Test
    @Transactional
    void fullUpdateTwitterWithPatch() throws Exception {
        // Initialize the database
        twitterRepository.saveAndFlush(twitter);

        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();

        // Update the twitter using partial update
        Twitter partialUpdatedTwitter = new Twitter();
        partialUpdatedTwitter.setId(twitter.getId());

        partialUpdatedTwitter.content(UPDATED_CONTENT).pubDate(UPDATED_PUB_DATE).publisher(UPDATED_PUBLISHER);

        restTwitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTwitter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTwitter))
            )
            .andExpect(status().isOk());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
        Twitter testTwitter = twitterList.get(twitterList.size() - 1);
        assertThat(testTwitter.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testTwitter.getPubDate()).isEqualTo(UPDATED_PUB_DATE);
        assertThat(testTwitter.getPublisher()).isEqualTo(UPDATED_PUBLISHER);
    }

    @Test
    @Transactional
    void patchNonExistingTwitter() throws Exception {
        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();
        twitter.setId(count.incrementAndGet());

        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTwitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, twitterDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTwitter() throws Exception {
        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();
        twitter.setId(count.incrementAndGet());

        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTwitterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTwitter() throws Exception {
        int databaseSizeBeforeUpdate = twitterRepository.findAll().size();
        twitter.setId(count.incrementAndGet());

        // Create the Twitter
        TwitterDTO twitterDTO = twitterMapper.toDto(twitter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTwitterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(twitterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Twitter in the database
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTwitter() throws Exception {
        // Initialize the database
        twitterRepository.saveAndFlush(twitter);

        int databaseSizeBeforeDelete = twitterRepository.findAll().size();

        // Delete the twitter
        restTwitterMockMvc
            .perform(delete(ENTITY_API_URL_ID, twitter.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Twitter> twitterList = twitterRepository.findAll();
        assertThat(twitterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
