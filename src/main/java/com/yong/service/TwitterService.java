package com.yong.service;

import com.yong.domain.Twitter;
import com.yong.repository.TwitterRepository;
import com.yong.service.dto.TwitterDTO;
import com.yong.service.mapper.TwitterMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Twitter}.
 */
@Service
@Transactional
public class TwitterService {

    private final Logger log = LoggerFactory.getLogger(TwitterService.class);

    private final TwitterRepository twitterRepository;

    private final TwitterMapper twitterMapper;

    public TwitterService(TwitterRepository twitterRepository, TwitterMapper twitterMapper) {
        this.twitterRepository = twitterRepository;
        this.twitterMapper = twitterMapper;
    }

    /**
     * Save a twitter.
     *
     * @param twitterDTO the entity to save.
     * @return the persisted entity.
     */
    public TwitterDTO save(TwitterDTO twitterDTO) {
        log.debug("Request to save Twitter : {}", twitterDTO);
        Twitter twitter = twitterMapper.toEntity(twitterDTO);
        twitter = twitterRepository.save(twitter);
        return twitterMapper.toDto(twitter);
    }

    /**
     * Partially update a twitter.
     *
     * @param twitterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TwitterDTO> partialUpdate(TwitterDTO twitterDTO) {
        log.debug("Request to partially update Twitter : {}", twitterDTO);

        return twitterRepository
            .findById(twitterDTO.getId())
            .map(
                existingTwitter -> {
                    twitterMapper.partialUpdate(existingTwitter, twitterDTO);
                    return existingTwitter;
                }
            )
            .map(twitterRepository::save)
            .map(twitterMapper::toDto);
    }

    /**
     * Get all the twitters.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TwitterDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Twitters");
        return twitterRepository.findAll(pageable).map(twitterMapper::toDto);
    }

    /**
     * Get one twitter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TwitterDTO> findOne(Long id) {
        log.debug("Request to get Twitter : {}", id);
        return twitterRepository.findById(id).map(twitterMapper::toDto);
    }

    /**
     * Delete the twitter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Twitter : {}", id);
        twitterRepository.deleteById(id);
    }
}
