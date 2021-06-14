package com.yong.service.mapper;

import com.yong.domain.*;
import com.yong.service.dto.MovieDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Movie} and its DTO {@link MovieDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface MovieMapper extends EntityMapper<MovieDTO, Movie> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    MovieDTO toDto(Movie s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MovieDTO toDtoId(Movie movie);
}
