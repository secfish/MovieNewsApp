package com.yong.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.yong.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TwitterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TwitterDTO.class);
        TwitterDTO twitterDTO1 = new TwitterDTO();
        twitterDTO1.setId(1L);
        TwitterDTO twitterDTO2 = new TwitterDTO();
        assertThat(twitterDTO1).isNotEqualTo(twitterDTO2);
        twitterDTO2.setId(twitterDTO1.getId());
        assertThat(twitterDTO1).isEqualTo(twitterDTO2);
        twitterDTO2.setId(2L);
        assertThat(twitterDTO1).isNotEqualTo(twitterDTO2);
        twitterDTO1.setId(null);
        assertThat(twitterDTO1).isNotEqualTo(twitterDTO2);
    }
}
