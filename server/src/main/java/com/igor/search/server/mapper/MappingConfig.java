package com.igor.search.server.mapper;

import org.mapstruct.MapperConfig;

import static org.mapstruct.CollectionMappingStrategy.ADDER_PREFERRED;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

@MapperConfig(
    componentModel = "spring",
    collectionMappingStrategy = ADDER_PREFERRED,
    nullValueCheckStrategy = ALWAYS
)
public interface MappingConfig {
}
