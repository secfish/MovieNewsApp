package com.yong.repository;

import com.yong.domain.Movie;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Movie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select movie from Movie movie where movie.user.login = ?#{principal.username}")
    List<Movie> findByUserIsCurrentUser();
}
