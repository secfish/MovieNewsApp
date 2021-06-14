package com.yong.web.rest;

import com.yong.repository.TwitterRepository;
import com.yong.service.TwitterService;
import com.yong.service.dto.TwitterDTO;
import com.yong.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.yong.domain.Twitter}.
 */
@RestController
@RequestMapping("/api")
public class TwitterResource {

    private final Logger log = LoggerFactory.getLogger(TwitterResource.class);

    private static final String ENTITY_NAME = "twitter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TwitterService twitterService;

    private final TwitterRepository twitterRepository;

    public TwitterResource(TwitterService twitterService, TwitterRepository twitterRepository) {
        this.twitterService = twitterService;
        this.twitterRepository = twitterRepository;
    }

    /**
     * {@code POST  /twitters} : Create a new twitter.
     *
     * @param twitterDTO the twitterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new twitterDTO, or with status {@code 400 (Bad Request)} if the twitter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/twitters")
    public ResponseEntity<TwitterDTO> createTwitter(@Valid @RequestBody TwitterDTO twitterDTO) throws URISyntaxException {
        log.debug("REST request to save Twitter : {}", twitterDTO);
        if (twitterDTO.getId() != null) {
            throw new BadRequestAlertException("A new twitter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TwitterDTO result = twitterService.save(twitterDTO);
        return ResponseEntity
            .created(new URI("/api/twitters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /twitters/:id} : Updates an existing twitter.
     *
     * @param id the id of the twitterDTO to save.
     * @param twitterDTO the twitterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated twitterDTO,
     * or with status {@code 400 (Bad Request)} if the twitterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the twitterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/twitters/{id}")
    public ResponseEntity<TwitterDTO> updateTwitter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TwitterDTO twitterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Twitter : {}, {}", id, twitterDTO);
        if (twitterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, twitterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!twitterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TwitterDTO result = twitterService.save(twitterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, twitterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /twitters/:id} : Partial updates given fields of an existing twitter, field will ignore if it is null
     *
     * @param id the id of the twitterDTO to save.
     * @param twitterDTO the twitterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated twitterDTO,
     * or with status {@code 400 (Bad Request)} if the twitterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the twitterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the twitterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/twitters/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<TwitterDTO> partialUpdateTwitter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TwitterDTO twitterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Twitter partially : {}, {}", id, twitterDTO);
        if (twitterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, twitterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!twitterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TwitterDTO> result = twitterService.partialUpdate(twitterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, twitterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /twitters} : get all the twitters.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of twitters in body.
     */
    @GetMapping("/twitters")
    public ResponseEntity<List<TwitterDTO>> getAllTwitters(Pageable pageable) {
        log.debug("REST request to get a page of Twitters");
        Page<TwitterDTO> page = twitterService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /twitters/:id} : get the "id" twitter.
     *
     * @param id the id of the twitterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the twitterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/twitters/{id}")
    public ResponseEntity<TwitterDTO> getTwitter(@PathVariable Long id) {
        log.debug("REST request to get Twitter : {}", id);
        Optional<TwitterDTO> twitterDTO = twitterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(twitterDTO);
    }

    /**
     * {@code DELETE  /twitters/:id} : delete the "id" twitter.
     *
     * @param id the id of the twitterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/twitters/{id}")
    public ResponseEntity<Void> deleteTwitter(@PathVariable Long id) {
        log.debug("REST request to delete Twitter : {}", id);
        twitterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
