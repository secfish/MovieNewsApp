package com.yong.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.yong.domain.Twitter} entity.
 */
public class TwitterDTO implements Serializable {

    private Long id;

    @NotNull
    private String content;

    private Instant pubDate;

    private String publisher;

    private MovieDTO movie;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getPubDate() {
        return pubDate;
    }

    public void setPubDate(Instant pubDate) {
        this.pubDate = pubDate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public MovieDTO getMovie() {
        return movie;
    }

    public void setMovie(MovieDTO movie) {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TwitterDTO)) {
            return false;
        }

        TwitterDTO twitterDTO = (TwitterDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, twitterDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TwitterDTO{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", pubDate='" + getPubDate() + "'" +
            ", publisher='" + getPublisher() + "'" +
            ", movie=" + getMovie() +
            "}";
    }
}
