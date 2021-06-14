package com.yong.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yong.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TwitterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Twitter.class);
        Twitter twitter1 = new Twitter();
        twitter1.setId(1L);
        Twitter twitter2 = new Twitter();
        twitter2.setId(twitter1.getId());
        assertThat(twitter1).isEqualTo(twitter2);
        twitter2.setId(2L);
        assertThat(twitter1).isNotEqualTo(twitter2);
        twitter1.setId(null);
        assertThat(twitter1).isNotEqualTo(twitter2);
    }
}
