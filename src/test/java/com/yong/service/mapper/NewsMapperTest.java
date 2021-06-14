package com.yong.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NewsMapperTest {

    private NewsMapper newsMapper;

    @BeforeEach
    public void setUp() {
        newsMapper = new NewsMapperImpl();
    }
}
