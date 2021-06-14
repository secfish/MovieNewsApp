package com.yong.service.mapper;

import com.yong.domain.*;
import com.yong.service.dto.TwitterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Twitter} and its DTO {@link TwitterDTO}.
 */
@Mapper(componentModel = "spring", uses = { MovieMapper.class })
public interface TwitterMapper extends EntityMapper<TwitterDTO, Twitter> {
    @Mapping(target = "movie", source = "movie", qualifiedByName = "id")
    TwitterDTO toDto(Twitter s);
}
