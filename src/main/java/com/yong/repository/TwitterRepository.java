package com.yong.repository;

import com.yong.domain.Twitter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Twitter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TwitterRepository extends JpaRepository<Twitter, Long> {}
