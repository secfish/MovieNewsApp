package com.yong.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Twitter.
 */
@Entity
@Table(name = "twitter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Twitter implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "pub_date")
    private Instant pubDate;

    @Column(name = "publisher")
    private String publisher;

    @ManyToOne
    @JsonIgnoreProperties(value = { "twitters", "user" }, allowSetters = true)
    private Movie movie;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Twitter id(Long id) {
        this.id = id;
        return this;
    }

    public String getContent() {
        return this.content;
    }

    public Twitter content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Instant getPubDate() {
        return this.pubDate;
    }

    public Twitter pubDate(Instant pubDate) {
        this.pubDate = pubDate;
        return this;
    }

    public void setPubDate(Instant pubDate) {
        this.pubDate = pubDate;
    }

    public String getPublisher() {
        return this.publisher;
    }

    public Twitter publisher(String publisher) {
        this.publisher = publisher;
        return this;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Movie getMovie() {
        return this.movie;
    }

    public Twitter movie(Movie movie) {
        this.setMovie(movie);
        return this;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Twitter)) {
            return false;
        }
        return id != null && id.equals(((Twitter) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Twitter{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            ", pubDate='" + getPubDate() + "'" +
            ", publisher='" + getPublisher() + "'" +
            "}";
    }
}
