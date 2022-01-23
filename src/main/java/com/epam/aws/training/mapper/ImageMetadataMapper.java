package com.epam.aws.training.mapper;

import com.epam.aws.training.dto.ImageMetadataDto;
import com.epam.aws.training.repository.model.ImageMetadata;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMetadataMapper {

  @Mapping(target = "size", source = "sizeInBytes")
  @Named("mapToMetadataDto")
  ImageMetadataDto convertTo(ImageMetadata metadata);

  @IterableMapping(qualifiedByName = "mapToMetadataDto")
  List<ImageMetadataDto> convertTo(List<ImageMetadata> metadataList);
}
