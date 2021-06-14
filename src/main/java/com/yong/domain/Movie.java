package com.yong.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Movie.
 */
@Entity
@Table(name = "movie")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Movie implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "director")
    private String director;

    @Column(name = "synopsis")
    private String synopsis;

    @Column(name = "comment")
    private String comment;

    @Column(name = "start_date")
    private Instant startDate;

    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @OneToMany(mappedBy = "movie")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "movie" }, allowSetters = true)
    private Set<Twitter> twitters = new HashSet<>();

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Movie id(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public Movie name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirector() {
        return this.director;
    }

    public Movie director(String director) {
        this.director = director;
        return this;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    public Movie synopsis(String synopsis) {
        this.synopsis = synopsis;
        return this;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getComment() {
        return this.comment;
    }

    public Movie comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Movie startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public byte[] getImage() {
        return this.image;
    }

    public Movie image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return this.imageContentType;
    }

    public Movie imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public Set<Twitter> getTwitters() {
        return this.twitters;
    }

    public Movie twitters(Set<Twitter> twitters) {
        this.setTwitters(twitters);
        return this;
    }

    public Movie addTwitter(Twitter twitter) {
        this.twitters.add(twitter);
        twitter.setMovie(this);
        return this;
    }

    public Movie removeTwitter(Twitter twitter) {
        this.twitters.remove(twitter);
        twitter.setMovie(null);
        return this;
    }

    public void setTwitters(Set<Twitter> twitters) {
        if (this.twitters != null) {
            this.twitters.forEach(i -> i.setMovie(null));
        }
        if (twitters != null) {
            twitters.forEach(i -> i.setMovie(this));
        }
        this.twitters = twitters;
    }

    public User getUser() {
        return this.user;
    }

    public Movie user(User user) {
        this.setUser(user);
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movie)) {
            return false;
        }
        return id != null && id.equals(((Movie) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Movie{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", director='" + getDirector() + "'" +
            ", synopsis='" + getSynopsis() + "'" +
            ", comment='" + getComment() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            "}";
    }
}
