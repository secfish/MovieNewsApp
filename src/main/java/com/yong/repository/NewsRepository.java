package com.yong.repository;

import com.yong.domain.News;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the News entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    @Query("select news from News news where news.user.login = ?#{principal.username}")
    List<News> findByUserIsCurrentUser();
}
