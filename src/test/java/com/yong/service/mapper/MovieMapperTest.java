package com.yong.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MovieMapperTest {

    private MovieMapper movieMapper;

    @BeforeEach
    public void setUp() {
        movieMapper = new MovieMapperImpl();
    }
}
