package com.yong.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TwitterMapperTest {

    private TwitterMapper twitterMapper;

    @BeforeEach
    public void setUp() {
        twitterMapper = new TwitterMapperImpl();
    }
}
