package com.yong.service.mapper;

import com.yong.domain.*;
import com.yong.service.dto.NewsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link News} and its DTO {@link NewsDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface NewsMapper extends EntityMapper<NewsDTO, News> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    NewsDTO toDto(News s);
}
